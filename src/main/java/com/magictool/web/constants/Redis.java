package com.magictool.web.constants;

/**
 * Redis 存储标识
 * 用来记录 hash 的存储 key
 *
 * @author : tx
 * @time : 2022/2/22 11:16
 **/
public interface Redis {

    /**
     * 通用 redis 存储
     */
    public static final String REDIS_COMMON = "redis_common" ;

    /**
     * Token 缓存
     */
    public static final String REDIS_TOKEN = "redis_token" ;

    /**
     * 验证码 缓存
     */
    public static final String REDIS_VERIFY = "redis_verify" ;

    /**
     * 分享数据缓存
     */
    public static final String REDIS_SHARE_FILE = "redis_share_file";

    /**
     * 文件名称缓存
     */
    public static final String REDIS_FILE_NAME = "redis_file_name";

    /**
     * 文件 id 缓存 ( 用于存储临时文件的 uuid )
     */
    public static final String REDIS_FILE_UUID = "redis_file_uuid";



}
