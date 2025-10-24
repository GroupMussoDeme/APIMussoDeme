# 🗄️ Migration Base de Données - Gestion des Contenus

## 📋 Vue d'ensemble

Ce document décrit les modifications à apporter à la base de données MySQL pour supporter la nouvelle gestion des contenus.

---

## ⚠️ IMPORTANT : Sauvegarde Obligatoire

**AVANT TOUTE MIGRATION, EFFECTUER UN BACKUP COMPLET !**

```bash
# Backup complet de la base
mysqldump -u root -p mussodeme_db > backup_mussodeme_$(date +%Y%m%d_%H%M%S).sql

# Ou via MySQL Workbench : Server → Data Export
```

---

## 🔧 Modifications Requises

### 1. Renommer la Table `audioConseil` → `contenu`

**Raison** : Nom plus générique pour supporter AUDIO, VIDEO, et INSTITUTION_FINANCIERE.

```sql
-- Vérifier que la table existe
SHOW TABLES LIKE 'audioConseil';

-- Renommer la table
ALTER TABLE audioConseil RENAME TO contenu;

-- Vérifier le renommage
SHOW TABLES LIKE 'contenu';
```

**Vérification** :
```sql
DESC contenu;
```

**Résultat attendu** :
```
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | bigint       | NO   | PRI | NULL    | auto_increment |
| titre       | varchar(255) | YES  |     | NULL    |                |
| langue      | varchar(50)  | YES  |     | NULL    |                |
| description | text         | YES  |     | NULL    |                |
| url_contenu | varchar(500) | YES  |     | NULL    |                |
| duree       | varchar(50)  | YES  |     | NULL    |                |
| categorie_id| bigint       | YES  | MUL | NULL    |                |
| admin_id    | bigint       | YES  | MUL | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+
```

---

### 2. Ajouter le Champ `date_lecture` dans `utilisateur_audio`

**Raison** : Tracer quand un utilisateur consulte un contenu.

```sql
-- Ajouter la colonne
ALTER TABLE utilisateur_audio 
ADD COLUMN date_lecture DATETIME 
COMMENT 'Date et heure de lecture du contenu par l\'utilisateur';

-- Vérifier l'ajout
DESC utilisateur_audio;
```

**Résultat attendu** :
```
+----------------+--------------+------+-----+---------+----------------+
| Field          | Type         | Null | Key | Default | Extra          |
+----------------+--------------+------+-----+---------+----------------+
| id             | bigint       | NO   | PRI | NULL    | auto_increment |
| id_utilisateur | bigint       | NO   | MUL | NULL    |                |
| audio_id       | bigint       | YES  | MUL | NULL    |                |
| date_lecture   | datetime     | YES  |     | NULL    |                |
+----------------+--------------+------+-----+---------+----------------+
```

---

## 🚀 Optimisations Recommandées

### 3. Ajouter des Index pour les Performances

```sql
-- Index sur categorie_id dans contenu (si n'existe pas déjà)
CREATE INDEX idx_contenu_categorie 
ON contenu(categorie_id);

-- Index sur admin_id dans contenu
CREATE INDEX idx_contenu_admin 
ON contenu(admin_id);

-- Index sur date_lecture pour les statistiques
CREATE INDEX idx_utilisateur_audio_date_lecture 
ON utilisateur_audio(date_lecture);

-- Index composite pour filtrage utilisateur + contenu
CREATE INDEX idx_utilisateur_audio_user_content 
ON utilisateur_audio(id_utilisateur, audio_id);
```

**Vérification des index** :
```sql
SHOW INDEX FROM contenu;
SHOW INDEX FROM utilisateur_audio;
```

---

## 📊 Vérifications Post-Migration

### Test 1 : Vérifier que les données existent toujours
```sql
-- Compter les contenus
SELECT COUNT(*) AS total_contenus FROM contenu;

-- Vérifier quelques enregistrements
SELECT id, titre, langue, duree FROM contenu LIMIT 5;
```

### Test 2 : Vérifier les relations
```sql
-- Vérifier la relation avec categorie
SELECT 
    c.id,
    c.titre,
    cat.id AS categorie_id,
    cat.type_categorie
FROM contenu c
INNER JOIN categorie cat ON c.categorie_id = cat.id
LIMIT 5;

-- Vérifier la relation avec admin
SELECT 
    c.id,
    c.titre,
    a.id AS admin_id,
    a.nom AS admin_nom
FROM contenu c
INNER JOIN admin a ON c.admin_id = a.id
LIMIT 5;
```

### Test 3 : Vérifier le nouveau champ date_lecture
```sql
-- Vérifier que la colonne existe
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'utilisateur_audio' 
AND COLUMN_NAME = 'date_lecture';
```

---

## 🔄 Script de Migration Complet

```sql
-- ================================================
-- MIGRATION GESTION DES CONTENUS
-- Date : 2025-10-22
-- Version : 1.0
-- ================================================

-- Démarrer une transaction pour rollback en cas d'erreur
START TRANSACTION;

-- 1. Renommer la table audioConseil
ALTER TABLE audioConseil RENAME TO contenu;

-- 2. Ajouter date_lecture dans utilisateur_audio
ALTER TABLE utilisateur_audio 
ADD COLUMN date_lecture DATETIME 
COMMENT 'Date et heure de lecture du contenu';

-- 3. Ajouter les index pour performances
CREATE INDEX IF NOT EXISTS idx_contenu_categorie 
ON contenu(categorie_id);

CREATE INDEX IF NOT EXISTS idx_contenu_admin 
ON contenu(admin_id);

CREATE INDEX IF NOT EXISTS idx_utilisateur_audio_date_lecture 
ON utilisateur_audio(date_lecture);

CREATE INDEX IF NOT EXISTS idx_utilisateur_audio_user_content 
ON utilisateur_audio(id_utilisateur, audio_id);

-- Valider si tout s'est bien passé
COMMIT;

-- En cas d'erreur, annuler avec : ROLLBACK;
```

---

## 🧪 Tests Fonctionnels

### 1. Insérer un contenu de test
```sql
INSERT INTO contenu (titre, langue, description, url_contenu, duree, categorie_id, admin_id)
VALUES (
    'Test Migration - Audio',
    'fr',
    'Contenu de test après migration',
    'https://example.com/test.mp3',
    '10:00',
    2, -- ID de la catégorie AUDIOS
    1  -- ID de l'admin
);

-- Vérifier l'insertion
SELECT * FROM contenu WHERE titre = 'Test Migration - Audio';
```

### 2. Insérer une lecture de contenu
```sql
INSERT INTO utilisateur_audio (id_utilisateur, audio_id, date_lecture)
VALUES (
    1,  -- ID utilisateur
    1,  -- ID contenu
    NOW()
);

-- Vérifier l'insertion
SELECT * FROM utilisateur_audio WHERE date_lecture IS NOT NULL;
```

---

## 🔙 Rollback en Cas de Problème

Si la migration échoue, restaurer le backup :

```bash
# Restaurer le backup
mysql -u root -p mussodeme_db < backup_mussodeme_20251022_103000.sql
```

Ou manuellement :

```sql
-- Annuler le renommage
ALTER TABLE contenu RENAME TO audioConseil;

-- Supprimer la colonne date_lecture
ALTER TABLE utilisateur_audio DROP COLUMN date_lecture;

-- Supprimer les index
DROP INDEX idx_contenu_categorie ON contenu;
DROP INDEX idx_contenu_admin ON contenu;
DROP INDEX idx_utilisateur_audio_date_lecture ON utilisateur_audio;
DROP INDEX idx_utilisateur_audio_user_content ON utilisateur_audio;
```

---

## 📈 Statistiques Post-Migration

### Vérifier la taille des tables
```sql
SELECT 
    TABLE_NAME,
    ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024, 2) AS 'Size (MB)',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'mussodeme_db'
AND TABLE_NAME IN ('contenu', 'utilisateur_audio', 'categorie', 'institution_financiere')
ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;
```

### Analyser les index
```sql
ANALYZE TABLE contenu;
ANALYZE TABLE utilisateur_audio;
```

---

## ✅ Checklist de Migration

- [ ] **BACKUP** : Sauvegarde complète effectuée
- [ ] **Test 1** : Renommer table audioConseil → contenu ✅
- [ ] **Test 2** : Ajouter colonne date_lecture ✅
- [ ] **Test 3** : Créer les index de performance ✅
- [ ] **Test 4** : Vérifier les données existantes
- [ ] **Test 5** : Tester insertion nouveau contenu
- [ ] **Test 6** : Tester insertion lecture utilisateur
- [ ] **Test 7** : Vérifier les relations FK (categorie, admin)
- [ ] **Test 8** : Lancer l'application Spring Boot
- [ ] **Test 9** : Tester les endpoints API
- [ ] **Test 10** : Vérifier les logs applicatifs

---

## 🔧 Configuration JPA/Hibernate

**Important** : Si vous utilisez `spring.jpa.hibernate.ddl-auto=update`, Hibernate devrait détecter automatiquement le nouveau champ `dateLecture`.

**Vérifier dans application.properties** :
```properties
# Option 1 : Laisser Hibernate gérer (DÉVELOPPEMENT uniquement)
spring.jpa.hibernate.ddl-auto=update

# Option 2 : Mode production (migration manuelle recommandée)
spring.jpa.hibernate.ddl-auto=validate
```

---

## 📝 Notes Importantes

1. **Environnement de test** : Tester d'abord sur une base de développement
2. **Horaire de migration** : Planifier en dehors des heures de pointe
3. **Durée estimée** : < 5 minutes pour une base < 100k enregistrements
4. **Monitoring** : Surveiller les logs MySQL pendant la migration
5. **Application** : Arrêter l'application Spring Boot pendant la migration

---

## 🆘 Support et Dépannage

### Erreur : "Table 'audioConseil' doesn't exist"
**Solution** : La table a peut-être déjà été renommée. Vérifier avec `SHOW TABLES;`

### Erreur : "Duplicate column name 'date_lecture'"
**Solution** : La colonne existe déjà. Vérifier avec `DESC utilisateur_audio;`

### Erreur : "Cannot add foreign key constraint"
**Solution** : Vérifier que les tables categorie et admin existent et ont les bonnes clés primaires.

---

## 📞 Contact

En cas de problème pendant la migration, contacter l'équipe technique MussoDeme.

---

**Date de création** : 2025-10-22  
**Version** : 1.0  
**Testé sur** : MySQL 8.0+
