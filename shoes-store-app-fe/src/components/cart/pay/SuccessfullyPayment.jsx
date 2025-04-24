import { Paper } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import orderDetailApi from '../../../api/orderDetailApi';
import { formatCurrency } from '../../../utils/formatPrice';

function SuccessfullyPayment() {
	const navigation = useNavigate();
	const [orderDetail, setOrderDetail] = useState([]);
	const totalPrice = useMemo(() => {
		return orderDetail.reduce((total, item) => {
			return total + item.pricePerItem * item.quantity;
		}, 0);
	}, [orderDetail]);

	const { current } = useSelector((state) => state.persistedReducer.order);

	useEffect(() => {
		const fetchOrderDetailsByOrderId = async () => {
			try {
				const response = await orderDetailApi.getOrderDetail(current.id);
				if (response.status !== 200) {
					console.error('Failed to fetch order details');
				}

				setOrderDetail(response?.data?.data);
			} catch (error) {
				console.error('Failed to fetch order details:', error);
			}
		};

		fetchOrderDetailsByOrderId();
	}, [current.id]);

	return (
		<div className='flex flex-col items-center w-full bg-slate-50 mb-4'>
			<h2 className='text-3xl font-semibold font-calibri text-green-600'>
				Đơn Đặt Hàng Của Bạn Đã Thành Công!
			</h2>
			<p className='text-base font-calibri font-semibold text-black'>
				Cảm ơn bạn! Đơn đặt hàng của bạn đã được chấp nhận!
			</p>
			<Paper elevation={3} className='p-4 mt-4'>
				<div className='flex'>
					<p className='font-calibri font-semibold mr-2'>Mã đơn đặt hàng: </p>
					<p className='font-calibri text-gray-600'>123456</p>
				</div>
				<div className='flex'>
					<p className='font-calibri font-semibold mr-2'>
						Tổng số tiền cần thanh toán:{' '}
					</p>
					<p>{formatCurrency(totalPrice)}</p>
				</div>
				<p className='font-semibold'>Chi tiết sản phẩm:</p>
				<div className='mt-2'>
					{orderDetail.map((item) => (
						<div className='flex justify-between border-b' key={item?.id}>
							<p className='font-calibri'>{item?.productItem?.product?.name}</p>
							<p className='font-calibri'>X{item?.quantity}</p>
							<p className='font-calibri'>{formatCurrency(item?.pricePerItem)}</p>
						</div>
					))}
				</div>
			</Paper>
			<div className='flex mt-4'>
				<button
					className='w-60 h-10 bg-sky-400 rounded-lg mr-2 shadow-md'
					onClick={() => navigation('/')}
				>
					Trở về trang chủ
				</button>
			</div>
		</div>
	);
}

export default SuccessfullyPayment;
