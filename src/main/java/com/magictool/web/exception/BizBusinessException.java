package com.magictool.web.exception;

import com.magictool.web.entity.dto.Code;

/**
 * 自定义全局异常类
 * @Author: ljf
 * @DateTime: 2022/5/30 21:42
 */
public class BizBusinessException extends RuntimeException {

    public BizBusinessException(){}
    public BizBusinessException(String exceptionMsg){
        super(exceptionMsg);
    }
    public BizBusinessException(Code code){
        super(code.getMsg());
    }

}
