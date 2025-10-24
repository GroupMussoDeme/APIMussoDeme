package com.mussodeme.MussoDeme.services;

import com.mussodeme.MussoDeme.dto.FemmeRuraleDTO;
import com.mussodeme.MussoDeme.dto.LoginRequest;
import com.mussodeme.MussoDeme.dto.LoginResponse;
import com.mussodeme.MussoDeme.dto.RefreshTokenRequest;
import com.mussodeme.MussoDeme.dto.RegisterRequest;
import com.mussodeme.MussoDeme.entities.Admin;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.enums.Role;
import com.mussodeme.MussoDeme.enums.TypeNotif;
import com.mussodeme.MussoDeme.exceptions.InvalidCredentialsException;
import com.mussodeme.MussoDeme.repository.AdminRepository;
import com.mussodeme.MussoDeme.repository.FemmeRuraleRepository;
import com.mussodeme.MussoDeme.security.util.JwtUtils;
import com.mussodeme.MussoDeme.services.NotificationService;
import com.mussodeme.MussoDeme.services.SMSService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.logging.Logger;

@Service
@Validated
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    private final AdminRepository adminRepository;
    private final FemmeRuraleRepository femmeRuraleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final NotificationService notificationService;
    private final SMSService smsService;

    // Constructor for dependency injection
    public AuthService(AdminRepository adminRepository, FemmeRuraleRepository femmeRuraleRepository,
                       PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
                       NotificationService notificationService, SMSService smsService) {
        this.adminRepository = adminRepository;
        this.femmeRuraleRepository = femmeRuraleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.notificationService = notificationService;
        this.smsService = smsService;
    }

    /**
     * Connexion (Admin par email / Femme Rurale par numéro)
     */
    public LoginResponse login(LoginRequest request) {
        String identifiant = request.getIdentifiant();
        String motDePasse = request.getMotDePasse();

        if (identifiant == null || identifiant.isBlank() || motDePasse == null || motDePasse.isBlank()) {
            logger.warning("Tentative de connexion avec des identifiants manquants");
            throw new BadCredentialsException("Identifiant ou mot de passe invalide");
        }

        boolean isEmail = identifiant.contains("@");

        if (isEmail) {
            return loginAdmin(identifiant, motDePasse);
        } else {
            return loginFemmeRurale(identifiant, motDePasse);
        }
    }

    /**
     * Connexion Admin
     */
    private LoginResponse loginAdmin(String email, String motDePasse) {
        logger.info("Tentative de connexion Admin avec email: " + email);
        
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warning("Tentative de connexion échouée pour l'email: " + email);
                    return new BadCredentialsException("Identifiant ou mot de passe invalide");
                });

        if (!passwordEncoder.matches(motDePasse, admin.getMotDePasse())) {
            logger.warning("Échec d'authentification pour l'admin: " + email);
            throw new BadCredentialsException("Identifiant ou mot de passe invalide");
        }

        String accessToken = jwtUtils.generateAccessToken(admin.getEmail(), admin.getId(), "ROLE_ADMIN");
        String refreshToken = jwtUtils.generateRefreshToken(admin.getEmail(), admin.getId());
        
        logger.info("Connexion réussie pour l'admin: " + email);
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(admin.getEmail())
                .role("ROLE_ADMIN")
                .userId(admin.getId())
                .expiresIn(jwtUtils.getAccessTokenExpiration())
                .build();
    }

    /**
     * Connexion Femme Rurale
     */
    private LoginResponse loginFemmeRurale(String numeroTel, String motCle) {
        logger.info("Tentative de connexion Femme Rurale avec numéro: " + numeroTel);
        
        FemmeRurale femme = femmeRuraleRepository.findByNumeroTel(numeroTel)
                .orElseThrow(() -> {
                    logger.warning("Tentative de connexion échouée pour le numéro: " + numeroTel);
                    return new BadCredentialsException("Identifiant ou mot de passe invalide");
                });

        if (!passwordEncoder.matches(motCle, femme.getMotCle())) {
            logger.warning("Échec d'authentification pour la femme rurale: " + numeroTel);
            throw new BadCredentialsException("Identifiant ou mot de passe invalide");
        }

        if (!femme.isActive()) {
            logger.warning("Tentative de connexion avec un compte désactivé: " + numeroTel);
            throw new BadCredentialsException("Votre compte est désactivé. Veuillez contacter l'administrateur.");
        }

        String accessToken = jwtUtils.generateAccessToken(femme.getNumeroTel(), femme.getId(), "ROLE_FEMME_RURALE");
        String refreshToken = jwtUtils.generateRefreshToken(femme.getNumeroTel(), femme.getId());
        
        logger.info("Connexion réussie pour la femme rurale: " + numeroTel);
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(femme.getNumeroTel())
                .role("ROLE_FEMME_RURALE")
                .userId(femme.getId())
                .expiresIn(jwtUtils.getAccessTokenExpiration())
                .build();
    }

    /**
     * Rafraîchir le token d'accès avec un refresh token
     */
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            
            // Vérifier que c'est bien un refresh token
            String tokenType = jwtUtils.getTokenType(refreshToken);
            if (!"REFRESH".equals(tokenType)) {
                logger.warning("Tentative d'utilisation d'un token non-refresh pour le rafraîchissement");
                throw new BadCredentialsException("Token invalide");
            }
            
            String username = jwtUtils.getUsernameFromToken(refreshToken);
            Long userId = jwtUtils.getUserIdFromToken(refreshToken);
            
            // Déterminer le type d'utilisateur et générer de nouveaux tokens
            boolean isEmail = username.contains("@");
            
            if (isEmail) {
                // Refresh pour Admin
                Admin admin = adminRepository.findByEmail(username)
                        .orElseThrow(() -> new BadCredentialsException("Utilisateur non trouvé"));
                
                String newAccessToken = jwtUtils.generateAccessToken(admin.getEmail(), admin.getId(), "ROLE_ADMIN");
                String newRefreshToken = jwtUtils.generateRefreshToken(admin.getEmail(), admin.getId());
                
                logger.info("Token rafraîchi avec succès pour l'admin: " + username);
                
                return LoginResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .username(admin.getEmail())
                        .role("ROLE_ADMIN")
                        .userId(admin.getId())
                        .expiresIn(jwtUtils.getAccessTokenExpiration())
                        .build();
            } else {
                // Refresh pour Femme Rurale
                FemmeRurale femme = femmeRuraleRepository.findByNumeroTel(username)
                        .orElseThrow(() -> new BadCredentialsException("Utilisateur non trouvé"));
                
                if (!femme.isActive()) {
                    throw new BadCredentialsException("Compte désactivé");
                }
                
                String newAccessToken = jwtUtils.generateAccessToken(femme.getNumeroTel(), femme.getId(), "ROLE_FEMME_RURALE");
                String newRefreshToken = jwtUtils.generateRefreshToken(femme.getNumeroTel(), femme.getId());
                
                logger.info("Token rafraîchi avec succès pour la femme rurale: " + username);
                
                return LoginResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .username(femme.getNumeroTel())
                        .role("ROLE_FEMME_RURALE")
                        .userId(femme.getId())
                        .expiresIn(jwtUtils.getAccessTokenExpiration())
                        .build();
            }
            
        } catch (Exception e) {
            logger.severe("Erreur lors du rafraîchissement du token: " + e.getMessage());
            throw new BadCredentialsException("Token invalide ou expiré");
        }
    }

    /**
     * Inscription Femme Rurale
     */
    public FemmeRuraleDTO registerFemmeRurale(@Valid RegisterRequest request) {
        logger.info("Tentative d'inscription pour le numéro: " + request.getNumeroTel());
        
        // Validation du numéro de téléphone
        String numeroTel = request.getNumeroTel().trim();
        if (!isValidPhoneNumber(numeroTel)) {
            logger.warning("Format de numéro de téléphone invalide: " + numeroTel);
            throw new IllegalArgumentException("Le format du numéro de téléphone est invalide");
        }
        
        // Vérifier si le numéro existe déjà
        if (femmeRuraleRepository.existsByNumeroTel(numeroTel)) {
            logger.warning("Tentative d'inscription avec un numéro déjà utilisé: " + numeroTel);
            throw new IllegalArgumentException("Ce numéro est déjà utilisé");
        }
        
        // Validation du mot clé (au moins 4 caractères)
        if (request.getMotCle().length() < 4) {
            logger.warning("Mot clé trop court lors de l'inscription");
            throw new IllegalArgumentException("Le mot clé doit contenir au moins 4 caractères");
        }

        FemmeRurale femme = new FemmeRurale();
        femme.setNom(request.getNom().trim());
        femme.setPrenom(request.getPrenom() != null ? request.getPrenom().trim() : null);
        femme.setNumeroTel(numeroTel);
        femme.setLocalite(request.getLocalite().trim());
        femme.setMotCle(passwordEncoder.encode(request.getMotCle()));
        femme.setRole(Role.FEMME_RURALE);
        femme.setActive(true);

        FemmeRurale saved = femmeRuraleRepository.save(femme);
        
        // Envoyer notification et SMS de bienvenue
        notificationService.creerNotification(saved, TypeNotif.INFO, 
            "Bienvenue dans MussoDeme ! Votre inscription a été effectuée avec succès.");
        
        // Envoyer SMS de bienvenue
        smsService.envoyerSMSBienvenue(saved);
        
        logger.info("Inscription réussie pour la femme rurale: " + saved.getNumeroTel());

        return new FemmeRuraleDTO(
                saved.getId(),
                saved.getNom(),
                saved.getPrenom(),
                saved.getLocalite(),
                saved.getNumeroTel(),
                null, // NE JAMAIS retourner le mot de passe
                saved.getRole(),
                saved.isActive()
        );
    }
    
    /**
     * Valide le format du numéro de téléphone
     * Accepte les formats internationaux et locaux
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Pattern pour numéros internationaux et locaux
        // Ex: +223 XX XX XX XX, 00223 XX XX XX XX, XX XX XX XX
        String phonePattern = "^(\\+?[0-9]{1,4})?[\\s.-]?\\(?[0-9]{1,4}\\)?[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$";
        return phoneNumber != null && phoneNumber.matches(phonePattern) && phoneNumber.length() >= 8;
    }
}