package com.magictool.web.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
<<<<<<< Updated upstream
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
=======
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
>>>>>>> Stashed changes
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 关于mybatis-plus的自带分页功能配置类
 * @author lijf
 */
@Configuration
<<<<<<< Updated upstream
@ConditionalOnClass(value = {PaginationInnerInterceptor.class})
public class MybatisPlusPageConfig {

    /**
     *  旧版
=======
public class MybatisPlusPageConfig {

    /**
     *  最新版
>>>>>>> Stashed changes
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

    /**
     *  最新版
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}
