import { Assistant } from '@mui/icons-material';
import { Link } from '@mui/material';
import PropTypes from 'prop-types';
import { formatCurrency } from '../../utils/formatPrice';

function AIResponse({ item }) {
	if (item.isLoading) {
		return (
			<div className='flex items-center mb-2 justify-start'>
				<div className='flex items-center justify-center bg-slate-100 p-2 rounded-full shadow-md border mr-1'>
					<Assistant sx={{ width: 14, height: 14, color: '#3b82f6' }} />
				</div>
				<div className='bg-slate-100 p-2 rounded-md shadow-md'>
					<p className='text-sm'>Đang tìm kiếm sản phẩm cho bạn...</p>
				</div>
			</div>
		);
	}
	return (
		<div className='flex items-center mb-2 justify-start'>
			<div className='flex items-center justify-center bg-slate-100 p-2 rounded-full shadow-md border mr-1'>
				<Assistant sx={{ width: 14, height: 14, color: '#3b82f6' }} />
			</div>
			<div className='bg-slate-100 flex flex-col justify-center items-center p-2 rounded-md shadow-md'>
				{item.result.length === 0 ? (
					<p className='text-sm'>Không tìm thấy sản phẩm nào</p>
				) : (
					item.result.map((product) => (
						<Link
							className='w-full h-full flex items-center border bg-white rounded-md shadow-md p-1 mb-1'
							href={`/products/${product.id}`}
							key={product.id}
						>
							<img
								src={product.images[0]}
								alt='ảnh sản phẩm'
								className='w-12 h-12 object-cover border rounded-md mr-1'
							/>
							<div className='flex items-start text-sm justify-between w-full'>
								<div className='flex flex-col justify-center items-start mr-1'>
									<p className='font-semibold'>{product.product.name}</p>
									<p>{`${product.quantityOfSize[0].size}/${product.color.name}`}</p>
								</div>
								<p>{formatCurrency(product.price)}</p>
							</div>
						</Link>
					))
				)}
			</div>
		</div>
	);
}

export default AIResponse;

AIResponse.propTypes = {
	item: PropTypes.shape({
		result: PropTypes.array,
		isAI: PropTypes.bool,
		isLoading: PropTypes.bool,
	}).isRequired,
};
