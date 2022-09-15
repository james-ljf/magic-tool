package com.magictool.web.util.redis;


import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Set;

/**
 * 缓存通用服务
 * @author linxinze
 */
public interface CacheService {

    /**
     * 递减1并且返回
     * @param cacheKey key
     * @return long
     */
     long decrement(String cacheKey);

    /**
     * 递增1并且返回
     * @param cacheKey key
     * @param expiredTime timeout
     * @return long
     */
     long increment(String cacheKey,long expiredTime);

    /**
     * 由外部传入递增因子进行递增并且返回
     * @param cacheKey key
     * @param delta data
     * @param expiredTime 大于0才设置过期时间，0或小于0不设置过期时间
     * @return long
     */
     long increment(String cacheKey, long delta, long expiredTime);

    /**
     * put a object T into the cache.
     * 压入一个对象到cache中
     * @param cacheKey key
     * @param data T
     */
     <T> void put(String cacheKey, T data);

    /**
     *
     * @param cacheKey key
     * @param data T
     * @param expiredTime timeout
     */
     <T> void put(String cacheKey, T data,long expiredTime);


    /**
     * refresh the cache key.
     * 更新cache key
     * @param cacheKey key
     * @param expiredTime timeout
     */
    <T> void refresh(String cacheKey, long expiredTime);

    /**
     * get an object of T from cache
     * 获取一个缓存对象 通过key和对应的class
     * @param key key
     * @param clazzT 类
     * @return long
     */
    <T> T get(String key, Class<T> clazzT);

    /**
     * 通过typeReference返回
     * @param key key
     * @param typeReference 序列换类型
     * @param <T> T
     * @return T
     */
     <T> T get(String key, TypeReference<T> typeReference) ;

    /**
     * get the cache value as string
     * 获取缓存对象用String object方式获取
     * @param cacheKey key
     * @return str
     */
     String getAsString(String cacheKey);

    /**
     * put string value to cache
     * 写入cache string类型
     * @param cacheKey key
     * @param cacheValue value
     */
     void put(String cacheKey, String cacheValue);


    /**
     * put string value to cache
     * 写入cache string类型
     * @param cacheKey key
     * @param cacheValue value
     * @param expiredTime timeout
     */
    public void put(String cacheKey, String cacheValue, long expiredTime);

    /**
     * delete the cache by cache key
     * 删除缓存
     * @param cacheKey key
     */
    public void delete(String cacheKey);

    /**
     * get by pattern key
     * 通过正则表达式获取所有的值
     * @param keyPattern 正则表达式
     * @return set
     */
    public Set<String> getPatternKeys(String keyPattern);

    /**
     * 从队列存储一个字符串
     * @param cacheKey key
     * @param cacheValue value
     */
    public void lPush(String cacheKey,String cacheValue);

    /**
     * 从队首获取一个值
     * @param cacheKey key
     * @return str
     */
    public String rpop(String cacheKey);

    /**
     * 获取队列长度
     * @param cacheKey key
     * @return long
     */
    public Long queueSize(String cacheKey);

    /**
     * 从队列存储一个对象
     * @param cacheKey key
     * @param data value
     */
    public <T> void lPushData(String cacheKey, T data);


    /**
     *  从队首获取一个对象
     * @param key key
     * @param clazzT 类
     * @return T
     */
    public <T> T rpopData(String key, Class<T> clazzT);



    /**
     * 获取集合 
     * @param key key
     * @return List<T>
     */
    public List<String> getStringList(String key);

    /**
     * 存储一个list
     * @param key key
     * @param data value
     */
    public void pushStringList(String key,List<String> data);

    /**
     * 检查key是否存在,如果不存在,则摄入
     * @param cacheKey key
     * @param value value
     * @param time timeout
     * @return boolean
     */
    public Boolean isPermissionInfoAlreadyPush(String cacheKey,Object value,long time);


}
