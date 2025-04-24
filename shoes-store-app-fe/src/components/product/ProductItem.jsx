import React from 'react';
import PropTypes from 'prop-types';
import { HeartOutlined } from '@ant-design/icons';
import { Link } from '@mui/material';
import { formatCurrency } from '../../utils/formatPrice';

function ProductItem({ item }) {
	const [isHeart, setIsHeart] = React.useState(false);
	const [currentImage, setCurrentImage] = React.useState(item?.images[0]);

	return (
		<div className='relative h-full w-full border-1 overflow-hidden group'>
			<Link
				className='w-full h-fit p-0 m-0 flex flex-col items-start relative'
				href={`/products/${item.id}`}
				underline='none'
				color='textPrimary'
			>
				<div className='relative z-20'>
					<button
						onClick={(e) => {
							e.preventDefault();
							setIsHeart((prev) => !prev);
						}}
						className='absolute top-2 right-2 text-gray-600 hover:text-gray-800 text-2xl z-20'
					>
						<HeartOutlined
							style={{ color: isHeart ? 'red' : 'grey' }}
							className='z-20'
						/>
					</button>

					<div className='absolute bg-green-500 text-white px-2 rounded-lg top-3 left-2'>
						Mới
					</div>
					<img
						src={currentImage}
						onMouseEnter={() => setCurrentImage(item.images[1] || item.images[0])}
						onMouseLeave={() => setCurrentImage(item.images[0])}
						alt={item?.product?.name}
						className='w-full h-full object-cover transition duration-700 delay-150 ease-in-out'
					/>
				</div>
				<div className='h-full w-full flex flex-col p-1.5'>
					<div className='flex justify-between w-full'>
						<p className='font-medium font-mono'>
							Màu sắc:
							<span
								style={{
									backgroundColor: item.color?.code,
									display: 'inline-block',
									width: '16px',
									height: '16px',
									borderRadius: '50%',
									marginLeft: '8px',
									boxShadow: '0 0 5px rgba(0, 0, 0, 0.3)',
									border: '1px solid #ccc',
								}}
							></span>
						</p>
						<p className='font-medium font-mono'>
							Có {item.quantityOfSize?.length} kích cỡ
						</p>
					</div>
					<div className='flex flex-col items-stretch w-full'>
						<p className='font-medium font-mono py-2 text-justify text-sm'>
							{item.product.name}
						</p>
						<p className='text-lg font-mono text-orange-600 text-center'>
							{formatCurrency(item.price)} VND
						</p>
					</div>
				</div>
			</Link>
			<div className='details absolute bottom-0 left-0 w-full bg-red-500 text-white p-2 transform translate-y-full transition-transform duration-300 ease-in-out group-hover:translate-y-0'>
				<p className='font-medium'>Chi tiết sản phẩm</p>
			</div>
		</div>
	);
}

ProductItem.propTypes = {
	item: PropTypes.shape({
		id: PropTypes.string.isRequired,
		price: PropTypes.number.isRequired,
		images: PropTypes.array.isRequired,
		color: {
			id: PropTypes.string,
			name: PropTypes.string,
			code: PropTypes.string,
		},
		quantityOfSize: [
			{
				size: PropTypes.number.isRequired,
				quantity: PropTypes.number.isRequired,
			},
		],
		product: PropTypes.shape({
			id: PropTypes.string.isRequired,
			name: PropTypes.string.isRequired,
			description: PropTypes.string,
			warrantyInformation: PropTypes.string,
			returnInformation: PropTypes.string,
			avatar: PropTypes.string.isRequired,
			shippingInformation: PropTypes.string,
			rating: PropTypes.number,
			createdDate: PropTypes.string,
		}),
	}),
};

export default ProductItem;
