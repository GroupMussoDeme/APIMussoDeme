# 👥 Gestion des Utilisateurs par l'Admin - Documentation

## 📋 Vue d'Ensemble

L'admin peut maintenant gérer tous les utilisateurs de la plateforme (FemmeRurale, Acheteur, Coopérative, etc.) avec des opérations CRUD complètes et des fonctionnalités de filtrage avancées.

---

## ✅ Fonctionnalités Implémentées

### **Opérations Admin sur les Utilisateurs:**
1. ✅ Lister tous les utilisateurs
2. ✅ Afficher un utilisateur par son ID
3. ✅ Lister les utilisateurs actifs
4. ✅ Lister les utilisateurs inactifs
5. ✅ Lister les utilisateurs par date de création (tri asc/desc)
6. ✅ Activer un utilisateur
7. ✅ Désactiver un utilisateur

---

## 📦 Fichiers Modifiés

### **1. [`UtilisateurDTO.java`](../src/main/java/com/mussodeme/MussoDeme/dto/UtilisateurDTO.java)**
**Ajouts:**
- ✅ Champ `active` (boolean)
- ✅ Champ `createdAt` (LocalDateTime) avec format JSON
- ✅ Import `@JsonFormat` pour le formatage des dates

**Avant:**
```java
private Role role;
```

**Après:**
```java
private Role role;
private boolean active;

@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createdAt;
```

---

### **2. [`UtilisateursRepository.java`](../src/main/java/com/mussodeme/MussoDeme/repository/UtilisateursRepository.java)**
**Nouvelles méthodes:**

```java
// Trouver tous les utilisateurs actifs
List<Utilisateur> findByActiveTrue();

// Trouver tous les utilisateurs inactifs
List<Utilisateur> findByActiveFalse();

// Tri par date décroissant (plus récent d'abord)
List<Utilisateur> findAllByOrderByCreatedAtDesc();

// Tri par date croissant (plus ancien d'abord)
List<Utilisateur> findAllByOrderByCreatedAtAsc();
```

---

### **3. [`AdminService.java`](../src/main/java/com/mussodeme/MussoDeme/services/AdminService.java)**
**Nouvelles méthodes (7):**

| Méthode | Description | Transactional |
|---------|-------------|---------------|
| `listerTousLesUtilisateurs()` | Liste complète | Non |
| `getUtilisateur(Long id)` | Récupération par ID | Non |
| `listerUtilisateursActifs()` | Filtre actifs | Non |
| `listerUtilisateursInactifs()` | Filtre inactifs | Non |
| `listerUtilisateursParDate(String ordre)` | Tri par date | Non |
| `activerUtilisateur(Long id)` | Activation | ✅ Oui |
| `desactiverUtilisateur(Long id)` | Désactivation | ✅ Oui |

---

### **4. [`AdminController.java`](../src/main/java/com/mussodeme/MussoDeme/controllers/AdminController.java)**
**Nouveaux endpoints (7):**

| Endpoint | Méthode HTTP | Description |
|----------|--------------|-------------|
| `/api/admin/utilisateurs` | GET | Liste tous les utilisateurs |
| `/api/admin/utilisateurs/{id}` | GET | Récupère un utilisateur |
| `/api/admin/utilisateurs/actifs` | GET | Liste utilisateurs actifs |
| `/api/admin/utilisateurs/inactifs` | GET | Liste utilisateurs inactifs |
| `/api/admin/utilisateurs/par-date?ordre=desc` | GET | Tri par date |
| `/api/admin/utilisateurs/{id}/activer` | PATCH | Active un utilisateur |
| `/api/admin/utilisateurs/{id}/desactiver` | PATCH | Désactive un utilisateur |

---

## 📡 Documentation des Endpoints

### **1. GET /api/admin/utilisateurs**
Lister tous les utilisateurs de la plateforme.

**Requête:**
```http
GET /api/admin/utilisateurs
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
[
  {
    "id": 1,
    "nom": "Diarra",
    "prenom": "Fatoumata",
    "numeroTel": "+223 70 12 34 56",
    "localite": "Bamako",
    "role": "FEMME_RURALE",
    "active": true,
    "createdAt": "2025-10-15 14:30:00"
  },
  {
    "id": 2,
    "nom": "Traoré",
    "prenom": "Aminata",
    "numeroTel": "+223 75 11 22 33",
    "localite": "Sikasso",
    "role": "FEMME_RURALE",
    "active": false,
    "createdAt": "2025-10-14 09:15:00"
  }
]
```

---

### **2. GET /api/admin/utilisateurs/{id}**
Récupérer un utilisateur spécifique par son ID.

**Requête:**
```http
GET /api/admin/utilisateurs/1
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
{
  "id": 1,
  "nom": "Diarra",
  "prenom": "Fatoumata",
  "numeroTel": "+223 70 12 34 56",
  "localite": "Bamako",
  "role": "FEMME_RURALE",
  "active": true,
  "createdAt": "2025-10-15 14:30:00"
}
```

**Erreurs:**
- `404 Not Found` - Utilisateur non trouvé

---

### **3. GET /api/admin/utilisateurs/actifs**
Lister uniquement les utilisateurs actifs.

**Requête:**
```http
GET /api/admin/utilisateurs/actifs
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
[
  {
    "id": 1,
    "nom": "Diarra",
    "prenom": "Fatoumata",
    "numeroTel": "+223 70 12 34 56",
    "localite": "Bamako",
    "role": "FEMME_RURALE",
    "active": true,
    "createdAt": "2025-10-15 14:30:00"
  }
]
```

---

### **4. GET /api/admin/utilisateurs/inactifs**
Lister uniquement les utilisateurs inactifs.

**Requête:**
```http
GET /api/admin/utilisateurs/inactifs
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
[
  {
    "id": 2,
    "nom": "Traoré",
    "prenom": "Aminata",
    "numeroTel": "+223 75 11 22 33",
    "localite": "Sikasso",
    "role": "FEMME_RURALE",
    "active": false,
    "createdAt": "2025-10-14 09:15:00"
  }
]
```

---

### **5. GET /api/admin/utilisateurs/par-date**
Lister les utilisateurs triés par date de création.

**Paramètres:**
- `ordre` (optionnel): `asc` ou `desc` (par défaut: `desc`)

**Requête (plus récents d'abord):**
```http
GET /api/admin/utilisateurs/par-date?ordre=desc
Authorization: Bearer {token}
```

**Requête (plus anciens d'abord):**
```http
GET /api/admin/utilisateurs/par-date?ordre=asc
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
[
  {
    "id": 1,
    "nom": "Diarra",
    "prenom": "Fatoumata",
    "createdAt": "2025-10-15 14:30:00",
    ...
  },
  {
    "id": 2,
    "nom": "Traoré",
    "prenom": "Aminata",
    "createdAt": "2025-10-14 09:15:00",
    ...
  }
]
```

---

### **6. PATCH /api/admin/utilisateurs/{id}/activer**
Activer un utilisateur (le réactiver s'il était désactivé).

**Requête:**
```http
PATCH /api/admin/utilisateurs/2/activer
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
"Utilisateur activé avec succès"
```

**Logs générés:**
```
INFO  - Activation de l'utilisateur: 2
INFO  - Utilisateur 2 activé avec succès (Nom: Aminata Traoré, Rôle: FEMME_RURALE)
```

**Cas particulier:**
Si l'utilisateur est déjà actif, l'opération ne fait rien et retourne le même message de succès.

---

### **7. PATCH /api/admin/utilisateurs/{id}/desactiver**
Désactiver un utilisateur (soft delete).

**Requête:**
```http
PATCH /api/admin/utilisateurs/1/desactiver
Authorization: Bearer {token}
```

**Réponse (200 OK):**
```json
"Utilisateur désactivé avec succès"
```

**Logs générés:**
```
INFO  - Désactivation de l'utilisateur: 1
INFO  - Utilisateur 1 désactivé avec succès (Nom: Fatoumata Diarra, Rôle: FEMME_RURALE)
```

**Cas particulier:**
Si l'utilisateur est déjà inactif, l'opération ne fait rien et retourne le même message de succès.

---

## 🔐 Sécurité

### **Autorisation:**
- ✅ Tous les endpoints nécessitent le rôle `ADMIN`
- ✅ `@PreAuthorize("hasRole('ADMIN')")` sur le controller
- ✅ Token JWT valide requis

### **Logging:**
- ✅ Toutes les actions sont loguées
- ✅ Détails de l'utilisateur modifié dans les logs
- ✅ Logs de warning si utilisateur non trouvé

---

## 💡 Exemples d'Utilisation

### **Exemple 1: Lister tous les utilisateurs**
```bash
curl -X GET http://localhost:5500/api/admin/utilisateurs \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **Exemple 2: Récupérer un utilisateur**
```bash
curl -X GET http://localhost:5500/api/admin/utilisateurs/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **Exemple 3: Lister les utilisateurs actifs**
```bash
curl -X GET http://localhost:5500/api/admin/utilisateurs/actifs \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **Exemple 4: Tri par date (plus récents d'abord)**
```bash
curl -X GET "http://localhost:5500/api/admin/utilisateurs/par-date?ordre=desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **Exemple 5: Activer un utilisateur**
```bash
curl -X PATCH http://localhost:5500/api/admin/utilisateurs/2/activer \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **Exemple 6: Désactiver un utilisateur**
```bash
curl -X PATCH http://localhost:5500/api/admin/utilisateurs/1/desactiver \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📊 Statistiques

| Métrique | Valeur |
|----------|--------|
| **Nouvelles méthodes Service** | 7 |
| **Nouveaux endpoints API** | 7 |
| **Méthodes Repository** | +4 |
| **Lignes ajoutées Service** | ~138 |
| **Lignes ajoutées Controller** | ~75 |

---

## 🔄 Logique Métier

### **Activation/Désactivation:**

```java
// Activation
if (utilisateur.isActive()) {
    log.info("L'utilisateur {} est déjà actif", userId);
    return; // Pas d'erreur, opération idempotente
}
utilisateur.setActive(true);

// Désactivation
if (!utilisateur.isActive()) {
    log.info("L'utilisateur {} est déjà inactif", userId);
    return; // Pas d'erreur, opération idempotente
}
utilisateur.setActive(false);
```

**Caractéristiques:**
- ✅ **Opérations idempotentes**: Appeler plusieurs fois ne cause pas d'erreur
- ✅ **Logging informatif**: Indique si l'état était déjà celui attendu
- ✅ **Transactional**: Rollback automatique en cas d'erreur
- ✅ **Logs détaillés**: Nom, prénom, et rôle de l'utilisateur

---

### **Tri par Date:**

```java
// Tri descendant (par défaut) - Plus récents d'abord
utilisateursRepository.findAllByOrderByCreatedAtDesc()

// Tri ascendant - Plus anciens d'abord
utilisateursRepository.findAllByOrderByCreatedAtAsc()
```

**Utilisation:**
- `?ordre=desc` → Nouveau → Ancien
- `?ordre=asc` → Ancien → Nouveau
- Pas de paramètre → `desc` par défaut

---

## ⚠️ Gestion d'Erreurs

### **404 Not Found:**
```json
{
  "timestamp": "2025-10-22T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Utilisateur non trouvé avec l'ID: 999",
  "path": "/api/admin/utilisateurs/999"
}
```

### **401 Unauthorized:**
```json
{
  "timestamp": "2025-10-22T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token JWT invalide ou expiré",
  "path": "/api/admin/utilisateurs"
}
```

### **403 Forbidden:**
```json
{
  "timestamp": "2025-10-22T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Accès refusé - Rôle ADMIN requis",
  "path": "/api/admin/utilisateurs"
}
```

---

## 🎯 Points Clés

### **✅ Avantages:**
1. **Soft Delete**: Désactivation au lieu de suppression définitive
2. **Historique préservé**: Les données utilisateur restent dans la DB
3. **Réversible**: Possibilité de réactiver un utilisateur
4. **Filtrage avancé**: Par statut, par date
5. **Logging complet**: Traçabilité de toutes les actions
6. **Idempotent**: Pas d'erreur si déjà dans l'état souhaité

### **⚠️ Points d'Attention:**
1. **Polymorphisme**: L'entité `Utilisateur` est abstraite, les instances sont `FemmeRurale`, `Acheteur`, etc.
2. **Pas de suppression**: Seulement désactivation (préserve les données)
3. **Tous les types**: Gère tous les types d'utilisateurs (FemmeRurale, Acheteur, Coopérative, etc.)

---

## 🚀 Prochaines Améliorations Possibles

### **Court Terme:**
- [ ] Filtrer par rôle (uniquement FemmeRurale, uniquement Acheteur, etc.)
- [ ] Filtrer par localité
- [ ] Recherche par nom/prénom
- [ ] Pagination des résultats

### **Moyen Terme:**
- [ ] Statistiques utilisateurs (nombre par rôle, par statut)
- [ ] Export CSV/Excel
- [ ] Historique des modifications
- [ ] Raison de désactivation

### **Long Terme:**
- [ ] Gestion des permissions granulaires
- [ ] Audit trail complet
- [ ] Notifications de changement de statut
- [ ] Dashboard analytics

---

## 📚 Utilisation avec les Autres Rôles

### **Types d'Utilisateurs Gérés:**
```java
public enum Role {
    ADMIN,           // Administrateur
    FEMME_RURALE,    // Utilisatrice principale
    ACHETEUR,        // Rôle dynamique (femme qui achète)
    COOPERATIVE,     // Groupe de femmes
    // ... autres rôles
}
```

**Tous ces types sont retournés par les endpoints de gestion des utilisateurs.**

---

## ✅ Checklist de Vérification

- [x] Repository enrichi avec méthodes de filtrage
- [x] DTO mis à jour avec `active` et `createdAt`
- [x] Service implémenté avec 7 nouvelles méthodes
- [x] Controller exposant 7 nouveaux endpoints
- [x] Logging complet sur toutes les opérations
- [x] Transactions sur les modifications
- [x] Gestion d'erreurs avec exceptions personnalisées
- [x] ModelMapper utilisé pour le mapping
- [x] Sécurité avec `@PreAuthorize`
- [x] Documentation complète
- [x] 0 erreur de compilation

---

**Date de création:** 22 octobre 2025  
**Version:** 1.0  
**Endpoints totaux Admin:** 23 (16 existants + 7 nouveaux)  
**Status:** ✅ **COMPLET ET TESTÉ**
