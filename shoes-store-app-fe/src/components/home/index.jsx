import { useEffect, useState } from 'react';
import ListProduct from '../product/ListProduct';
import SliderBar from './SliderBar';
import productItemApi from '../../api/productItemApi';
import { useDispatch, useSelector } from 'react-redux';
import { setBannerShow } from '../../hooks/bannerStore';
import recommendationApi from '../../api/recommendationApi';

function HomePage() {
	const { isBannerShow } = useSelector((state) => state.bannerShow);
	const dispatch = useDispatch();
	const [listNewProducts, setListNewProducts] = useState([]);
	const [listTopSaleProducts, setListTopSaleProducts] = useState([]);
	const [listRecommendationProducts, setListRecommendationProducts] = useState(
		[],
	);

	const { user } = useSelector((state) => state.userInfo);

	useEffect(() => {
		const fetchNewProducts = async () => {
			try {
				const response = await productItemApi.getNewProductItems();
				if (!response.status === 200) {
					throw new Error('Error fetching new products');
				}

				setListNewProducts(response.data?.result?.data);
			} catch (error) {
				console.error('Error fetching new products: ', error);
			}
		};

		const fetchTopSaleProducts = async () => {
			try {
				const response = await productItemApi.getTopSaleProductItems();

				if (!response.status === 200) {
					throw new Error('Error fetching top sale products');
				}

				setListTopSaleProducts(response.data?.result?.data);
			} catch (error) {
				console.error('Error fetching top sale products: ', error);
			}
		};

		fetchNewProducts();
		fetchTopSaleProducts();
	}, []);

	useEffect(() => {
		const fetchRecommendationProducts = async () => {
			try {
				const response = await recommendationApi.getRecommendationProducts(
					user?.id,
				);

				if (response.status !== 200) {
					throw new Error('Error fetching recommendation products');
				}

				if (response.data.code === 503) {
					console.error(
						'Error fetching recommendation products: ',
						response.data.message,
					);
					return;
				}
				setListRecommendationProducts(response.data);
			} catch (error) {
				throw new Error('Error fetching recommendation products: ', error);
			}
		};

		fetchRecommendationProducts();
	}, [user?.id]);

	const closePopup = () => {
		dispatch(setBannerShow(false));
	};

	return (
		<div className='w-full flex flex-col items-center'>
			{/* Banner */}
			{isBannerShow && (
				<div className='w-full flex flex-col items-center'>
					<div className='fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50'>
						<div className='relative bg-white p-0 m-0 rounded-lg shadow-lg max-w-lg w-full'>
							<button
								className='absolute top-2 right-2 text-gray-600 hover:text-gray-800'
								onClick={() => closePopup()}
							>
								✖
							</button>
							<img
								src='/banner/banner.png'
								alt='Popup Image'
								className='rounded-lg object-cover w-full h-full'
							/>
						</div>
					</div>
				</div>
			)}

			<div className='w-full'>
				<SliderBar />
			</div>
			<div className='w-full flex flex-col items-center my-5'>
				<ListProduct
					items={listRecommendationProducts}
					title='Có Thể Bạn Sẽ Thích'
					path='/listRecentProducts'
				/>
			</div>
			<div className='w-full'>
				<ListProduct
					items={listNewProducts}
					title='Danh sách sản phẩm mới nhất'
					path='/listNewProducts'
				/>
			</div>
			<div className='w-full'>
				<ListProduct
					items={listTopSaleProducts}
					title='Danh sách sản phẩm bán chạy'
					path='/listTopSaleProducts'
				/>
			</div>
		</div>
	);
}

HomePage.propTypes = {};

export default HomePage;
