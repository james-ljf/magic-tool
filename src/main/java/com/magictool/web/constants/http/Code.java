package com.magictool.web.constants.http;

/**
 * Http状态码类
 *
 * @author : tx
 * @version : 1.0.0
 * @time : 2021/12/27 14:09
 **/
public enum Code {
    /**
     * 请求成功
     */
    CODE_OK(200),
    /**
     * 参数错误
     */
    CODE_PARAM_ERROR(400),
    /**
     * 没有认证
     */
    CODE_NOT_CERTIFIED(401),
    /**
     * 跨域错误
     */
    CODE_CROSS_DOMAIN(403),
    /**
     * 找不到页面
     */
    CODE_NOT_FOUND(404),
    /**
     * 系统内部错误
     */
    CODE_ERROR(500);

    private final int value;

    Code(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
