package com.zhph.main;

import com.sun.org.apache.regexp.internal.RE;
import com.zhph.base.redis.JedisLock;
import com.zhph.base.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-redis.xml","classpath:spring-pro.xml"})
@Transactional
public class TestRedisPool {



    @Test
    public void test() throws  Exception{

        /*Jedis jedis = RedisUtil.getJedis();
        try {
            Set<String> keySet = jedis.keys("*lock_singlePayment_*");
            System.out.println(keySet.size());
            Iterator<String> keyIterator = keySet.iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                System.out.println(key);
                jedis.del(key);
            }
        }finally {
            System.out.println("释放");
            if(jedis != null){
                jedis.close();
            }
        }*/
    }

}
