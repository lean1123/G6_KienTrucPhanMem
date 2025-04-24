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

const Cart = () => {
	const dispatch = useDispatch();
	const navigation = useNavigate();

	const { cartItems } = useSelector((state) => state.persistedReducer.cart);
	const { userId, user } = useSelector((state) => state.persistedReducer.user);

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
	}, [dispatch, userId]);

	const handleQuantityChange = (item, newQuantity) => {
		if (newQuantity < 0) {
			return;
		}
		const updatedItem = {
			cartId: item?.cartDetailPK?.cartId || null,
			productId: item?.productItem?.id,
			quantity: newQuantity,
		};

		console.log('updatedItem', updatedItem);

		if (updatedItem.quantity === 0) {
			dispatch(deleteCartDetail(updatedItem.productId));
			return;
		}

		dispatch(updateQuantity(updatedItem));
	};

	const handleRemoveProduct = (productId) => {
		console.log('productId', productId);

		dispatch(deleteCartDetail(productId));
	};

	return (
		<div className='flex justify-center p-6 bg-gray-100'>
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

					<div className='mb-6'>
						<label className='block mb-2 text-gray-700'>Ghi chú đơn hàng</label>
						<input
							type='text'
							className='w-full border p-2 rounded-md'
							placeholder='Thêm ghi chú cho đơn hàng...'
						/>
					</div>

					<div className='flex items-center'>
						<input type='checkbox' className='mr-2' />
						<label>Xuất hoá đơn cho đơn hàng</label>
					</div>
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
				<p className='text-gray-500 mb-6'>
					Phí vận chuyển sẽ được tính ở trang thanh toán.
				</p>
				<p className='text-gray-500 mb-6'>
					Bạn cũng có thể nhập mã giảm giá ở trang thanh toán.
				</p>

				<button
					className='w-full bg-red-500 text-white py-2 rounded mb-4 font-semibold'
					onClick={() => {
						if (user === null || userId === null) {
							enqueueSnackbar('Vui lòng đăng nhập để tiếp tục!', {
								variant: 'info',
							});
							return;
						}

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
					ĐẶT HÀNG NGAY (Áp dụng cho Việt Nam)
				</button>
			</div>
		</div>
	);
};

export default Cart;
