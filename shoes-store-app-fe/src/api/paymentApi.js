import AdminAxiosClient from './axiosClient';

const paymentApi = {
	getPaymentInfoByOrderId: async (orderId) => {
		const url = `/payments/${orderId}`;
		return await AdminAxiosClient.get(url, {
			withCredentials: true,
		});
	},
};

export default paymentApi;
