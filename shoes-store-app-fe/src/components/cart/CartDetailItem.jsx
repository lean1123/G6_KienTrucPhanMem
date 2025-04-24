import PropTypes from 'prop-types';
import { useMemo, useState } from 'react';

CartDetailItem.propTypes = {
	item: PropTypes.object,
	onQuantityChange: PropTypes.func,
	onRemoveProduct: PropTypes.func,
};

function CartDetailItem({ item, onQuantityChange, onRemoveProduct }) {
	const [quantity, setQuantity] = useState(item?.quantity);

	const totalPrice = useMemo(
		() => item.productItem?.price * quantity,
		[item.productItem?.price, quantity],
	);

	const increaseQuantity = () => {
		setQuantity(quantity + 1);
		if (onQuantityChange) onQuantityChange(item, quantity + 1);
	};

	const decreaseQuantity = () => {
		if (quantity === 0) return;
		setQuantity(quantity - 1);
		if (onQuantityChange) onQuantityChange(item, quantity - 1);
	};

	const handleRemoveProduct = () => {
		if (onRemoveProduct) onRemoveProduct(item?.productItem?.id);
	};

	return (
		<div>
			<div className='flex items-center justify-between border p-4 rounded-md mb-6'>
				<img
					src={item.productItem.listDetailImages[0]}
					alt={'product item'}
					className='w-20 h-20 object-cover rounded-md'
				/>
				<div className='flex-1 ml-4'>
					<p className='font-medium'>{item.product.name}</p>
					<div className='flex justify-start items-center space-x-4 text-gray-500 mt-2'>
						<p>
							Màu sắc: <span className='ml-2'>{item.productItem?.color}</span>
						</p>
						<p>Kích thước: {item.productItem?.size}</p>
					</div>
					{/* Price per item */}
					<p className='font-semibold text-red-500 mt-2'>
						Giá: {item.productItem?.price.toLocaleString()} ₫
					</p>
				</div>

				<div className='flex flex-col items-start mt-4 space-y-2 relative'>
					<button
						type='button'
						className='bg-red-400 text-red-700 hover:text-black absolute -top-11 -right-5 rounded-lg px-2 py-1 text-center'
						onClick={handleRemoveProduct}
					>
						Remove
					</button>
					<div className='flex items-center space-x-2'>
						<button
							type='button'
							onClick={decreaseQuantity}
							className='px-2 py-1 bg-gray-200 rounded'
						>
							-
						</button>
						<span>{quantity}</span>
						<button
							type='button'
							onClick={increaseQuantity}
							className='px-2 py-1 bg-gray-200 rounded'
						>
							+
						</button>
					</div>
					<p className='font-semibold text-red-500'>
						{totalPrice.toLocaleString()} ₫
					</p>
				</div>
			</div>
		</div>
	);
}

export default CartDetailItem;
