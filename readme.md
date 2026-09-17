# Task Manager — Application Full Stack

Application de gestion de tâches avec authentification JWT, CRUD complet et déploiement Docker.

---

## Fonctionnalités

### Authentification
- Inscription avec email unique
- Connexion sécurisée par JWT
- Mots de passe hashés avec BCrypt
- Routes protégées

### Gestion des tâches
- Créer, modifier et supprimer une tâche
- Liste personnelle par utilisateur
- Recherche par titre
- Filtrage par statut (TODO, IN_PROGRESS, DONE)

### Technique
- Dockerisation complète (backend + frontend + MySQL)
- Documentation Swagger/OpenAPI
- Build multi-stage Docker
- Interface Tailwind CSS

---

## Stack technique

### Backend
| Technologie | Version |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| Hibernate | 6.4 |
| MySQL | 8.0 |
| JJWT | 0.11.5 |
| Lombok | 1.18 |
| Springdoc OpenAPI | 2.5 |
| Maven | 3.9 |

### Frontend
| Technologie | Version |
|---|---|
| React | 19 |
| Vite | 7.x |
| TypeScript | 5.x |
| Tailwind CSS | 3.x |
| Axios | 1.x |
| React Router | 7.x |

### DevOps
| Outil | Rôle |
|---|---|
| Docker | Conteneurisation |
| Docker Compose | Orchestration |
| Nginx | Serveur frontend |

---

## Architecture

### Vue d'ensemble

```
React Frontend (Nginx :3000)  ──HTTP──▶  Spring Boot API (Tomcat :8080)
                                                    │ JDBC
                                                    ▼
                                            MySQL 8 (:3306)
```

### Couches backend

```
HTTP Request
     │
     ▼
JwtAuthenticationFilter
     │
     ▼
Controller  →  Service  →  Repository  →  MySQL
```

### Choix techniques

- Architecture en couches (Controller → Service → Repository)
- DTO pattern : les entités JPA ne sont jamais exposées
- JWT stateless (pas de session serveur)
- Isolation des tâches par utilisateur
- Gestion d'erreurs centralisée
- Configuration externalisée (variables d'environnement)

---

## Installation

### Prérequis

- Docker Desktop (recommandé)
- OU : Java 17+, Node.js 20+, MySQL 8

### Méthode 1 : Docker

```bash
git clone https://github.com/joram-nzietchou/task-manager.git
cd task-manager

docker compose -f task-manager-backend/docker-compose.yml up --build -d
```

**Accès :**

| Service | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| MySQL | localhost:3307 |

**Arrêter :**

```bash
docker compose -f task-manager-backend/docker-compose.yml down
```

### Méthode 2 : Manuelle

#### Backend

```bash
cd task-manager-backend

mysql -u root -p -e "CREATE DATABASE taskmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

./mvnw spring-boot:run
```

API disponible sur http://localhost:8080

#### Frontend

```bash
cd task-manager-frontend

npm install
npm run dev
```

Frontend disponible sur http://localhost:5173

---

## Utilisation

1. Ouvrir http://localhost:3000
2. S'inscrire avec un email, un mot de passe (min. 6 caractères) et un nom complet
3. Se connecter
4. Créer une tâche via le bouton "+ Nouvelle tâche"
5. Filtrer par statut ou rechercher par titre
6. Modifier ou supprimer une tâche
7. Se déconnecter

### Test avec cURL

```bash
# Inscription
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"secret123","fullName":"Test User"}'

# Créer une tâche (remplacer <TOKEN>)
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"title":"Ma première tâche","description":"Test","status":"TODO"}'

# Lister les tâches
curl http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <TOKEN>"
```

---

## API Endpoints

### Authentification (public)

| Méthode | Endpoint | Body |
|---|---|---|
| POST | `/api/auth/register` | `{ email, password, fullName }` |
| POST | `/api/auth/login` | `{ email, password }` |

**Réponse :**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "user@example.com",
  "fullName": "utilisateur test"
}
```

### Tâches (JWT requis)

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/tasks` | Liste (`?status=TODO&search=titre`) |
| POST | `/api/tasks` | Créer |
| PUT | `/api/tasks/{id}` | Modifier |
| DELETE | `/api/tasks/{id}` | Supprimer |

**Body (POST/PUT) :**

```json
{
  "title": "Titre",
  "description": "Description optionnelle",
  "status": "TODO"
}
```

### Codes d'erreur

| Code | Signification |
|---|---|
| 200 | Succès |
| 201 | Créé |
| 204 | Supprimé |
| 400 | Erreur de validation |
| 401 | Non authentifié |
| 404 | Non trouvé |
| 500 | Erreur interne |

**Format d'erreur :**

```json
{
  "timestamp": "2026-09-17T18:00:00",
  "status": 400,
  "message": "Erreur de validation",
  "errors": {
    "email": "Format d'email invalide"
  }
}
```

---

## Tests

### Backend

```bash
cd task-manager-backend
./mvnw test
```

### Test manuel via Swagger

1. Ouvrir http://localhost:8080/swagger-ui.html
2. Tester **POST /api/auth/register** → copier le token
3. Cliquer sur **Authorize** → coller le token
4. Tester les endpoints `/api/tasks`

---

## Structure du projet

```
task-manager/
├── task-manager-backend/
│   ├── src/main/java/com/taskmanager/task_manager_backend/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── TaskManagerBackendApplication.java
│   ├── src/main/resources/application.yml
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── pom.xml
│
├── task-manager-frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── context/
│   │   ├── pages/
│   │   ├── types/
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
└── README.md
```

---

## Auteur

**Joram Nzietchou**
- Email : joramnzietchou@gmail.com
- GitHub : [joram-nzietchou](https://github.com/joram-nzietchou)
```