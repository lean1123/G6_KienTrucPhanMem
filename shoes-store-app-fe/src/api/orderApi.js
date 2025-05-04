import AdminAxiosClient from './axiosClient';

const orderApi = {
	createOrder: async (orderRequest) => {
		const url = '/orders/add-new-order';
		return await AdminAxiosClient.post(url, orderRequest, {
			withCredentials: true,
			timeout: 120000,
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
		const url = `/orders/get-order-by-id/${orderId}`;
		return AdminAxiosClient.get(url);
	},
	search: (keyword) => {
		const url = `/orders/search`;
		return AdminAxiosClient.get(url, {
			params: { keyword },
		});
	},

	getMyOrders: async () => {
		const url = '/orders/get-my-orders';
		return await AdminAxiosClient.get(url, {
			withCredentials: true,
		});
	},

	cancleOrder: async (orderId) => {
		const url = `orders/canceling-order/${orderId}`;
		return await AdminAxiosClient.post(
			url,
			{},
			{
				withCredentials: true,
				timeout: 120000,
			},
		);
	},
};

export default orderApi;
