package com.lyf.check.food;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableRabbit
@EnableFeignClients
@EnableDiscoveryClient
@EnableTransactionManagement
@SpringBootApplication
public class LyfFoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyfFoodApplication.class, args);
    }

}
