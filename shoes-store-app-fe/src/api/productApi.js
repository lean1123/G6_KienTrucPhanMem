import AdminAxiosClient from './axiosClient';

const productApi = {
	getAll: () => {
		const url = '/products';
		return AdminAxiosClient.get(url);
	},
	getById: (id) => {
		const url = `/products/${id}`;
		return AdminAxiosClient.get(url);
	},
	addNew: (productData) => {
		const url = '/products';
		return AdminAxiosClient.post(url, productData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	},
	update: (id, productData) => {
		const url = `/products/${id}`;
		return AdminAxiosClient.put(url, productData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	},
	searchByKeyword: (keyword) => {
		const url = `/products/searchByKeyword`;
		return AdminAxiosClient.get(url, {
			params: { keyword },
		});
	},
};

export default productApi;
