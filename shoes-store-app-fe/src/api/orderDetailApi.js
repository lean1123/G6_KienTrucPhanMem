import AdminAxiosClient from './axiosClient';

const orderDetailApi = {
	getOrderDetail: (orderId) => {
		const url = `/order-details/order/${orderId}`;
		return AdminAxiosClient.get(url);
	},
};

export default orderDetailApi;
