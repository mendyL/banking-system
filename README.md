
# 🏦 Banking System

Système modulaire pour la gestion de messages bancaires via IBM MQ Series et la configuration de partenaires techniques.

## 📦 Structure du projet

```
banking-system/
│
├── banking-partner-service     # API REST pour la gestion des partenaires MQ
├── banking-queue-service       # Traitement et stockage des messages MQ
├── partners-front              # Frontend Angular 19 (Messages + Partenaires)
├── initdb                      # Script SQL d'initialisation de la base
├── docker-compose.yml          # Stack complète (services, MQ, DB)
└── README.md
```

## ⚙️ Environnement technique

- **Java 21**, **Spring Boot 3**
- **Angular 19** + Angular Material
- **PostgreSQL** 16
- **IBM MQ Series**
- **Docker** & `docker-compose`
- Architecture **Hexagonale** (Ports & Adapters)
- Tests unitaires & d’intégration

## 🧩 Fonctionnalités principales

### 🎯 Objectifs

- Lire des messages IBM MQ et les stocker en base
- Afficher les messages dans l’IHM (liste + détail en popin)
- Exposer des API REST (consultation + suppression)
- Gérer des partenaires MQ via API + IHM
- Suppression en cascade des messages liés à un partenaire

### 🗃️ Format message IBM MQ

Messages textuels au format JSON :

```json
{
  "PARTNER_ID": "550e8400-e29b-41d4-a716-446655440000",
  "PAYLOAD": "partner message"
}
```

## 🚀 Lancer le projet

### Prérequis

- Docker et Docker Compose

### Démarrage

```bash
docker-compose up --build
```

IHM disponible sur : [http://localhost:4200/home](http://localhost:4200/home)

### Envoi d’un message de test

```bash
docker exec -it banking-system-ibmmq-1 bash

cat > /tmp/banking-message.txt << EOL
ID:414d5120514d312020202020202020207e5d0e62028b0040
Test message
EOL

/opt/mqm/samp/bin/amqsput DEV.QUEUE.1 QM1 < /tmp/banking-message.txt
```

## 📡 Endpoints REST

### 🔁 `banking-queue-service`

- `GET /api/messages?page=0&size=20` : liste paginée des messages
- `DELETE /api/messages/partners/{partnerId}` : suppression des messages d’un partenaire

### 👥 `banking-partner-service`

- `POST /api/partners` : créer un partenaire
- `GET /api/partners` : liste des partenaires
- `GET /api/partners/{id}` : détail partenaire
- `DELETE /api/partners/{id}` : suppression


### 🔍 Exemple de requête cURL

Récupérer les 3 premiers messages paginés :

```bash
curl --location 'http://localhost:8081/api/messages?page=0&size=3'
```

### ✅ Validation partenaire

- `alias`, `type`, `direction` (INBOUND / OUTBOUND), `processedFlowType` (MESSAGE / ALERTING / NOTIFICATION), `description` sont obligatoires
- Validation personnalisée via annotation `@ValidatedPartnerRequest`

## 🧪 Tests

- Tests d’intégration MQ (`MqMessageListenerIntegrationTest`)
- Tests controller `PartnerControllerTest` (valeurs valides/invalides, enums, statut HTTP)
- Exécutables via `mvn test`

## 📁 Données de démo

Données partenaires injectées via `initdb/1-data.sql` à l'initialisation de Postgres.

## 📃 Licence

Projet open-source à usage démonstratif.
