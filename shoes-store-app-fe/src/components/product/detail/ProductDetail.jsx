import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { Link } from '@mui/material';
import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { Outlet, useNavigate, useParams } from 'react-router';
import useProductItem from '../../../hooks/product/useProductItem';
import './Style.css';
import SubProductDetail from './SubProductDetail';
import { enqueueSnackbar } from 'notistack';
import { unwrapResult } from '@reduxjs/toolkit';
import { addToCart } from '../../../hooks/cart/cartSlice';
import { formatCurrency } from '../../../utils/formatPrice';

function ProductDetail() {
	const params = useParams();
	const dispatch = useDispatch();
	const { isLoading, productItem } = useProductItem(params.id);

	const [selectedImage, setSelectedImage] = useState(null);
	const [selectedSize, setSelectedSize] = useState(null);
	const [quantity, setQuantity] = useState(null);
	const [showSizeChoosenGuid, setShowSizeChoosenGuid] = useState(false);

	useEffect(() => {
		if (productItem?.images?.length) {
			setSelectedImage(productItem.images[0]);
		}

		if (productItem?.quantityOfSize?.length) {
			setSelectedSize(productItem.quantityOfSize[0].size);
			setQuantity(productItem.quantityOfSize[0].quantity);
		}
	}, [productItem]);

	const handleSizeSelect = (size) => {
		setSelectedSize(size);
	};

	const handleAddToCart = async () => {
		try {
			const result = await dispatch(
				addToCart({
					productId: productItem.id,
					quantity: 1,
				}),
			);

			const data = unwrapResult(result);

			if (data?.length > 0 || typeof data !== 'string') {
				enqueueSnackbar('Thêm vào giỏ hàng thành công', { variant: 'success' });
				return;
			}

			enqueueSnackbar('Thêm vào giỏ hàng thất bại', { variant: 'error' });
			return;
		} catch (error) {
			console.error('Error adding to cart:', error);
			enqueueSnackbar('Thêm vào giỏ hàng thất bại', { variant: 'error' });
			return;
		}
	};

	if (!productItem || productItem === null) {
		return (
			<div className='w-full h-screen flex justify-center items-center'>
				Không tìm thấy sản phẩm
			</div>
		);
	}

	if (isLoading) {
		return (
			<div className='w-full h-screen flex justify-center items-center'>
				Loading...
			</div>
		);
	}

	return (
		<div className='grid grid-cols-2 my-6 font-calibri'>
			<div className='flex flex-row justify-center'>
				<div className='flex-col h-96 overflow-auto scrollbar-hidden p-2 mr-2'>
					{productItem?.images.map((item, index) => (
						<div
							key={index}
							className={`border rounded-lg object-cover p-2 mr-2 mb-2 ${selectedImage === item && 'bg-orange-300'}`}
							onClick={() => setSelectedImage(item)}
						>
							<img className={`w-20`} src={item} alt='product' />
						</div>
					))}
				</div>
				<div className='border rounded-lg w-96 h-96 object-cover p-2'>
					<img className='w-full h-full' src={selectedImage} alt='product avatar' />
				</div>
			</div>
			<div className='mb-10'>
				<h2 className='text-2xl font-bold mb-4'>{productItem?.product.name}</h2>
				<div className='w-full flex justify-start mb-4'>
					<p className='text-lg mr-10 font-semibold text-orange-600'>
						{formatCurrency(productItem?.price)}
					</p>
					<p className='text-lg text-orange-600'>
						Tình trạng còn hàng: {quantity === 0 ? 'Hết hàng' : quantity}
					</p>
				</div>
				<div className='flex justify-start mb-4'>
					<p className='text-base font-bold mr-2'>Màu Sắc:</p>
					<div className='flex justify-start'>
						<span
							className='shadow-md border-2 border-gray-300 rounded-full w-8 h-8'
							style={{ backgroundColor: productItem.color.code }}
						></span>
					</div>
				</div>
				<div className='flex justify-start mb-4'>
					<p className='text-base font-bold mr-2'>Chọn size:</p>
					<div className='flex justify-start'>
						{productItem?.quantityOfSize?.map((item, index) => (
							<div
								key={index}
								className={`border rounded-lg w-10 h-10 flex justify-center items-center mr-2 cursor-pointer ${
									selectedSize === item.size ? 'bg-orange-300 shadow-md' : ''
								}`}
								onClick={() => {
									handleSizeSelect(item.size);
									setQuantity(item.quantity);
								}}
							>
								{item.size}
							</div>
						))}
					</div>
				</div>
				<div className='mb-8 text-base text-slate-800 ml-24'>
					<button
						className='font-medium text-orange-600'
						onClick={() => setShowSizeChoosenGuid(true)}
					>
						Hướng dẫn chọn size
					</button>
				</div>
				<div className='flex justify-start ml-24 mb-10'>
					<button
						className='bg-orange-600 px-8 py-2 font-bold hover:bg-slate-950 hover:text-sky-50'
						// onClick={handleAddToCart}
					>
						THÊM VÀO GIỎ HÀNG
					</button>
					<div className='px-8 py-2 font-bold border w-30 text-orange-600 border-orange-600'>
						<FavoriteBorderIcon />
					</div>
				</div>

				<div className='w-full flex flex-col'>
					<div className='w-3/4 flex justify-between items-center border-t border-gray-800 py-2'>
						<SubProductDetail
							title='Mô tả sản phẩm'
							content={productItem?.product.description}
							path={`/products/${params.id}/description`}
						/>
					</div>
					<div className='w-3/4 flex justify-between items-center border-t border-gray-800 py-2'>
						<SubProductDetail
							title='Thông tin bảo hành'
							content='Sản phẩm được bảo hành trong vòng 3 tháng kể từ thời điểm mua hàng. Ngoài ra sản phẩm còn được bảo hành trọn đời với lỗi bong keo, đứt chỉ (vật tư của sản phẩm đủ điều kiện tái chế không bị rách,…)'
						/>
					</div>
					<div className='w-3/4 flex justify-between items-center border-t border-gray-800 py-2'>
						<SubProductDetail
							title='Chính sách đổi trả'
							content='Trường hợp lỗi từ Biti’s giao sai thông tin sản phẩm (không đúng kích cỡ, không đúng sản phẩm), hư hỏng trong quá trình vận chuyển hoặc từ nhà sản xuất khách hàng được miễn phí hoàn toàn chi phí vận chuyển đổi hàng. Khách hàng gửi hàng về trong thời gian 7 ngày (không tính thứ 7, Chủ nhật) kể từ ngày nhận hàng, khách mang hàng ra bưu điện và chọn người nhận thanh toán.'
						/>
					</div>
					<div className='w-3/4 flex justify-between items-center border-t border-gray-800 py-2'>
						<SubProductDetail title='Đánh giá' content={productItem.product.rating} />
					</div>
				</div>
			</div>
			{showSizeChoosenGuid && (
				<div className='fixed inset-0 z-50 bg-gray-300 bg-opacity-70 flex justify-center items-center'>
					<div className='relative w-full h-full'>
						<img
							src='/chonsize.webp'
							alt='Cách chọn size'
							className='w-full h-full object-contain'
						/>
						<button
							onClick={() => setShowSizeChoosenGuid(false)}
							className='absolute top-4 right-4 bg-white text-black px-4 py-2 rounded shadow-lg hover:bg-red-500 hover:text-white transition'
						>
							Đóng
						</button>
					</div>
				</div>
			)}
		</div>
	);
}

export default ProductDetail;
