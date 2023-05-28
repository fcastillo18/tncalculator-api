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

    @Value("${cors.allow.all.origins}")
    private boolean allowAllOrigins;

    @Value("${cors.allowed.urls}")
    private String[] allowedUrls;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if(allowAllOrigins){
                    registry.addMapping("/**")
                            .allowedOriginPatterns(CorsConfiguration.ALL) // We will allow all origins
                            .maxAge(maxAge)
                            .allowedMethods("*")
                            .allowedHeaders("*");
                } else {
                    registry.addMapping("/**")
                            .allowedOriginPatterns(allowedUrls) // We will allow just these origins
                            .maxAge(maxAge)
                            .allowedMethods("*")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                }
            }
        };
    }
}
