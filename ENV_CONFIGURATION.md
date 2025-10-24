#  Configuration des Variables d'Environnement

##  Vue d'Ensemble

Ce projet utilise des **variables d'environnement** pour gérer les configurations sensibles (mots de passe, secrets JWT, etc.) au lieu de les mettre en dur dans le code. Ceci améliore la sécurité et facilite le déploiement multi-environnement.

---

##  Démarrage Rapide (5 minutes)

### **1. Copier le fichier template**
```bash
# Windows (PowerShell)
Copy-Item .env.example .env

# Linux/Mac
cp .env.example .env
```

### **2. Éditer le fichier `.env`**
Ouvrez `.env` et modifiez les valeurs selon vos besoins:
```properties
# Exemple de configuration locale
JWT_SECRET=votre_secret_jwt_super_long_et_securise_123456789
ADMIN_EMAIL=admin@localhost
ADMIN_PASSWORD=VotreMotDePasseAdmin123!
DB_PASSWORD=votre_password_mysql
```

### **3. Utiliser le script de configuration automatique**

**Windows:**
```powershell
.\setup-env.ps1
```

**Linux/Mac:**
```bash
chmod +x setup-env.sh
./setup-env.sh
```

### **4. Lancer l'application**
```bash
mvn spring-boot:run
```

---

##  Variables Disponibles

| Variable | Description | Valeur par Défaut | Obligatoire |
|----------|-------------|-------------------|-------------|
| `SERVER_PORT` | Port du serveur | `5500` | Non |
| `DB_URL` | URL de la base de données | `jdbc:mysql://localhost:3306/mussodeme_db` | Non |
| `DB_USERNAME` | Utilisateur DB | `root` | Non |
| `DB_PASSWORD` | Mot de passe DB | _(vide)_ | Non |
| `JWT_SECRET` | Clé secrète JWT | _(default dev key)_ | **Oui (prod)** |
| `JWT_ACCESS_TOKEN_EXPIRATION` | Durée access token (ms) | `86400000` (24h) | Non |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Durée refresh token (ms) | `2592000000` (30j) | Non |
| `ADMIN_EMAIL` | Email admin par défaut | `admin@mussodeme.com` | **Oui** |
| `ADMIN_PASSWORD` | Mot de passe admin | _(default)_ | **Oui** |
| `ADMIN_NOM` | Nom de l'admin | `Administrateur Système` | Non |
| `JPA_DDL_AUTO` | Mode Hibernate | `update` | Non |
| `JPA_SHOW_SQL` | Afficher SQL | `true` | Non |

---

##  Méthodes de Configuration

### **Méthode 1: Variables d'Environnement Système (Recommandé pour Production)**

**Windows (PowerShell):**
```powershell
# Session actuelle
$env:JWT_SECRET="votre_secret"
$env:ADMIN_EMAIL="admin@example.com"

# Permanent (utilisateur)
[System.Environment]::SetEnvironmentVariable('JWT_SECRET', 'votre_secret', 'User')
```

**Linux/Mac:**
```bash
# Session actuelle
export JWT_SECRET="votre_secret"
export ADMIN_EMAIL="admin@example.com"

# Permanent (ajouter à ~/.bashrc ou ~/.zshrc)
echo 'export JWT_SECRET="votre_secret"' >> ~/.bashrc
source ~/.bashrc
```

---

### **Méthode 2: Fichier .env (Recommandé pour Développement)**

1. Créez `.env` depuis le template
2. Éditez avec vos valeurs
3. Utilisez le script de setup pour charger les variables

** IMPORTANT:** Le fichier `.env` est dans `.gitignore` et ne sera jamais commité!

---

### **Méthode 3: Profils Spring Boot**

Créez des fichiers spécifiques:
- `application-dev.properties` → Développement
- `application-prod.properties` → Production

Activez avec:
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

---

##  Génération de Secrets Sécurisés

### **JWT Secret (256+ bits recommandé):**

**Windows (PowerShell):**
```powershell
# Générer un secret aléatoire
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

**Linux/Mac:**
```bash
# Avec OpenSSL
openssl rand -base64 64

# Avec Python
python3 -c "import secrets; print(secrets.token_urlsafe(64))"
```

### **Mots de Passe Forts:**

**Windows (PowerShell):**
```powershell
Add-Type -AssemblyName System.Web
[System.Web.Security.Membership]::GeneratePassword(16, 4)
```

**Linux/Mac:**
```bash
openssl rand -base64 32
```

---

##  Configuration par Environnement

### **Développement:**
```properties
# .env (développement)
JWT_SECRET=dev_secret_key_not_for_production_12345678
ADMIN_EMAIL=admin@localhost
ADMIN_PASSWORD=DevAdmin123!
DB_PASSWORD=
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true
```

### **Production:**
```bash
# Variables système ou gestionnaire de secrets
export JWT_SECRET="$(openssl rand -base64 64)"
export ADMIN_EMAIL="admin@mussodeme.com"
export ADMIN_PASSWORD="MotDePasseTresSecurise123!@#"
export DB_PASSWORD="MotDePasseDBProd456$%^"
export JPA_DDL_AUTO="validate"
export JPA_SHOW_SQL="false"
```

---

##  Checklist de Sécurité

### **Avant de démarrer:**
- [ ] Fichier `.env` créé depuis `.env.example`
- [ ] Variables sensibles modifiées (ne pas utiliser les valeurs par défaut)
- [ ] JWT secret > 32 caractères
- [ ] Mot de passe admin fort (12+ caractères)
- [ ] `.env` dans `.gitignore`

### **Avant la production:**
- [ ] Générer un nouveau JWT secret fort
- [ ] Changer tous les mots de passe par défaut
- [ ] Utiliser variables d'environnement système (pas de fichier .env)
- [ ] `JPA_DDL_AUTO=validate` (JAMAIS create en prod!)
- [ ] `JPA_SHOW_SQL=false`
- [ ] HTTPS activé
- [ ] Sauvegardes DB configurées

---

##  Documentation Complète

Pour plus de détails, consultez:
- [`CONFIGURATION_GUIDE.md`](./CONFIGURATION_GUIDE.md) - Guide complet de configuration
- [`.env.example`](./.env.example) - Template des variables

---

##  Résolution de Problèmes

### **Les variables ne se chargent pas:**
1. Vérifiez que les variables sont définies:
   ```bash
   # Windows
   echo $env:JWT_SECRET
   
   # Linux/Mac
   echo $JWT_SECRET
   ```
2. Redémarrez votre IDE/terminal
3. Vérifiez la syntaxe dans `.env` (pas d'espaces autour du `=`)

### **Erreur "JWT secret manquant":**
- Assurez-vous que `JWT_SECRET` est défini
- Vérifiez le nom de la variable (sensible à la casse)

### **Admin non créé au démarrage:**
- Vérifiez les logs pour voir les erreurs
- Assurez-vous que `ADMIN_EMAIL` et `ADMIN_PASSWORD` sont définis
- Vérifiez la connexion à la base de données

---

##  Astuces

### **Charger rapidement les variables (Linux/Mac):**
```bash
source ./load-env.sh
```

### **Vérifier les variables chargées:**
```bash
# Windows
Get-ChildItem Env: | Where-Object { $_.Name -like "*JWT*" -or $_.Name -like "*ADMIN*" }

# Linux/Mac
env | grep -E "JWT|ADMIN"
```

### **IntelliJ IDEA - Charger .env automatiquement:**
1. Installer le plugin "EnvFile"
2. Run → Edit Configurations
3. EnvFile → Add → `.env`

---

##  Liens Utiles

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [12 Factor App - Configuration](https://12factor.net/config)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)

---

**Date de création:** 22 octobre 2025  
**Version:** 1.0  
**Status:**  Prêt pour utilisation
