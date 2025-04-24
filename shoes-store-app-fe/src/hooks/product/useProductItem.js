import { useEffect, useState } from 'react';
import productItemApi from '../../api/productItemApi';

export default function useProductItem(productItemId) {
	const [isLoading, setIsLoading] = useState(false);
	const [productItem, setProductItem] = useState(null);

	useEffect(() => {
		const fetchProductItem = async () => {
			setIsLoading(true);
			try {
				const response = await productItemApi.getProductItemById(productItemId);
				if (!response.status === 200) {
					throw new Error('Error fetching product item');
				}
				console.log('response of product item', response);

				setProductItem(response.data?.data);
			} catch (error) {
				console.error('Error fetching product item: ', error);
			} finally {
				setIsLoading(false);
			}
		};

		fetchProductItem();
	}, [productItemId]);

	return { isLoading, productItem };
}
