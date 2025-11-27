package com.mussodeme.MussoDeme.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        // Permet de gérer correctement les requêtes multipart avec charset
        resolver.setStrictServletCompliance(false);
        return resolver;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(2L * 1024 * 1024 * 1024)); // 2GB
        factory.setMaxRequestSize(DataSize.ofBytes(2L * 1024 * 1024 * 1024)); // 2GB
        return factory.createMultipartConfig();
    }
}