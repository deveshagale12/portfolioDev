package com.portfolio_dev;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps the URL path /uploads/** to the physical "uploads" folder on your disk
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic()) 
                .resourceChain(true);
    }

   // @Override
    //public void addCorsMappings(CorsRegistry registry) {
        // This allows your frontend (React, Vue, or Live Server) to call your Spring Boot API
        //registry.addMapping("/**")
            //    .allowedOrigins(
          //          "http://localhost:3000", 
             //       "http://localhost:5173", 
              //      "http://127.0.0.1:5500", // Default for VS Code Live Server
              //      "http://localhost:8080"
              //  )
              //  .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
             //   .allowedHeaders("*")
             //   .allowCredentials(true);
   // }
}