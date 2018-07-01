package com.harry.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 单机版的连接池工具类
 */
@Component
public class JedisPoolsUtils {

	@Autowired
	private JedisPool jedisPool;

	private JedisPoolsUtils() {
	}

	private static JedisPoolsUtils jedisPoolsUtils;  
    
    @PostConstruct  
    public void init(){  
    	jedisPoolsUtils = this;  
    	jedisPoolsUtils.jedisPool = this.jedisPool;  
    }  

	public static JedisPoolsUtils getInstance() {
		return jedisPoolsUtils;
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
