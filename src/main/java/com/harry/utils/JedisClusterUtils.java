package com.harry.utils;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

/**
 * 集群版版连接池工具类
 */
@Component
public class JedisClusterUtils {

	@Autowired
	private JedisCluster jedisCluster;

	private JedisClusterUtils() {
	}

	private static JedisClusterUtils jedisClusterUtils;  
    
    @PostConstruct  
    public void init(){  
    	jedisClusterUtils = this;  
    	jedisClusterUtils.jedisCluster = this.jedisCluster;  
    }  

	public static JedisClusterUtils getInstance() {
		return jedisClusterUtils;
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
