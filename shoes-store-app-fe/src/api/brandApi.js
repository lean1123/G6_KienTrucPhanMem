import AdminAxiosClient from './axiosClient';

const brandApi = {
	getAllBrands: () => {
		return AdminAxiosClient.get('/brands');
	},
	getBrandById: (id) => {
		return AdminAxiosClient.get(`/brands/${id}`);
	},
	searchBrands: (keyword) => {
		return AdminAxiosClient.get(`/brands/search`, {
			params: { keyword },
		});
	},
	addNewBrand: (brandData) => {
		return AdminAxiosClient.post('/brands', brandData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	},
	updateBrand: (id, brandData) => {
		return AdminAxiosClient.put(`/brands/${id}`, brandData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	},
};

export default brandApi;
