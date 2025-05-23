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

	search: ({
		color,
		size,
		minPrice,
		maxPrice,
		productName,
		page,
		pageSize,
		categoryName,
	}) => {
		return AdminAxiosClient.get('/product/item/search', {
			params: {
				colorName: color,
				size,
				minPrice,
				maxPrice,
				productName,
				categoryName,
				page,
				pageSize,
			},
		});
	},

	likeProductItem: (productItemId) => {
		const url = `/product/item/${productItemId}/like`;

		return AdminAxiosClient.post(url, null, {
			withCredentials: true,
		});
	},

	unlikeProductItem: (productItemId) => {
		const url = `/product/item/${productItemId}/unlike`;

		return AdminAxiosClient.post(url, null, {
			withCredentials: true,
		});
	},
	getAllColors: () => {
		return AdminAxiosClient.get('/product/external/colors');
	},

	getAllCategories: () => {
		return AdminAxiosClient.get('/product/external/categories');
	},
};

export default productItemApi;
