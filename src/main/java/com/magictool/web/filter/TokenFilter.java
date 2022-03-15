package com.magictool.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.magictool.web.constants.Redis;
import com.magictool.web.entity.Response;
import com.magictool.web.entity.dto.Code;
import com.magictool.web.util.JwtUtils;
import com.magictool.web.util.ObjectUtil;
import com.magictool.web.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token 拦截器
 * 进行前端 token 验证, 并且跳过一部分接口的验证
 *
 * @author : tx
 * @time : 2022/3/3 9:36
 **/
public class TokenFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info(" TokenFilter init success ");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String requestUri = httpServletRequest.getRequestURI();
        // path[1] 是第一个路径的地址
        String[] path = requestUri.split("/");
        // 这里可以配置不进行 token 过滤的地址
        if (true) {
            // 以 rest 开始的是可以跳过的url ( /rest/** )
            // 验证, 当不是可以跳过的 url 的时候就进行 token 验证
            try {
                String token = httpServletRequest.getParameter("token");
                String userId = JwtUtils.getUserId(token);
                String redisToken = ObjectUtil.objToT(RedisUtil.getHash(Redis.REDIS_TOKEN, userId), String.class);
                if (!token.equals(redisToken)) {
                    sendResponse(httpServletResponse);
                }
            } catch (Exception e) {
                logger.error(" verify token error ", e);
                sendResponse(httpServletResponse);
            }
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    /**
     * 发送 token 验证失败消息
     *
     * @param httpServletResponse 返回 Response
     * @throws IOException 当出现 IO 异常
     */
    public void sendResponse(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(500);
        httpServletResponse.getWriter().write(JSONObject.toJSONString(Response.fail(Code.ERROR_TOKEN.getCode(), " incorrect token ")));
    }

    @Override
    public void destroy() {
        logger.info(" TokenFilter init success ");
    }
}
