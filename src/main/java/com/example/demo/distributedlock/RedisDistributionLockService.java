package com.example.demo.distributedlock;

/**
 * @author Kimmy
 * Redis分布式锁接口
 */
public interface RedisDistributionLockService {
    /**
     * 加锁成功，返回加锁时间
     *
     * @param lockKey
     * @param threadName
     * @return
     */
    long lock(String lockKey, String threadName);

    /**
     * 解锁， 需要更新加锁时间，判断是否有权限
     *
     * @param lockKey
     * @param lockValue
     * @param threadName
     */
    void unlock(String lockKey, long lockValue, String threadName);

    /**
     * 多服务器集群，使用下面的方法，代替System.currentTimeMillis()，获取redis时间，避免多服务的时间不一致问题！！！
     *
     * @return
     */
    long currtTimeForRedis();
}
