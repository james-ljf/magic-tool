package com.magictool.web.convert;

import org.springframework.beans.BeanUtils;

/**
 * 对象转换器
 * @author lijf
 */
public class ObjectConvert {

    /**
     * 将对象的属性copy到另一个对象
     * @param startObj  初始对象
     * @param targetObj 目标对象
     * @param <T>   T
     * @return  T
     */
    public static  <T> T getTarget(T startObj, T targetObj){
        BeanUtils.copyProperties(startObj, targetObj);
        return targetObj;
    }

}
