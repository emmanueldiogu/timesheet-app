# -----------------------------
# Stage 1: Install dependencies
# -----------------------------
FROM node:24.13.1-slim AS dependencies

WORKDIR /app

# Copy only dependency manifests (better caching)
COPY package.json package-lock.json ./

# Deterministic install
RUN npm ci

# -----------------------------
# Stage 2: Development runtime
# -----------------------------
FROM node:24.13.1-slim AS development

WORKDIR /app

# Reuse installed dependencies
COPY --from=dependencies /app/node_modules ./node_modules

# Copy application source
COPY . .

EXPOSE 5173

# Start Vite dev server
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "5173"]
