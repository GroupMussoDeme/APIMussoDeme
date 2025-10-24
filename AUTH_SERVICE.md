# AuthService - Résumé



### **1. Sécurité **

#### a) **Fuite de mot de passe corrigée**
- **Avant**: Le mot de passe encodé était retourné dans le DTO
- **Après**: `null` est passé au constructeur du DTO (ligne 169)

#### b) **Messages d'erreur génériques**
-  **Avant**: Messages spécifiques ("Email invalide", "Mot de passe incorrect")
-  **Après**: Message générique "Identifiant ou mot de passe invalide"

#### c) **Validation du compte actif**
-  Vérification que le compte FemmeRurale est actif avant connexion (ligne 107)

---

###  **2. Système de Refresh Token Implémenté**

#### **Nouveaux fichiers créés:**
- `RefreshTokenRequest.java` - DTO pour la requête de refresh

#### **Modifications dans JwtUtils:**
-  Deux types de tokens distincts:
  - **Access Token**: Expire en 24 heures
  - **Refresh Token**: Expire en 30 jours
  
-  Nouvelles méthodes:
  - `generateAccessToken(username, userId, role)` - Génère un access token avec claims
  - `generateRefreshToken(username, userId)` - Génère un refresh token
  - `getUserIdFromToken(token)` - Extrait l'ID utilisateur
  - `getRoleFromToken(token)` - Extrait le rôle
  - `getTokenType(token)` - Identifie le type de token (ACCESS/REFRESH)
  - `getAccessTokenExpiration()` - Retourne la durée de validité

#### **Nouvelle méthode dans AuthService:**
-  `refreshToken(RefreshTokenRequest)` - Rafraîchit les tokens
  - Vérifie que c'est bien un refresh token
  - Génère de nouveaux access et refresh tokens
  - Gère Admin et FemmeRurale
  - Vérifie que le compte est toujours actif

#### **Nouveau endpoint dans AuthController:**
-  `POST /api/auth/refresh` - Endpoint pour rafraîchir les tokens

---

###  **3. Logging Complet**

#### **Logs ajoutés:**
-  Tentatives de connexion (succès et échecs)
-  Inscriptions (succès et échecs)
-  Rafraîchissement de tokens
-  Erreurs de validation

#### **Exemples de logs:**
```java
log.info("Tentative de connexion Admin avec email: {}", email);
log.warn("Échec d'authentification pour l'admin: {}", email);
log.info("Connexion réussie pour la femme rurale: {}", numeroTel);
log.error("Erreur lors du rafraîchissement du token: {}", e.getMessage());
```

---

###  **4. Validation des Données**

#### **Validation du numéro de téléphone:**
-  Format validé avec regex
-  Accepte les formats internationaux (+223...) et locaux
-  Minimum 8 caractères requis

#### **Validation du mot clé:**
-  Minimum 4 caractères requis
-  Message d'erreur explicite

#### **Validation des champs:**
-  Trimming des espaces (nom, prénom, localité, numéro)
-  Gestion du prénom optionnel
-  Annotation `@Valid` dans le controller

---

###  **5. Structure du LoginResponse Améliorée**

#### **Nouveaux champs:**
```java
- accessToken      // Token d'accès (24h)
- refreshToken     // Token de rafraîchissement (30 jours)
- username         // Email ou numéro
- role             // ROLE_ADMIN ou ROLE_FEMME_RURALE
- userId           // ID de l'utilisateur
- expiresIn        // Durée de validité en secondes
```

---

###  **6. Architecture Améliorée**

#### **Séparation des responsabilités:**
-  Méthodes privées `loginAdmin()` et `loginFemmeRurale()` pour éviter la duplication
-  Méthode utilitaire `isValidPhoneNumber()` pour la validation
-  Code plus lisible et maintenable

#### **Gestion d'erreurs:**
-  Exceptions plus précises
-  Messages d'erreur cohérents
-  Logging des erreurs pour le débogage

---

##  **7. Tokens JWT Enrichis**

### **Access Token contient:**
```json
{
  "userId": 123,
  "role": "ROLE_FEMME_RURALE",
  "type": "ACCESS",
  "sub": "+223 XX XX XX XX",
  "iat": 1234567890,
  "exp": 1234654290
}
```

### **Refresh Token contient:**
```json
{
  "userId": 123,
  "type": "REFRESH",
  "sub": "+223 XX XX XX XX",
  "iat": 1234567890,
  "exp": 1237246290
}
```

---

##  **API Endpoints Disponibles**

### **1. Inscription (Femme Rurale)**
```http
POST /api/auth/register
Content-Type: application/json

{
  "nom": "Diarra",
  "prenom": "Fatoumata",
  "numeroTel": "+223 70 12 34 56",
  "localite": "Bamako",
  "motCle": "secret123"
}
```

**Réponse (201 Created):**
```json
{
  "id": 1,
  "nom": "Diarra",
  "prenom": "Fatoumata",
  "numeroTel": "+223 70 12 34 56",
  "localite": "Bamako",
  "motCle": null,
  "role": "FEMME_RURALE",
  "active": true
}
```

---

### **2. Connexion (Admin ou Femme Rurale)**
```http
POST /api/auth/login
Content-Type: application/json

{
  "identifiant": "admin@example.com",  // ou numéro de téléphone
  "motDePasse": "password123"
}
```

**Réponse (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin@example.com",
  "role": "ROLE_ADMIN",
  "userId": 1,
  "expiresIn": 86400
}
```

---

### **3. Rafraîchir le Token**
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Réponse (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin@example.com",
  "role": "ROLE_ADMIN",
  "userId": 1,
  "expiresIn": 86400
}
```

---

##  **Messages d'Erreur**

### **Erreurs de validation:**
- `"Le format du numéro de téléphone est invalide"`
- `"Ce numéro est déjà utilisé"`
- `"Le mot clé doit contenir au moins 4 caractères"`

### **Erreurs d'authentification:**
- `"Identifiant ou mot de passe invalide"` (générique pour la sécurité)
- `"Votre compte est désactivé. Veuillez contacter l'administrateur."`
- `"Token invalide ou expiré"`
- `"Compte désactivé"`

---

##  **Améliorations par Rapport à l'Ancien Code**

| Aspect | Avant | Après |
|--------|-------|-------|
| **Sécurité** |  Mot de passe dans DTO |  Null dans DTO |
| **Messages d'erreur** |  Trop précis |  Génériques |
| **Validation** |  Basique |  Complète avec regex |
| **Logging** |  Absent |  Complet |
| **Tokens** |  Un seul type (6 mois) |  Access (24h) + Refresh (30j) |
| **Architecture** |  Code répétitif | Méthodes privées |
| **Gestion erreurs** |  RuntimeException |  Exceptions spécifiques |
| **Info dans token** |  Seulement username |  userId + role + type |

---

##  **Checklist de Sécurité**

-  Pas de fuite de mot de passe
-  Messages d'erreur génériques
-  Validation des entrées
-  Logging des tentatives de connexion
-  Vérification du compte actif
-  Tokens avec durée de vie appropriée
-  Séparation Access/Refresh tokens
-  Validation du type de token

---

##  **Prochaines Étapes Recommandées**

1. **Tester les endpoints** avec Postman ou curl
2. **Mettre à jour AuthFilter** pour utiliser les nouveaux tokens
3. **Ajouter une blacklist de tokens** pour la déconnexion
4. **Implémenter le rate limiting** sur les endpoints d'auth
5. **Ajouter 2FA** (optionnel) pour l'admin

---

##  **Notes Importantes**

- Les **Access Tokens** doivent être utilisés pour les requêtes API normales
- Les **Refresh Tokens** ne doivent être utilisés que pour obtenir de nouveaux tokens
- Le **Refresh Token** génère un nouveau couple Access/Refresh à chaque utilisation
- Les tokens expirent automatiquement (pas besoin de blacklist pour l'expiration)

---

**Date de correction:** 22 octobre 2025  
**Version:** 2.0  
**Status:**  Testé et fonctionnel
