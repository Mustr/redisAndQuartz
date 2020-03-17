package com.harry.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 单机版的连接池工具类
 */
public class JedisPoolsUtils {

	private JedisPool jedisPool;

	private JedisPoolsUtils() {
		jedisPool = SpringUtils.getBean("jedisPool");
	}

    
    static class Holder{
    	static JedisPoolsUtils instance = new JedisPoolsUtils();
    }

	public static JedisPoolsUtils getInstance() {
		return Holder.instance;
	}

	/**
	 * 获取连接池中的实例
	 * @return
	 */
	public Jedis getJedisClient() {
		return jedisPool.getResource();
	}
	
	/**
	 * 回收
	 * @param jedis
	 */
	public void closeJedis(Jedis jedis) {
		jedis.close();
	}
}
