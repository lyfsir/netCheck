package com.lyf.check.food.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lyf
 * @date 2020/10/18-16:10
 */

@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties properties) {
        return new ThreadPoolExecutor(properties.getCoreSize(),properties.getMaxSize(),
                properties.getKeepAliveTime(), TimeUnit.SECONDS,new LinkedBlockingDeque<>(10000),
                Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
