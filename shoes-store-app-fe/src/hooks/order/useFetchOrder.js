import { useEffect, useState } from 'react';
import orderApi from '../../api/orderApi';

const UseFetchOrder = (orderId) => {
	const [order, setOrder] = useState(null);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);

	useEffect(() => {
		if (!orderId) return;
		const fetchOrder = async () => {
			try {
				const response = await orderApi.getOrderById(orderId);
				if (!response.status === 200) {
					throw new Error('Failed to fetch order data in useFetchOrder hook');
				}

				const data = response.data;
				setOrder(data);
			} catch (error) {
				console.error('Error fetching order:', error);
				setError(error);
			} finally {
				setLoading(false);
			}
		};
		fetchOrder();
	}, [orderId]);

	return { order, loading, error };
};

export default UseFetchOrder;
