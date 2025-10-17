package com.mussodeme.MussoDeme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Dossier local "uploads" exposé à travers l’URL "/uploads/**"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}

