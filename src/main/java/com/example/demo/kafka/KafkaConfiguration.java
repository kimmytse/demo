package com.example.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    @Value("${kafka.rule.server}")  // 规则平台的kafka地址  GEMI 阿里云
    private String kafkaServerOfRule;
    @Value(("${kafka.bigData.server}")) // 大数据公共kafka地址 阿里云
    private String kafkaServerOfBigData;

    /**
     * 批量kafka手动消费工厂
     *
     * @return 消息批量消费，手动ack kafka工厂
     */
    @Bean(name = "batchManualConsumerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> batchManualConsumerFactory() {
        //开启3个容器并发处理线程
        return kafkaBatchManualConsumerFactory(kafkaServerOfRule, 100, 20000, 3,
                2L, KafkaCnst.BUSINESS_CONSUMER_GROUP_OF_TOPIC_GEMI_DEVICE_REPORT_WOVEN, "60000");
    }

    /**
     * <p>生产者的属性配置</p>
     *
     * @return
     */
    private Map<String, Object> producerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 500);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerOfBigData);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, FactoryPartitioner.class);
        return props;
    }

    @Bean
    @Qualifier(("kafkaProducerFactoryOfBigData"))
    public DefaultKafkaProducerFactory produceFactory() {
        return new DefaultKafkaProducerFactory(producerProperties());
    }

    @Bean
    @Qualifier("kafkaTemplateOfBigData")
    public KafkaTemplate kafkaTemplate(DefaultKafkaProducerFactory kafkaProducerFactoryOfBigData) {
        return new KafkaTemplate(kafkaProducerFactoryOfBigData);
    }

    private static final String MAX_POLL_RECORDS_CONFIG_DEFAULT = "5";
    private static final String AUTO_COMMIT_INTERVAL_MS_CONFIG_DEFAULT = "2000";

    /***
     * kafka 消费工厂默认配置
     *
     * @param brokerAddress broker地址
     * @param maxPollRecords 当批量获取消息设置为true时,每次批量poll消息的最大数
     * @param maxPollIntervalTime 当消息数量达不到获取的批次数时,达到该时长则直接批量poll消息
     * @param groupId 消费者组id
     * @param sessionMs session超时时间
     * @return 默认配置
     */
    private Map<String, Object> consumerConfigs(String brokerAddress, Integer maxPollRecords,
                                                Integer maxPollIntervalTime, String groupId, String sessionMs) {
        Map<String, Object> propsMap = new HashMap<>();
        if (maxPollRecords != null && maxPollRecords > 0) {
            propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(maxPollRecords));
        } else {
            propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS_CONFIG_DEFAULT);
        }
        if (maxPollIntervalTime != null && maxPollIntervalTime > 0) {
            propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalTime);
        }
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress);
        // 禁止自动提交
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 自动提交间隔
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, AUTO_COMMIT_INTERVAL_MS_CONFIG_DEFAULT);
        // session存活时间
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionMs);
        // 序列化
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 消费者的组,最先顺序,可以直接在KafkaListener上直接修改groupId
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.LATEST.name().toLowerCase());
        return propsMap;
    }

    /***
     * 获取默认配置的kafka 消费工厂
     *
     * @param brokerAddress broker地址
     * @param maxPollRecords 当批量获取消息设置为true时，每次批量poll消息的最大数
     * @param maxPollIntervalTime 当消息数量达不到获取的批次数时，达到该时长则直接批量poll消息
     * @param groupId 消费者组id
     * @param sessionMs session超时时间
     * @return 默认配置的kafka 消费工厂
     */
    public ConsumerFactory<String, String> consumerFactory(String brokerAddress, Integer maxPollRecords, Integer maxPollIntervalTime,
                                                           String groupId, String sessionMs) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(brokerAddress, maxPollRecords, maxPollIntervalTime, groupId, sessionMs));
    }


    /***
     * kafka 自定义消费工厂
     *
     * @param brokerAddress broker地址
     * @param isBatch 是否批量获取消息
     * @param maxPollRecords 当批量获取消息设置为true时，每次批量poll消息的最大数
     * @param maxPollIntervalTime 当消息数量达不到获取的批次数时，达到该时长则直接批量poll消息
     * @param concurrency 每个topic并发消费线程数，不能设置大于patistion数
     * @param pollTimeOut Container等待获取消息的阻塞时间，默认5秒
     * @param groupId 消费者组id
     * @param sessionMs session超时时间
     * @param manualCommit 是否手动提交 ack
     * @return kafka 自定义消费工厂
     */
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaCustomConsumerFactory(String brokerAddress, boolean isBatch,
                                                                                                                        Integer maxPollRecords, Integer maxPollIntervalTime,
                                                                                                                        Integer concurrency, long pollTimeOut,
                                                                                                                        String groupId, String sessionMs,
                                                                                                                        boolean manualCommit) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(brokerAddress, maxPollRecords, maxPollIntervalTime, groupId, sessionMs));
        //  设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(isBatch);
        // 处理并发线程,启用多少个消费者线程
        factory.setConcurrency(concurrency);
        if (pollTimeOut > 0L) {
            // 不设置默认5秒
            factory.getContainerProperties().setPollTimeout(pollTimeOut);
        }
        if (isBatch) {
            if (manualCommit) {
                // 批量手动提交 ack

                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
            } else {
                // 批量自动提交 ack
                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
            }
        } else {
            if (manualCommit) {
                // 单条手动提交 ack
                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
            } else {
                // 单条自动提交 ack
                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
            }
        }
        return factory;
    }

    /***
     * 消息单条消费，自动ack kafka工厂
     *
     * @param brokerAddress broker地址
     * @param concurrency 每个topic并发消费线程数，不能设置大于patistion数
     * @param pollTimeOut Container等待获取消息的阻塞时间，默认5秒
     * @param groupId 消费者组id
     * @param sessionMs session超时时间
     * @return kafka消费工厂
     */
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaRecordConsumerFactory(String brokerAddress,
                                                                                                                        Integer concurrency, long pollTimeOut,
                                                                                                                        String groupId, String sessionMs) {
        return kafkaCustomConsumerFactory(brokerAddress, false, null, null, concurrency,
                pollTimeOut, groupId, sessionMs, false);
    }

    /***
     * 消息单条消费，手动ack kafka工厂（注意：该消费工厂需要手动提交位移ack，手动调用Acknowledgment.acknowledge()后立即提交）
     *
     * @param brokerAddress broker地址
     * @param concurrency 每个topic并发消费线程数，不能设置大于patistion数
     * @param pollTimeOut Container等待获取消息的阻塞时间，默认5秒
     * @param groupId 消费者组id
     * @param sessionMs session超时时间
     * @return kafka消费工厂
     */
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaRecordManualConsumerFactory(String brokerAddress,
                                                                                                                              Integer concurrency, long pollTimeOut,
                                                                                                                              String groupId, String sessionMs) {
        return kafkaCustomConsumerFactory(brokerAddress, false, null, null, concurrency,
                pollTimeOut, groupId, sessionMs, true);
    }

    /***
     * 消息批量消费，自动ack kafka工厂
     *
     * @param brokerAddress broker地址
     * @param maxPollRecords 每次批量poll消息的最大数
     * @param maxPollIntervalTime 当消息数量达不到获取的批次数时，达到该时长则直接批量poll消息
     * @param concurrency 每个topic并发消费线程数，不能设置大于patistion数
     * @param pollTimeOut Container等待获取消息的阻塞时间，默认5秒
     * @param groupId 消费者组id
     * @param sessionMs session超时时间
     * @return kafka消费工厂
     */
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaBatchAutoConsumerFactory(String brokerAddress, Integer maxPollRecords,
                                                                                                                           Integer maxPollIntervalTime, Integer concurrency,
                                                                                                                           long pollTimeOut, String groupId, String sessionMs) {
        return kafkaCustomConsumerFactory(brokerAddress, true, maxPollRecords, maxPollIntervalTime, concurrency, pollTimeOut, groupId, sessionMs, false);
    }

    /***
     * 消息批量消费，手动ack kafka工厂（注意：该消费工厂需要手动提交位移ack，手动调用Acknowledgment.acknowledge()后立即提交）
     *
     * @param brokerAddress broker地址
     * @param maxPollRecords 每次批量poll消息的最大数
     * @param maxPollIntervalTime 当消息数量达不到获取的批次数时，达到该时长则直接批量poll消息
     * @param concurrency 每个topic并发消费线程数，不能设置大于分区数
     * @param pollTimeOut Container等待获取消息的阻塞时间，默认5秒
     * @param groupId 消费者组id
     * @param sessionMs session超时时间
     * @return kafka消费工厂
     */
    private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaBatchManualConsumerFactory(String brokerAddress, Integer maxPollRecords,
                                                                                                                              Integer maxPollIntervalTime, Integer concurrency,
                                                                                                                              long pollTimeOut, String groupId, String sessionMs) {
        return kafkaCustomConsumerFactory(brokerAddress, true, maxPollRecords, maxPollIntervalTime, concurrency, pollTimeOut, groupId, sessionMs, true);
    }
}
