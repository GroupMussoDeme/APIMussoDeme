# API pour les Femmes Rurales - Documentation

##  Vue d'ensemble

L'application **MusoDeme** est conçue spécifiquement pour les **femmes rurales** avec une interface **audio et pictogrammes**. Les femmes utilisent l'application uniquement par audio (elles écoutent les informations et enregistrent leurs commandes vocales).

##  Concept Important : Femmes Rurales = Acheteuses

**IMPORTANT** : Il n'y a **PAS de profil "Acheteur" séparé** dans le système.

- Les **Femmes Rurales** deviennent des **"Acheteuses"** lorsqu'elles achètent des produits
- Les relations `Commande` et `Paiement` pointent vers `Utilisateur` (classe parente)
- Une `FemmeRurale` peut donc passer des commandes directement

##  Fonctionnalités Principales

### 1️ Gestion des Contenus (Audio/Vidéo)

#### Écouter un contenu
```http
POST /api/femmes-rurales/{femmeId}/contenus/{contenuId}/ecouter
```
- Enregistre la date de lecture pour traçabilité
- Retourne les détails du contenu (titre, URL audio/vidéo, durée, etc.)

#### Lister les audios
```http
GET /api/femmes-rurales/contenus/audios
```
Retourne tous les contenus audio disponibles.

#### Lister les vidéos
```http
GET /api/femmes-rurales/contenus/videos
```
Retourne toutes les vidéos de formation disponibles.

#### Lister par TypeInfo
```http
GET /api/femmes-rurales/contenus/type/{typeInfo}
```
**TypeInfo disponibles :**
- `SANTE` - Informations sur la santé
- `NUTRITION` - Conseils nutritionnels
- `DROIT` - Informations juridiques
- `VIDEO_FORMATION` - Vidéos de formation professionnelle

#### Historique de lecture
```http
GET /api/femmes-rurales/{femmeId}/historique-lecture
```
Retourne l'historique complet des contenus écoutés/regardés.

---

### 2️⃣ Gestion des Produits

#### Publier un produit
```http
POST /api/femmes-rurales/{femmeId}/produits
```
**Body (JSON) :**
```json
{
  "nom": "Savon artisanal au karité",
  "description": "Savon naturel fait main",
  "image": "https://example.com/savon.jpg",
  "quantite": 50,
  "prix": 2500,
  "audioGuideUrl": "https://example.com/audio/savon-guide.mp3"
}
```
- `audioGuideUrl` : Audio vocal décrivant le produit (ex: "Ce savon est fait avec du beurre de karité...")

#### Voir mes produits
```http
GET /api/femmes-rurales/{femmeId}/mes-produits
```
Retourne tous les produits publiés par cette femme rurale.

#### Voir tous les produits
```http
GET /api/femmes-rurales/produits
```
Retourne tous les produits de toutes les femmes rurales.

#### Détails d'un produit avec audio
```http
GET /api/femmes-rurales/produits/{produitId}
```
Retourne les détails complets incluant l'audio de guide.

---

### 3️⃣ Chat Vocal

#### Envoyer un message vocal privé
```http
POST /api/femmes-rurales/{expediteurId}/chats/envoyer
  ?destinataireId=123
  &audioUrl=https://example.com/message.mp3
  &duree=00:45
```
Envoie un message vocal à une autre femme rurale.

#### Historique de chat
```http
GET /api/femmes-rurales/{femme1Id}/chats/{femme2Id}
```
Retourne l'historique complet des messages vocaux échangés.

#### Marquer comme lu
```http
PUT /api/femmes-rurales/chats/{messageId}/lu
```
Marque un message vocal comme écouté.

---

### 4️⃣ Gestion des Coopératives

Les coopératives fonctionnent comme des **groupes WhatsApp** où les femmes rurales peuvent :
- Échanger des messages vocaux
- Partager des informations
- **Partager des contenus éducatifs (audio, vidéo, photos)**
- Collaborer

#### Créer une coopérative
```http
POST /api/femmes-rurales/{femmeId}/cooperatives
  ?nom=Coopérative Karité Bamako
  &description=Groupe pour producteurs de karité
```

#### Joindre une coopérative
```http
POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/joindre
```

#### Lister mes coopératives
```http
GET /api/femmes-rurales/{femmeId}/mes-cooperatives
```

#### Envoyer un message dans une coopérative
```http
POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/messages
  ?audioUrl=https://example.com/message-coop.mp3
  &duree=01:30
```

#### Messages d'une coopérative
```http
GET /api/femmes-rurales/cooperatives/{cooperativeId}/messages
```

---

### 5️⃣ Partage de Contenus dans les Coopératives

#### Partager un contenu (audio/vidéo/photo)
```http
POST /api/femmes-rurales/{femmeId}/cooperatives/{cooperativeId}/partager
  ?contenuId=789
  &messageAudioUrl=https://example.com/audio/mon-message.mp3
```
Permet de partager un contenu éducatif avec un message vocal optionnel.

**Exemple de message vocal :**
 "Écoutez cet audio sur la nutrition, c'est très utile pour nos bébés"

#### Voir les contenus partagés
```http
GET /api/femmes-rurales/cooperatives/{cooperativeId}/partages
```
Retourne tous les contenus partagés dans la coopérative.

#### Filtrer par type
```http
GET /api/femmes-rurales/cooperatives/{cooperativeId}/partages/type/{typeInfo}
```
**Types disponibles :** SANTE, NUTRITION, DROIT, VIDEO_FORMATION

#### Mes partages
```http
GET /api/femmes-rurales/{femmeId}/mes-partages
```
Liste tous les contenus que j'ai partagés.

#### Supprimer un partage
```http
DELETE /api/femmes-rurales/{femmeId}/partages/{partageId}
```
Seule la femme qui a partagé peut supprimer.

---

### 6️⃣ Commandes et Paiements

#### Passer une commande
```http
POST /api/femmes-rurales/{femmeId}/commandes
  ?produitId=456
  &quantite=5
```
La femme rurale devient "acheteuse" en passant une commande.

#### Payer par Mobile Money
```http
POST /api/femmes-rurales/{femmeId}/commandes/{commandeId}/payer
  ?montant=12500
```
**Mode de paiement :** `MOBILE_MONEY`

Actions automatiques :
- Statut commande → `PAYEE`
- Quantité produit → Déduction automatique
- Enregistrement du paiement

#### Mes commandes
```http
GET /api/femmes-rurales/{femmeId}/mes-commandes
```
Liste toutes les commandes passées par cette femme.

---

##  Sécurité

Tous les endpoints nécessitent :
- **Authentification JWT**
- **Rôle** : `FEMME_RURALE`

Header requis :
```
Authorization: Bearer <JWT_TOKEN>
```

---

##  Architecture des Données

### Hiérarchie des Utilisateurs

```
Utilisateur (abstract)
├── FemmeRurale (SINGLE_TABLE inheritance)
│   ├── Produits
│   ├── Appartenances (coopératives)
│   ├── Commandes (en tant qu'acheteuse)
│   └── Paiements (en tant qu'acheteuse)
├── Admin
└── Acheteur (existe mais les FemmeRurale sont aussi acheteuses)
```

### Relations Clés

- **Commande.acheteur** → `Utilisateur` (peut être une FemmeRurale)
- **Paiement.acheteur** → `Utilisateur` (peut être une FemmeRurale)
- **Produit.femmeRurale** → `FemmeRurale` (vendeuse)
- **ChatVocal.expediteur/destinataire** → `FemmeRurale`
- **Appartenance** → Lie FemmeRurale et Coopérative

---

##  Interface Audio

### Principe
Les femmes rurales **NE LISENT PAS** de texte. Elles utilisent :
1. **Audio** pour écouter les informations
2. **Pictogrammes** pour naviguer
3. **Enregistrement vocal** pour leurs actions

### Exemples d'utilisation

**Inscription :**
```
 "Dites votre nom après le bip" → Enregistrement
 "Dites votre numéro de téléphone" → Enregistrement
```

**Recherche de produits :**
```
 Pictogramme "Santé" → Liste audio des produits santé
 Écoute des descriptions vocales de chaque produit
```

**Passage de commande :**
```
 Pictogramme "Panier"
 "Combien voulez-vous ?" → Enregistrement vocal "5"
 Pictogramme "Mobile Money"
 Confirmation audio
```

---

##  Technologies Utilisées

- **Framework** : Spring Boot 3.5.6
- **Java** : 21
- **Base de données** : MySQL
- **Sécurité** : Spring Security + JWT
- **Mapping** : ModelMapper
- **Validation** : Jakarta Validation

---

##  Notes Importantes

1. **Pas de profil Acheteur séparé** : Les FemmeRurale achètent directement
2. **Audio obligatoire** : Chaque produit doit avoir un `audioGuideUrl`
3. **Chat asynchrone** : Upload audio → stockage → notification (adapté zones rurales)
4. **Mobile Money** : Mode de paiement principal
5. **Coopératives** : Groupes collaboratifs entre femmes rurales
6. **Partage de contenus** : Les femmes peuvent partager des contenus éducatifs dans leurs coopératives avec message vocal d'accompagnement

---

##  Prochaines Étapes

- [ ] Intégration API Mobile Money (Orange Money, MTN, etc.)
- [ ] Système de notifications push audio
- [ ] Reconnaissance vocale pour commandes
- [ ] Géolocalisation pour recherche de produits locaux
- [ ] Statistiques pour les femmes rurales
