function FailedPayment() {
	return (
		<div className='flex flex-col items-center w-full bg-slate-50 mb-4'>
			<h2 className='text-3xl font-semibold text-red-600'>Đặt Hàng Thất Bại!</h2>
			<p>Đã có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại sau.</p>
		</div>
	);
}

export default FailedPayment;
