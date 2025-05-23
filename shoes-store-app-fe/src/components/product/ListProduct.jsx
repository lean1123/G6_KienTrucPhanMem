import PropTypes from 'prop-types';
import ProductItem from './ProductItem';
import { Link } from '@mui/material';

function ListProduct({ items, title = '', path = '' }) {
	return (
		<div className='w-full flex flex-col justify-center items-center px-9 py-1 rounded-b-lg'>
			{title && (
				<p className='text-3xl text-black font-sans font-bold my-4'>{title}</p>
			)}
			<div className='grid grid-cols-5 gap-4 px-10'>
				{items?.length === 0 && (
					<div className='col-span-5 flex justify-center items-center'>
						<p className=' text-black font-sans italic my-4 text-center'>
							Chưa có sản phẩm nào được hiển thị
						</p>
					</div>
				)}
				{items.map((item) => (
					<ProductItem item={item} key={item.id} />
				))}
			</div>
			{path && items?.length > 0 && (
				<Link href={path} underline='always' color='textPrimary' className='mt-4'>
					<p className='text-base text-black font-semibold'>
						Còn nhiều lắm , xem thêm
					</p>
				</Link>
			)}
		</div>
	);
}

ListProduct.propTypes = {
	items: PropTypes.array.isRequired,
	title: PropTypes.string,
	path: PropTypes.string,
};

export default ListProduct;
