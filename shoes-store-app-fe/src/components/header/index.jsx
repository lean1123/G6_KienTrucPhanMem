import { Badge, Link } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../../hooks/auth/authSlice';
import { useNavigate } from 'react-router';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './style.css';
import { viewCart } from '../../hooks/cart/cartSlice';

function Header() {
	const [isOpen, setIsOpen] = useState(false);
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const [isOpendDiv, setIsOpenDiv] = useState(false);
	const [name, setName] = useState('TRẢ HÀNG DỄ DÀNG');
	const [isDropdownVisible, setIsDropdownVisible] = useState(false);
	const { user } = useSelector((state) => state.persistedReducer.userInfo);

	const token = useSelector(
		(state) => state.persistedReducer?.user?.accessToken,
	);

	const { cartItems } = useSelector((state) => state.persistedReducer.cart);

	const totalPrice = useMemo(() => {
		return cartItems?.reduce(
			(acc, item) => acc + item?.productItem?.price * item?.quantity,
			0,
		);
	}, [cartItems]);

	useEffect(() => {
		if (token) {
			setIsOpen(false);
		}
	}, [token]);

	const handleLogout = () => {
		const action = logout();
		dispatch(action);
		navigate('/');
	};

	useEffect(() => {
		// dispatch(viewCart());
		const interval = setInterval(() => {
			setName((prevName) =>
				prevName === 'TRẢ HÀNG DỄ DÀNG'
					? 'GIAO HÀNG MIỄN PHÍ CHO THÀNH VIÊN CỦA LENDOM'
					: 'TRẢ HÀNG DỄ DÀNG',
			);
		}, 3000);

		return () => clearInterval(interval);
	}, [dispatch]);

	const toggleDropdown = () => {
		setIsOpenDiv(!isOpendDiv);
	};

	const handleButtonClick = () => {
		setIsDropdownVisible(!isDropdownVisible);
	};

	return (
		<>
			<div
				className='bg-black h-10 text-white flex justify-center items-center cursor-pointer'
				onClick={toggleDropdown}
			>
				<p className='text-xs font-medium leading-loose'>{name}</p>
				<button className='bg-black text-white font-bold py-1 px-2 rounded'>
					<i className='fas fa-chevron-down'></i>
				</button>
			</div>
			{isOpendDiv && (
				<div
					className={`fixed top-0 left-0 right-0 h-3/5 bg-white z-10 transition-transform duration-500 ease-in-out ${
						isOpendDiv ? 'translate-y-0' : '-translate-y-full'
					}`}
				>
					<button
						className='text-2xl font-serif text-black border border-black w-10 h-10 flex items-center justify-center fixed right-4 top-4'
						onClick={toggleDropdown}
					>
						×
					</button>

					<div className='flex mt-24 ml-40'>
						<div className='mr-20'>
							<p className='text-sl font-calibri tracking-tight font-bold'>
								GIAO HÀNG MIỄN PHÍ CHO THÀNH VIÊN CỦA LENDOM
							</p>
							<div className='flex flex-col mt-2 text-sm'>
								<div className='font-calibri text-sl'>
									Đăng ký thành viên LENDOM để hưởng thụ dịch vụ giao hàng
								</div>
								<div className='font-calibri'>
									miễn phí! Hoặc bạn chỉ được nhận ưu đãi miễn phí giao hàng với
								</div>
								<div className='font-calibri'>
									hóa đơn có trị giá ít nhất 1.6 triệu đồng
								</div>
								<Link class='text-base font-calibri font-bold text-black hover:underline mt-5'>
									THAM GIA NGAY
								</Link>
							</div>
						</div>
						<div className=''>
							<p className='text-sl font-calibri tracking-tight font-bold'>
								TRẢ HÀNG DỄ DÀNG
							</p>
							<div className='flex flex-col mt-2 text-sm'>
								<div className='font-calibri text-sl'>
									Nếu bạn không hài lòng với đơn hàng của mình, bạn có thể
								</div>
								<div className='font-calibri text-sl'>
									được hoàn lại tiền. Vui lòng xem Chính Sách Trả Hàng của chúng
								</div>
								<div className='font-calibri text-sl'>tôi để biết thêm chi tiết.</div>
								<Link class='text-base font-calibri font-bold text-black hover:underline mt-5'>
									TRẢ HÀNG DỄ DÀNG
								</Link>
							</div>
						</div>
					</div>
				</div>
			)}

			<nav className='bg-white'>
				<div className='mx-auto max-w-7xl px-2 sm:px-6 lg:px-8'>
					<div className='relative flex h-16 items-center justify-between'>
						<div className='flex flex-1 items-center justify-center sm:items-stretch sm:justify-start'>
							<div className='flex-shrink-0'>
								<img
									className='block h-16 w-auto'
									src='/logo.png'
									alt='Logo'
									width={40}
									height={40}
								/>
							</div>
							<div className='hidden sm:flex justify-center items-center mx-auto'>
								<div className='flex space-x-4 '>
									<button
										onClick={() => navigate('/')}
										className='text-black hover:text-red-500 px-3 py-2 rounded-md text-ml font-medium'
									>
										TRANG CHỦ
									</button>
									<button
										onClick={() => navigate('/products')}
										className='text-black hover:text-red-500 px-3 py-2 rounded-md text-ml font-medium'
									>
										TẤT CẢ SẢN PHẨM
									</button>
									<button
										onClick={() => navigate('/about')}
										className='text-black hover:text-red-500 px-3 py-2 rounded-md text-ml font-medium'
									>
										VỀ CHÚNG TÔI
									</button>
									<button
										onClick={() => navigate('/categories')}
										className='text-black  hover:text-red-500 px-3 py-2 rounded-md text-ml font-medium'
									>
										DANH MỤC
									</button>
									<button
										onClick={() => navigate('/categoryforBoy')}
										className='text-black  hover:text-red-500 px-3 py-2 rounded-md text-ml font-medium'
									>
										NAM
									</button>
									<button
										onClick={() => navigate('/categoryforGirl')}
										className='text-black  hover:text-red-500 px-3 py-2 rounded-md text-ml font-medium'
									>
										NỮ
									</button>
								</div>
							</div>
						</div>
						<div className='relative ml-3'>
							<div>
								<div className='flex items-center'>
									<button
										className='px-3 py-2 rounded-md text-sm font-medium'
										onClick={handleButtonClick}
									>
										<img
											className='block h-5 w-auto'
											src='/search.png'
											width={17}
											height={17}
										/>
									</button>
									<div
										className={`fixed top-0 left-0 right-0 h-2/5 bg-white z-10 transition-transform duration-500 ease-in-out ${
											isDropdownVisible ? 'translate-y-0' : '-translate-y-full'
										}`}
									>
										<button
											className='absolute top-4 right-4 text-4xl'
											onClick={handleButtonClick}
										>
											&times;
										</button>
										{/* Nội dung của div khi được hiển thị */}
										<p className='font-bold text-xl mt-16 ml-28'>Bạn đang tìm kiếm gì?</p>
										<div className='search-container mt-10 ml-36 flex justify-between w-4/5 mx-auto p-2'>
											<input
												type='text'
												placeholder='Tìm kiếm sản phẩm, thương hiệu, và hơn thế nữa'
												className='w-full border-none no-border py-2 mr-2'
											/>
											<button className='search-button bg-white rounded-md p-2'>
												<img
													className='block h-5 w-auto'
													src='/search.png'
													width={17}
													height={17}
												/>
											</button>
										</div>
									</div>
									<button
										onClick={() => {
											token ? setIsOpen(!isOpen) : navigate('/login');
										}}
										className='px-3 py-2 rounded-md text-sm font-medium'
										id='user-menu-button'
										aria-expanded={isOpen}
										aria-haspopup='true'
									>
										<img
											className='block h-5 w-auto'
											src='/people.png'
											width={17}
											height={17}
										/>
									</button>
									<button className='px-3 py-2 rounded-md text-sm font-medium'>
										<Badge badgeContent={0} color='warning'>
											<img
												className='block h-5 w-auto'
												src='/heart.png'
												width={17}
												height={17}
											/>
										</Badge>
									</button>
									<div className='group relative'>
										<button
											className='px-3 py-2 rounded-md text-sm font-medium relative inline-block'
											onClick={() => {
												navigate('/cart');
											}}
										>
											<Badge badgeContent={cartItems?.length} color='warning'>
												<img
													className='block h-5 w-auto'
													src='/market.png'
													width={17}
													height={17}
												/>
											</Badge>
										</button>
										<div
											className='absolute -right-56 top-10 transform -translate-x-1/2 mt-2 p-2 bg-white
                      						text-black text-sm rounded hidden group-hover:block transition-opacity duration-600 z-50
                      							w-96 min-h-60 shadow-inner'
										>
											<div className='w-full flex flex-col items-center'>
												<span className='w-full text-base font-semibold font-calibri border-b py-2'>
													GIỎ HÀNG
												</span>
												<div className='w-full h-52 flex flex-col overflow-y-auto scroll-smooth scrollbar-thin scrollbar-webkit mb-1'>
													{cartItems?.map((item, index) => (
														<div
															className='w-full flex items-center justify-around border-b p-1'
															key={index}
														>
															<img
																src={item?.productItem?.listDetailImages[0]}
																alt='san pham'
																className='w-20 h-20'
															/>

															<div className='flex flex-col font-calibri items-start'>
																<span className='text-base font-semibold'>
																	{item?.product?.name}
																</span>
																<span className='text-base'>
																	{item?.productItem?.color}/{item?.productItem?.size}
																</span>
																<span className='text-sm text-gray-400 font-semibold'>
																	{item?.quantity}
																</span>
															</div>
															<span className='text-sm font-semibold font-calibri'>
																{item?.productItem?.price} VND
															</span>
														</div>
													))}
												</div>
												<div className='w-full flex justify-between items-center p-2 shadow-lg'>
													<span className='text-base font-calibri'>TỔNG TIỀN:</span>
													<span className='text-lg font-calibri'>{totalPrice || 0}</span>
												</div>
												<button className='w-full bg-black text-white p-2'>
													ĐI TỚI GIỎ HÀNG
												</button>
											</div>
										</div>
										<div className='h-4 absolute left-0 right-0 p-2 z-10 hidden group-hover:block'></div>
									</div>
								</div>
							</div>
							{isOpen && token && (
								<div
									className='absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none'
									role='menu'
									aria-orientation='vertical'
									aria-labelledby='user-menu-button'
									tabIndex={-1}
								>
									<a
										className='block px-4 py-2 text-sm text-gray-700 hover:cursor-pointer hover:bg-gray-300'
										role='menuitem'
										tabIndex={-1}
										id='user-menu-item-0'
										onClick={() => navigate('/profile')}
									>
										Thông Tin Của Tôi
									</a>
									{user?.role === 'ADMIN' && (
										<a
											className='block px-4 py-2 text-sm text-gray-700 hover:cursor-pointer hover:bg-gray-300'
											role='menuitem'
											tabIndex={-1}
											id='user-menu-item-1'
											onClick={() => navigate('/admin')}
										>
											Chuyển trang quản lý
										</a>
									)}
									<a
										className='block px-4 py-2 text-sm text-gray-700 hover:cursor-pointer hover:bg-gray-300'
										role='menuitem'
										tabIndex={-1}
										id='user-menu-item-2'
										onClick={handleLogout}
									>
										Đăng Xuất
									</a>
								</div>
							)}
						</div>
					</div>
				</div>
			</nav>
		</>
	);
}

export default Header;
