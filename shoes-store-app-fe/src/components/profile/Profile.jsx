import VisibilityIcon from '@mui/icons-material/Visibility';
import { Link } from '@mui/material';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchOrderByUserId, fetchUser } from '../../hooks/user/userSlice';
import CancelIcon from '@mui/icons-material/Cancel';
import { convertTimestampToDateTime } from '../../utils/dateFormat';

function Profile() {
	const navigate = useNavigate();
	const dispatch = useDispatch();

	const { userId } = useSelector((state) => state.persistedReducer.user);
	const { user, orders } = useSelector(
		(state) => state.persistedReducer.userInfo,
	);

	useEffect(() => {
		const fetchUserInfo = async () => {
			if (userId) {
				await dispatch(fetchUser(userId));
				await dispatch(fetchOrderByUserId(userId));
			}
		};

		fetchUserInfo();
	}, [dispatch, userId]);

	const handleUpdateProfile = () => navigate('/updateProfile');
	const handleBackAddress = () => navigate('/address');
	const handleBackProfile = () => navigate('/profile');

	return (
		<div className='flex p-5 font-sans'>
			{/* Sidebar */}
			<div className='w-64 mr-5'>
				<div className='bg-orange-500 p-5 text-center rounded-full w-24 h-24 text-4xl text-white mx-auto flex items-center justify-center'>
					{user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
				</div>
				<p className='text-center mt-3'>
					Xin chào{' '}
					<span className='text-blue-500 font-bold'>
						{user?.fullName || 'Khách'}
					</span>
				</p>
				<ul className='list-none p-0 text-center mt-5 space-y-2'>
					<li className='text-blue-500'>
						<i className='fa fa-user-circle-o' aria-hidden='true'></i>
						<a href='#' onClick={handleBackProfile}>
							Thông tin tài khoản
						</a>
					</li>
					<li className='text-blue-500'>
						<i className='fa fa-list-alt' aria-hidden='true'></i>
						<Link href='#'>Quản lý đơn hàng</Link>
					</li>
					<li className='text-blue-500'>
						<i className='fa fa-map-marker' aria-hidden='true'></i>
						<a href='#' onClick={handleBackAddress}>
							Danh sách địa chỉ
						</a>
					</li>
					<li className='text-red-500'>
						<i className='fa fa-sign-out' aria-hidden='true'></i>
						<a href='#'>Đăng xuất</a>
					</li>
				</ul>
			</div>

			{/* Main Content */}
			<div className='flex-1'>
				{/* Account Information */}
				<div className='mb-5'>
					<h2 className='text-2xl font-bold text-gray-800'>THÔNG TIN TÀI KHOẢN</h2>
					{user && (
						<>
							<p>
								<span>Họ và tên: </span>
								{user?.firstName} {user?.lastName}
							</p>
							<p>
								<span>Email: </span>
								{user.email}
							</p>

							<p>
								<span>Ngày sinh: </span>
								{user.dateOfBirth}
							</p>
							<p>
								<span>Điện thoại: </span>
								{user.phone}
							</p>
							<div className='mt-3 space-x-4'>
								<button
									onClick={handleUpdateProfile}
									className='bg-blue-500 text-white py-2 px-4 rounded font-semibold hover:bg-blue-600'
								>
									CẬP NHẬT
								</button>
							</div>
						</>
					)}
				</div>

				<div>
					<h2 className='text-2xl font-bold text-gray-800'>
						DANH SÁCH ĐƠN HÀNG GẦN ĐÂY
					</h2>
					{orders.length > 0 ? (
						<table className='w-full border-collapse mt-2'>
							<thead>
								<tr className='border-b bg-gray-100 text-gray-700'>
									<th className='p-2 text-left'>Mã đơn hàng</th>
									<th className='p-2 text-left'>Ngày đặt</th>
									<th className='p-2 text-left'>Thành tiền</th>
									<th className='p-2 text-left'>Phương thức thanh toán</th>
									<th className='p-2 text-left'>Vận chuyển</th>
									<th className='p-2 text-left'>Hành động</th>
								</tr>
							</thead>
							<tbody>
								{orders.map((order) => (
									<tr key={order?.id} className='border-b'>
										<td className='p-2'># {order?.id}</td>
										<td className='p-2'>
											{convertTimestampToDateTime(order?.createdDate)}
										</td>
										<td className='p-2'>{order?.totalPrice} đ</td>
										<td className='p-2'>{order?.paymentMethod}</td>
										<td className='p-2'>{order?.orderStatus}</td>
										<td colSpan={2} className='p-2 text-center'>
											<button
												className='text-gray-600'
												onClick={() => navigate(`/order/${order?.id}`)}
											>
												<VisibilityIcon />
											</button>
											{order?.orderStatus === 'PENDING' && (
												<button
													className='text-red-600'
													onClick={() => navigate(`/order/${order?.id}`)}
												>
													<CancelIcon />
												</button>
											)}
										</td>
									</tr>
								))}
							</tbody>
						</table>
					) : (
						<p>Không có đơn hàng nào gần đây.</p>
					)}
				</div>
			</div>
		</div>
	);
}

export default Profile;
