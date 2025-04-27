import CancelIcon from '@mui/icons-material/Cancel';
import VisibilityIcon from '@mui/icons-material/Visibility';
import { Link } from 'react-router-dom';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { fetchUser } from '../../hooks/user/userSlice';
import { convertTimestampToDateTime } from '../../utils/dateFormat';

function Profile() {
	const navigate = useNavigate();
	const dispatch = useDispatch();
	const { user, orders } = useSelector((state) => state.userInfo);

	useEffect(() => {
		dispatch(fetchUser());
	}, [dispatch]);

	const handleUpdateProfile = () => navigate('/updateProfile');

	return (
		<div className='flex p-5 font-sans'>
			{/* Sidebar */}
			<div className='w-64 mr-5'>
				<div className='bg-orange-500 p-5 text-center rounded-full w-24 h-24 text-4xl text-white mx-auto flex items-center justify-center'>
					{user?.fullName?.charAt(0).toUpperCase() || 'U'}
				</div>
				<p className='text-center mt-3'>
					Xin chào{' '}
					<span className='text-blue-500 font-bold'>
						{user?.fullName || 'Khách'}
					</span>
				</p>
				<ul className='list-none p-0 text-center mt-5 space-y-4'>
					<li>
						<Link to='/profile' className='text-blue-500 hover:underline'>
							<i className='fa fa-user-circle-o mr-2' aria-hidden='true'></i>
							Thông tin tài khoản
						</Link>
					</li>
					<li>
						<Link to='/orders' className='text-blue-500 hover:underline'>
							<i className='fa fa-list-alt mr-2' aria-hidden='true'></i>
							Quản lý đơn hàng
						</Link>
					</li>
					<li>
						<Link to='/address' className='text-blue-500 hover:underline'>
							<i className='fa fa-map-marker mr-2' aria-hidden='true'></i>
							Danh sách địa chỉ
						</Link>
					</li>
					<li>
						<Link to='/logout' className='text-red-500 hover:underline'>
							<i className='fa fa-sign-out mr-2' aria-hidden='true'></i>
							Đăng xuất
						</Link>
					</li>
				</ul>
			</div>

			{/* Main Content */}
			<div className='flex-1'>
				{/* Account Information */}
				<div className='mb-10'>
					<h2 className='text-2xl font-bold text-gray-800 mb-4'>
						THÔNG TIN TÀI KHOẢN
					</h2>
					{user && (
						<div className='space-y-2'>
							<p>
								<strong>Họ và tên:</strong> {user.firstName} {user.lastName}
							</p>
							<p>
								<strong>Email:</strong> {user.email}
							</p>
							<p>
								<strong>Ngày sinh:</strong> {user.dob}
							</p>
							<p>
								<strong>Điện thoại:</strong> {user.phone}
							</p>
							<div className='mt-4'>
								<button
									onClick={handleUpdateProfile}
									className='bg-blue-500 text-white py-2 px-6 rounded hover:bg-blue-600 transition-all'
								>
									Cập nhật
								</button>
							</div>
						</div>
					)}
				</div>

				{/* Recent Orders */}
				<div>
					<h2 className='text-2xl font-bold text-gray-800 mb-4'>
						DANH SÁCH ĐƠN HÀNG GẦN ĐÂY
					</h2>
					{orders.length > 0 ? (
						<table className='w-full border-collapse'>
							<thead>
								<tr className='border-b bg-gray-100 text-gray-700'>
									<th className='p-2 text-left'>Mã đơn hàng</th>
									<th className='p-2 text-left'>Ngày đặt</th>
									<th className='p-2 text-left'>Thành tiền</th>
									<th className='p-2 text-left'>Phương thức thanh toán</th>
									<th className='p-2 text-left'>Trạng thái</th>
									<th className='p-2 text-center'>Hành động</th>
								</tr>
							</thead>
							<tbody>
								{orders.map((order) => (
									<tr key={order.id} className='border-b'>
										<td className='p-2'>#{order.id}</td>
										<td className='p-2'>
											{convertTimestampToDateTime(order.createdDate)}
										</td>
										<td className='p-2'>{order.totalPrice} đ</td>
										<td className='p-2'>{order.paymentMethod}</td>
										<td className='p-2'>{order.orderStatus}</td>
										<td className='p-2 text-center space-x-2'>
											<button
												title='Xem chi tiết'
												className='text-blue-600 hover:text-blue-800'
												onClick={() => navigate(`/order/${order.id}`)}
											>
												<VisibilityIcon />
											</button>
											{order.orderStatus === 'PENDING' && (
												<button
													title='Hủy đơn hàng'
													className='text-red-600 hover:text-red-800'
													onClick={() => navigate(`/order/${order.id}`)}
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
						<p className='text-gray-600'>Không có đơn hàng nào gần đây.</p>
					)}
				</div>
			</div>
		</div>
	);
}

export default Profile;
