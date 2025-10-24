#  Démarrage Rapide - MussoDeme API

##  5 Minutes pour Démarrer!

### **Étape 1: Créer votre configuration (30 secondes)**

```bash
# Windows (PowerShell)
Copy-Item .env.example .env

# Linux/Mac
cp .env.example .env
```

---

### **Étape 2: Générer un secret JWT sécurisé (30 secondes)**

**Option A: Utiliser le script automatique (RECOMMANDÉ)**

```bash
# Windows
.\setup-env.ps1

# Linux/Mac
chmod +x setup-env.sh
./setup-env.sh
```

**Option B: Générer manuellement**

```bash
# Windows PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))

# Linux/Mac
openssl rand -base64 64
```

Copiez le résultat dans `.env`:
```properties
JWT_SECRET=votre_secret_genere_ici
```

---

### **Étape 3: Configurer l'admin (1 minute)**

Éditez `.env` et changez les informations de l'admin:

```properties
ADMIN_EMAIL=admin@mussodeme.com
ADMIN_PASSWORD=VotreMotDePasseSecurise123!
ADMIN_NOM=Administrateur Système
```

 **IMPORTANT:** Utilisez un mot de passe FORT (12+ caractères, majuscules, minuscules, chiffres, symboles)

---

### **Étape 4: Configurer la base de données (1 minute)**

#### **Option A: MySQL local (par défaut)**
Assurez-vous que MySQL est démarré et créez la base:

```sql
CREATE DATABASE mussodeme_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Dans `.env`:
```properties
DB_URL=jdbc:mysql://localhost:3306/mussodeme_db
DB_USERNAME=root
DB_PASSWORD=votre_password_mysql
```

#### **Option B: Autre configuration**
Modifiez simplement les valeurs dans `.env`.

---

### **Étape 5: Lancer l'application! (2 minutes)**

```bash
mvn spring-boot:run
```

**Vous devriez voir:**
```
 Vérification de l'administrateur par défaut...
 Administrateur créé avec succès : admin@mussodeme.com
️ IMPORTANT: Changez le mot de passe par défaut pour la sécurité!

 JWT Configuration initialisée
   - Access Token Expiration: 86400000 ms (24 heures)
   - Refresh Token Expiration: 2592000000 ms (30 jours)

Started MussoDemeApplication in X.XXX seconds
```

---

##  C'est Prêt!

### **Testez l'API:**

#### **1. Inscription d'une Femme Rurale:**
```bash
curl -X POST http://localhost:5500/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Diarra",
    "prenom": "Fatoumata",
    "numeroTel": "+223 70 12 34 56",
    "localite": "Bamako",
    "motCle": "secret123"
  }'
```

#### **2. Connexion Admin:**
```bash
curl -X POST http://localhost:5500/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "identifiant": "admin@mussodeme.com",
    "motDePasse": "VotreMotDePasseSecurise123!"
  }'
```

#### **3. Connexion Femme Rurale:**
```bash
curl -X POST http://localhost:5500/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "identifiant": "+223 70 12 34 56",
    "motDePasse": "secret123"
  }'
```

---

##  Configuration Complète (Optionnel)

Éditez `.env` pour personnaliser:

```properties
# Serveur
SERVER_PORT=5500

# Base de données
DB_URL=jdbc:mysql://localhost:3306/mussodeme_db
DB_USERNAME=root
DB_PASSWORD=votre_password

# JWT (durées en millisecondes)
JWT_SECRET=votre_secret_super_securise_64_caracteres_minimum
JWT_ACCESS_TOKEN_EXPIRATION=86400000    # 24 heures
JWT_REFRESH_TOKEN_EXPIRATION=2592000000 # 30 jours

# Admin par défaut
ADMIN_EMAIL=admin@mussodeme.com
ADMIN_PASSWORD=VotreMotDePasseSecurise123!
ADMIN_NOM=Administrateur Système

# JPA
JPA_DDL_AUTO=update  # Options: create, update, validate, none
JPA_SHOW_SQL=true    # Afficher les requêtes SQL (dev uniquement)
```

---

##  Documentation Complète

Pour aller plus loin:

-  [`ENV_CONFIGURATION.md`](./ENV_CONFIGURATION.md) - Guide de configuration
-  [`CONFIGURATION_GUIDE.md`](./CONFIGURATION_GUIDE.md) - Guide technique complet
-  [`MIGRATION_ENV_VARS.md`](./MIGRATION_ENV_VARS.md) - Résumé de la migration
-  [`CORRECTIONS_AUTH_SERVICE.md`](./CORRECTIONS_AUTH_SERVICE.md) - Corrections authentification

---

## ️ Troubleshooting

### **Problème: "JWT secret manquant"**
```
Solution: Assurez-vous que JWT_SECRET est défini dans .env
```

### **Problème: "Cannot connect to MySQL"**
```
Solution: 
1. Vérifiez que MySQL est démarré
2. Vérifiez DB_URL, DB_USERNAME, DB_PASSWORD dans .env
3. Créez la base de données: CREATE DATABASE mussodeme_db;
```

### **Problème: "Admin not created"**
```
Solution: 
1. Vérifiez les logs pour voir l'erreur
2. Assurez-vous que ADMIN_EMAIL et ADMIN_PASSWORD sont définis
3. Vérifiez la connexion DB
```

### **Problème: Variables d'environnement non chargées**
```
Solution:
1. Windows: Utilisez .\setup-env.ps1
2. Linux/Mac: Utilisez ./setup-env.sh
3. Redémarrez votre IDE/terminal
```

---

##  Commandes Utiles

```bash
# Compiler le projet
mvn clean install

# Compiler sans tests
mvn clean install -DskipTests

# Lancer l'application
mvn spring-boot:run

# Lancer les tests
mvn test

# Créer un JAR exécutable
mvn clean package

# Lancer le JAR
java -jar target/MussoDeme-0.0.1-SNAPSHOT.jar
```

---

##  Conseils Pro

### **Développement:**
-  Utilisez `.env` local
-  Activez `JPA_SHOW_SQL=true`
-  Utilisez `JPA_DDL_AUTO=update`
-  Mot de passe simple OK

### **Production:**
-  Variables d'environnement système
-  Désactivez `JPA_SHOW_SQL=false`
-  Utilisez `JPA_DDL_AUTO=validate`
-  Mots de passe TRÈS forts
-  Secrets générés cryptographiquement
-  HTTPS obligatoire

---

##  Besoin d'Aide?

1. Consultez la [documentation complète](./CONFIGURATION_GUIDE.md)
2. Vérifiez les [problèmes courants](./ENV_CONFIGURATION.md#-résolution-de-problèmes)
3. Vérifiez les logs de l'application

---

**Bon développement! **
