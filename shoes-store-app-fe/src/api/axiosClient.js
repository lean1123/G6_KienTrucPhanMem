import axios from 'axios';
import AuthAPI from './authApi';
import { localStorageService } from '../service/localStorageService';

const AdminAxiosClient = axios.create({
	// baseURL: 'deployed API url',
	// baseURL: 'https://lean1123.online/api',
	baseURL: 'http://localhost:8888/api/v1',
	headers: {
		'Content-Type': 'application/json',
	},
});

AdminAxiosClient.interceptors.request.use(
	async (config) => {
		const publicEndpoints = [
			{ urlPattern: /\/identity\/auth\/token/, methods: ['POST'] },
			{ urlPattern: /\/identity\/accounts\/registration/, methods: ['POST'] },
			{ urlPattern: /\/identity\/auth\/refresh/, methods: ['POST'] },
			{ urlPattern: /\/identity\/auth\/logout/, methods: ['POST'] },
			{ urlPattern: /^\/product(\/.*)?$/, methods: ['GET'] },
			{ urlPattern: /^\/cart(\/.*)?$/, methods: ['GET', 'POST', 'PUT'] },
			{ urlPattern: /^\/chat(\/.*)?$/, methods: ['POST'] },
		];

		const isPublicEndpoint = publicEndpoints.some(
			(endpoint) =>
				endpoint.urlPattern.test(config.url) &&
				endpoint.methods.includes(config.method.toUpperCase()),
		);

		if (isPublicEndpoint) {
			if (config.headers.Authorization) {
				delete config.headers.Authorization;
			}
			return config;
		}

		const token = localStorageService.getToken();

		config.headers.Authorization = `Bearer ${token}`;

		return config;
	},
	(error) => {
		return Promise.reject(error);
	},
);

AdminAxiosClient.interceptors.response.use(
	(response) => {
		return response;
	},
	async (error) => {
		const originalRequest = error.config;

		if (originalRequest.url.includes('/auth/refresh')) {
			window.location.href = '/login';
			return Promise.reject(error);
		}

		if (error.response.status === 401 && !originalRequest._retry) {
			originalRequest._retry = true;

			let token = localStorageService.getToken();
			let refreshToken = localStorageService.getRefreshToken();

			if (token && refreshToken) {
				try {
					const refreshedToken = await AuthAPI.refreshToken(refreshToken);

					if (refreshedToken.status !== 200) {
						return Promise.reject(error);
					}
					token = await refreshedToken.data.result.token;
					localStorageService.setToken(token);
					originalRequest.headers.Authorization = `Bearer ${token}`;

					return AdminAxiosClient(originalRequest);
				} catch (error) {
					console.error('Error during token refresh request', error);

					return Promise.reject(error);
				}
			}

			return Promise.reject(error);
		}

		if (error.response) {
			console.error('Response error', error.response);
		} else if (error.request) {
			console.error('No response received', error.request);
		} else {
			console.error('Request setup error', error.message);
		}

		return Promise.reject(error);
	},
);

export default AdminAxiosClient;
