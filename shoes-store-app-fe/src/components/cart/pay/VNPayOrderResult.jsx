import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import { setOrder } from '../../../hooks/order/orderSlice';
import UseFetchOrder from '../../../hooks/order/useFetchOrder';
import { formatISODate, formatVNPayDate } from '../../../utils/dateFormat';
import { formatCurrency } from '../../../utils/formatPrice';
import OrderDetailHorizaltal from '../../product/OrderDetailHorizal';

function VNPayOrderResult() {
	const navigatetion = useNavigate();

	const urlParams = new URLSearchParams(window.location.search);

	const vnpAmount = urlParams.get('vnp_Amount');
	const vnpBankCode = urlParams.get('vnp_BankCode');
	const vnpBankTranNo = urlParams.get('vnp_BankTranNo');
	const vnpCardType = urlParams.get('vnp_CardType');
	const vnpOrderInfo = decodeURIComponent(urlParams.get('vnp_OrderInfo'));
	const vnpPayDate = urlParams.get('vnp_PayDate');
	const vnpResponseCode = urlParams.get('vnp_ResponseCode');
	const vnpTmnCode = urlParams.get('vnp_TmnCode');
	const vnpTransactionNo = urlParams.get('vnp_TransactionNo');
	const vnpTransactionStatus = urlParams.get('vnp_TransactionStatus');
	const vnpTxnRef = urlParams.get('vnp_TxnRef');

	const dispatch = useDispatch();

	const { order } = UseFetchOrder(vnpTxnRef);
	useEffect(() => {
		if (order) {
			dispatch(setOrder(order));
		}
	}, [dispatch, order]);

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
						<span>
							{vnpAmount ? `${formatCurrency(vnpAmount / 100)}` : 'Không có thông tin'}
						</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Mã ngân hàng:</span>
						<span>{vnpBankCode || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Mã giao dịch ngân hàng:</span>
						<span>{vnpBankTranNo || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Loại thẻ:</span>
						<span>{vnpCardType || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Ngày thanh toán:</span>
						<span>{formatVNPayDate(vnpPayDate) || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Mã phản hồi:</span>
						<span>{vnpResponseCode || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Thông tin đơn hàng:</span>
						<span>{vnpOrderInfo || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Mã máy chủ:</span>
						<span>{vnpTmnCode || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Mã giao dịch VNPay:</span>
						<span>{vnpTransactionNo || 'Không có thông tin'}</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Trạng thái giao dịch:</span>
						<span
							className={
								vnpTransactionStatus === '00' ? 'text-green-600' : 'text-red-600'
							}
						>
							{vnpTransactionStatus === '00' ? 'Thành công' : 'Thất bại'}
						</span>
					</div>
					<div className='flex justify-between'>
						<span className='font-semibold'>Mã tham chiếu giao dịch:</span>
						<span>{vnpTxnRef || 'Không có thông tin'}</span>
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
					onClick={() => navigatetion('/')}
					className='px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700'
				>
					Quay lại trang chủ
				</button>
			</div>
		</div>
	);
}

export default VNPayOrderResult;
