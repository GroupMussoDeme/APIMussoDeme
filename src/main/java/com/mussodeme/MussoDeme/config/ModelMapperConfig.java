package com.mussodeme.MussoDeme.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true) // Permet de mapper les champs (même sans getter/setter explicite)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // Autorise l'accès aux champs privés (par réflexion)
                .setMatchingStrategy(MatchingStrategies.STANDARD); // Définit la stratégie de correspondance (STANDARD = mapping flexible et courant)
        return modelMapper; // Retourne l'instance configurée pour que Spring la gère comme un bean
    }
}
