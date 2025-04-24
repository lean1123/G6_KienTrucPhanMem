import { useEffect, useState } from 'react';
import ListProduct from '../product/ListProduct';
import SliderBar from './SliderBar';
import productItemApi from '../../api/productItemApi';

function HomePage() {
	const [isVisible, setIsVisible] = useState(true);

	const [listRecentProducts, setListRecentProducts] = useState([]);
	const [listNewProducts, setListNewProducts] = useState([]);
	const [listTopSaleProducts, setListTopSaleProducts] = useState([]);

	useEffect(() => {
		const fetchRecentProducts = async () => {
			try {
				const response = await productItemApi.getRecentProducts();
				if (!response.status === 200) {
					throw new Error('Error fetching recent products');
				}

				console.log('response of list recent', response);

				setListRecentProducts(response.data?.data);
			} catch (error) {
				console.error('Error fetching recent products: ', error);
			}
		};

		const fetchNewProducts = async () => {
			try {
				const response = await productItemApi.getNewProductItems();
				if (!response.status === 200) {
					throw new Error('Error fetching new products');
				}

				setListNewProducts(response.data?.data?.data);
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

				setListTopSaleProducts(response.data?.data?.data);
			} catch (error) {
				console.error('Error fetching top sale products: ', error);
			}
		};

		fetchRecentProducts();
		fetchNewProducts();
		fetchTopSaleProducts();
	}, []);

	const closePopup = () => {
		setIsVisible(false);
	};

	return (
		<div className='w-full flex flex-col items-center'>
			{/* Banner */}
			{isVisible && (
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
					items={listRecentProducts}
					title='Danh sách sản phẩm đã xem'
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
