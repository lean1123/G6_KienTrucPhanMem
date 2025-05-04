import { Badge } from '@mui/material';
import { useMemo } from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import { formatCurrency } from '../../utils/formatPrice';

function CartButton() {
	const navigate = useNavigate();
	const { cartItems } = useSelector((state) => state.cart);
	const totalPrice = useMemo(() => {
		return cartItems?.reduce(
			(acc, item) => acc + item?.productItem?.price * item?.quantity,
			0,
		);
	}, [cartItems]);
	return (
		<div className='group relative'>
			<button
				className='px-3 py-2 rounded-md text-sm font-medium relative inline-block'
				onClick={() => {
					navigate('/cart');
				}}
			>
				<Badge badgeContent={cartItems?.length} color='warning'>
					<img
						className='block h-5 w-auto'
						src='/market.png'
						width={17}
						height={17}
					/>
				</Badge>
			</button>
			<div
				className='absolute -right-56 top-10 transform -translate-x-1/2 mt-2 p-2 bg-white
          text-black text-sm rounded hidden group-hover:block transition-opacity duration-600 z-50
              w-96 min-h-60 shadow-lg'
			>
				<div className='w-full flex flex-col items-center'>
					<span className='w-full text-base font-semibold font-calibri border-b py-2'>
						GIỎ HÀNG
					</span>
					<div className='w-full h-52 flex flex-col overflow-y-auto scroll-smooth scrollbar-thin scrollbar-webkit mb-1'>
						{cartItems?.map((item, index) => (
							<div
								className='w-full flex items-center justify-around border-b p-1'
								key={index}
							>
								<img
									src={item?.productItem?.images[0]}
									alt='san pham'
									className='w-20 h-20'
								/>

								<div className='flex flex-col font-calibri items-start'>
									<span className='text-base font-semibold'>{item?.product?.name}</span>
									<span className='text-base'>
										{item?.productItem?.color?.name}/{item?.cartDetailPK?.size}
									</span>
									<span className='text-sm text-gray-400 font-semibold'>
										{item?.quantity}
									</span>
								</div>
								<span className='text-sm font-semibold font-calibri'>
									{formatCurrency(item?.productItem?.price * item?.quantity)}
								</span>
							</div>
						))}
					</div>
					<div className='w-full flex justify-between items-center p-2 shadow-lg'>
						<span className='text-base font-calibri'>TỔNG TIỀN:</span>
						<span className='text-lg font-calibri'>{formatCurrency(totalPrice)}</span>
					</div>
					<button
						className='w-full bg-black text-white p-2'
						onClick={() => navigate('/cart')}
					>
						ĐI TỚI GIỎ HÀNG
					</button>
				</div>
			</div>
			<div className='h-4 absolute left-0 right-0 p-2 z-10 hidden group-hover:block'></div>
		</div>
	);
}

export default CartButton;
