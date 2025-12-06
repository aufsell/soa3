// import { fileURLToPath, URL } from 'node:url'

// import { defineConfig } from 'vite'
// import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
// import fs from 'fs'
// import path from 'path'

// // https://vite.dev/config/
// export default defineConfig({
//   plugins: [vue(), vueDevTools()],
//   server: {
//     https: {
//       key: fs.readFileSync(path.resolve(__dirname, '../TLS/frontend/frontend.key')),
//       cert: fs.readFileSync(path.resolve(__dirname, '../TLS/frontend/frontend-fullchain.crt')),
//     },
//     host: '0.0.0.0',
//     port: 5173,
//     strictPort: true,
//   },
//   resolve: {
//     alias: {
//       '@': fileURLToPath(new URL('./src', import.meta.url)),
//     },
//   },
// })
// --------------

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import fs from 'fs'
import path from 'path'
import { fileURLToPath } from 'url'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

export default defineConfig({
  plugins: [vue()],
  server: {
    https: {
      key: fs.readFileSync(path.resolve(__dirname, '../TLS/frontend/localhost.key')),
      cert: fs.readFileSync(path.resolve(__dirname, '../TLS/frontend/localhost.crt')),
    },
    port: 5173, // или твой порт
    proxy: {
      '/api': {
        target: 'https://localhost:8088',
        changeOrigin: true,
        secure: false, // <<< разрешить self-signed сертификат гейтвея
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
