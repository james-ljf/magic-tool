package com.magictool.web.exception.handler;

import com.magictool.web.entity.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 自定义全局异常handler
 *
 * @Author: ljf
 * @DateTime: 2022/5/30 21:40
 */
public class BizBusinessExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(BizBusinessExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response<Object> errorHandler(Exception e){
        log.error("[40010.an biz business exception] : msg = {}", e.getMessage());
        return Response.fail("40010", e.toString());
    }

}
