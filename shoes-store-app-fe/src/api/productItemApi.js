import AdminAxiosClient from './axiosClient';

const productItemApi = {
	getAllProductItems: (productId) => {
		return AdminAxiosClient.get(`/product/item/product/${productId}`);
	},

	getProductItemById: (id) => {
		return AdminAxiosClient.get(`/product/item/${id}`, {
			withCredentials: true,
		});
	},

	addNewProductItem: (productItemData) => {
		return AdminAxiosClient.post('/product-items', productItemData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	},

	updateProductItem: (id, productItemData) => {
		return AdminAxiosClient.put(`/product-items/${id}`, productItemData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		});
	},

	deleteProductItem: (id) => {
		return AdminAxiosClient.delete(`/product-items/${id}`);
	},

	getRecentProducts: () => {
		return AdminAxiosClient.get('/product-items/recent', {
			withCredentials: true,
		});
	},

	getTopSaleProductItems: (page = 1, size = 8) => {
		return AdminAxiosClient.get('/product/item/top-sale', {
			params: { page, size },
		});
	},

	getNewProductItems: (page = 1, size = 8) => {
		return AdminAxiosClient.get('/product/item/newest', {
			params: { page, size },
		});
	},

	getProductItemByColorAndSize: (productId, color, size) => {
		return AdminAxiosClient.get(`/product-items/get-by-color-and-size`, {
			params: {
				productId,
				color,
				size,
			},
		});
	},

	search: ({ color, size, minPrice, maxPrice, productName, page, pageSize }) => {
		return AdminAxiosClient.get('/product/item/search', {
			params: { color, size, minPrice, maxPrice, productName, page, pageSize },
		});
	},
};

export default productItemApi;
