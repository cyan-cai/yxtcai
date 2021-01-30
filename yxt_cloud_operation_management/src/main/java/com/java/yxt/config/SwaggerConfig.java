package com.java.yxt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SwaggerConfig
 *
 * @author zanglei
 * @version V1.0
 * @description swagger配置
 * @Package com.java.yxt.config
 * @date 2020/9/17
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("zanglei", "", "");
        return new ApiInfoBuilder()
                // 标题
                .title("zanglei测试API接口")
                // 文档接口的描述
                .description("API接口的描述")
                .contact(contact)
                // 版本号
                .version("1.1.0")
                .build();
    }

}
