import PropTypes from 'prop-types';
import { formatCurrency } from '../../utils/formatPrice';

function OrderDetailHorizaltal({ item }) {
	return (
		<div
			className='flex items-center justify-between border rounded-lg p-2 shadow-sm hover:shadow-md transition'
			key={item?.id}
		>
			<div className='flex items-center space-x-3'>
				<img
					src={item.productItem.images[0]}
					alt='product'
					className='w-16 h-16 rounded-lg object-cover'
				/>
				<div className='flex flex-col'>
					<p className='font-calibri font-medium text-gray-800'>
						{item?.productItem?.product?.name}
					</p>
					<p className='font-calibri text-sm text-gray-500'>
						Số lượng: {item?.quantity}
					</p>
				</div>
			</div>
			<div className='font-calibri font-semibold text-green-600'>
				{formatCurrency(item?.price)}
			</div>
		</div>
	);
}

export default OrderDetailHorizaltal;

OrderDetailHorizaltal.propTypes = {
	item: PropTypes.shape({
		id: PropTypes.string.isRequired,
		productItem: PropTypes.shape({
			images: PropTypes.arrayOf(PropTypes.string).isRequired,
			product: PropTypes.shape({
				name: PropTypes.string.isRequired,
			}).isRequired,
		}).isRequired,
		quantity: PropTypes.number.isRequired,
		price: PropTypes.number.isRequired,
	}),
};
