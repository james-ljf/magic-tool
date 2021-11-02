package com.magictool.web.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Future类获取结果工具
 * @author lijf
 */
@Slf4j
public class FutureResultUtils {

    /**
     * 获取结果方法
     * @param future    Future类
     * @param timeout   设置获取结果的超时时间
     * @param <T>   T
     * @return  T
     */
    public static <T> T getResultQuietly(Future<T> future, int timeout){
        try {
            if (future != null){
                if (timeout > 0){
                    return future.get(timeout, TimeUnit.MILLISECONDS);
                }else {
                    return future.get();
                }
            }
        }catch (Exception e){
            log.warn("获取结果超时!", e);
        }
        return null;
    }

}