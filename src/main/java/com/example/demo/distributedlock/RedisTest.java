package com.example.demo.distributedlock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisTest {

    @Autowired
    private RedisUtil redisUtil;

    private static final String LOCK_NO = "redis_distribution_lock_no_";

    @Autowired
    RedisDistributionLockService redisDistributionLockService;

    /**
     * 功能描述 测试redis分布式锁
     *
     * @param redisKey 缓存key
     * @return
     */
    public String testDistributedLock(String redisKey) {
        String result = null;
        //加锁时间
        Long lockTime = redisDistributionLockService.lock(LOCK_NO + 1, Thread.currentThread().getName());
        if (lockTime != null) {
            //todo add by kimmy 存在并发访问操作,支付异步回调,重复请求拦截等用分布式锁
            result = (String) redisUtil.get(redisKey);

            redisDistributionLockService.unlock(LOCK_NO + 1, lockTime, Thread.currentThread().getName());
            return result;
        }
        return null;
    }

}
