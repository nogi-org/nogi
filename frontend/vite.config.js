import { fileURLToPath, URL } from 'node:url';
import { defineConfig } from 'vite';
import fs from 'fs';
import vue from '@vitejs/plugin-vue';

// 현재 실행 모드 확인 (default: "development")
const isLocal = process.env.npm_lifecycle_event === "local";

export default defineConfig(({ mode }) => ({
  server: {
    port: 5173,
    https: isLocal
      ? {
        key: fs.readFileSync('localhost-key.pem'),
        cert: fs.readFileSync('localhost.pem')
      }
      : false // local이 아닐 때는 HTTPS 비활성화
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
}));
