import AdminAxiosClient from './axiosClient';

const AuthAPI = {
	login: ({ email, password }) => {
		const url = '/identity/auth/token';
		const body = { email, password };
		return AdminAxiosClient.post(url, body, { withCredentials: true });
	},

	register: ({ firstName, lastName, email, password, dob, phone, gender }) => {
		const url = '/identity/accounts/registration';
		const body = { firstName, lastName, email, password, dob, phone, gender };
		return AdminAxiosClient.post(url, body, { withCredentials: true });
	},

	refreshToken: (refreshToken) => {
		const url = '/identity/auth/refresh';
		return AdminAxiosClient.post(
			url,
			{ refreshToken },
			{ withCredentials: true },
		);
	},

	logout: (accessToken, refreshToken) => {
		const url = '/identity/auth/logout';
		return AdminAxiosClient.post(
			url,
			{ token: accessToken, refreshToken },
			{ withCredentials: true },
		);
	},

	getUser: () => {
		const url = '/auth/test';
		return AdminAxiosClient.get(url);
	},
};

export default AuthAPI;
