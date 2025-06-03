package com.example.demo.base;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class SeckillService {
    private static final String STOCK_KEY = "seckill:stock";

    public String seckill() {
        Jedis jedis = new Jedis("localhost");
        try {
            jedis.watch(STOCK_KEY);
            int stock = Integer.parseInt(jedis.get(STOCK_KEY));
            if (stock > 0) {
                Transaction transaction = jedis.multi();
                transaction.decr(STOCK_KEY);
                transaction.exec();
                return "秒杀成功";
            } else {
                jedis.unwatch();
                return "秒杀失败，库存不足";
            }
        } finally {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        new SeckillService().seckill();
    }
}

