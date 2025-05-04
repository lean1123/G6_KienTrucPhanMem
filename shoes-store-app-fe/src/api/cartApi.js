import AdminAxiosClient from './axiosClient';

const cartApi = {
	viewCart: () => {
		const url = '/cart';
		return AdminAxiosClient.get(url, { withCredentials: true });
	},
	addToCart: (cartDetailRequest) => {
		const url = '/cart/addToCart';
		return AdminAxiosClient.post(url, cartDetailRequest, {
			withCredentials: true,
		});
	},

	updateQuantity: (cartDetailRequest) => {
		console.log('cartDetailRequest', cartDetailRequest);

		const url = '/cart/updateQuantity';
		return AdminAxiosClient.put(url, cartDetailRequest, {
			withCredentials: true,
		});
	},

	deleteCartDetail: (productItemId, size) => {
		const url = `/cart/delete`;
		return AdminAxiosClient.delete(url, {
			data: { productItemId, size },
			withCredentials: true,
		});
	},

	clearCart: () => {
		const url = '/cart/clear';
		return AdminAxiosClient.delete(url, { withCredentials: true });
	},
};

export default cartApi;
