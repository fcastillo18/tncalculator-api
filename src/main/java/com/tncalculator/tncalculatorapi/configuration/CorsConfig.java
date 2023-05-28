package com.tncalculator.tncalculatorapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${cors.maxAge}")
    private Long maxAge;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // TODO if we need to add custom origins, we can then add it here or do a impl with properties
//                        .allowedOrigins("http://localhost:5173")
//                        .allowCredentials(true)
                        .allowedOriginPatterns(CorsConfiguration.ALL) // We will allow all origins
                        .maxAge(maxAge)
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
