import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
export default defineConfig({
  plugins: [react()],
  server: {
    port: 20105,
    host: "0.0.0.0",
    // 本地开发时代理到后端（默认 BACKEND_PORT=21105）；生产环境由 nginx 反代 /api/。
    proxy: {
      "/api": {
        target: "http://localhost:21105",
        changeOrigin: true
      }
    }
  }
});
