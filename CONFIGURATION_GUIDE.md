#  Guide de Configuration - MussoDeme API

##  Table des Matières
1. [Configuration des Variables d'Environnement](#1-configuration-des-variables-denvironnement)
2. [Configuration pour le Développement](#2-configuration-pour-le-développement)
3. [Configuration pour la Production](#3-configuration-pour-la-production)
4. [Génération de Secrets Sécurisés](#4-génération-de-secrets-sécurisés)
5. [Bonnes Pratiques de Sécurité](#5-bonnes-pratiques-de-sécurité)

---

## 1️⃣ Configuration des Variables d'Environnement

### **Méthode 1: Utiliser les Variables d'Environnement Système**

#### **Windows (PowerShell):**
```powershell
# Définir temporairement (session actuelle)
$env:JWT_SECRET="votre_secret_jwt_super_securise_123456789"
$env:ADMIN_EMAIL="admin@mussodeme.com"
$env:ADMIN_PASSWORD="VotreMotDePasseSecurise123!"
$env:DB_PASSWORD="votre_mot_de_passe_db"

# Définir de manière permanente (utilisateur)
[System.Environment]::SetEnvironmentVariable('JWT_SECRET', 'votre_secret', 'User')
[System.Environment]::SetEnvironmentVariable('ADMIN_EMAIL', 'admin@mussodeme.com', 'User')
```

#### **Linux/Mac (Bash):**
```bash
# Définir temporairement (session actuelle)
export JWT_SECRET="votre_secret_jwt_super_securise_123456789"
export ADMIN_EMAIL="admin@mussodeme.com"
export ADMIN_PASSWORD="VotreMotDePasseSecurise123!"
export DB_PASSWORD="votre_mot_de_passe_db"

# Définir de manière permanente (ajouter à ~/.bashrc ou ~/.zshrc)
echo 'export JWT_SECRET="votre_secret"' >> ~/.bashrc
source ~/.bashrc
```

---

### **Méthode 2: Utiliser un Fichier .env (Recommandé pour le développement)**

#### **Étape 1: Créer le fichier `.env`**
```bash
# À la racine du projet
cp .env.example .env
```

#### **Étape 2: Éditer `.env` avec vos valeurs**
```properties
# Exemple de configuration locale
JWT_SECRET=ma_cle_secrete_jwt_dev_123456789
ADMIN_EMAIL=admin@localhost
ADMIN_PASSWORD=DevPassword123!
DB_PASSWORD=root_password
```

#### **Étape 3: Charger les variables au démarrage**

**Option A: Via IDE (IntelliJ IDEA)**
1. Run → Edit Configurations
2. Environment Variables → Add
3. Charger depuis le fichier `.env`

**Option B: Via ligne de commande**
```bash
# Windows PowerShell
Get-Content .env | ForEach-Object {
    if ($_ -match '^\s*([^#][^=]+)=(.+)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
    }
}

# Linux/Mac
export $(cat .env | grep -v '^#' | xargs)
```

---

### **Méthode 3: Fichiers de Profil Spring Boot**

Créez des fichiers spécifiques par environnement:

#### **`application-dev.properties`** (Développement)
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mussodeme_dev
spring.datasource.username=root
spring.datasource.password=

# JWT
jwt.secret=dev_secret_key_not_for_production
jwt.access-token-expiration=86400000
jwt.refresh-token-expiration=2592000000

# Admin
app.admin.email=admin@dev.local
app.admin.password=DevAdmin123!
app.admin.nom=Admin Développement

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### **`application-prod.properties`** (Production)
```properties
# Database (utilise les variables d'environnement)
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JWT (utilise les variables d'environnement)
jwt.secret=${JWT_SECRET}
jwt.access-token-expiration=${JWT_ACCESS_TOKEN_EXPIRATION:3600000}
jwt.refresh-token-expiration=${JWT_REFRESH_TOKEN_EXPIRATION:604800000}

# Admin (utilise les variables d'environnement)
app.admin.email=${ADMIN_EMAIL}
app.admin.password=${ADMIN_PASSWORD}
app.admin.nom=${ADMIN_NOM:Administrateur}

# JPA (Production)
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

**Activer un profil:**
```bash
# Développement
java -jar app.jar --spring.profiles.active=dev

# Production
java -jar app.jar --spring.profiles.active=prod
```

---

## 2️⃣ Configuration pour le Développement

### **Configuration Recommandée:**

```properties
# .env (développement)
SERVER_PORT=5500
DB_URL=jdbc:mysql://localhost:3306/mussodeme_dev
DB_USERNAME=root
DB_PASSWORD=

JWT_SECRET=dev_jwt_secret_key_12345678901234567890
JWT_ACCESS_TOKEN_EXPIRATION=86400000
JWT_REFRESH_TOKEN_EXPIRATION=2592000000

ADMIN_EMAIL=admin@dev.local
ADMIN_PASSWORD=Admin123!
ADMIN_NOM=Admin Dev

JPA_DDL_AUTO=update
JPA_SHOW_SQL=true
```

---

## 3️⃣ Configuration pour la Production

### **⚠️ IMPORTANT: Sécurité en Production**

 **À FAIRE:**
-  Utiliser des variables d'environnement système
-  Générer des secrets forts et uniques
-  Utiliser HTTPS uniquement
-  Changer le mot de passe admin par défaut
-  Utiliser un utilisateur DB avec privilèges limités
-  Désactiver `jpa.show-sql`
-  Utiliser `jpa.hibernate.ddl-auto=validate`

 **À NE PAS FAIRE:**
-  Commiter le fichier `.env`
-  Laisser les mots de passe par défaut
-  Utiliser `create` ou `create-drop` en production
-  Exposer les erreurs détaillées

### **Configuration Production:**

```bash
# Variables d'environnement serveur
export JWT_SECRET="$(openssl rand -base64 64)"
export ADMIN_EMAIL="admin@mussodeme.com"
export ADMIN_PASSWORD="MotDePasseTresFort123!@#"
export DB_URL="jdbc:mysql://prod-server:3306/mussodeme_prod"
export DB_USERNAME="mussodeme_user"
export DB_PASSWORD="MotDePasseDBSecurise456$%^"
export JPA_DDL_AUTO="validate"
export JPA_SHOW_SQL="false"
```

---

## 4️⃣ Génération de Secrets Sécurisés

### **JWT Secret:**

```bash
# Linux/Mac
openssl rand -base64 64

# PowerShell (Windows)
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))

# Python
python -c "import secrets; print(secrets.token_urlsafe(64))"
```

### **Mots de Passe Forts:**

```bash
# Linux/Mac
openssl rand -base64 32

# PowerShell (Windows)
Add-Type -AssemblyName System.Web
[System.Web.Security.Membership]::GeneratePassword(16, 4)
```

---

## 5️⃣ Bonnes Pratiques de Sécurité

###  **Checklist de Sécurité:**

#### **Configuration:**
- [ ] JWT secret > 256 bits (32+ caractères)
- [ ] Access token expire < 24 heures
- [ ] Refresh token expire < 30 jours
- [ ] Mot de passe admin > 12 caractères (majuscules, minuscules, chiffres, symboles)
- [ ] `.env` dans `.gitignore`
- [ ] Variables sensibles jamais en dur dans le code

#### **Base de Données:**
- [ ] Utilisateur DB avec privilèges minimums
- [ ] Mot de passe DB fort
- [ ] Connexions SSL/TLS en production
- [ ] Sauvegardes régulières
- [ ] `ddl-auto=validate` en production

#### **Application:**
- [ ] HTTPS obligatoire en production
- [ ] CORS configuré correctement
- [ ] Rate limiting sur les endpoints d'authentification
- [ ] Logging des tentatives de connexion
- [ ] Surveillance des erreurs

#### **Déploiement:**
- [ ] Variables d'environnement au niveau du serveur/conteneur
- [ ] Secrets dans un gestionnaire sécurisé (AWS Secrets Manager, HashiCorp Vault, etc.)
- [ ] Rotation régulière des secrets
- [ ] Monitoring et alertes

---

##  Exemples de Configuration par Environnement

### **Développement Local (Windows):**

**PowerShell:**
```powershell
# Créer un fichier de variables
$envVars = @"
JWT_SECRET=dev_secret_12345678901234567890
ADMIN_EMAIL=admin@localhost
ADMIN_PASSWORD=Admin123!
DB_PASSWORD=
"@

# Charger les variables
$envVars -split "`n" | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        $env:($matches[1].Trim()) = $matches[2].Trim()
    }
}

# Lancer l'application
mvn spring-boot:run
```

---

### **Production (Docker):**

**docker-compose.yml:**
```yaml
version: '3.8'
services:
  mussodeme-api:
    image: mussodeme/api:latest
    environment:
      - JWT_SECRET=${JWT_SECRET}
      - ADMIN_EMAIL=${ADMIN_EMAIL}
      - ADMIN_PASSWORD=${ADMIN_PASSWORD}
      - DB_URL=jdbc:mysql://db:3306/mussodeme_prod
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
      - JPA_DDL_AUTO=validate
      - JPA_SHOW_SQL=false
    env_file:
      - .env.production  #  Ne jamais commiter ce fichier!
```

---

### **Production (Kubernetes):**

**secret.yaml:**
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mussodeme-secrets
type: Opaque
stringData:
  jwt-secret: "votre_secret_jwt_base64"
  admin-email: "admin@mussodeme.com"
  admin-password: "VotreMotDePasseSecurise"
  db-password: "VotreMotDePasseDB"
```

**deployment.yaml:**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mussodeme-api
spec:
  template:
    spec:
      containers:
      - name: api
        image: mussodeme/api:latest
        env:
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: mussodeme-secrets
              key: jwt-secret
        - name: ADMIN_EMAIL
          valueFrom:
            secretKeyRef:
              name: mussodeme-secrets
              key: admin-email
        # ... autres variables
```

---

##  Démarrage Rapide

### **Pour commencer immédiatement (DEV):**

1. **Copiez le template:**
   ```bash
   cp .env.example .env
   ```

2. **Éditez `.env` avec vos valeurs**

3. **Chargez les variables:**
   ```bash
   # Linux/Mac
   export $(cat .env | grep -v '^#' | xargs)
   
   # Windows PowerShell
   Get-Content .env | ForEach-Object {
       if ($_ -match '^\s*([^#][^=]+)=(.+)$') {
           [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
       }
   }
   ```

4. **Lancez l'application:**
   ```bash
   mvn spring-boot:run
   ```

---

## ❓ FAQ

### **Q: Mes variables d'environnement ne sont pas chargées?**
**R:** Vérifiez que:
- Les variables sont bien définies (`echo $JWT_SECRET` ou `echo $env:JWT_SECRET`)
- Vous avez redémarré votre IDE/terminal
- Le nom de la variable correspond exactement (sensible à la casse)

### **Q: Dois-je utiliser .env ou application.properties?**
**R:** 
- **DEV**: `.env` ou `application-dev.properties` sont OK
- **PROD**: Variables d'environnement système (Docker, K8s, etc.)
- **Best practice**: Variables d'environnement partout

### **Q: Comment changer le mot de passe admin après le premier démarrage?**
**R:** Créez un endpoint dans `AdminController` pour permettre à l'admin de changer son mot de passe.

---

**Date de création:** 22 octobre 2025  
**Version:** 1.0  
**Auteur:** Équipe MussoDeme
