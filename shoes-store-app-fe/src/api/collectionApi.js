import AdminAxiosClient from './axiosClient';

const collectionApi = {
	getCollectionByBrand: (brand) => {
		return AdminAxiosClient.get(`/collections/brand/${brand}`);
	},
	getCollectionById: (id) => {
		return AdminAxiosClient.get(`/collections/${id}`);
	},
	searchCollections: (brandId, keyword) => {
		return AdminAxiosClient.get(`/collections/${brandId}/search`, {
			params: { keyword },
		});
	},
	addNewCollection: (collectionData) => {
		return AdminAxiosClient.post('/collections', collectionData);
	},
	updateCollection: (id, collectionData) => {
		return AdminAxiosClient.put(`/collections/${id}`, collectionData);
	},
};

export default collectionApi;
