package com.keliseev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan("com.keliseev")
@Import({SwaggerConfig.class})
public class WebAppConfig {

    @Bean
    public ViewResolver contentNegotiatingViewResolver() {

        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        List<View> views = new ArrayList<>();
        views.add(new MappingJackson2JsonView());
        resolver.setDefaultViews(views);

        return resolver;
    }
}
