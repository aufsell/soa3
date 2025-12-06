# 1️⃣ Этап сборки фронтенда
FROM node:20 AS build-stage

WORKDIR /app

# Скопируем package.json и package-lock.json из папки frontend
COPY frontend/package*.json ./
RUN npm install

# Скопируем весь фронт и соберем его
COPY frontend/ ./
# В build-stage
COPY tls/frontend/frontend.key /tls/frontend/frontend.key
COPY tls/frontend/frontend-fullchain.crt /tls/frontend/frontend-fullchain.crt

RUN npm run build

# 2️⃣ Этап деплоя (Nginx)
FROM nginx:1.25-alpine AS production-stage

# Копируем собранный проект из build-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Копируем сертификаты
COPY tls/frontend/frontend.key /etc/nginx/certs/frontend.key
COPY tls/frontend/frontend-fullchain.crt /etc/nginx/certs/frontend.crt

# Копируем конфиг nginx
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]
