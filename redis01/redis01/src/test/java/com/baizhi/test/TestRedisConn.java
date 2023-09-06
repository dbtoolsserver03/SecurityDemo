package com.baizhi.test;

import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * Created by HIAPAD on 2019/11/13.
 */
public class TestRedisConn {

	@Test
	public void testConn() {
		//  ps -ef | grep redis
		Jedis jedis = null;
		//创建jedis客户端对象
		//参数1:主机ip 参数2:端口
		jedis = new Jedis("172.16.158.1", 6379);
		System.out.println(jedis.get("name"));
		
		jedis = new Jedis("172.16.158.131", 6379);
		System.out.println(jedis.get("name"));
		
		jedis = new Jedis("172.16.158.128", 6379);
		System.out.println(jedis.get("name"));
		//jedis.auth("123");
		//操作redis 选择一个库 默认也是0号库

		//jedis.select(0);//选择零号库

	}
}
