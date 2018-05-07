package com.exception.qms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jiangbing(江冰)
 * @date 2018/3/28
 * @time 下午2:16
 * @discription Swagger 配置
 **/
@Configuration
@EnableSwagger2
@Profile({"dev","prd"})
public class Swagger2Config {

    @Value("${swagger2.package}")
    private String basePackage;
    @Value("${spring.application.name}")
    private String title;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("allen-jiang",
                "www.exception.site", "weiwosuoai1991@gmail.com");

        return new ApiInfoBuilder()
                .title(title + " Exception 问答社区 API 文档")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
