import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import { setOrder } from '../../../hooks/order/orderSlice';
import UseFetchOrder from '../../../hooks/order/useFetchOrder';
import { formatISODate, formatVNPayDate } from '../../../utils/dateFormat';
import { formatCurrency } from '../../../utils/formatPrice';
import OrderDetailHorizaltal from '../../product/OrderDetailHorizal';
import paymentApi from '../../../api/paymentApi';

function VNPayOrderResult() {
	const navigation = useNavigate();
	const dispatch = useDispatch();
	const [paymentInfo, setPaymentInfo] = useState(null);

	const { current } = useSelector((state) => state.order);
	const { order, loading } = UseFetchOrder(current?.id); // giả sử UseFetchOrder trả ra cả isLoading

	useEffect(() => {
		if (!current.id || loading) return; // thêm điều kiện: Đừng fetch sớm khi order còn loading

		dispatch(setOrder(order));

		const fetchPaymentInfo = async () => {
			try {
				const response = await paymentApi.getPaymentInfoByOrderId(current.id);
				if (response.status !== 200) {
					console.error('Failed to fetch payment info');
					return;
				}
				console.log('Payment info fetched successfully:', response);
				setPaymentInfo(response.data);
			} catch (error) {
				console.error('Failed to fetch payment info:', error);
				return;
			}
		};

		fetchPaymentInfo();
	}, [current.id, order, loading, dispatch]);
	return (
		<div className='max-w-6xl mx-auto p-6 grid grid-cols-1 md:grid-cols-2 gap-6'>
			{/* Cột thông tin thanh toán */}
			<div className='bg-white bg-opacity-50 shadow-lg p-6 rounded-lg space-y-4'>
				<h2 className='text-2xl font-semibold text-center mb-4'>
					Kết Quả Thanh Toán VNPay
				</h2>
				<div className='space-y-2'>
					<div className='flex justify-between'>
						<span className='font-semibold'>Số tiền thanh toán:</span>
						<span>{formatCurrency(paymentInfo?.amount || 0)}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Ngày thanh toán:</span>
						<span>
							{formatVNPayDate(paymentInfo?.transactionDate) || 'Không có thông tin'}
						</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Thông tin đơn hàng:</span>
						<span>{paymentInfo?.orderId || 'Không có thông tin'}</span>
					</div>

					<div className='flex justify-between'>
						<span className='font-semibold'>Mã giao dịch VNPay:</span>
						<span>{paymentInfo?.transactionId || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Trạng thái giao dịch:</span>
						<span
							className={
								paymentInfo?.status === 'SUCCESS' ? 'text-green-600' : 'text-red-600'
							}
						>
							{paymentInfo?.status === 'SUCCESS' ? 'Thành công' : 'Thất bại'}
						</span>
					</div>
				</div>
			</div>
			{/* Cột thông tin đơn hàng */}
			<div className='bg-white bg-opacity-50 shadow-lg p-6 rounded-lg space-y-4'>
				<h2 className='text-2xl font-semibold text-center mb-4'>
					Thông Tin Đơn Hàng
				</h2>
				<div className='space-y-2'>
					<div className='flex justify-between'>
						<span className='font-semibold'>Mã đơn hàng:</span>
						<span>{order?.id}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Địa chỉ giao hàng:</span>
						<span>{`${order?.address?.homeNumber}/${order?.address?.ward}/
						${order?.address?.district}/${order?.address?.city}`}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Ngày đặt:</span>
						<span>{formatISODate(order?.createdDate)}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Trạng thái:</span>
						<span className='text-green-600'>
							{order?.status === 'PENDING' ? 'Đang chờ xác nhận' : 'Thất bại'}
						</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Tổng tiền:</span>
						<span>{formatCurrency(order?.total)}</span>
					</div>

					<div className='flex justify-between'>
						<span className='font-semibold'>Chi tiết đơn hàng:</span>
					</div>
					<div className='flex flex-col items-center justify-between'>
						{order?.orderDetails?.map((item) => (
							<div key={item.productItemId + item.size} className='mb-1'>
								<OrderDetailHorizaltal item={item} />
							</div>
						))}
					</div>
				</div>
			</div>
			{/* Nút quay lại trang chủ */}
			<div className='col-span-2 text-center mt-6'>
				<button
					onClick={() => navigation('/')}
					className='px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700'
				>
					Quay lại trang chủ
				</button>
			</div>
		</div>
	);
}

export default VNPayOrderResult;
