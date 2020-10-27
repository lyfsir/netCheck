package com.lyf.check.food.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lyf
 * @date 2020/10/18-16:14
 */

@ConfigurationProperties(prefix = "lyf.pool")
@Component
@Data
public class ThreadPoolProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;

}
