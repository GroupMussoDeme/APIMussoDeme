package com.mussodeme.MussoDeme.config;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Logger;

@Configuration
public class AdminResetConfig {

    private static final Logger logger = Logger.getLogger(AdminResetConfig.class.getName());

    @Value("${ADMIN_EMAIL:djassikone22@gmail.com}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:Admin@MussoDemeV1!}")
    private String adminPassword;

    @Bean
    public CommandLineRunner resetAdmin(AdminRepository adminRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            adminRepository.findByEmail(adminEmail).ifPresentOrElse(
                    admin -> {
                        // On remet un mot de passe propre et on force les champs importants
                        admin.setMotDePasse(passwordEncoder.encode(adminPassword));
                        admin.setActive(true);
                        admin.setRole(Role.ADMIN);

                        adminRepository.save(admin);
                        logger.info("✅ Admin réinitialisé : " + adminEmail);
                    },
                    () -> {
                        // Si jamais il n’existe pas, on le crée
                        Admin admin = new Admin();
                        admin.setNom("Administrateur Système");
                        admin.setEmail(adminEmail);
                        admin.setMotDePasse(passwordEncoder.encode(adminPassword));
                        admin.setActive(true);
                        admin.setRole(Role.ADMIN);
                        adminRepository.save(admin);

                        logger.info("✅ Admin créé car absent : " + adminEmail);
                    }
            );
        };
    }
}
