import CancelIcon from '@mui/icons-material/Cancel';
import VisibilityIcon from '@mui/icons-material/Visibility';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { fetchMyOrders } from '../../hooks/order/orderSlice';
import { fetchUser } from '../../hooks/user/userSlice';
import { formatISODate } from '../../utils/dateFormat';
import orderApi from '../../api/orderApi';
import { enqueueSnackbar } from 'notistack';
import { useTranslation } from 'react-i18next';

function Profile() {
	const { t } = useTranslation();
	const navigate = useNavigate();
	const dispatch = useDispatch();
	const { user } = useSelector((state) => state.userInfo);
	const { orders } = useSelector((state) => state.order);

	useEffect(() => {
		dispatch(fetchUser());
		dispatch(fetchMyOrders());
	}, [dispatch]);

	const handleUpdateProfile = () => navigate('/updateProfile');

	const handleCancelOrder = async (orderId) => {
		try {
			const response = await orderApi.cancleOrder(orderId);
			if (response.status !== 200) {
				console.error('Failed to cancel order');
				enqueueSnackbar('Hủy đơn hàng không thành công', {
					variant: 'error',
				});
				return;
			} else {
				enqueueSnackbar('Đơn hàng đã được hủy thành công', {
					variant: 'success',
				});
				return;
			}
		} catch (error) {
			console.error('Failed to cancel order:', error);
			enqueueSnackbar('Hủy đơn hàng không thành công', {
				variant: 'error',
			});
			return;
		} finally {
			dispatch(fetchMyOrders());
		}
	};

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
					{orders?.length > 0 ? (
						<table className='w-full border-collapse'>
							<thead>
								<tr className='border-b bg-gray-100 text-gray-700'>
									<th className='p-2 text-left'>Mã đơn hàng</th>
									<th className='p-2 text-left'>Ngày đặt</th>
									<th className='p-2 text-left'>Thành tiền</th>
									<th className='p-2 text-left'>Phương thức thanh toán</th>
									<th className='p-2 text-left'>Trạng thái</th>
									<th className='p-2 text-left'>Trạng thái thanh toán</th>
									<th className='p-2 text-center'>Hành động</th>
								</tr>
							</thead>
							<tbody>
								{orders?.map((order) => (
									<tr key={order.id} className='border-b'>
										<td className='p-2'>#{order.id}</td>
										<td className='p-2'>{formatISODate(order.createdDate)}</td>
										<td className='p-2'>{order.total} đ</td>
										<td className='p-2'>{t(`payment.${order.paymentMethod}`)}</td>
										<td
											className={`p-2 ${order.status != 'CANCELLED' ? 'text-green-600' : 'text-red-600'}`}
										>
											{t(`status.${order.status}`)}
										</td>
										<td
											className={`p-2 ${order.payed ? 'text-green-600' : 'text-red-600'}`}
										>
											{order.payed ? 'Đã thanh toán' : 'Chưa thanh toán'}
										</td>
										<td className='p-2 text-center space-x-2'>
											<button
												title='Xem chi tiết'
												className='text-blue-600 hover:text-blue-800'
												onClick={() => navigate(`/order/${order.id}`)}
											>
												<VisibilityIcon />
											</button>
											{order.status === 'PENDING' && (
												<button
													title='Hủy đơn hàng'
													className='text-red-600 hover:text-red-800'
													onClick={handleCancelOrder.bind(null, order.id)}
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
