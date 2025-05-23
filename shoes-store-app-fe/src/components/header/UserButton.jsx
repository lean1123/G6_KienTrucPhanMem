import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import { authInitialState, logout } from '../../hooks/auth/authSlice';
import { bannerShowInitialState } from '../../hooks/bannerStore';
import { filterInitialState } from '../../hooks/filter/filterSlice';
import { orderInitialState } from '../../hooks/order/orderSlice';
import { initialOrderStep } from '../../hooks/orderProgressStore';
import { productItemInitialState } from '../../hooks/product/productItemSlice';
import { persistor } from '../../hooks/redux/store';

import { chatInitialize } from '../../hooks/chat/messageSlice';
import { userInitialState } from '../../hooks/user/userSlice';

function UserButton() {
	const [isOpen, setIsOpen] = useState(false);
	const { user } = useSelector((state) => state.userInfo);
	const token = useSelector((state) => state?.user?.accessToken);
	const navigate = useNavigate();
	const dispatch = useDispatch();
	useEffect(() => {
		if (token) {
			setIsOpen(false);
		}
	}, [token]);

	const handleLogout = async () => {
		localStorage.clear();

		await persistor.purge();
		const action = logout();
		dispatch(action);
		dispatch(authInitialState());
		dispatch(userInitialState());
		dispatch(productItemInitialState());
		dispatch(filterInitialState());
		dispatch(bannerShowInitialState());
		dispatch(initialOrderStep());
		dispatch(orderInitialState());
		dispatch(chatInitialize());

		setIsOpen(false);
		navigate('/');
	};
	return (
		<div>
			<div className='flex items-center'>
				<button
					onClick={() => {
						token ? setIsOpen(!isOpen) : navigate('/login');
					}}
					className='py-2 rounded-md text-sm font-medium'
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
				<p className='ml-1 font-semibold'>
					{user && user?.firstName + ' ' + user?.lastName}
				</p>
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
						onClick={() => {
							setIsOpen(false);
							navigate('/profile');
						}}
					>
						Thông Tin Của Tôi
					</a>

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
	);
}

export default UserButton;
