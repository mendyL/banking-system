FROM node:20-alpine
WORKDIR /app

# 1) Installer deps
COPY package*.json ./
RUN npm ci

# 2) Copier le code pour le live‑reload
COPY . .

# 3) Exposer et lancer le dev server sur 0.0.0.0
EXPOSE 4200
CMD ["npm","run","start","--","--host","0.0.0.0"]
