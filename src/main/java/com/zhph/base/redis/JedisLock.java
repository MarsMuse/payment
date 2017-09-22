package com.zhph.base.redis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * Author: Zou Yao
 * Description: (通过Redis获取到分布式锁)
 * Time: 2017/8/9 11:35
 *
**/
public class JedisLock extends AbstractJedisLock {
    //本地线程锁
    private static ReentrantLock localLock = new ReentrantLock();
    //循环获取锁时默认线程休眠时间
    private final static int LOOP_SLEEP_TIME = 100;
    //存放锁超时时间
    private long nowLockExpireMsecs = 0L;

    public JedisLock(Jedis jedis, String lockKey, int expireMsecs, int timeoutMsecs) {
        super(jedis, lockKey, expireMsecs, timeoutMsecs);
    }

    public JedisLock(Jedis jedis, String lockKey, int expireMsecs) {
        this(jedis, lockKey, expireMsecs, TIMEOUT_MSECS_DEF);
    }

    public JedisLock(Jedis jedis, String lockKey) {
        this(jedis, lockKey, EXPIRE_MSECS_DEF, TIMEOUT_MSECS_DEF);
    }

    public JedisLock(String lockKey) {
        this(null, lockKey, EXPIRE_MSECS_DEF, TIMEOUT_MSECS_DEF);
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到锁}
     * Date: 2017/8/9 11:52
     *
    **/
    @Override
    public boolean acquire(Jedis jedis) throws Exception {
        //加锁，避免本地线程之间由于出现竟态导致过多的获取锁超时
        localLock.lock();
        try{

            //获取到锁超时时间
            int nowTimeOut = timeoutMsecs;
            //循环获取到锁
            while(nowTimeOut >0){
                //设置此锁到期时间
                nowLockExpireMsecs = System.currentTimeMillis()+expireMsecs+1L;
                //转成字符串
                String expireMsecseStr = String.valueOf(nowLockExpireMsecs);
                if(jedis.setnx(lockKey , expireMsecseStr) == 1){
                    locked = true;
                    return true;
                }
                //没有获取到锁，先获取缓存的超时时间
                String  oldExpireMsecs = jedis.get(lockKey);
                //如果前一个锁超时了，并且没有释放锁
                if(oldExpireMsecs != null && Long.parseLong(oldExpireMsecs)<System.currentTimeMillis()){
                    //设置此锁到期时间,减少误差
                    nowLockExpireMsecs = System.currentTimeMillis()+expireMsecs+1L;
                    expireMsecseStr = String.valueOf(nowLockExpireMsecs);
                    //获取到缓存中的超时时间
                    String cacheExpireMsecs = jedis.getSet(lockKey , expireMsecseStr);
                    //如果缓存中的超时时间和上一个锁的超时时间一直，代表锁没有被改变，则获取到锁
                    if(cacheExpireMsecs != null && oldExpireMsecs.equals(cacheExpireMsecs)){
                        locked = true;
                        return true;
                    }
                }
                //超时时间减100毫秒
                nowTimeOut -=LOOP_SLEEP_TIME;
                //线程休眠100毫秒
                Thread.sleep(LOOP_SLEEP_TIME);
            }
        }finally {
            //释放本地锁
            localLock.unlock();
        }
        return false;
    }


    /**
     *
     * Author: zou yao
     * Description: {释放锁}
     * Date: 2017/8/9 11:51
     *
    **/
    @Override
    public void release(Jedis jedis){
        if(jedis != null){
            if(locked){
                //在锁没有超时的情况下才去释放锁
                if(nowLockExpireMsecs>=System.currentTimeMillis()) {
                    jedis.del(lockKey);
                }
                locked = false;
            }
            //释放jedis
            jedis.close();
            System.out.println(jedis);
        }
    }

    public String getValue(){
        return jedis.get(lockKey);
    }
}
