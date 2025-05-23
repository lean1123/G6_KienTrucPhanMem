export const localStorageService = {
	getToken: () => {
		const token = localStorage.getItem('accessToken');
		if (!token) {
			console.error('Access token not found in localStorage');
			return null;
		}
		if (token === null || token === undefined) {
			console.error('Access token is undefined in localStorage');
			return null;
		}
		return token;
	},

	getRefreshToken: () => {
		const refreshToken = localStorage.getItem('refreshToken');
		if (!refreshToken) {
			console.error('Refresh token not found in localStorage');
			return null;
		}
		if (refreshToken === null || refreshToken === undefined) {
			console.error('Refresh token is undefined in localStorage');
			return null;
		}
		return refreshToken;
	},

	setToken: (token) => {
		if (!token) {
			console.error('Attempted to set an undefined token');
			return;
		}
		if (token === null || token === undefined) {
			console.error('Attempted to set a null token');
			return;
		}
		// Check if the token is a valid string (you can add more validation if needed)
		if (typeof token !== 'string' || token.trim() === '') {
			console.error('Attempted to set an invalid token');
			return;
		}
		// If the token is valid, set it in localStorage
		localStorage.setItem('accessToken', token);
	},

	setRefreshToken: (refreshToken) => {
		if (!refreshToken) {
			console.error('Attempted to set an undefined refresh token');
			return;
		}
		if (refreshToken === null || refreshToken === undefined) {
			console.error('Attempted to set a null refresh token');
			return;
		}
		// Check if the refresh token is a valid string (you can add more validation if needed)
		if (typeof refreshToken !== 'string' || refreshToken.trim() === '') {
			console.error('Attempted to set an invalid refresh token');
			return;
		}
		// If the refresh token is valid, set it in localStorage
		localStorage.setItem('refreshToken', refreshToken);
	},
};
