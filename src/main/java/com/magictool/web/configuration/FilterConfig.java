package com.magictool.web.configuration;

import com.magictool.web.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 *
 * @author : tx
 * @version : 1.0.0
 **/
@Configuration
public class FilterConfig {

    // =======================================================================================> Filter

    /**
     * 注册过滤器 bean
     * @return filter bean
     */
    @Bean
    public FilterRegistrationBean<TokenFilter> allFilterBean(){
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置 拦截所用的过滤器
        registrationBean.setFilter(new TokenFilter());
        // 设置 拦截的路径
        registrationBean.addUrlPatterns("/*");
        // 设置 bean 名称
        registrationBean.setName("tokenFilter");
        // 设置 bean 生成的顺序
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
