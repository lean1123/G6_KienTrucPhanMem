import { useEffect, useState } from 'react';
import productItemApi from '../../api/productItemApi';

export default function useProductItem(productItemId) {
	const [isLoading, setIsLoading] = useState(false);
	const [productItem, setProductItem] = useState(null);
	const [error, setError] = useState(null);

	useEffect(() => {
		const fetchProductItem = async () => {
			setIsLoading(true);
			try {
				const response = await productItemApi.getProductItemById(productItemId);
				if (!response.status === 200) {
					throw new Error('Error fetching product item');
				}

				console.log('Product item response:', response);

				setProductItem(response.data?.result);
			} catch (error) {
				console.error('Error fetching product item: ', error);
				setError('Error fetching product item');
			} finally {
				setIsLoading(false);
			}
		};

		fetchProductItem();
	}, [productItemId]);

	return { isLoading, productItem, error };
}
