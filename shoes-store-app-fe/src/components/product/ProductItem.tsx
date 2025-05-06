import React from 'react';
import { HeartOutlined } from '@ant-design/icons';
import { Link } from '@mui/material';

import { ProductItemType } from '../../types/productItemType';
import { formatCurrency } from '../../utils/formatPrice';

type ProductItemProps = {
	item: ProductItemType;
};

function ProductItem({ item }: ProductItemProps) {
	const [isHeart, setIsHeart] = React.useState(false);

	return (
		<div
			className='relative h-full w-full border-1 rounded-lg shadow-md overflow-hidden group 
		transition delay-150 duration-300 ease-in-out hover:-translate-y-1 hover:scale-110 bg-slate-100'
		>
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
						src={item.images[0]}
						alt={item?.product?.name}
						className='w-full h-full object-cover'
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
							{formatCurrency(item.price)}
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

export default ProductItem;
