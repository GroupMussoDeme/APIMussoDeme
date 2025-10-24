# 📡 Admin API Endpoints - Documentation

## 🔐 Authentification

Tous les endpoints nécessitent:
- ✅ **Token JWT** valide dans le header `Authorization: Bearer {token}`
- ✅ **Rôle ADMIN** requis (`@PreAuthorize("hasRole('ADMIN')")`)

---

## 📋 Table des Matières

1. [Gestion du Profil Admin](#1-gestion-du-profil-admin)
2. [Gestion des Contenus](#2-gestion-des-contenus)
3. [Gestion des Institutions Financières](#3-gestion-des-institutions-financières)

---

## 1️⃣ Gestion du Profil Admin

### **GET /api/admin/profile/{id}**
Récupérer le profil d'un admin par son ID.

**Requête:**
```http
GET /api/admin/profile/1
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
{
  "id": 1,
  "nom": "Administrateur",
  "email": "admin@mussodeme.com",
  "role": "ADMIN"
}
```

---

### **GET /api/admin/profile/email/{email}**
Récupérer le profil d'un admin par son email.

**Requête:**
```http
GET /api/admin/profile/email/admin@mussodeme.com
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
{
  "id": 1,
  "nom": "Administrateur",
  "email": "admin@mussodeme.com",
  "role": "ADMIN"
}
```

---

### **PUT /api/admin/profile/{id}**
Mettre à jour le profil d'un admin.

**Requête:**
```http
PUT /api/admin/profile/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Nouvel Admin",
  "email": "nouvel.admin@mussodeme.com",
  "ancienMotDePasse": "oldPassword123",
  "nouveauMotDePasse": "newSecurePassword456!"
}
```

**Champs (tous optionnels):**
- `nom` - Nouveau nom (2-100 caractères)
- `email` - Nouvel email (format email valide)
- `ancienMotDePasse` - Requis si changement de mot de passe
- `nouveauMotDePasse` - Nouveau mot de passe (min 8 caractères)

**Réponse (200 OK):**
```json
{
  "id": 1,
  "nom": "Nouvel Admin",
  "email": "nouvel.admin@mussodeme.com",
  "role": "ADMIN"
}
```

**Erreurs:**
- `400 Bad Request` - Validation échouée
- `400 Bad Request` - Email déjà utilisé
- `400 Bad Request` - Ancien mot de passe incorrect
- `404 Not Found` - Admin non trouvé

---

### **PATCH /api/admin/profile/{id}/deactivate**
Désactiver un compte admin (soft delete).

**Requête:**
```http
PATCH /api/admin/profile/1/deactivate
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
"Compte administrateur désactivé avec succès"
```

---

### **PATCH /api/admin/profile/{id}/activate**
Activer un compte admin.

**Requête:**
```http
PATCH /api/admin/profile/1/activate
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
"Compte administrateur activé avec succès"
```

---

### **DELETE /api/admin/profile/{id}**
⚠️ Supprimer définitivement un compte admin (hard delete).

**Requête:**
```http
DELETE /api/admin/profile/1
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
"Compte administrateur supprimé définitivement"
```

**⚠️ Attention:** Cette action est irréversible!

---

## 2️⃣ Gestion des Contenus

### **POST /api/admin/contenus**
Ajouter un nouveau contenu éducatif.

**Requête:**
```http
POST /api/admin/contenus
Authorization: Bearer {token}
Content-Type: application/json

{
  "titre": "Formation Agriculture",
  "langue": "Bambara",
  "description": "Formation sur les techniques modernes",
  "urlContenu": "https://example.com/video.mp4",
  "duree": "30",
  "adminId": 1,
  "categorieId": 2
}
```

**Champs obligatoires:**
- `titre` - Titre du contenu
- `adminId` - ID de l'admin créateur
- `categorieId` - ID de la catégorie

**Réponse (201 Created):**
```json
{
  "id": 10,
  "titre": "Formation Agriculture",
  "langue": "Bambara",
  "description": "Formation sur les techniques modernes",
  "urlContenu": "https://example.com/video.mp4",
  "duree": "30",
  "adminId": 1,
  "categorieId": 2
}
```

---

### **GET /api/admin/contenus/{id}**
Récupérer un contenu par son ID.

**Requête:**
```http
GET /api/admin/contenus/10
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
{
  "id": 10,
  "titre": "Formation Agriculture",
  "langue": "Bambara",
  "description": "Formation sur les techniques modernes",
  "urlContenu": "https://example.com/video.mp4",
  "duree": "30",
  "adminId": 1,
  "categorieId": 2
}
```

---

### **GET /api/admin/contenus**
Lister tous les contenus.

**Requête:**
```http
GET /api/admin/contenus
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
[
  {
    "id": 1,
    "titre": "Formation Agriculture",
    "langue": "Bambara",
    "description": "...",
    "urlContenu": "https://...",
    "duree": "30",
    "adminId": 1,
    "categorieId": 2
  },
  {
    "id": 2,
    "titre": "Gestion Financière",
    "langue": "Français",
    "description": "...",
    "urlContenu": "https://...",
    "duree": "45",
    "adminId": 1,
    "categorieId": 3
  }
]
```

---

### **PUT /api/admin/contenus/{id}**
Modifier un contenu existant.

**Requête:**
```http
PUT /api/admin/contenus/10
Authorization: Bearer {token}
Content-Type: application/json

{
  "titre": "Formation Agriculture Avancée",
  "description": "Formation approfondie sur les techniques modernes"
}
```

**Champs (tous optionnels):**
- Seuls les champs fournis seront mis à jour
- Les autres champs restent inchangés

**Réponse (200 OK):**
```json
{
  "id": 10,
  "titre": "Formation Agriculture Avancée",
  "langue": "Bambara",
  "description": "Formation approfondie sur les techniques modernes",
  "urlContenu": "https://example.com/video.mp4",
  "duree": "30",
  "adminId": 1,
  "categorieId": 2
}
```

---

### **DELETE /api/admin/contenus/{id}**
Supprimer un contenu.

**Requête:**
```http
DELETE /api/admin/contenus/10
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
"Contenu supprimé avec succès"
```

---

## 3️⃣ Gestion des Institutions Financières

### **POST /api/admin/institutions**
Ajouter une nouvelle institution financière.

**Requête:**
```http
POST /api/admin/institutions
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Banque Agricole du Mali",
  "numeroTel": "+223 20 22 33 44",
  "description": "Banque spécialisée dans le financement agricole",
  "logoUrl": "https://example.com/logo.png"
}
```

**Champs obligatoires:**
- `nom` - Nom de l'institution

**Réponse (201 Created):**
```json
{
  "id": 5,
  "nom": "Banque Agricole du Mali",
  "numeroTel": "+223 20 22 33 44",
  "description": "Banque spécialisée dans le financement agricole",
  "logoUrl": "https://example.com/logo.png"
}
```

---

### **GET /api/admin/institutions/{id}**
Récupérer une institution par son ID.

**Requête:**
```http
GET /api/admin/institutions/5
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
{
  "id": 5,
  "nom": "Banque Agricole du Mali",
  "numeroTel": "+223 20 22 33 44",
  "description": "Banque spécialisée dans le financement agricole",
  "logoUrl": "https://example.com/logo.png"
}
```

---

### **GET /api/admin/institutions**
Lister toutes les institutions financières.

**Requête:**
```http
GET /api/admin/institutions
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
[
  {
    "id": 1,
    "nom": "Banque Agricole du Mali",
    "numeroTel": "+223 20 22 33 44",
    "description": "...",
    "logoUrl": "https://..."
  },
  {
    "id": 2,
    "nom": "Caisse d'Épargne",
    "numeroTel": "+223 20 11 22 33",
    "description": "...",
    "logoUrl": "https://..."
  }
]
```

---

### **PUT /api/admin/institutions/{id}**
Modifier une institution financière.

**Requête:**
```http
PUT /api/admin/institutions/5
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Banque Agricole du Mali - Agence Bamako",
  "numeroTel": "+223 20 22 33 55"
}
```

**Champs (tous optionnels):**
- Seuls les champs fournis seront mis à jour

**Réponse (200 OK):**
```json
{
  "id": 5,
  "nom": "Banque Agricole du Mali - Agence Bamako",
  "numeroTel": "+223 20 22 33 55",
  "description": "Banque spécialisée dans le financement agricole",
  "logoUrl": "https://example.com/logo.png"
}
```

---

### **DELETE /api/admin/institutions/{id}**
Supprimer une institution financière.

**Requête:**
```http
DELETE /api/admin/institutions/5
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
"Institution supprimée avec succès"
```

---

## 🔒 Codes de Statut HTTP

| Code | Signification | Utilisation |
|------|---------------|-------------|
| `200` | OK | Requête réussie |
| `201` | Created | Ressource créée avec succès |
| `400` | Bad Request | Validation échouée ou données invalides |
| `401` | Unauthorized | Token manquant ou invalide |
| `403` | Forbidden | Pas les permissions (non-admin) |
| `404` | Not Found | Ressource non trouvée |
| `500` | Internal Server Error | Erreur serveur |

---

## ⚠️ Gestion des Erreurs

### **Exemple d'erreur 400 - Validation**
```json
{
  "timestamp": "2025-10-22T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Le titre du contenu est obligatoire",
  "path": "/api/admin/contenus"
}
```

### **Exemple d'erreur 404 - Not Found**
```json
{
  "timestamp": "2025-10-22T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Administrateur non trouvé avec l'ID: 999",
  "path": "/api/admin/profile/999"
}
```

### **Exemple d'erreur 401 - Unauthorized**
```json
{
  "timestamp": "2025-10-22T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token JWT invalide ou expiré",
  "path": "/api/admin/contenus"
}
```

---

## 📝 Notes Importantes

### **Sécurité:**
- 🔒 Tous les endpoints nécessitent un token JWT valide
- 🔒 Seuls les utilisateurs avec le rôle `ADMIN` peuvent y accéder
- 🔒 Le mot de passe n'est jamais retourné dans les réponses

### **Validation:**
- ✅ Tous les DTOs sont validés avec `@Valid`
- ✅ Les champs texte sont automatiquement trimés
- ✅ Les emails sont validés avec un format correct

### **Transactions:**
- ✅ Toutes les opérations de modification sont atomiques
- ✅ Rollback automatique en cas d'erreur

### **Logging:**
- 📝 Toutes les requêtes sont loguées
- 📝 Les actions importantes sont tracées
- 📝 Les erreurs sont détaillées dans les logs

---

## 🧪 Exemples avec cURL

### **Mise à jour du profil (changement de mot de passe)**
```bash
curl -X PUT http://localhost:5500/api/admin/profile/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "ancienMotDePasse": "oldPassword123",
    "nouveauMotDePasse": "newSecurePassword456!"
  }'
```

### **Ajout d'un contenu**
```bash
curl -X POST http://localhost:5500/api/admin/contenus \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Formation Agriculture",
    "langue": "Bambara",
    "description": "Formation complète",
    "urlContenu": "https://example.com/video.mp4",
    "duree": "30",
    "adminId": 1,
    "categorieId": 2
  }'
```

### **Liste des institutions**
```bash
curl -X GET http://localhost:5500/api/admin/institutions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📊 Récapitulatif des Endpoints

### **Profil Admin: 6 endpoints**
- `GET /api/admin/profile/{id}` - Récupérer par ID
- `GET /api/admin/profile/email/{email}` - Récupérer par email
- `PUT /api/admin/profile/{id}` - Mettre à jour
- `PATCH /api/admin/profile/{id}/deactivate` - Désactiver
- `PATCH /api/admin/profile/{id}/activate` - Activer
- `DELETE /api/admin/profile/{id}` - Supprimer définitivement

### **Contenus: 5 endpoints**
- `POST /api/admin/contenus` - Créer
- `GET /api/admin/contenus/{id}` - Récupérer
- `GET /api/admin/contenus` - Lister
- `PUT /api/admin/contenus/{id}` - Modifier
- `DELETE /api/admin/contenus/{id}` - Supprimer

### **Institutions: 5 endpoints**
- `POST /api/admin/institutions` - Créer
- `GET /api/admin/institutions/{id}` - Récupérer
- `GET /api/admin/institutions` - Lister
- `PUT /api/admin/institutions/{id}` - Modifier
- `DELETE /api/admin/institutions/{id}` - Supprimer

**Total: 16 endpoints**

---

**Date de création:** 22 octobre 2025  
**Version API:** 2.0  
**Base URL:** `http://localhost:5500/api/admin`
