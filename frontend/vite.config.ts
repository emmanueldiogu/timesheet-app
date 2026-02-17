import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5173,
    host: true, // 0.0.0.0 for Docker
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Dev proxy
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      
      }
    }
  }
})
