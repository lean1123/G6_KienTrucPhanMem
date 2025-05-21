import { Assistant } from '@mui/icons-material';
import { Link } from '@mui/material';
import PropTypes from 'prop-types';
import { formatCurrency } from '../../utils/formatPrice';
import { useMemo } from 'react';

const getRandomNoResultMessage = () => {
	const messages = [
		'Xin lỗi, tôi chưa tìm thấy sản phẩm phù hợp 😥',
		'Có vẻ như sản phẩm bạn cần chưa có trong hệ thống!',
		'Tôi đã tìm kỹ nhưng vẫn chưa thấy sản phẩm nào khớp 😔',
		'Hmm... chưa có sản phẩm nào như vậy, bạn thử lại với mô tả khác nhé!',
		'Hiện tại tôi không tìm thấy sản phẩm tương ứng. Mong bạn thông cảm!',
	];
	const index = Math.floor(Math.random() * messages.length);
	return messages[index];
};
function AIResponse({ item }) {
	const noResultMessage = useMemo(() => getRandomNoResultMessage(), []);

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
				<p className='text-sm mb-2'>{item.message}</p>
				{item?.results?.length === 0 && item?.intent === 'search_product' ? (
					<p className='text-sm italic mb-2'>{noResultMessage}</p>
				) : (
					item?.results.map((product) => (
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
		results: PropTypes.array,
		isAI: PropTypes.bool,
		isLoading: PropTypes.bool,
		message: PropTypes.string,
	}).isRequired,
};
