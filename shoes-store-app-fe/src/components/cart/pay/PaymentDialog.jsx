import React from 'react';
import PropTypes from 'prop-types';

function PaymentDialog({ isOpen = false, url = '' }) {
	const [isShow, setIsShow] = React.useState(isOpen);

	const handleCloseModal = () => {
		setIsShow(false);
	};

	const handleRedirect = () => {
		window.location.href = url;
	};

	if (!isShow) return null;
	return (
		<div className='fixed inset-0 flex items-center justify-center bg-gray-700 bg-opacity-70 z-50'>
			<div className='bg-white rounded-xl p-8 w-full max-w-md shadow-lg text-center'>
				<div className='flex items-center justify-center mb-5'>
					<div className='bg-green-100 rounded-full p-4'>
						<svg
							className='w-8 h-8 text-green-500'
							fill='none'
							stroke='currentColor'
							viewBox='0 0 24 24'
							xmlns='http://www.w3.org/2000/svg'
						>
							<path
								strokeLinecap='round'
								strokeLinejoin='round'
								strokeWidth='2'
								d='M5 13l4 4L19 7'
							/>
						</svg>
					</div>
				</div>
				<h2 className='text-xl font-bold mb-3'>Di Chuyển Đến Trang Thanh Toán</h2>
				<p className='text-gray-500 mb-6'>
					Vui lòng chọn đồng ý để thanh toán cho đơn hàng của bạn tại VNPay
				</p>
				<div className='flex justify-center space-x-4'>
					<button
						onClick={handleCloseModal}
						className='px-6 py-2 border border-gray-300 rounded-md text-black hover:bg-gray-100 font-semibold'
					>
						Hủy bỏ
					</button>
					<button
						onClick={handleRedirect}
						className='px-6 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 font-semibold'
					>
						Tôi đồng ý
					</button>
				</div>
			</div>
		</div>
	);
}

export default PaymentDialog;

PaymentDialog.propTypes = {
	isOpen: PropTypes.bool.isRequired,
	url: PropTypes.string.isRequired,
};
