# 🔒 Contraintes de Suppression Admin - Documentation

## 📋 Vue d'Ensemble

La suppression d'un compte administrateur est maintenant **conditionnée** à la vérification qu'il ne gère aucune donnée sur le système. Cette règle de sécurité empêche la perte de données et garantit l'intégrité référentielle.

---

## ✅ Règle Métier

**Un administrateur ne peut être supprimé définitivement QUE SI:**
1. ✅ Il ne gère **aucun contenu** éducatif
2. ✅ Il ne gère **aucune catégorie**
3. ✅ Il n'a **aucune action de gestion** associée

---

## 🔍 Vérifications Effectuées

### **1. Vérification des Contenus**
```java
if (admin.getContenus() != null && !admin.getContenus().isEmpty()) {
    throw new IllegalStateException(
        "Impossible de supprimer cet administrateur. " +
        "Il gère encore X contenu(s). Veuillez d'abord réassigner ou supprimer ces contenus."
    );
}
```

**Relation:** `Admin` → `List<Contenu> contenus`

---

### **2. Vérification des Catégories**
```java
if (admin.getCategories() != null && !admin.getCategories().isEmpty()) {
    throw new IllegalStateException(
        "Impossible de supprimer cet administrateur. " +
        "Il gère encore X catégorie(s). Veuillez d'abord réassigner ou supprimer ces catégories."
    );
}
```

**Relation:** `Admin` → `List<Categorie> categories`

---

### **3. Vérification des Actions de Gestion**
```java
if (admin.getGestionsAdmin() != null && !admin.getGestionsAdmin().isEmpty()) {
    throw new IllegalStateException(
        "Impossible de supprimer cet administrateur. " +
        "Il a encore X action(s) de gestion associée(s). Veuillez d'abord nettoyer ces données."
    );
}
```

**Relation:** `Admin` → `List<GestionAdmin> gestionsAdmin`

---

## 📡 Endpoint Concerné

### **DELETE /api/admin/profile/{id}**
Supprimer définitivement un compte admin.

**Requête:**
```http
DELETE /api/admin/profile/1
Authorization: Bearer {token}
```

---

### **Scénario 1: Suppression Réussie** ✅

**Conditions:**
- Admin n'a aucun contenu
- Admin n'a aucune catégorie
- Admin n'a aucune action de gestion

**Réponse (200 OK):**
```json
"Compte administrateur supprimé définitivement"
```

**Logs:**
```
WARN  - Tentative de suppression définitive du compte admin: 1
INFO  - Compte admin 1 supprimé définitivement (Nom: Jean Dupont, Email: jean@mussodeme.com)
```

---

### **Scénario 2: Erreur - Contenus Associés** ❌

**Condition:** Admin gère 5 contenus

**Réponse (400 Bad Request):**
```json
{
  "timestamp": "2025-10-22T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Impossible de supprimer cet administrateur. Il gère encore 5 contenu(s). Veuillez d'abord réassigner ou supprimer ces contenus.",
  "path": "/api/admin/profile/1"
}
```

**Logs:**
```
WARN  - Tentative de suppression définitive du compte admin: 1
WARN  - Impossible de supprimer l'admin 1: 5 contenu(s) rattaché(s)
```

---

### **Scénario 3: Erreur - Catégories Associées** ❌

**Condition:** Admin gère 3 catégories

**Réponse (400 Bad Request):**
```json
{
  "timestamp": "2025-10-22T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Impossible de supprimer cet administrateur. Il gère encore 3 catégorie(s). Veuillez d'abord réassigner ou supprimer ces catégories.",
  "path": "/api/admin/profile/1"
}
```

**Logs:**
```
WARN  - Tentative de suppression définitive du compte admin: 1
WARN  - Impossible de supprimer l'admin 1: 3 catégorie(s) rattachée(s)
```

---

### **Scénario 4: Erreur - Actions de Gestion** ❌

**Condition:** Admin a 10 actions de gestion

**Réponse (400 Bad Request):**
```json
{
  "timestamp": "2025-10-22T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Impossible de supprimer cet administrateur. Il a encore 10 action(s) de gestion associée(s). Veuillez d'abord nettoyer ces données.",
  "path": "/api/admin/profile/1"
}
```

---

## 🔄 Processus de Suppression

### **Diagramme de Flux:**

```
Demande de suppression admin
         ↓
Récupération de l'admin par ID
         ↓
Admin existe? → NON → 404 Not Found
         ↓ OUI
Admin a des contenus? → OUI → 400 Bad Request (Contenus)
         ↓ NON
Admin a des catégories? → OUI → 400 Bad Request (Catégories)
         ↓ NON
Admin a des gestions? → OUI → 400 Bad Request (Gestions)
         ↓ NON
Suppression autorisée
         ↓
DELETE FROM admin
         ↓
200 OK - Supprimé avec succès
```

---

## 💡 Recommandations

### **Avant de Supprimer un Admin:**

#### **Étape 1: Vérifier les Contenus**
```http
GET /api/admin/contenus
Authorization: Bearer {token}
```
Filtrer par `adminId` et:
- Soit **supprimer** les contenus
- Soit **réassigner** à un autre admin

#### **Étape 2: Vérifier les Catégories**
```http
GET /api/admin/categories
Authorization: Bearer {token}
```
Filtrer par `adminId` et:
- Soit **supprimer** les catégories
- Soit **réassigner** à un autre admin

#### **Étape 3: Nettoyer les Actions de Gestion**
- Supprimer ou archiver les actions de gestion associées

#### **Étape 4: Tenter la Suppression**
```http
DELETE /api/admin/profile/{id}
Authorization: Bearer {token}
```

---

## 🎯 Alternative: Désactivation

**Au lieu de supprimer définitivement**, il est recommandé de **désactiver** le compte:

```http
PATCH /api/admin/profile/{id}/deactivate
Authorization: Bearer {token}
```

### **Avantages de la Désactivation:**
- ✅ Préserve les données et l'historique
- ✅ Aucune contrainte sur les données rattachées
- ✅ Révocable (peut être réactivé)
- ✅ Traçabilité complète
- ✅ Pas de perte de données

---

## 🔧 Code Implémenté

### **Méthode AdminService.deleteAdmin():**

```java
@Transactional
public void deleteAdmin(Long adminId) {
    log.warn("Tentative de suppression définitive du compte admin: {}", adminId);
    
    Admin admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new NotFoundException(...));
    
    // 1. Vérification Contenus
    if (admin.getContenus() != null && !admin.getContenus().isEmpty()) {
        int nbContenus = admin.getContenus().size();
        log.warn("Impossible de supprimer l'admin {}: {} contenu(s) rattaché(s)", 
                adminId, nbContenus);
        throw new IllegalStateException(...);
    }
    
    // 2. Vérification Catégories
    if (admin.getCategories() != null && !admin.getCategories().isEmpty()) {
        int nbCategories = admin.getCategories().size();
        log.warn("Impossible de supprimer l'admin {}: {} catégorie(s) rattachée(s)", 
                adminId, nbCategories);
        throw new IllegalStateException(...);
    }
    
    // 3. Vérification Actions de Gestion
    if (admin.getGestionsAdmin() != null && !admin.getGestionsAdmin().isEmpty()) {
        int nbGestions = admin.getGestionsAdmin().size();
        log.warn("Impossible de supprimer l'admin {}: {} action(s) de gestion", 
                adminId, nbGestions);
        throw new IllegalStateException(...);
    }
    
    // 4. Suppression autorisée
    adminRepository.deleteById(adminId);
    log.info("Compte admin {} supprimé définitivement (Nom: {}, Email: {})", 
            adminId, admin.getNom(), admin.getEmail());
}
```

---

## 🧪 Tests

### **Test 1: Admin Sans Données** ✅
```java
@Test
void deleteAdmin_WhenNoDataAttached_ShouldSucceed() {
    // Admin sans contenus, catégories, ni gestions
    adminService.deleteAdmin(1L);
    // Devrait réussir
}
```

### **Test 2: Admin Avec Contenus** ❌
```java
@Test
void deleteAdmin_WhenHasContenus_ShouldThrowException() {
    // Admin avec 5 contenus
    assertThrows(IllegalStateException.class, 
        () -> adminService.deleteAdmin(1L));
}
```

### **Test 3: Admin Avec Catégories** ❌
```java
@Test
void deleteAdmin_WhenHasCategories_ShouldThrowException() {
    // Admin avec 3 catégories
    assertThrows(IllegalStateException.class, 
        () -> adminService.deleteAdmin(1L));
}
```

---

## 📊 Comparaison Avant/Après

| Aspect | Avant | Après |
|--------|-------|-------|
| **Vérifications** | ❌ Aucune | ✅ 3 vérifications |
| **Intégrité données** | ⚠️ Risque de perte | ✅ Protégée |
| **Messages d'erreur** | ❌ Génériques | ✅ Spécifiques avec compteurs |
| **Logging** | ⚠️ Basique | ✅ Détaillé avec contexte |
| **Sécurité** | ⚠️ Permissive | ✅ Restrictive |

---

## ⚠️ Points d'Attention

### **1. Cascade Delete:**
- Les vérifications se font **avant** la suppression
- Pas de cascade automatique
- L'admin doit **manuellement nettoyer** ses données

### **2. Performance:**
- Les relations sont **lazy-loaded** par défaut
- La vérification force le chargement des collections
- Pour un admin avec beaucoup de données, peut être lent

### **3. Transaction:**
- Tout se fait dans une transaction `@Transactional`
- En cas d'erreur, rollback automatique

---

## 🚀 Améliorations Futures

### **Court Terme:**
- [ ] Endpoint pour compter les dépendances d'un admin
- [ ] Endpoint pour réassigner les données en masse
- [ ] Vérification supplémentaire des utilisateurs créés

### **Moyen Terme:**
- [ ] Interface UI pour visualiser les dépendances
- [ ] Migration automatique des données vers un autre admin
- [ ] Archivage au lieu de suppression

### **Long Terme:**
- [ ] Soft delete avec timestamp de suppression
- [ ] Historique complet des actions de suppression
- [ ] Workflow d'approbation pour suppression admin

---

## 💡 Exemples d'Utilisation

### **Exemple 1: Suppression Impossible - Contenus**
```bash
# Tentative de suppression
curl -X DELETE http://localhost:5500/api/admin/profile/1 \
  -H "Authorization: Bearer {token}"

# Réponse
{
  "message": "Impossible de supprimer cet administrateur. Il gère encore 12 contenu(s). Veuillez d'abord réassigner ou supprimer ces contenus.",
  "status": 400
}

# Action: Supprimer ou réassigner les 12 contenus
# Puis réessayer
```

### **Exemple 2: Suppression Réussie**
```bash
# Admin nettoyé de toutes ses données
curl -X DELETE http://localhost:5500/api/admin/profile/5 \
  -H "Authorization: Bearer {token}"

# Réponse
{
  "message": "Compte administrateur supprimé définitivement",
  "status": 200
}
```

---

## ✅ Checklist de Sécurité

- [x] Vérification des contenus associés
- [x] Vérification des catégories associées
- [x] Vérification des actions de gestion
- [x] Messages d'erreur informatifs
- [x] Logging détaillé
- [x] Transaction atomique
- [x] Comptage précis des dépendances
- [x] Alternative (désactivation) disponible

---

**Date de création:** 22 octobre 2025  
**Version:** 1.0  
**Status:** ✅ Implémenté et documenté
