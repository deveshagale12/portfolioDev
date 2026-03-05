package com.portfolio_dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/v1/**") // Protect everything
                .excludePathPatterns(
                    "/api/v1/portfolio/summary", // Public can see summary
                    "/api/v1/files/display/**",  // Public can see images
                    "/api/v1/contact/send"       // Public can send messages
                );
    }
}