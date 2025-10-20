package com.mussodeme.MussoDeme.config;

import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Dotenv dotenv;

    // Constructeur
    public AdminInitializer() {
        // Chargement du fichier .env à la racine du projet
        this.dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .ignoreIfMissing()
                .load();
    }

    @PostConstruct
    public void init() {
        String email = dotenv.get("APP_ADMIN_EMAIL");
        String password = dotenv.get("APP_ADMIN_PASSWORD");

        if (email == null || password == null) {
            System.err.println("❌ Variables APP_ADMIN_EMAIL ou APP_ADMIN_PASSWORD manquantes dans le fichier .env");
            return;
        }

        boolean exists = adminRepository.findByEmail(email).isPresent();
        if (!exists) {
            Admin admin = Admin.builder()
                    .nom("Administrateur")
                    .email(email)
                    .motDePasse(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            adminRepository.save(admin);
            System.out.println("✅ Administrateur créé avec succès : " + email);
        } else {
            System.out.println("ℹ️ Un administrateur existe déjà : " + email);
        }
    }
}
