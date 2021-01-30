package com.java.yxt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * com.java.yxt.YxtOperationManagementApplication
 *
 * @author zanglei
 * @version V1.0
 * @description
 * @Package
 * @date 2020/8/26
 */
@SpringBootApplication
@MapperScan(basePackages = "com.java.yxt.dao")
@EnableFeignClients(basePackages = "com.java.yxt")
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableSwagger2
@EnableOpenApi
@EnableScheduling
public class YxtOperationManagementApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(YxtOperationManagementApplication.class,args);
    }

}
