import { useEffect, useMemo } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
	viewCart,
	updateQuantity,
	deleteCartDetail,
} from '../../hooks/cart/cartSlice';
import CartDetailItem from './CartDetailItem';
import { useNavigate } from 'react-router';
import { enqueueSnackbar } from 'notistack';
import { fetchUser } from '../../hooks/user/userSlice';
import { initialOrderStep } from '../../hooks/orderProgressStore';

const Cart = () => {
	const dispatch = useDispatch();
	const navigation = useNavigate();

	const { cartItems } = useSelector((state) => state.cart);
	const { userId, user } = useSelector((state) => state.user);

	const totalPrice = useMemo(() => {
		if (cartItems?.length === 0 || typeof cartItems === 'string') return 0;

		return cartItems?.reduce((total, item) => {
			return (total += item.productItem.price * item.quantity);
		}, 0);
	}, [cartItems]);

	const quantity = useMemo(() => {
		if (cartItems?.length === 0 || typeof cartItems === 'string') return 0;
		return cartItems?.length;
	}, [cartItems]);

	useEffect(() => {
		dispatch(viewCart());
		dispatch(fetchUser(userId));
		dispatch(initialOrderStep());
	}, [dispatch, userId]);

	const handleQuantityChange = (item, newQuantity) => {
		if (newQuantity < 0) {
			return;
		}
		const updatedItem = {
			cartId: item?.cartDetailPK?.cartId || null,
			productItemId: item?.productItem?.id,
			quantity: newQuantity,
			size: item?.cartDetailPK?.size,
		};

		console.log('updatedItem', updatedItem);

		if (updatedItem.quantity === 0) {
			dispatch(deleteCartDetail(updatedItem));
			return;
		}

		dispatch(updateQuantity(updatedItem));
	};

	const handleRemoveProduct = (productItemId, size) => {
		console.log('productId', productItemId);

		dispatch(deleteCartDetail({ productItemId, size }));
	};

	return (
		<div className='flex justify-center p-6 '>
			<div className='w-2/3 bg-white p-6 shadow-md rounded-lg mr-6'>
				<form action='/cart' method='post'>
					<h2 className='text-xl font-semibold mb-4'>GIỎ HÀNG CỦA BẠN</h2>
					<p className='mb-6'>
						Bạn đang có <span className='font-semibold'>{quantity} sản phẩm</span>{' '}
						trong giỏ hàng
					</p>

					{typeof cartItems === 'string' ? (
						<p>Không có sản phẩm nào trong giỏ hàng của bạn!</p>
					) : (
						cartItems?.map((item) => (
							<CartDetailItem
								key={item.cartDetailPK?.cartId + item.cartDetailPK?.productItemId}
								item={item}
								onQuantityChange={handleQuantityChange}
								onRemoveProduct={handleRemoveProduct}
							/>
						))
					)}
				</form>
			</div>

			{/* Right Section: Order Summary */}
			<div className='w-1/3 bg-white p-6 shadow-md rounded-lg'>
				<h2 className='text-xl font-semibold mb-4'>THÔNG TIN ĐƠN HÀNG</h2>
				<div className='flex justify-between mb-4'>
					<span>Tổng tiền:</span>
					<span className='text-red-500 font-semibold text-lg'>
						{totalPrice?.toLocaleString()} ₫
					</span>
				</div>

				<button
					className='w-full bg-blue-400 text-white py-2 rounded mb-4 font-semibold'
					onClick={() => {
						if (cartItems?.length === 0 || typeof cartItems === 'string') {
							enqueueSnackbar('Không có sản phẩm nào trong giỏ hàng của bạn!', {
								variant: 'error',
							});
							return;
						} else {
							navigation('/pay');
						}
					}}
				>
					ĐẶT HÀNG NGAY
				</button>
			</div>
		</div>
	);
};

export default Cart;
