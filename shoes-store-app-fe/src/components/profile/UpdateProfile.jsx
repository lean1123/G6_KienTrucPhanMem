import { yupResolver } from '@hookform/resolvers/yup';
import { unwrapResult } from '@reduxjs/toolkit';
import 'font-awesome/css/font-awesome.min.css';
import { enqueueSnackbar } from 'notistack';
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import * as yup from 'yup';
import { fetchUser, updateUserInfo } from '../../hooks/user/userSlice';
import { subYears } from 'date-fns';
import dayjs from 'dayjs';

function UpdateProfile() {
	const navigate = useNavigate();
	const [avatar, setAvatar] = useState(null);
	const [previewUrl, setPreviewUrl] = useState(null);

	const handleBackProfile = () => navigate('/profile');
	const handleBackAddress = () => navigate('/address');

	const handleAvatarChange = (e) => {
		const file = e.target.files[0];
		if (file) {
			setAvatar(file);
			setPreviewUrl(URL.createObjectURL(file));
		}
	};

	useEffect(() => {
		dispatch(fetchUser());
	}, []);

	const { user } = useSelector((state) => state.userInfo);
	const dispatch = useDispatch();
	const todayMinus10Years = subYears(new Date(), 10);

	const updateInfo = yup.object().shape({
		firstName: yup.string().required('Tên đầu là bắt buộc'),
		lastName: yup.string().required('Tên cuối là bắt buộc'),
		phone: yup
			.string()
			.matches(
				/^[0-9]{10}$/,
				'Số điện thoại hợp lệ phải có 10 chữ số: (vd: 0123456789)',
			)
			.required('Số điện thoại là bắt buộc'),
		gender: yup
			.string()
			.oneOf(['MALE', 'FEMALE'], 'Giới tính không hợp lệ')
			.required('Giới tính là bắt buộc'),
		dob: yup
			.date()
			.typeError('Ngày sinh không hợp lệ')
			.required('Ngày sinh là bắt buộc')
			.max(todayMinus10Years, 'Bạn phải từ 10 tuổi trở lên'),
	});

	const {
		register,
		handleSubmit,
		formState: { errors },
	} = useForm({
		defaultValues: {
			firstName: user?.firstName || '',
			lastName: user?.lastName || '',
			phone: user?.phone || '',
			gender: user?.gender || 'FEMALE', // set mặc định nếu muốn
			dob: user?.dob || '',
		},
		resolver: yupResolver(updateInfo),
	});

	const onSubmit = async (data) => {
		try {
			const formData = new FormData();
			formData.append('firstName', data.firstName);
			formData.append('lastName', data.lastName);
			formData.append('phone', data.phone);
			formData.append('gender', data.gender);
			formData.append('dob', dayjs(data.dob).format('YYYY-MM-DD'));

			console.log('formData', formData.get('dob'));

			const result = await dispatch(
				updateUserInfo({ userId: user?.id, userData: formData }),
			);
			const dataResult = unwrapResult(result);
			console.log('dataResult', dataResult);

			if (dataResult === null || dataResult === undefined) {
				console.error('Failed to update profile');
				enqueueSnackbar(`Cập nhật thất bại!`, { variant: 'error' });
				return;
			}

			if (dataResult?.status === 503) {
				console.error('User Service is not available');
				enqueueSnackbar('User Service is not available. Please try again later.', {
					variant: 'error',
				});
				return;
			}

			enqueueSnackbar('Cập nhật thành công', { variant: 'success' });
			navigate('/profile');
			return;
		} catch (error) {
			console.error('Failed to update profile:', error);
			enqueueSnackbar(
				`Cập nhật thất bại: ${error?.response?.data?.message || 'Lỗi từ máy chủ'}`,
				{ variant: 'error' },
			);
			return;
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
						<a onClick={handleBackProfile}>Thông tin tài khoản</a>
					</li>
					<li className='text-blue-500'>
						<i className='fa fa-list-alt' aria-hidden='true'></i>{' '}
						<a>Quản lý đơn hàng</a>
					</li>
					<li className='text-blue-500'>
						<i className='fa fa-map-marker' aria-hidden='true'></i>{' '}
						<a onClick={handleBackAddress}>Danh sách địa chỉ</a>
					</li>
					<li className='text-red-500'>
						<i className='fa fa-sign-out' aria-hidden='true'></i> <a>Đăng xuất</a>
					</li>
				</ul>
			</div>

			{/* Main Content */}
			<div className='flex-1'>
				<h2 className='text-2xl font-bold mb-5'>THÔNG TIN TÀI KHOẢN</h2>
				<form
					className='space-y-4'
					onSubmit={handleSubmit(onSubmit)}
					encType='multipart/form-data'
				>
					{/* <div className='mb-4'>
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
					</div> */}
					<div className='flex items-center pt-3'>
						<label htmlFor='firstName' className='ml-5 font-bold'>
							Tên đầu:
						</label>
						<div className='ml-8 mb-4 search-container w-3/5'>
							<input
								className='w-full boder no-border py-1 input-field'
								type='text'
								id='firstName'
								{...register('firstName')}
							/>
							{errors.firstName && (
								<p className='text-red-500 text-sm'>{errors.firstName.message}</p>
							)}
						</div>
					</div>
					<div className='flex items-center pt-3'>
						<label htmlFor='lastName' className='ml-5 font-bold'>
							Tên cuối:
						</label>
						<div className='ml-8 mb-4 search-container w-3/5'>
							<input
								className='w-full boder no-border py-1 input-field'
								type='text'
								id='lastName'
								{...register('lastName')}
							/>
							{errors.lastName && (
								<p className='text-red-500 text-sm'>{errors.lastName.message}</p>
							)}
						</div>
					</div>
					<div className='flex items-center pt-3'>
						<label htmlFor='phone' className='ml-5 font-bold'>
							Số điện thoại:
						</label>
						<div className='ml-8 mb-4 search-container w-3/5'>
							<input
								className='w-full boder no-border py-1 input-field'
								type='text'
								id='phone'
								{...register('phone')}
								readOnly={user?.phone ? true : false}
							/>
							{errors.phone && (
								<p className='text-red-500 text-sm'>{errors.phone.message}</p>
							)}
						</div>
					</div>
					<div className='flex items-center pt-3'>
						<label className='ml-5 font-bold'>Giới tính:</label>
						<div className='ml-8 mb-4 w-3/5'>
							<div className='flex items-center mb-4'>
								<input
									id='default-radio-1'
									type='radio'
									value='MALE'
									name='gender'
									className='w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 
										dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600'
									{...register('gender')}
								/>
								<label
									htmlFor='default-radio-1'
									className='ms-2 text-sm font-medium text-gray-900 dark:text-gray-300'
								>
									Nam
								</label>
							</div>
							<div className='flex items-center'>
								<input
									id='default-radio-2'
									type='radio'
									value='FEMALE'
									name='gender'
									className='w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 
										dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600'
									{...register('gender')}
								/>
								<label
									htmlFor='default-radio-2'
									className='ms-2 text-sm font-medium text-gray-900 dark:text-gray-300'
								>
									Nữ
								</label>
							</div>
							{errors.gender && (
								<p className='text-red-500 text-sm'>{errors.gender.message}</p>
							)}
						</div>
					</div>
					<div className='flex items-center pt-3'>
						<label htmlFor='dob' className='ml-5 font-bold'>
							Ngày sinh:
						</label>
						<div className='ml-8 mb-4 search-container w-3/5'>
							<input
								className='w-full boder no-border py-1 input-field'
								type='date'
								id='dob'
								{...register('dob')}
							/>
							{errors.dob && (
								<p className='text-red-500 text-sm'>{errors.dob.message}</p>
							)}
						</div>
					</div>
					<button
						type='submit'
						className='w-full bg-blue-600 text-white py-2 mt-5 rounded'
					>
						Cập nhật thông tin
					</button>
				</form>
			</div>
		</div>
	);
}

export default UpdateProfile;
