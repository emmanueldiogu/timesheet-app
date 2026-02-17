# Development Stage (vite dev server)
FROM node:24-slim AS development
WORKDIR /app

# 1. Clean npm install (no cache, no host deps)
COPY package*.json ./
RUN rm -rf node_modules package-lock.json \
    && npm cache clean --force \
    && npm install

# 2. Copy source AFTER deps
COPY . .

# 3. Skip native binaries
ENV ROLLUP_FORCE_NATIVE=0

EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "5173"]

# Production Build Stage
FROM node:24-slim AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production Serve (Nginx)
FROM nginx:alpine AS production
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80

# Multi‑stage build selector
ARG BUILD_ENV=development
COPY --from=development /dev/null /dev/null
