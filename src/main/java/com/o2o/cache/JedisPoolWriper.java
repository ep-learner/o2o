package com.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 连接池的构造函数
 * 封装IP 端口和 XML中的连接池配置
 */
public class JedisPoolWriper {

    private JedisPool jedisPool;//Redis 连接池对象

    public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host, final int port) {
        try{
            jedisPool = new JedisPool(poolConfig, host, port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

}
