package com.harry.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * sharded版连接池工具类
 */
@Component
public class JedisShardedPoolsUtils {

	@Autowired
	private ShardedJedisPool shardedJedisPool;

	private JedisShardedPoolsUtils() {
	}

	private static JedisShardedPoolsUtils jedisShardedPoolsUtils;  
    
    @PostConstruct  
    public void init(){  
    	jedisShardedPoolsUtils = this;  
    	jedisShardedPoolsUtils.shardedJedisPool = this.shardedJedisPool;  
    }  

	public static JedisShardedPoolsUtils getInstance() {
		return jedisShardedPoolsUtils;
	}

	/**
	 * 获取连接池中的实例
	 * @return
	 */
	public ShardedJedis getJedisClient() {
		return shardedJedisPool.getResource();
	}
	
	/**
	 * 回收
	 * @param jedis
	 */
	public void closeJedis(ShardedJedis shardedJedis) {
		shardedJedis.close();
	}
}
