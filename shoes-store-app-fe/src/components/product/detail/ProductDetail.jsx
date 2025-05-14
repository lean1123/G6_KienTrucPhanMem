import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { unwrapResult } from '@reduxjs/toolkit';
import { enqueueSnackbar } from 'notistack';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router';
import productItemApi from '../../../api/productItemApi';
import { addToCart } from '../../../hooks/cart/cartSlice';
import useProductItem from '../../../hooks/product/useProductItem';
import { formatCurrency } from '../../../utils/formatPrice';
import './Style.css';
import SubProductDetail from './SubProductDetail';
import FavoriteIcon from '@mui/icons-material/Favorite';

function ProductDetail() {
	const params = useParams();
	const dispatch = useDispatch();
	const { isLoading, productItem } = useProductItem(params.id);

	const [selectedImage, setSelectedImage] = useState(null);
	const [selectedSize, setSelectedSize] = useState(null);
	const [quantity, setQuantity] = useState(null);
	const [showSizeChoosenGuid, setShowSizeChoosenGuid] = useState(false);
	const [isLiked, setIsLiked] = useState(false);
	const { user } = useSelector((state) => state.userInfo);

	useEffect(() => {
		if (productItem && user) {
			setIsLiked(productItem.likes?.includes(user.id));
		}
	}, [productItem, user]);

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
					productItemId: productItem.id,
					quantity: 1,
					size: selectedSize,
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

	const handleLikeProduct = async () => {
		try {
			const response = await productItemApi.likeProductItem(productItem.id);

			if (response?.status === 200) {
				if (response.data.code === 503) {
					enqueueSnackbar('Máy chủ tạm thời không khả dụng vui lòng thử lại sau', {
						variant: 'error',
					});
					return;
				}
				enqueueSnackbar('Đã thêm vào danh sách sản phẩm yêu thích', {
					variant: 'success',
				});
				setIsLiked(true);
				productItem.likes = [...productItem.likes, user.id];
				return;
			}

			enqueueSnackbar('Thêm vào danh sách yêu thích thất bại', {
				variant: 'error',
			});
			return;
		} catch (error) {
			console.error('Error adding to cart:', error);
			enqueueSnackbar('Thêm vào danh sách yêu thích thất bại', {
				variant: 'error',
			});
			return;
		}
	};

	const handleUnlikeProduct = async () => {
		try {
			const response = await productItemApi.unlikeProductItem(productItem.id);

			if (response?.status === 200) {
				if (response.data.code === 503) {
					enqueueSnackbar('Máy chủ tạm thơi không khả dụng vui lòng thử lại sau', {
						variant: 'error',
					});
					return;
				}
				enqueueSnackbar('Đã loại khỏi vào danh sách sản phẩm yêu thích', {
					variant: 'success',
				});
				setIsLiked(false);
				productItem.likes = productItem.likes.filter((item) => item !== user.id);
				return;
			}
			enqueueSnackbar('Xóa khỏi danh sách yêu thích thất bại', {
				variant: 'error',
			});
			return;
		} catch (error) {
			console.error('Error adding to cart:', error);
			enqueueSnackbar('Xóa khỏi danh sách yêu thích thất bại', {
				variant: 'error',
			});
			return;
		}
	};

	const uniqueImages = productItem?.images
		? Array.from(new Set(productItem.images))
		: [];

	return (
		<div className='grid grid-cols-5 font-calibri py-10 gap-8'>
			<div className='col-span-3 flex flex-row justify-center'>
				<div className='flex flex-col h-[550px] overflow-auto p-2 mr-4 gap-2'>
					{uniqueImages?.map((item, index) => (
						<div
							key={index}
							className={`border rounded-lg p-1 cursor-pointer transition-transform duration-200 mb-1 ${
								selectedImage === item
									? 'ring-2 ring-orange-400 scale-105'
									: 'hover:ring-1 hover:ring-orange-300 hover:scale-105'
							}`}
							onClick={() => setSelectedImage(item)}
						>
							<img
								className='w-20 h-20 object-cover rounded'
								src={item}
								alt='product thumb'
							/>
						</div>
					))}
				</div>

				{/* Main Image */}
				<div className='border rounded-lg w-[550px] h-[550px] object-cover shadow-lg overflow-hidden'>
					<img
						className='w-full h-full object-cover transition-transform duration-300 hover:scale-105'
						src={selectedImage}
						alt='product large'
					/>
				</div>
			</div>

			{/* RIGHT - PRODUCT INFO (chiếm 2 cột) */}
			<div className='col-span-2'>
				<h2 className='text-3xl font-extrabold text-slate-900 mb-4'>
					{productItem?.product.name}
				</h2>

				<div className='flex items-center mb-4'>
					<p className='text-xl font-bold text-orange-600 mr-10'>
						{formatCurrency(productItem?.price)}
					</p>
					<p className='text-lg font-semibold text-slate-800'>
						Tình trạng: {quantity === 0 ? 'Hết hàng' : `Còn ${quantity} sản phẩm`}
					</p>
				</div>

				<div className='flex justify-start'>
					<div className='mr-10'>
						<p className='text-base font-semibold text-slate-800 mb-1'>Màu sắc:</p>
						<div
							className='shadow-md border-2 border-gray-300 rounded-full w-8 h-8'
							style={{ backgroundColor: productItem.color.code }}
						></div>
					</div>

					<div>
						<p className='text-base font-semibold text-slate-800 mb-1'>Chọn size:</p>
						<div className='flex'>
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
				</div>

				<div className='mb-6 ml-1'>
					<button
						className='text-base font-medium text-orange-600 underline z-999999'
						onClick={() => setShowSizeChoosenGuid(true)}
					>
						Hướng dẫn chọn size
					</button>
				</div>

				<div className='flex items-center gap-4 mb-10'>
					<button
						className='bg-orange-600 px-8 py-2 text-white font-bold hover:bg-slate-900 transition rounded-md shadow-md'
						onClick={handleAddToCart}
					>
						THÊM VÀO GIỎ HÀNG
					</button>
					<button
						className={`px-8 py-2 font-bold border border-orange-600 text-orange-600 ${
							isLiked ? 'bg-orange-600 text-white' : 'bg-white'
						} hover:bg-slate-900 transition rounded-md shadow-md`}
						onClick={!isLiked ? handleLikeProduct : handleUnlikeProduct}
					>
						{isLiked ? <FavoriteIcon /> : <FavoriteBorderIcon />}
					</button>
					<p className='text-sm text-gray-600'>
						{productItem.likes?.length || 0} người đã thích sản phẩm này
					</p>
				</div>

				{/* Sub Detail */}
				<div className='flex flex-col gap-2'>
					<SubProductDetail
						title='Mô tả sản phẩm'
						content={productItem?.product.description}
					/>
					<SubProductDetail
						title='Thông tin bảo hành'
						content='Sản phẩm được bảo hành trong vòng 3 tháng...'
					/>
					<SubProductDetail
						title='Chính sách đổi trả'
						content='Trường hợp lỗi từ Biti’s giao sai...'
					/>
					<SubProductDetail title='Đánh giá' content={productItem.product.rating} />
					<SubProductDetail
						title='Danh mục'
						content={
							productItem.product?.category?.name || 'Sản phẩm chưa có danh mục'
						}
					/>
				</div>
			</div>

			{/* Modal chọn size */}
			{showSizeChoosenGuid && (
				<div className='fixed inset-0 z-[999999] bg-gray-300 bg-opacity-70 flex justify-center items-center'>
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
