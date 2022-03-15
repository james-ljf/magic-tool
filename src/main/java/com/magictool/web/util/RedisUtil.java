package com.magictool.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 * @author : tx
 * @time : 2022/2/21 16:43
 **/
@Component
public final class RedisUtil implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static ApplicationContext applicationContext;

    private static RedisTemplate<String, Object> redisTemplate ;

    // ============================= constant ============================

    public static final long EXPIRE_ERROR = -999L;

    private static final long NEVER_EXPIRE = 0;

    // ============================= init ============================

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisUtil.applicationContext = applicationContext;
        redisTemplate = RedisUtil.applicationContext.getBean("redisTemplate", RedisTemplate.class);
    }

    // ============================= common ============================

    /**
     * 指定缓存失效时间,单位为秒
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public static void expire(String key, long time) {
        expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间
     * @param unit 时间单位
     */
    public static void expire(String key, long time, TimeUnit unit) {
        Objects.requireNonNull(key);
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, unit);
            }
        } catch (Exception e) {
            logger.error(" [expire] fail to set expire time ", e);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key  键 不能为null
     * @param unit 时间单位
     * @return 时间(秒) 返回 0 代表为永久有效 , 返回 {@link #EXPIRE_ERROR} 表示出错
     */
    public static long getExpire(String key, TimeUnit unit) {
        Long expireTime = redisTemplate.getExpire(key, unit);
        return expireTime != null
                ? expireTime
                : EXPIRE_ERROR;
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        boolean result = Boolean.FALSE;
        try {
            Boolean have = redisTemplate.hasKey(key);
            result = have != null ? have : result;
        } catch (Exception e) {
            logger.error(" [hasKey] hasKey occur error ", e);
        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }


    // ============================ query =============================

    /**
     * 获取普通缓存
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error(" [get] occur error ", e);
            return null;
        }
    }

    /**
     * 获取 hash 缓存
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object getHash(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> getHashAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return set 中的缓存数据
     */
    public static Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error(" [getSet] occur error ", e);
            return null;
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return 缓存内容
     */
    public static List<Object> getList(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error(" [getList] occur error ", e);
            return null;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index >= 0时， 0 表头，1 第二个元素，依次类推；index < 0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 对应的值
     */
    public static Object getListIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ============================ update =============================

    /**
     * 放入普通缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        set(key, value, 0);
    }

    /**
     * 普通缓存放入
     * 可以设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public static void set(String key, Object value, long time) {
        set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入
     * 可以设置时间和时间单位
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @throws NullPointerException 当 key 为 null 时
     */
    public static void set(String key, Object value, long time, TimeUnit unit) {
        Objects.requireNonNull(key);

        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, unit);
            } else {
                set(key, value);
            }

        } catch (Exception e) {
            logger.error(" [set] occur error ", e);
        }

    }

    /**
     * hash 缓存放入多个键值
     *
     * @param key 键
     * @param map 对应多个键值
     */
    public static void setHashAll(String key, Map<String, Object> map) {
        set(key, map, NEVER_EXPIRE);
    }

    /**
     * hash 缓存放入多个键值
     * 并且可以设置缓存时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     */
    public static void setHashAll(String key, Map<String, Object> map, long time) {
        Objects.requireNonNull(key);
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            logger.error(" [setHashAll] occur error ", e);
        }
    }

    /**
     * 向一张 hash 表中放入数据
     *
     * @param key   键
     * @param item  项
     * @param value 值
     */
    public static void setHash(String key, String item, Object value) {
        setHash(key, item, value, NEVER_EXPIRE);
    }

    /**
     * 向一张hash表中放入数据
     * 并且可以设置缓存时间
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public static void setHash(String key, String item, Object value, long time) {
        Objects.requireNonNull(key, item);
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            logger.error("[setHash] occur error ", e);
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     */
    public static void setSet(String key, Object... values) {
        setSet(key, values, NEVER_EXPIRE);
    }

    /**
     * 将set数据放入缓存
     * 并且可以设置缓存时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     */
    public static void setSet(String key, long time, Object... values) {
        try {
            redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            logger.error(" [setSet] occur error ", e);
        }
    }

    /**
     * 将 list 属性放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void setList(String key, Object value) {
        setList(key, value, NEVER_EXPIRE);
    }

    /**
     * 将 list 属性放入缓存
     * 并且可以设置缓存时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public static void setList(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            logger.error(" [setList] occur error ", e);
        }
    }

    /**
     * 将 list 整个放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void setListAll(String key, List<Object> value) {
        setListAll(key, value, NEVER_EXPIRE);
    }

    /**
     * 将 list 整个放入缓存
     * 并且可以设置缓存时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public static void setListAll(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            logger.error(" [setListAll] occur error ", e);
        }
    }

    /**
     * 根据 index 修改 list 中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public static void setListIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
        } catch (Exception e) {
            logger.error(" [setListIndex] occur error ", e);
        }
    }

    // ================================del=================================

    /**
     * 删除 hash 表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void delHash(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 在 set 缓存里面，移除值为 value 的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long delSet(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            if (count == null) {
                count = 0L;
            }
            return count;
        } catch (Exception e) {
            logger.error(" [delSet] occur error ", e);
            return 0;
        }
    }

    /**
     * 在 list 缓存里面， 移除 count 个值为 value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long delList(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            if (remove == null) {
                remove = 0L;
            }
            return remove;
        } catch (Exception e) {
            logger.error(" [delList] occur error ", e);
            return 0;
        }
    }

    // ============================ function =============================

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean hasSetKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            logger.error(" [hasSetKey] occur error ", e);
            return false;
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 存在 {@code true}, 不存在 {@code false}
     */
    public static boolean hasHashKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return set 缓存的长度
     */
    public static long getSetSize(String key) {
        try {
            Long size = redisTemplate.opsForSet().size(key);
            if (size == null) {
                size = 0L;
            }
            return size;
        } catch (Exception e) {
            logger.error(" [getSetSize] occur error ", e);
            return 0;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return list 缓存的长度
     */
    public long getListSize(String key) {
        try {
            Long size = redisTemplate.opsForList().size(key);
            if (size == null) {
                size = 0L;
            }
            return size;
        } catch (Exception e) {
            logger.error(" [getListSize] occur error ", e);
            return 0;
        }
    }

    /**
     * 递减和递减 （ 对应 string 的存储形式）
     *
     * @param key       键
     * @param increment 递减 （小于 0）, 递增 （大于 0）
     */
    public static void increment(String key, long increment) {
        redisTemplate.opsForValue().increment(key, increment);
    }


}
