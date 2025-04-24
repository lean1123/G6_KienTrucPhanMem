import AdminAxiosClient from './axiosClient';

const userApi = {
	getAll: async () => {
		return AdminAxiosClient.get('/users');
	},
	getAccount: async (id) => {
		return AdminAxiosClient.get(`/accounts/${id}`);
	},
	getById: async (id) => {
		return AdminAxiosClient.get(`/users/${id}`);
	},
	getUserInfo: async (userId) => {
		const url = `/users/${userId}`;
		console.log('Calling getUserInfo with URL:', url);
		console.log('UserId received:', userId);

		try {
			const response = await AdminAxiosClient.get(url);
			console.log('getUserInfo success response:', response);
			return response;
		} catch (error) {
			console.log('getUserInfo error details:', {
				status: error.response?.status,
				data: error.response?.data,
				message: error.message,
			});
			throw error;
		}
	},
	update: async (id, userData) => {
		return AdminAxiosClient.put(`/users/${id}`, userData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	},

	search: async (keyword) => {
		return AdminAxiosClient.get(`/users/search`, {
			params: { keyword },
		});
	},
};
export default userApi;
