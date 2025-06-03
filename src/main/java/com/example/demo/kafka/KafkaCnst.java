package com.example.demo.kafka;

public final class KafkaCnst {

    public static final String BUSINESS_CONSUMER_GROUP_OF_TOPIC_GEMI_DEVICE_REPORT_WOVEN = "fs_woven_iot_adapter_business";

    public static final String RULE_CONSUMER_GROUP_OF_TOPIC_GEMI_DEVICE_REPORT_WOVEN = "fs_woven_iot_adapter_rule";

    // 产业园
    public static final String INDUSTRIAL_PARK_CONSUMER_GROUP_OF_TOPIC_GEMI_DEVICE_REPORT_WOVEN = "gemi_device_report_woven_consumer_woven_iot_adapter_20220915";

    // GEMI上报梭织kafka的TOPIC // 规则平台发送数据 业务数据、故障数据、状态数据 的主题
    public static final String TOPIC_GEMI_DEVICE_REPORT_WOVEN = "gemi_device_report_woven";

    public static final String BATCH_MANUAL_CONSUMER_FACTORY = "batchManualConsumerFactory";
}
