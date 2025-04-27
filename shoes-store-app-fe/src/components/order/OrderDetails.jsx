import { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import orderDetailApi from '../../api/orderDetailApi';
import { Box, Paper } from '@mui/material';
import { formatCurrency } from '../../utils/formatPrice';

function OrderDetails() {
	const [orderDetails, setOrderDetails] = useState([]);

	const { orderId } = useParams();

	useEffect(() => {
		const fetchOrderDetailsByOrderId = async () => {
			try {
				const response = await orderDetailApi.getOrderDetail(orderId);
				if (response.status !== 200) {
					console.error('Failed to fetch order details');
				}

				setOrderDetails(response?.data?.data);
			} catch (error) {
				console.error('Failed to fetch order details:', error);
			}
		};

		fetchOrderDetailsByOrderId();
	}, [orderId]);

	return (
		<Box className='w-full flex flex-col items-center bg-slate-200'>
			{orderDetails?.map((item) => (
				<Paper elevation={3} className='p-4 mt-4 mb-4 w-2/3 flex' key={item?.id}>
					<img
						className='h-60 w-60 rounded-lg mr-2'
						src={item?.productItem?.listDetailImages[0]}
						alt={item?.productItem?.product?.name}
					/>
					<div className='flex flex-col flex-1 p-6'>
						<div className='flex items-center justify-around'>
							<p className='text-lg font-semibold'>Tên sản phẩm: </p>
							<p className='font-calibri font-semibold text-lg'>
								{item?.productItem?.product?.name}
							</p>
						</div>
						<div className='flex items-center justify-around'>
							<p className='text-lg font-semibold'>Kích cỡ: </p>
							<p className='font-calibri font-semibold text-lg'>
								{item?.productItem?.size}
							</p>
						</div>
						<div className='flex items-center justify-around'>
							<p className='text-lg font-semibold'>Màu sắc: </p>
							<p className='font-calibri font-semibold text-lg'>
								{item?.productItem?.color}
							</p>
						</div>
						<div className='flex items-center justify-around'>
							<p className='text-lg font-semibold'>Giá: </p>
							<p className='font-calibri font-semibold text-lg'>
								{formatCurrency(item?.pricePerItem)}
							</p>
						</div>
						<div className='flex items-center justify-around'>
							<p className='text-lg font-semibold'>Số lượng: </p>
							<input
								className='h-10 rounded-md shadow-md border text-center'
								type='number'
								value={item?.quantity}
							/>
							<button className='w-40 h-10 bg-green-400 rounded-lg shadow-md'>
								Cập nhật số lượng
							</button>
						</div>
					</div>
				</Paper>
			))}
		</Box>
	);
}

export default OrderDetails;
