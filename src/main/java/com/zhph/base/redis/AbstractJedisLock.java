package com.zhph.base.redis;


import redis.clients.jedis.Jedis;

/**
 *
 * Author: Zou Yao
 * Description: (Redis锁抽象类)
 * Time: 2017/8/9 11:02
 *
**/
public abstract class AbstractJedisLock implements DistributedLock {


    //默认的锁到期时间
    protected final static int EXPIRE_MSECS_DEF =1000*60;
    //获取锁超时时间
    protected final static int TIMEOUT_MSECS_DEF =1000*10;

    //Redis Client By Java
    protected Jedis jedis;
    //关键字
    protected String lockKey;
    //锁到期时间
    protected int expireMsecs;
    //获取锁超时时间，避免出现线程节
    protected int timeoutMsecs;
    //是否获取到锁
    protected boolean locked;

    public AbstractJedisLock(Jedis jedis, String lockKey, int expireMsecs, int timeoutMsecs) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.expireMsecs = expireMsecs;
        this.timeoutMsecs = timeoutMsecs;
        this.locked = false;
    }

    /**
     *
     * Author: zou yao
     * Description: {获取到键值}
     * Date: 2017/8/9 11:19
     *
    **/
    public String getKey() {
        return this.lockKey;
    }



    /**
     *
     * Author: zou yao
     * Description: {获取到锁}
     * Date: 2017/8/9 11:20
     *
    **/
    public boolean acquire() throws Exception {
        return acquire(jedis);
    }


    /**
     *
     * Author: zou yao
     * Description: {通过jedis获取到锁}
     * Date: 2017/8/9 11:29
     *
    **/
    public abstract boolean acquire(Jedis jedis)  throws Exception;
    
    
    /**
     *
     * Author: zou yao
     * Description: {描述}
     * Date: 2017/8/9 11:34
     * 
    **/
    public void release() {
        release(jedis);
    }


    /**
     *
     * Author: zou yao
     * Description: {释放redis缓存}
     * Date: 2017/8/9 11:30
     *
    **/
    public abstract void release(Jedis jedis);
}
