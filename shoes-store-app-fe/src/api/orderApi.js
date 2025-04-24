import AdminAxiosClient from './axiosClient';

const orderApi = {
	createOrder: (orderRequest) => {
		const url = '/orders';
		return AdminAxiosClient.post(url, orderRequest, {
			withCredentials: true,
		});
	},
	getOrdersByUserId: (userId) => {
		const url = `/orders/user/${userId}`;
		return AdminAxiosClient.get(url);
	},
	getAll: () => {
		const url = '/orders';
		return AdminAxiosClient.get(url);
	},
	getOrderById: (orderId) => {
		const url = `/orders/${orderId}`;
		return AdminAxiosClient.get(url);
	},
	search: (keyword) => {
		const url = `/orders/search`;
		return AdminAxiosClient.get(url, {
			params: { keyword },
		});
	},
};

export default orderApi;
