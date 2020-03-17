package com.harry.utils;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * sharded版连接池工具类
 */
public class JedisShardedPoolsUtils {

	private ShardedJedisPool shardedJedisPool;

	private JedisShardedPoolsUtils() {
		shardedJedisPool = SpringUtils.getBean("shardedJedisPool");
	}

	static class Holder{
		static JedisShardedPoolsUtils instance = new JedisShardedPoolsUtils();
	}
    

	public static JedisShardedPoolsUtils getInstance() {
		return Holder.instance;
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
