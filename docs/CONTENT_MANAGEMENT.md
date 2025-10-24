# 📚 Documentation Gestion des Contenus

## Vue d'ensemble

Le système de gestion des contenus permet à l'administrateur de gérer **trois types de contenus** via des catégories :
- 🎵 **AUDIOS** : Contenus audio éducatifs
- 🎬 **VIDEOS** : Contenus vidéo éducatifs  
- 🏦 **INSTITUTION_FINANCIERE** : Informations sur les institutions financières

## Architecture

### 1. TypeCategorie Enum

```java
public enum TypeCategorie {
    AUDIOS,
    VIDEOS,
    INSTITUTION_FINANCIERE
}
```

### 2. Entité Contenu

**Table** : `contenu` (renommée depuis `audioConseil`)

**Attributs** :
- `id` : Identifiant unique
- `titre` : Titre du contenu
- `langue` : Langue du contenu
- `description` : Description détaillée
- `urlContenu` : URL du fichier média ou logo
- `duree` : Durée (pour AUDIO et VIDEO uniquement)
- `categorie` : Catégorie associée (relation ManyToOne)
- `admin` : Admin qui a créé le contenu (relation ManyToOne)

### 3. Entité InstitutionFinanciere

**Reste séparée** pour préserver les spécificités métier.

**Table** : `institution_financiere`

**Attributs** :
- `id` : Identifiant unique
- `nom` : Nom de l'institution
- `numeroTel` : Numéro de téléphone
- `description` : Description
- `logoUrl` : URL du logo

### 4. Classe Associative UtilisateurAudio

Gère la relation **Many-to-Many** entre `Utilisateur` et `Contenu`.

**Table** : `utilisateur_audio`

**Attributs** :
- `id` : Identifiant unique
- `utilisateur` : L'utilisateur (relation ManyToOne)
- `contenu` : Le contenu lu (relation ManyToOne)
- `dateLecture` : **NOUVEAU** - Date et heure de lecture du contenu

---

## 📋 Fonctionnalités CRUD

### Contenus (AUDIO & VIDEO)

**⚠️ IMPORTANT** : Les contenus audio et vidéo **ne peuvent pas être modifiés** après création. Seules les opérations **ajout** et **suppression** sont disponibles.

#### 1. Ajouter un contenu
**Endpoint** : `POST /api/admin/contenus`

**Body** :
```json
{
  "titre": "Introduction à l'épargne",
  "langue": "fr",
  "description": "Guide complet sur l'épargne",
  "urlContenu": "https://example.com/audio.mp3",
  "duree": "15:30",
  "adminId": 1,
  "categorieId": 2
}
```

#### 2. Supprimer un contenu
**Endpoint** : `DELETE /api/admin/contenus/{id}`

#### 3. Récupérer un contenu
**Endpoint** : `GET /api/admin/contenus/{id}`

---

### Institutions Financières

#### 1. Ajouter une institution
**Endpoint** : `POST /api/admin/institutions`

**Body** :
```json
{
  "nom": "Banque Nationale",
  "numeroTel": "+223 70 00 00 00",
  "description": "Institution de microfinance",
  "logoUrl": "https://example.com/logo.png"
}
```

#### 2. Modifier une institution
**Endpoint** : `PUT /api/admin/institutions/{id}`

#### 3. Supprimer une institution
**Endpoint** : `DELETE /api/admin/institutions/{id}`

#### 4. Récupérer une institution
**Endpoint** : `GET /api/admin/institutions/{id}`

#### 5. Lister toutes les institutions
**Endpoint** : `GET /api/admin/institutions`

---

## 🔍 Fonctionnalités de Filtrage

### 1. Lister TOUS les contenus
**Endpoint** : `GET /api/admin/contenus`

**Réponse** :
```json
[
  {
    "id": 1,
    "titre": "Introduction à l'épargne",
    "langue": "fr",
    "description": "...",
    "urlContenu": "https://...",
    "duree": "15:30",
    "adminId": 1,
    "categorieId": 2
  }
]
```

---

### 2. Lister les contenus par TYPE de catégorie
**Endpoint** : `GET /api/admin/contenus/type/{type}`

**Exemples** :
- `GET /api/admin/contenus/type/AUDIOS` → Tous les audios
- `GET /api/admin/contenus/type/VIDEOS` → Toutes les vidéos
- `GET /api/admin/contenus/type/INSTITUTION_FINANCIERE` → Toutes les institutions (via contenus)

**Validation** : Lance une exception si le type n'est pas valide.

---

### 3. Lister les contenus par DATE d'ajout
**Endpoint** : `GET /api/admin/contenus/par-date?ordre={asc|desc}`

**Paramètres** :
- `ordre` : 
  - `desc` (par défaut) → Plus récent d'abord
  - `asc` → Plus ancien d'abord

**Exemples** :
- `GET /api/admin/contenus/par-date` → Plus récents d'abord
- `GET /api/admin/contenus/par-date?ordre=asc` → Plus anciens d'abord

---

### 4. Lister les contenus avec DURÉE
**Endpoint** : `GET /api/admin/contenus/avec-duree`

**Description** : Retourne uniquement les contenus ayant une durée renseignée (AUDIO et VIDEO).

**Filtre** : Exclut les contenus sans durée (institutions financières, contenus incomplets).

---

## 📊 Résumé des Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| **GET** | `/api/admin/contenus` | Liste tous les contenus |
| **GET** | `/api/admin/contenus/{id}` | Récupère un contenu par ID |
| **GET** | `/api/admin/contenus/type/{type}` | Filtre par type (AUDIOS, VIDEOS, INSTITUTION_FINANCIERE) |
| **GET** | `/api/admin/contenus/par-date?ordre={asc\|desc}` | Trie par date d'ajout |
| **GET** | `/api/admin/contenus/avec-duree` | Liste contenus avec durée |
| **POST** | `/api/admin/contenus` | Ajoute un nouveau contenu |
| **DELETE** | `/api/admin/contenus/{id}` | Supprime un contenu |
| **GET** | `/api/admin/institutions` | Liste toutes les institutions |
| **GET** | `/api/admin/institutions/{id}` | Récupère une institution par ID |
| **POST** | `/api/admin/institutions` | Ajoute une institution |
| **PUT** | `/api/admin/institutions/{id}` | Modifie une institution |
| **DELETE** | `/api/admin/institutions/{id}` | Supprime une institution |

---

## 🔐 Sécurité

Tous les endpoints sont protégés par :
```java
@PreAuthorize("hasRole('ADMIN')")
```

Seuls les utilisateurs avec le rôle `ADMIN` peuvent accéder à ces fonctionnalités.

---

## 📝 Logging

Chaque opération est tracée avec des logs détaillés :
- **INFO** : Opérations réussies
- **WARN** : Tentatives avec données invalides
- **DEBUG** : Compteurs et détails techniques

---

## 🎯 Points Clés

✅ **Séparation maintenue** : `InstitutionFinanciere` reste une entité à part  
✅ **TypeCategorie étendu** : Inclut maintenant `INSTITUTION_FINANCIERE`  
✅ **Historique de lecture** : `UtilisateurAudio.dateLecture` pour suivre les consultations  
✅ **Filtrage avancé** : Par type, date, et durée  
✅ **Validation** : Tous les DTOs sont validés avec `@Valid`  
✅ **Gestion d'erreurs** : Exceptions personnalisées avec messages explicites

---

## 🚀 Exemple de Workflow Complet

### Scénario : Ajouter un audio éducatif

1. **Créer une catégorie AUDIOS** (si elle n'existe pas)
2. **Uploader le fichier audio** (via FileController)
3. **Créer le contenu** :
   ```bash
   POST /api/admin/contenus
   {
     "titre": "Gestion de budget",
     "langue": "bambara",
     "description": "Conseils pratiques",
     "urlContenu": "https://cdn.mussodeme.com/audios/budget.mp3",
     "duree": "12:45",
     "adminId": 1,
     "categorieId": 5
   }
   ```

4. **Vérifier l'ajout** :
   ```bash
   GET /api/admin/contenus/type/AUDIOS
   ```

5. **Utilisateur lit le contenu** → Enregistrement dans `utilisateur_audio` avec `dateLecture`

---

## 📌 Notes Techniques

- **Migration BD** : Renommer la table `audioConseil` → `contenu`
- **Index recommandé** : Sur `categorie_id`, `admin_id` pour optimiser les requêtes
- **Cascade** : Suppression d'une catégorie supprime ses contenus
- **Durée** : Format libre (string) pour flexibilité ("15:30", "1h20", etc.)

---

## 🔄 Prochaines Évolutions Possibles

- [ ] Pagination sur les listes de contenus
- [ ] Recherche full-text sur titre/description
- [ ] Upload direct de fichiers dans le même endpoint
- [ ] Statistiques de consultation par contenu
- [ ] Export CSV/Excel des contenus

---

**Date de création** : 2025-10-22  
**Version** : 1.0  
**Auteur** : MussoDeme API Team
