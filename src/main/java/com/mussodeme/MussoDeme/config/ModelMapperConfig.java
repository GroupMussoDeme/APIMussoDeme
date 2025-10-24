package com.mussodeme.MussoDeme.config;

import com.mussodeme.MussoDeme.dto.CommandeDTO;
import com.mussodeme.MussoDeme.entities.Commande;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.PropertyMap;
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
        
        // Configuration personnalisée pour mapper le montant total de Commande vers CommandeDTO
        modelMapper.addMappings(new PropertyMap<Commande, CommandeDTO>() {
            @Override
            protected void configure() {
                map().setMontantTotal(source.getMontantTotal());
            }
        });
        
        return modelMapper; // Retourne l'instance configurée pour que Spring la gère comme un bean
    }
}