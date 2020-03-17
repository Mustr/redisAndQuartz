package com.harry.utils;

import java.io.IOException;

import redis.clients.jedis.JedisCluster;

/**
 * 集群版版连接池工具类
 */
public class JedisClusterUtils {

	private JedisCluster jedisCluster;

	private JedisClusterUtils() {
		jedisCluster = SpringUtils.getBean("jedisCluster");
	}
    
	static class Holder {
		static JedisClusterUtils instance= new JedisClusterUtils();
	}

	public static JedisClusterUtils getInstance() {
		return Holder.instance;
	}

	/**
	 * 获取连接池中的实例
	 * @return
	 */
	public JedisCluster getJedisClient() {
		return jedisCluster;
	}
	
	/**
	 * 回收
	 * @param jedis
	 */
	public void closeJedis(JedisCluster jedisCluster) {
		try {
			jedisCluster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
