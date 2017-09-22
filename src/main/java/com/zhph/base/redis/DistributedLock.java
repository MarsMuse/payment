package com.zhph.base.redis;


/**
 *
 * Author: Zou Yao
 * Description: (分布式锁接口)
 * Time: 2017/8/9 10:56
 *
**/
public interface DistributedLock {



    /**
     *
     * Author: zou yao
     * Description: {获取到锁关键字}
     * Date: 2017/8/9 10:59
     *
    **/

    String  getKey();
    /**
     *
     * Author: zou yao
     * Description: {获取到锁，成功获取到返回true,超时后还未获取到锁返回false，获取异常直接抛出}
     * Date: 2017/8/9 10:57
     *
    **/
    boolean  acquire() throws Exception;



    /**
     *
     * Author: zou yao
     * Description: {释放锁}
     * Date: 2017/8/9 11:00
     *
    **/
    void  release();
}
