# 📌 RÔLE : Image Docker du frontend React (build multi-stage + Nginx).
# 📌 POINTS CLÉS :
#   - Stage 1 : build avec Node (npm ci + npm run build)
#   - Stage 2 : sert les fichiers statiques avec Nginx
#   - Image finale : ~30 Mo (au lieu de ~500 Mo avec Node)
# 📌 QUESTION POSSIBLE : "Pourquoi Nginx et pas Vite preview ?"
#     → Nginx est un vrai serveur de prod, optimisé pour les fichiers statiques

# --- Étape 1 : Build ---
FROM node:20 AS build
WORKDIR /app

# Cache des dépendances (optimisation)
COPY package*.json ./
RUN npm ci

# Build du projet
COPY . .
RUN npm run build

# --- Étape 2 : Runtime avec Nginx ---
FROM nginx:alpine

# Copie des fichiers buildés
COPY --from=build /app/dist /usr/share/nginx/html

# Configuration Nginx personnalisée (SPA routing)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]