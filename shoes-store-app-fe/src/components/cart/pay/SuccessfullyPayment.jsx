import { Paper } from '@mui/material';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import { formatCurrency } from '../../../utils/formatPrice';
import OrderDetailHorizaltal from '../../product/OrderDetailHorizal';

function SuccessfullyPayment() {
	const navigation = useNavigate();

	const { current } = useSelector((state) => state.order);

	return (
		<div className='w-full px-2 py-10'>
			<Paper
				elevation={3}
				className='w-full flex flex-col items-center mb-4 p-4'
				sx={{
					backgroundColor: 'rgba(255, 255, 255, 0.5)',
					borderRadius: 3,
					padding: 4,
					boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.37)',
					backdropFilter: 'blur(2px)',
					WebkitBackdropFilter: 'blur(2px)',
					border: '1px solid rgba(255, 255, 255, 0.18)',
				}}
			>
				{/* <HorizontalLinearAlternativeLabelStepper steps={progressSteps} /> */}

				<h2 className='text-3xl font-bold font-calibri text-green-600 mb-2 text-center'>
					Đơn Đặt Hàng Của Bạn Đã Thành Công!
				</h2>
				<p className='text-lg font-calibri font-semibold text-gray-800 mb-4'>
					Cảm ơn bạn! Đơn đặt hàng của bạn đang được chúng tôi xử lý!
				</p>

				<div className='space-y-2 mb-6'>
					<div className='flex items-center'>
						<p className='font-calibri font-semibold mr-2 text-gray-700'>
							Mã đơn đặt hàng:
						</p>
						<p className='font-calibri text-gray-600'>{current.id}</p>
					</div>
					<div className='flex items-center'>
						<p className='font-calibri font-semibold mr-2 text-gray-700'>
							Tổng số tiền cần thanh toán:
						</p>
						<p className='text-green-600 font-semibold'>
							{formatCurrency(current.total)}
						</p>
					</div>
				</div>

				<p className='font-semibold text-lg mb-2 text-gray-800'>
					Chi tiết sản phẩm:
				</p>
				<div className='space-y-4'>
					{current.orderDetails.map((item) => (
						<OrderDetailHorizaltal item={item} key={item.productItemId + item.size} />
					))}
				</div>

				<div className='flex justify-center mt-6'>
					<button
						className='w-60 h-12 bg-sky-500 hover:bg-sky-600 text-white font-semibold rounded-lg shadow-md transition'
						onClick={() => navigation('/')}
					>
						Trở về trang chủ
					</button>
				</div>
			</Paper>
		</div>
	);
}

export default SuccessfullyPayment;
