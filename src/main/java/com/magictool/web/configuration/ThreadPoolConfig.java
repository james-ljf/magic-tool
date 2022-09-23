package com.magictool.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * @Author ljf
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public Executor asyncPromiseExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(100);
        //配置最大线程数
        executor.setMaxPoolSize(200);
        //配置队列大小
        executor.setQueueCapacity(5000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("thread-pool-");
        /*
         * rejection-policy：当pool已经达到max size的时候，如何处理新任务
         * CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化执行器
        executor.initialize();
        return executor;
    }

}
