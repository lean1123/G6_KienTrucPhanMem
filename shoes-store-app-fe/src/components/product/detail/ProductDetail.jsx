import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { Link } from '@mui/material';
import { unwrapResult } from '@reduxjs/toolkit';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Outlet, useNavigate, useParams } from 'react-router';
import { addToCart } from '../../../hooks/cart/cartSlice';
import {
	fetchProductItem,
	getProductItemByColorAndSize,
} from '../../../hooks/product/productItemSlice';
import './Style.css';
import SubProductDetail from './SubProductDetail';
import { enqueueSnackbar } from 'notistack';

function ProductDetail() {
	const params = useParams();
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const productItem = useSelector(
		(state) => state.persistedReducer.productItem.productItem,
	);

	useEffect(() => {
		dispatch(fetchProductItem(params.id));
	}, [params.id, dispatch]);

	const [selectedSize, setSelectedSize] = useState(productItem?.size);
	const [selectedColor, setSelectedColor] = useState(productItem?.color);
	const [selectedImage, setSelectedImage] = useState(
		productItem?.listDetailImages[0],
	);

	const handleColorSelect = (color) => {
		setSelectedColor(color);
	};

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
		} catch (error) {
			console.error('Error adding to cart:', error);
			enqueueSnackbar('Thêm vào giỏ hàng thất bại', { variant: 'error' });
		}
	};

	useEffect(() => {
		const fetchData = async () => {
			if (selectedSize && selectedColor && productItem?.product?.id) {
				try {
					const result = await dispatch(
						getProductItemByColorAndSize({
							productId: productItem.product.id,
							color: selectedColor,
							size: selectedSize,
						}),
					);

					const data = unwrapResult(result);
					console.log('Product item:', data);
					if (data?.id && data.id !== productItem.id) {
						enqueueSnackbar('Đã chuyển sang sản phẩm khác', { variant: 'success' });
						navigate(`/products/${data.id}`);
						return;
					}
				} catch (error) {
					console.error('Error fetching product item:', error);
				}
			}
		};

		fetchData();
	}, [
		selectedColor,
		selectedSize,
		productItem?.product?.id,
		dispatch,
		navigate,
		productItem?.id,
	]);

	return (
		<div className='grid grid-cols-2 my-6 font-calibri'>
			<div className='flex flex-row justify-center'>
				<div className='flex-col h-96 overflow-auto scrollbar-hidden p-2 mr-2'>
					{productItem?.listDetailImages.map((item, index) => (
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
						{productItem?.price} VND
					</p>
					<p className='text-lg text-orange-600'>
						Tình trạng còn hàng: {productItem?.quantity}
					</p>
				</div>
				<div className='flex justify-start mb-4'>
					<p className='text-base font-bold mr-2'>Chọn màu:</p>
					<div className='flex justify-start'>
						{productItem?.product?.colors.map((color) => (
							<button
								key={color}
								onClick={handleColorSelect.bind(this, color)}
								className={`mr-2 border px-3 py-2 ${color === selectedColor ? 'bg-orange-300' : ''}`}
							>
								<span>{color}</span>
							</button>
						))}
					</div>
				</div>
				<div className='flex justify-start mb-4'>
					<p className='text-base font-bold mr-2'>Chọn size:</p>
					<div className='flex justify-start'>
						{productItem?.product.sizes.map((size) => (
							<button
								key={size}
								className={`mr-2 border px-3 py-2 ${size === selectedSize ? 'bg-orange-300' : ''}`}
								onClick={handleSizeSelect.bind(this, size)}
							>
								<span>{size}</span>
							</button>
						))}
					</div>
				</div>
				<div className='mb-8 text-base text-slate-800 ml-24'>
					<Link href='/products/1' color='textPrimary'>
						Hướng dẫn chọn size
					</Link>
				</div>
				<div className='flex justify-start ml-24 mb-10'>
					<button
						className='bg-orange-600 px-8 py-2 font-bold hover:bg-slate-950 hover:text-sky-50'
						onClick={handleAddToCart}
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
							path={`/products/${params.id}/warrantyPolicy`}
						/>
					</div>
					<div className='w-3/4 flex justify-between items-center border-t border-gray-800 py-2'>
						<SubProductDetail
							title='Chính sách đổi trả'
							path={`/products/${params.id}/returnPolicy`}
						/>
					</div>
					<div className='w-3/4 flex justify-between items-center border-t border-gray-800 py-2'>
						<SubProductDetail
							title='Reviews'
							path={`/products/${params.id}/reviews`}
						/>
					</div>
				</div>
			</div>
			<Outlet />
			<div className='w-full'>
				<p>Danh sách sản phẩm cùng dòng</p>
			</div>
		</div>
	);
}

export default ProductDetail;
