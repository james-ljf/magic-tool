package com.magictool.web.annotation;

/**
 * @Author: ljf
 * @DateTime: 2022/5/30 21:47
 */

import java.lang.annotation.*;

/**
 * 记录接口响应时间
 * @Author: ljf
 * @DateTime: 2022/3/24 13:09
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemTime {

}
