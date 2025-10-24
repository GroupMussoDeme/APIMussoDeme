# 🔌 Exemples d'Utilisation de l'API - Gestion des Contenus

## Base URL
```
http://localhost:8080/api/admin
```

## 🔐 Authentication
Tous les endpoints nécessitent un token JWT avec le rôle `ADMIN`.

**Header requis** :
```
Authorization: Bearer {votre_token_jwt}
```

---

## 📋 CONTENUS (AUDIO & VIDEO)

**⚠️ IMPORTANT** : Les contenus audio et vidéo **ne peuvent PAS être modifiés** après leur création. Seules les opérations **ajout** et **suppression** sont disponibles.

### 1. Créer un Audio
```http
POST /api/admin/contenus
Content-Type: application/json
Authorization: Bearer {token}

{
  "titre": "L'importance de l'épargne",
  "langue": "bambara",
  "description": "Conseils pratiques pour bien épargner son argent",
  "urlContenu": "https://cdn.mussodeme.com/audios/epargne_2024.mp3",
  "duree": "15:30",
  "adminId": 1,
  "categorieId": 2
}
```

**Réponse (201 Created)** :
```json
{
  "id": 10,
  "titre": "L'importance de l'épargne",
  "langue": "bambara",
  "description": "Conseils pratiques pour bien épargner son argent",
  "urlContenu": "https://cdn.mussodeme.com/audios/epargne_2024.mp3",
  "duree": "15:30",
  "adminId": 1,
  "categorieId": 2
}
```

---

### 2. Créer une Vidéo
``http
POST /api/admin/contenus
Content-Type: application/json
Authorization: Bearer {token}

{
  "titre": "Comment créer une coopérative agricole",
  "langue": "fr",
  "description": "Guide complet en vidéo sur la création d'une coopérative",
  "urlContenu": "https://cdn.mussodeme.com/videos/coop_guide.mp4",
  "duree": "25:45",
  "adminId": 1,
  "categorieId": 3
}
```

---

### 3. Récupérer un Contenu
```http
GET /api/admin/contenus/10
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
{
  "id": 10,
  "titre": "L'importance de l'épargne",
  "langue": "bambara",
  "description": "Conseils pratiques pour bien épargner son argent",
  "urlContenu": "https://cdn.mussodeme.com/audios/epargne_2024.mp3",
  "duree": "15:30",
  "adminId": 1,
  "categorieId": 2
}
```

---

### 4. Supprimer un Contenu
```http
DELETE /api/admin/contenus/10
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
"Contenu supprimé avec succès"
```

---

## 🔍 FILTRAGE DES CONTENUS

### 5. Lister TOUS les Contenus
```http
GET /api/admin/contenus
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
[
  {
    "id": 1,
    "titre": "Introduction au maraîchage",
    "langue": "fr",
    "description": "...",
    "urlContenu": "https://cdn.mussodeme.com/videos/maraichage.mp4",
    "duree": "20:15",
    "adminId": 1,
    "categorieId": 3
  },
  {
    "id": 2,
    "titre": "Gestion de budget familial",
    "langue": "bambara",
    "description": "...",
    "urlContenu": "https://cdn.mussodeme.com/audios/budget.mp3",
    "duree": "12:30",
    "adminId": 1,
    "categorieId": 2
  }
]
```

---

### 6. Filtrer par Type : AUDIOS uniquement
```http
GET /api/admin/contenus/type/AUDIOS
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
[
  {
    "id": 2,
    "titre": "Gestion de budget familial",
    "langue": "bambara",
    "urlContenu": "https://cdn.mussodeme.com/audios/budget.mp3",
    "duree": "12:30",
    "adminId": 1,
    "categorieId": 2
  },
  {
    "id": 5,
    "titre": "Crédit agricole expliqué",
    "langue": "fr",
    "urlContenu": "https://cdn.mussodeme.com/audios/credit.mp3",
    "duree": "18:00",
    "adminId": 1,
    "categorieId": 2
  }
]
```

---

### 7. Filtrer par Type : VIDEOS uniquement
```http
GET /api/admin/contenus/type/VIDEOS
Authorization: Bearer {token}
```

---

### 8. Filtrer par Type : INSTITUTION_FINANCIERE
```http
GET /api/admin/contenus/type/INSTITUTION_FINANCIERE
Authorization: Bearer {token}
```

**Note** : Retourne les contenus liés aux institutions (si implémenté via contenus).

---

### 9. Trier par Date (Plus Récent d'Abord)
```http
GET /api/admin/contenus/par-date?ordre=desc
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
[
  {
    "id": 10,
    "titre": "Contenu le plus récent",
    "...": "..."
  },
  {
    "id": 9,
    "titre": "Avant-dernier contenu",
    "...": "..."
  }
]
```

---

### 10. Trier par Date (Plus Ancien d'Abord)
```http
GET /api/admin/contenus/par-date?ordre=asc
Authorization: Bearer {token}
```

---

### 11. Lister Contenus avec Durée Uniquement
```http
GET /api/admin/contenus/avec-duree
Authorization: Bearer {token}
```

**Utilité** : Filtre les AUDIOS et VIDEOS ayant une durée renseignée.

**Réponse (200 OK)** :
```json
[
  {
    "id": 1,
    "titre": "Introduction au maraîchage",
    "duree": "20:15",
    "...": "..."
  },
  {
    "id": 2,
    "titre": "Gestion de budget familial",
    "duree": "12:30",
    "...": "..."
  }
]
```

---

## 🏦 INSTITUTIONS FINANCIÈRES

### 12. Créer une Institution
```http
POST /api/admin/institutions
Content-Type: application/json
Authorization: Bearer {token}

{
  "nom": "Kafo Jiginew",
  "numeroTel": "+223 70 12 34 56",
  "description": "Institution de microfinance rurale spécialisée dans le crédit agricole",
  "logoUrl": "https://cdn.mussodeme.com/logos/kafo_jiginew.png"
}
```

**Réponse (201 Created)** :
```json
{
  "id": 5,
  "nom": "Kafo Jiginew",
  "numeroTel": "+223 70 12 34 56",
  "description": "Institution de microfinance rurale spécialisée dans le crédit agricole",
  "logoUrl": "https://cdn.mussodeme.com/logos/kafo_jiginew.png"
}
```

---

### 13. Modifier une Institution
```http
PUT /api/admin/institutions/5
Content-Type: application/json
Authorization: Bearer {token}

{
  "nom": "Kafo Jiginew SA",
  "numeroTel": "+223 70 12 34 99"
}
```

---

### 14. Récupérer une Institution
```http
GET /api/admin/institutions/5
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
{
  "id": 5,
  "nom": "Kafo Jiginew SA",
  "numeroTel": "+223 70 12 34 99",
  "description": "Institution de microfinance rurale spécialisée dans le crédit agricole",
  "logoUrl": "https://cdn.mussodeme.com/logos/kafo_jiginew.png"
}
```

---

### 15. Supprimer une Institution
```http
DELETE /api/admin/institutions/5
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
"Institution supprimée avec succès"
```

---

### 16. Lister Toutes les Institutions
```http
GET /api/admin/institutions
Authorization: Bearer {token}
```

**Réponse (200 OK)** :
```json
[
  {
    "id": 1,
    "nom": "Banque Nationale de Développement Agricole",
    "numeroTel": "+223 20 22 33 44",
    "description": "Financement de projets agricoles",
    "logoUrl": "https://cdn.mussodeme.com/logos/bnda.png"
  },
  {
    "id": 2,
    "nom": "Microcred Mali",
    "numeroTel": "+223 70 55 66 77",
    "description": "Microfinance pour entrepreneurs ruraux",
    "logoUrl": "https://cdn.mussodeme.com/logos/microcred.png"
  }
]
```

---

## ❌ Gestion des Erreurs

### Erreur : Type de Catégorie Invalide
```http
GET /api/admin/contenus/type/INVALID_TYPE
Authorization: Bearer {token}
```

**Réponse (400 Bad Request)** :
```json
{
  "timestamp": "2025-10-22T10:30:45",
  "status": 400,
  "error": "Bad Request",
  "message": "Type de catégorie invalide. Valeurs acceptées: AUDIOS, VIDEOS, INSTITUTION_FINANCIERE",
  "path": "/api/admin/contenus/type/INVALID_TYPE"
}
```

---

### Erreur : Contenu Non Trouvé
```http
GET /api/admin/contenus/999
Authorization: Bearer {token}
```

**Réponse (404 Not Found)** :
```json
{
  "timestamp": "2025-10-22T10:35:12",
  "status": 404,
  "error": "Not Found",
  "message": "Contenu non trouvé avec l'ID: 999",
  "path": "/api/admin/contenus/999"
}
```

---

### Erreur : Données Invalides
```http
POST /api/admin/contenus
Content-Type: application/json
Authorization: Bearer {token}

{
  "titre": "",
  "adminId": 1
}
```

**Réponse (400 Bad Request)** :
```json
{
  "timestamp": "2025-10-22T10:40:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Le titre du contenu est obligatoire",
  "path": "/api/admin/contenus"
}
```

---

### Erreur : Non Autorisé
```http
GET /api/admin/contenus
Authorization: Bearer {invalid_or_expired_token}
```

**Réponse (401 Unauthorized)** :
```json
{
  "timestamp": "2025-10-22T10:45:30",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token JWT invalide ou expiré",
  "path": "/api/admin/contenus"
}
```

---

## 🧪 Tests avec cURL

### Exemple complet avec cURL

```bash
# 1. Se connecter et récupérer le token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@mussodeme.com",
    "motDePasse": "Admin@123456"
  }'

# Réponse contient : "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."

# 2. Utiliser le token pour lister les contenus
curl -X GET http://localhost:8080/api/admin/contenus \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6..."

# 3. Créer un nouveau contenu
curl -X POST http://localhost:8080/api/admin/contenus \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6..." \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Test Audio",
    "langue": "fr",
    "description": "Description de test",
    "urlContenu": "https://example.com/test.mp3",
    "duree": "10:00",
    "adminId": 1,
    "categorieId": 2
  }'

# 4. Filtrer par type AUDIOS
curl -X GET http://localhost:8080/api/admin/contenus/type/AUDIOS \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6..."

# 5. Trier par date
curl -X GET "http://localhost:8080/api/admin/contenus/par-date?ordre=desc" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6..."
```

---

## 📱 Tests avec Postman

### Collection Postman (Importer ce JSON)

```json
{
  "info": {
    "name": "MussoDeme - Gestion Contenus",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Lister tous les contenus",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          }
        ],
        "url": {
          "raw": "{{baseUrl}}/api/admin/contenus",
          "host": ["{{baseUrl}}"],
          "path": ["api", "admin", "contenus"]
        }
      }
    },
    {
      "name": "Filtrer par type AUDIOS",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          }
        ],
        "url": {
          "raw": "{{baseUrl}}/api/admin/contenus/type/AUDIOS",
          "host": ["{{baseUrl}}"],
          "path": ["api", "admin", "contenus", "type", "AUDIOS"]
        }
      }
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    },
    {
      "key": "token",
      "value": "your_jwt_token_here"
    }
  ]
}
```

---

## 🎯 Scénarios d'Usage Courants

### Scénario 1 : Ajouter un Audio Éducatif
1. Upload du fichier audio → FileController
2. Créer le contenu via `POST /api/admin/contenus`
3. Vérifier avec `GET /api/admin/contenus/type/AUDIOS`

### Scénario 2 : Mettre à Jour une Institution
1. Récupérer l'institution → `GET /api/admin/institutions/{id}`
2. Modifier → `PUT /api/admin/institutions/{id}`
3. Vérifier → `GET /api/admin/institutions/{id}`

### Scénario 3 : Statistiques de Contenus
1. Tous les contenus → `GET /api/admin/contenus`
2. Audios uniquement → `GET /api/admin/contenus/type/AUDIOS`
3. Vidéos uniquement → `GET /api/admin/contenus/type/VIDEOS`
4. Comparer les volumes par type

---

**Dernière mise à jour** : 2025-10-22  
**Version API** : 1.0
