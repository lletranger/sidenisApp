package com.keliseev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Profile("!test")
public class SwaggerConfig {

    private static final String APP_NAME = "Money transfer application";
    private static final String SWAGGER_API_TITLE = "REST API documentation";
    private static final String EMPTY_DESCRIPTION = "";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                APP_NAME,
                SWAGGER_API_TITLE,
                "test",
                EMPTY_DESCRIPTION,
                null,
                EMPTY_DESCRIPTION,
                EMPTY_DESCRIPTION,
                Collections.emptyList());
    }
}