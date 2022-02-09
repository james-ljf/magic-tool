package com.magictool.web.entity;

import com.magictool.web.constants.http.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 控制器返回实体类
 *
 * @author : tx
 * @version : 1.0.0
 * @time : 2021/12/27 14:00
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 状态信息
     */
    private String message;
    /**
     * 需要返回的数据
     */
    private T data;
    /**
     * 需要返回多个数据
     */
    private List<T> dataList;

    /**
     * 成功状态的返回
     * 更加方便返回成功数据
     *
     * @param message 返回的详细信息
     * @param <R> 返回的数据类型
     * @return 一个返回类的实体
     */
    public static <R> Response<R> ok(String message){
        Response<R> response = new Response<>();
        response.code = Code.CODE_OK.getValue();
        response.message = message;
        return response;
    }

    /**
     * 成功状态的返回
     * 更加方便返回成功数据
     *
     * @param message 返回的详细信息
     * @param data 返回的数据
     * @param <R> 返回的数据类型
     * @return 一个返回类的实体
     */
    public static <R> Response<R> ok(String message, R data){
        Response<R> response = new Response<>();
        response.code = Code.CODE_OK.getValue();
        response.message = message;
        response.data = data;
        return response;
    }

    /**
     * 错误状态的返回
     * 只返回 500 错误
     *
     * @param message 返回的详细信息
     * @param <R> 返回的数据类型
     * @return 一个返回类的实体
     */
    public static <R> Response<R> error(String message){
        Response<R> response = new Response<>();
        response.code = Code.CODE_ERROR.getValue();
        response.message = message;
        return response;
    }

    /**
     * 错误状态的返回
     *
     * @param code 状态码
     * @param message 返回的详细信息
     * @param <R> 返回的数据类型
     * @return 一个返回类的实体
     */
    public static <R> Response<R> error(Integer code,String message){
        Response<R> response = new Response<>();
        response.code = code;
        response.message = message;
        return response;
    }

}
