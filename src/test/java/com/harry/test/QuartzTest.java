package com.harry.test;

import org.junit.Test;

import com.harry.services.SchedulerService;
import com.harry.utils.JedisClusterUtils;
import com.harry.utils.JedisPoolsUtils;
import com.harry.utils.JedisShardedPoolsUtils;
import com.harry.utils.JedisTemplateUtils;
import com.harry.utils.SpringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;

public class QuartzTest extends TestBase{

	@Test
	public void testJobServices() {
		String jobName = "firstJob";
		SchedulerService schedulerService = (SchedulerService) SpringUtils.loadBeanByName("schedulerServiceImpl");
		schedulerService.exectJob(jobName, null);
	}
	
	@Test
	public void testJedisPool() {
		Jedis jedisClient = JedisPoolsUtils.getInstance().getJedisClient();
		System.out.println(jedisClient.ping());
		/*JedisPool bean = SpringUtils.getBean("jedisPool");
		System.out.println(bean.getResource().set("k1", "v1"));*/
	}
	
	@Test
	public void testShardedJedisPool() {
		JedisShardedPoolsUtils instance = JedisShardedPoolsUtils.getInstance();
		ShardedJedis jedisClient = instance.getJedisClient();
		/*for (int i = 0; i < 20; i++) {
			jedisClient.set("sharedJedis" + i, "value"+ i);
			instance.closeJedis(jedisClient);
		}*/
		System.out.println(jedisClient.get("sharedJedis17"));
		instance.closeJedis(jedisClient);
	}
	
	@Test
	public void testJedisCluster() {
		JedisCluster jedisClient = JedisClusterUtils.getInstance().getJedisClient();
		
		//System.out.println(jedisClient);
		/*jedisClient.set("clusK1", "clusValue1");
		String val = jedisClient.get("clusK1");
		System.out.println(val);*/
		jedisClient.del("clusK1");
		JedisClusterUtils.getInstance().closeJedis(jedisClient);
	}
	
	@Test
	public void testTemplate() {
		JedisTemplateUtils utils = SpringUtils.getBean("jedisTemplateUtils");
	}
}
