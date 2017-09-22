package com.zhph.base.redis;

import com.zhph.base.utils.SerializeUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.Resource;

@Component
public class RedisUtil {


    private static JedisSentinelPool sentinelPool;

    @Resource(name = "sentinelPool")
    public synchronized void setSentinelPool(JedisSentinelPool sentinelPool) {
        RedisUtil.sentinelPool = sentinelPool;
    }

    private static Logger log = Logger.getLogger(RedisUtil.class);


    private static Jedis getJedis()  {
        Jedis jedis = null;
        try {
            jedis = sentinelPool.getResource();
            return  jedis;
        } catch (JedisConnectionException e) {
            log.error("获取Redis 异常", e);
            throw e;
        }
    }

    /**
     * 获取指定键值的快速响应锁,同时设置获取锁超时时间和锁过期时间
     *
     * @param lockKey 锁的键值
     */
    private static JedisLock getFastResponseLock(String lockKey) {
        Jedis jedis = getJedis();
        return new JedisLock(jedis, lockKey, 50, 960000);
    }


    /**
     * 获取指定键值的串行锁,同时设置获取锁超时时间和锁过期时间
     *
     * @param lockKey 锁的键值
     */
    private static JedisLock getSerialLock(String lockKey) {
        Jedis jedis = getJedis();
        return new JedisLock(jedis, lockKey, 960000, 20000);
    }

    /**
     * 获取调用类的方法锁
     * @param isFast true 使用快速响应锁可快速响应页面，
     *               false 使用串行锁，可保证多线程下为获得锁的线程能够持有锁，能执行后续操作
     * @return 返回一个JedisLock 对象
     */
    public static JedisLock getMethodLock(boolean isFast) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        if (isFast) {
            return getFastResponseLock("lock_method_" + stack.getClassName() + "_" + stack.getMethodName());
        }
        return getSerialLock("lock_method_" + stack.getClassName() + "_" + stack.getMethodName());
    }

    /**
     * 获取调用类的类锁
     * @param isFast true 使用快速响应锁可快速响应页面，
     *               false 使用串行锁，可保证多线程下为获得锁的线程能够持有锁，能执行后续操作
     * @return 返回一个JedisLock 对象
     */
    public static JedisLock getClassLock(boolean isFast) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        if (isFast) {
            return getFastResponseLock("lock_class_" + stack.getClassName());
        }
        return getSerialLock("lock_class_" + stack.getClassName());
    }

    /**
     * 获取调用类的变量锁
     *
     * @param value 需要锁定的唯一值
     * @param isFast true 使用快速响应锁可快速响应页面，
     *               false 使用串行锁，可保证多线程下为获得锁的线程能够持有锁，能执行后续操作
     * @return 返回一个JedisLock 对象
     */
    public static JedisLock getValueLock(String value,boolean isFast) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        if (isFast) {
            return getFastResponseLock("lock_value_" + stack.getClassName() + "_" + stack.getMethodName() + "_" + value);
        }
        return getSerialLock("lock_value_" + stack.getClassName() + "_" + stack.getMethodName() + "_" + value);
    }

    /**
     * 获取对象锁
     *
     * @param obj 需要加锁的对象
     * @param isFast true 使用快速响应锁可快速响应页面，
     *               false 使用串行锁，可保证多线程下为获得锁的线程能够持有锁，能执行后续操作
     * @return 返回一个JedisLock 对象
     */
    public static JedisLock getObjLock(Object obj,boolean isFast) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        if (isFast) {
            return getFastResponseLock("lock_object_" + stack.getClassName() + "_" + stack.getMethodName() + "_" + obj.getClass().getName());
        }
        return getSerialLock("lock_object_" + stack.getClassName() + "_" + stack.getMethodName() + "_" + obj.getClass().getName());
    }

    /**
     * 单扣锁
     *
     * @param loanContractNo 合同编号
     * @param isFast true 使用快速响应锁可快速响应页面，
     *               false 使用串行锁，可保证多线程下为获得锁的线程能够持有锁，能执行后续操作
     * @return 返回一个JedisLock 对象
     */
    public static  JedisLock getSinglePaymentLock(String loanContractNo,boolean isFast) {
        if (isFast) {
            return getFastResponseLock("lock_singlePayment_" + loanContractNo);
        }
        return getSerialLock("lock_singlePayment_" + loanContractNo);
    }

    /**
     * 保存对象到Redis 对象不过期
     *
     * @param key    待缓存的key
     * @param object 待缓存的对象
     * @return 返回是否缓存成功
     */
    public static boolean setObject(String key, Object object) throws Exception {
        return setObject(key, object, -1);
    }

    /**
     * 保存对象到Redis 并设置超时时间
     *
     * @param key     缓存key
     * @param object  缓存对象
     * @param timeout 超时时间
     * @return 返回是否缓存成功
     * @throws Exception 异常上抛
     */
    public static boolean setObject(String key, Object object, int timeout) throws Exception {
        String value = SerializeUtil.serialize(object);
        boolean result = false;
        try {
            //为-1时不设置超时时间
            if (timeout != -1) {
                setString(key,value,timeout);
            } else {
                setString(key,value);
            }
            result = false;
        } catch (Exception e) {
            throw e;
        }
        return  result;
    }

    /**
     * 从Redis中获取对象
     *
     * @param key 待获取数据的key
     * @return 返回key对应的对象
     */
    public static Object getObject(String key) throws Exception {
        Object object = null;
        try {
            String serializeObj = getString(key);
            if (null == serializeObj || serializeObj.length() == 0) {
                object = null;
            } else {
                object = SerializeUtil.deserialize(serializeObj);
            }
        }  catch (Exception e) {
            throw e;
        }
        return object;
    }

    /**
     * 缓存String类型的数据,数据不过期
     *
     * @param key   待缓存数据的key
     * @param value 需要缓存的额数据
     * @return 返回是否缓存成功
     */
    public static boolean setString(String key, String value) throws Exception {
        return setString(key, value, -1);
    }

    /**
     * 缓存String类型的数据并设置超时时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间
     * @return 返回是否缓存成功
     */
    public static boolean setString(String key, String value, int timeout) throws Exception {
        String result;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.set(key, value);
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            if ("OK".equals(result)) {
                return true;
            } else {
                return  false;
            }
        } catch (Exception e){
            throw  e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 获取String类型的数据
     *
     * @param key 需要获取数据的key
     * @return 返回key对应的数据
     */
    @SuppressWarnings("deprecation")
    public static  String getString(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * Jedis 对象释放
     * @param jedis
     */
    public static void releaseRedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    /**
     * 删除缓存中的数据
     *
     * @param key 需要删除数据的key
     * @return 返回是否删除成功
     */
    public static boolean del(String key) throws Exception {
        Long num;
        Jedis jedis = null;
        Boolean result = false;
        try {
            jedis = getJedis();
            num = jedis.del(key);
            if (num.equals(1L)) {
                result = true;
            }
        } catch (Exception e) {
            throw  e;
        } finally {
            releaseRedis(jedis);
        }
        return result;
    }


}
