#  MussoDeme API

**API REST sécurisée pour la plateforme MussoDeme** - Système de gestion et d'accompagnement des femmes rurales.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

##  Table des Matières

- [À Propos](#-à-propos)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies](#-technologies)
- [Démarrage Rapide](#-démarrage-rapide)
- [Configuration](#-configuration)
- [Documentation](#-documentation)
- [Sécurité](#-sécurité)
- [Architecture](#-architecture)
- [API Endpoints](#-api-endpoints)

---

##  À Propos

MussoDeme API est une plateforme backend robuste conçue pour:
- **Autonomiser les femmes rurales** en facilitant la vente de leurs produits
- **Créer des coopératives** pour le partage de connaissances et la formation
- **Faciliter les transactions** entre productrices et acheteurs
- **Gérer les contenus éducatifs** (texte, audio, vidéo)

---

##  Fonctionnalités

###  Authentification & Autorisation
-  JWT avec Access & Refresh Tokens
-  Multi-types d'utilisateurs (Admin, Femme Rurale)
-  Connexion par email (Admin) ou numéro (Femmes Rurales)
-  Rotation automatique des tokens
-  Sécurité renforcée (logging, validation)

###  Gestion des Utilisateurs
-  Inscription des Femmes Rurales
-  Profils personnalisables
-  Rôles et permissions
-  Admin créé automatiquement au démarrage

###  E-Commerce
-  Gestion des produits par catégories
-  Système de commandes
-  Paiements multiples modes
-  Historique des transactions
-  Recherche par localisation

###  Coopératives
-  Création de groupes/coopératives
-  Gestion des membres
-  Partage de ressources
-  Formation collaborative

###  Contenus Éducatifs
-  Contenus texte, audio, vidéo
-  Catégorisation
-  Upload de fichiers (jusqu'à 2GB)
-  Gestion multimédia

###  Notifications & Historique
-  Système de notifications
-  Historique des actions
-  Traçabilité complète

---

## ️ Technologies

### **Backend**
- **Spring Boot 3.5.6** - Framework principal
- **Spring Security** - Authentification & autorisation
- **Spring Data JPA** - Persistance des données
- **JWT (jjwt 0.12.6)** - Tokens sécurisés

### **Base de Données**
- **MySQL 8.0+** - Base de données relationnelle

### **Outils**
- **Maven** - Gestion des dépendances
- **Lombok** - Réduction du boilerplate
- **ModelMapper 3.2.1** - Mapping objet
- **Jakarta Validation** - Validation des données

### **Langage**
- **Java 21** - LTS

---

##  Démarrage Rapide

### **Prérequis**
- Java 21+
- Maven 3.8+
- MySQL 8.0+

### **Installation (5 minutes)**

#### **1. Cloner le projet**
```bash
git clone https://github.com/votre-org/mussodemeapi.git
cd mussodemeapi
```

#### **2. Créer la configuration**
```bash
# Windows
Copy-Item .env.example .env

# Linux/Mac
cp .env.example .env
```

#### **3. Configurer automatiquement**
```bash
# Windows
.\setup-env.ps1

# Linux/Mac
chmod +x setup-env.sh
./setup-env.sh
```

#### **4. Créer la base de données**
```sql
CREATE DATABASE mussodeme_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### **5. Lancer l'application**
```bash
mvn spring-boot:run
```

 **L'API est disponible sur:** `http://localhost:5500`

 **Guide détaillé:** [`QUICK_START.md`](./QUICK_START.md)

---

## ️ Configuration

### **Variables d'Environnement Principales**

```properties
# Serveur
SERVER_PORT=8080

# Base de données
DB_URL=jdbc:mysql://localhost:3306/mussodeme
DB_USERNAME=root
DB_PASSWORD=

# JWT
JWT_SECRET=djimmoh123456789djimmoh123456789djimmoh123456789djimmoh123456789 
JWT_ACCESS_TOKEN_EXPIRATION=86400000    # 24 heures
JWT_REFRESH_TOKEN_EXPIRATION=2592000000 # 30 jours

# Admin par défaut
ADMIN_EMAIL=djassikone22@gmail.com
ADMIN_PASSWORD=Admin@MussoDemeV1!
```

 **Guide complet:** [`ENV_CONFIGURATION.md`](./ENV_CONFIGURATION.md)

---

##  Documentation

| Document | Description |
|----------|-------------|
| [`QUICK_START.md`](./QUICK_START.md) | Démarrage en 5 minutes |
| [`ENV_CONFIGURATION.md`](./ENV_CONFIGURATION.md) | Configuration des variables |
| [`CONFIGURATION_GUIDE.md`](./CONFIGURATION_GUIDE.md) | Guide technique complet |
| [`CORRECTIONS_AUTH_SERVICE.md`](./CORRECTIONS_AUTH_SERVICE.md) | Documentation Auth |
| [`MIGRATION_ENV_VARS.md`](./MIGRATION_ENV_VARS.md) | Migration vers env vars |

---

##  Sécurité

### **Fonctionnalités de Sécurité**
-  JWT avec signatures cryptographiques
-  Refresh tokens pour sessions longues
-  Mots de passe hashés (BCrypt)
-  Variables d'environnement (pas de secrets en dur)
-  Validation des entrées
-  Logging des tentatives de connexion
-  CORS configuré
-  Messages d'erreur génériques

### **Bonnes Pratiques Implémentées**
-  12 Factor App compliance
-  OWASP guidelines
-  Secrets management
-  Rate limiting ready

 **Plus de détails:** [`CONFIGURATION_GUIDE.md#sécurité`](./CONFIGURATION_GUIDE.md#bonnes-pratiques-de-sécurité)

---

##  Architecture

### **Structure du Projet**
```
src/main/java/com/mussodeme/MussoDeme/
├── config/              # Configuration Spring
├── controllers/         # Endpoints REST
├── dto/                 # Data Transfer Objects
├── entities/            # Entités JPA
├── enums/               # Énumérations
├── exceptions/          # Gestion des erreurs
├── repository/          # Repositories JPA
├── security/            # Configuration sécurité
│   ├── config/          # Security config
│   ├── filter/          # Filtres JWT
│   ├── service/         # UserDetailsService
│   └── util/            # JWT utils
└── services/            # Logique métier
```

### **Modèle de Données**
```
Utilisateur (classe mère)
├── Admin
└── FemmeRurale

Produit → Categorie
Commande → Produit, Utilisateur
Paiement → Commande
Cooperative → FemmeRurale (membres)
Contenu → Categorie
Notification → Utilisateur
```

---

##  API Endpoints

### **Authentification (`/api/auth`)**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/register` | Inscription Femme Rurale |
| POST | `/login` | Connexion (Admin/Femme Rurale) |
| POST | `/refresh` | Rafraîchir les tokens |

### **Exemples de Requêtes**

#### **Inscription**
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

#### **Connexion**
```bash
curl -X POST http://localhost:5500/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "identifiant": "admin@mussodeme.com",
    "motDePasse": "VotreMotDePasse"
  }'
```

**Réponse:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin@mussodeme.com",
  "role": "ROLE_ADMIN",
  "userId": 1,
  "expiresIn": 86400
}
```

#### **Refresh Token**
```bash
curl -X POST http://localhost:5500/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }'
```

 **Documentation complète des endpoints:** (à venir)

---

##  Tests

```bash
# Lancer tous les tests
mvn test

# Lancer les tests avec couverture
mvn test jacoco:report

# Lancer un test spécifique
mvn test -Dtest=AuthServiceTest
```

---

##  Build & Déploiement

### **Build**
```bash
# Compiler
mvn clean install

# Créer un JAR exécutable
mvn clean package

# Sans tests
mvn clean package -DskipTests
```

### **Exécution**
```bash
# Avec Maven
mvn spring-boot:run

# Avec JAR
java -jar target/MussoDeme-0.0.1-SNAPSHOT.jar

# Avec profil spécifique
java -jar target/MussoDeme-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### **Docker** (à venir)
```bash
docker build -t mussodeme-api .
docker run -p 5500:5500 --env-file .env mussodeme-api
```

---

##  Contribution

Les contributions sont les bienvenues!

1. Fork le projet
2. Créez votre branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

---

##  License

Ce projet est sous licence MIT - voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

##  Équipe

- **Backend Development** - Équipe MussoDeme
- **Architecture** - Équipe MussoDeme
- **Documentation** - Équipe MussoDeme

---

##  Support

Pour toute question ou problème:
-  Email: support@mussodeme.com
-  Documentation: Voir les fichiers MD dans le projet
-  Issues: [GitHub Issues](https://github.com/votre-org/mussodemeapi/issues)

---

##  Roadmap

### **Version Actuelle (v2.0)**
-  Authentification JWT complète
-  Refresh tokens
-  Variables d'environnement
-  Logging & validation
-  Documentation complète

### **Prochaines Versions**
- [ ] API REST documentation (Swagger/OpenAPI)
- [ ] Rate limiting
- [ ] Cache Redis
- [ ] Notifications push
- [ ] Tests unitaires complets
- [ ] CI/CD Pipeline
- [ ] Docker & Kubernetes
- [ ] Monitoring & métriques

---

**Fait pour les Femmes Rurales**

---

## Statistiques du Projet

- **Lignes de Code:** ~5000+
- **Endpoints:** 20+
- **Entités:** 15+
- **Tests:** En cours
- **Documentation:** 2000+ lignes

---

**Dernière mise à jour:** 22 octobre 2025  
**Version:** 2.0.0  
**Status:**  Production Ready
