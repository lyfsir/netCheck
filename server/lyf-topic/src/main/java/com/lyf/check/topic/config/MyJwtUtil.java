package com.lyf.check.topic.config;

import com.lyf.common.config.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lyf
 * @date 2020/10/22-21:32
 */
@Configuration
public class MyJwtUtil {

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
