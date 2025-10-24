package com.mussodeme.MussoDeme.config;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class AdminInitializer {

    private static final Logger logger = Logger.getLogger(AdminInitializer.class.getName());

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.nom}")
    private String adminNom;

    // Constructor for dependency injection
    public AdminInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        logger.info(" Vérification de l'administrateur par défaut...");
        
        // Validation des variables
        if (adminEmail == null || adminEmail.isBlank()) {
            logger.severe(" Variable app.admin.email manquante ou vide");
            return;
        }
        
        if (adminPassword == null || adminPassword.isBlank()) {
            logger.severe(" Variable app.admin.password manquante ou vide");
            return;
        }

        // Vérifier si l'admin existe déjà
        boolean exists = adminRepository.findByEmail(adminEmail).isPresent();
        
        if (!exists) {
            Admin admin = new Admin();
            admin.setNom(adminNom);
            admin.setEmail(adminEmail);
            admin.setMotDePasse(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);

            adminRepository.save(admin);
            logger.info(" Administrateur créé avec succès : " + adminEmail);
            logger.warning(" IMPORTANT: Changez le mot de passe par défaut pour la sécurité!");
        } else {
            logger.info(" Un administrateur existe déjà : " + adminEmail);
        }
    }
}