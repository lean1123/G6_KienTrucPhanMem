import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [react()],
	resolve: {
		alias: {
			'@components': path.resolve(__dirname, 'src/components'),
		},
	},
	envPrefix: 'VITE_',
	server: {
		port: 5173,
		proxy: {
			// Chỉ hoạt động khi chạy `npm run dev`, không ảnh hưởng bản build
			'/api': {
				target: 'http://localhost:8888',
				changeOrigin: true,
				rewrite: (path) => path.replace(/^\/api/, '/api/v1'),
			},
		},
	},
});
