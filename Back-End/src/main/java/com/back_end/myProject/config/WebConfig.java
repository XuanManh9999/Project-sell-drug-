package com.back_end.myProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig  implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")  // Cho phép mọi đường dẫn
                .allowedOrigins("http://localhost:5173/")  // Cho phép mọi domain
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Các method cho phép
                .allowedHeaders("*")  // Các header cho phép
                .exposedHeaders("Authorization");  // Header được lộ ra
    }
}
