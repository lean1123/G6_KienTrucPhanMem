import { useState } from 'react';
import 'font-awesome/css/font-awesome.min.css';
import { useNavigate } from 'react-router-dom';

function UpdateProfile() {
	const navigate = useNavigate();
	const [avatar, setAvatar] = useState(null);
	const [previewUrl, setPreviewUrl] = useState(null);

	const handleBackToProfile = () => {
		navigate('/profile');
	};
	const handleBackAddress = () => {
		navigate('/address');
	};

	const handleAvatarChange = (e) => {
		const file = e.target.files[0];
		if (file) {
			setAvatar(file);
			const url = URL.createObjectURL(file);
			setPreviewUrl(url);
		}
	};

	return (
		<div className='flex p-5 font-sans'>
			{/* Sidebar */}
			<div className='w-64 mr-5'>
				<div className='bg-orange-500 p-5 text-center rounded-full w-24 h-24 text-4xl text-white mx-auto flex items-center justify-center'>
					LA
				</div>
				<p className='text-center mt-3'>
					Xin chào <span className='text-blue-500 font-bold'>Lê Thanh An</span>
				</p>
				<ul className='list-none p-0 text-center mt-5 space-y-2'>
					<li className='text-blue-500'>
						<i className='fa fa-user-circle-o' aria-hidden='true'></i>{' '}
						<a href='' onClick={handleBackToProfile}>
							Thông tin tài khoản
						</a>
					</li>
					<li className='text-blue-500'>
						<i className='fa fa-list-alt' aria-hidden='true'></i>{' '}
						<a href=''>Quản lý đơn hàng</a>
					</li>
					<li className='text-blue-500'>
						<i className='fa fa-map-marker' aria-hidden='true'></i>{' '}
						<a href='' onClick={handleBackAddress}>
							Danh sách địa chỉ
						</a>
					</li>
					<li className='text-red-500'>
						<i className='fa fa-sign-out' aria-hidden='true'></i>{' '}
						<a href=''>Đăng xuất</a>
					</li>
				</ul>
			</div>

			{/* Main Content */}
			<div className='flex-1'>
				<h2 className='text-2xl font-bold mb-5'>THÔNG TIN TÀI KHOẢN</h2>

				{/* Form */}
				<form className='space-y-4'>
					{/* Avatar upload */}
					<div className='mb-4'>
						<label className='block text-sm font-semibold mb-2'>Ảnh đại diện:</label>
						<div className='flex items-center space-x-4'>
							<div className='w-24 h-24 border rounded-full overflow-hidden'>
								{previewUrl ? (
									<img
										src={previewUrl}
										alt='Avatar preview'
										className='w-full h-full object-cover'
									/>
								) : (
									<div className='w-full h-full bg-gray-200 flex items-center justify-center'>
										<span className='text-gray-500'>No image</span>
									</div>
								)}
							</div>
							<input
								type='file'
								accept='image/*'
								onChange={handleAvatarChange}
								className='hidden'
								id='avatar-input'
							/>
							<label
								htmlFor='avatar-input'
								className='bg-blue-500 text-white px-4 py-2 rounded cursor-pointer hover:bg-blue-600'
							>
								Chọn ảnh
							</label>
						</div>
					</div>

					<div>
						<label className='block text-sm font-semibold'>Họ:</label>
						<input type='text' className='w-full border p-2' placeholder='Lê' />
					</div>
					<div>
						<label className='block text-sm font-semibold'>Tên:</label>
						<input type='text' className='w-full border p-2' placeholder='Thanh An' />
					</div>

					{/* Giới tính */}
					<div>
						<label className='block text-sm font-semibold mb-2'>Giới tính:</label>
						<div className='flex space-x-4'>
							<label className='flex items-center'>
								<input type='radio' name='gender' value='male' className='mr-2' />
								Nam
							</label>
							<label className='flex items-center'>
								<input type='radio' name='gender' value='female' className='mr-2' />
								Nữ
							</label>
						</div>
					</div>

					<div>
						<label className='block text-sm font-semibold'>Số điện thoại:</label>
						<input
							type='text'
							className='w-full border p-2'
							placeholder='Số điện thoại (*)'
						/>
					</div>

					<button
						type='submit'
						className='w-full bg-blue-600 text-white py-2 mt-5 rounded'
					>
						CẬP NHẬT THÔNG TIN
					</button>
				</form>
			</div>
		</div>
	);
}

export default UpdateProfile;
