import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
	addAddress,
	fetchAddress,
	updateAddress,
} from '../../hooks/user/userSlice';



function AddressForm({ initialData, onCancel }) {
	const dispatch = useDispatch();
	const { userId } = useSelector((state) => state.persistedReducer.user);
	const [formData, setFormData] = useState({
		...(initialData.id && { id: initialData.id }),
		homeNumber: initialData.homeNumber || '',
		street: initialData.street || '',
		ward: initialData.ward || '',
		district: initialData.district || '',
		city: initialData.city || '',
	});

	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData((prev) => ({
			...prev,
			[name]: value,
		}));
	};

	const validateForm = (data) => {
		const errors = [];
		if (!data.homeNumber?.trim()) errors.push('Vui lòng nhập số nhà');
		if (!data.street?.trim()) errors.push('Vui lòng nhập tên đường');
		if (!data.ward?.trim()) errors.push('Vui lòng nhập phường/xã');
		if (!data.district?.trim()) errors.push('Vui lòng nhập quận/huyện');
		if (!data.city?.trim()) errors.push('Vui lòng nhập tỉnh/thành phố');
		return errors;
	};

	const handleSubmit = async (e) => {
		e.preventDefault();

		// Validate form trước khi gửi
		const errors = validateForm(formData);
		if (errors.length > 0) {
			alert(errors.join('\n'));
			return;
		}

		try {
			const addressData = {
				homeNumber: formData.homeNumber.trim(),
				street: formData.street.trim(),
				ward: formData.ward.trim(),
				district: formData.district.trim(),
				city: formData.city.trim(),
			};

			console.log('Submitting address data:', addressData);

			if (initialData.id) {
				await dispatch(
					updateAddress({
						userId,
						addressId: initialData.id,
						addressData,
					}),
				).unwrap();
			} else {
				await dispatch(
					addAddress({
						userId,
						addressData,
					}),
				).unwrap();
			}

			await dispatch(fetchAddress(userId));
			onCancel();
		} catch (error) {
			console.error('Form submission error:', error);
			// Hiển thị thông báo lỗi cụ thể từ backend nếu có
			const errorMessage =
				error.response?.data?.message || 'Có lỗi xảy ra khi lưu địa chỉ';
			alert(errorMessage);
		}
	};

	return (
		<form onSubmit={handleSubmit} className='space-y-4'>
			<h3 className='text-xl font-bold mb-4'>
				{initialData.id ? 'Sửa địa chỉ' : 'Thêm địa chỉ mới'}
			</h3>
			<div>
				<label className='block mb-1'>Số nhà:</label>
				<input
					type='text'
					name='homeNumber'
					value={formData.homeNumber}
					onChange={handleChange}
					className='w-full p-2 border rounded'
					required
				/>
			</div>

			<div>
				<label className='block mb-1'>Đường:</label>
				<input
					type='text'
					name='street'
					value={formData.street}
					onChange={handleChange}
					className='w-full p-2 border rounded'
					required
				/>
			</div>

			<div>
				<label className='block mb-1'>Phường/Xã:</label>
				<input
					type='text'
					name='ward'
					value={formData.ward}
					onChange={handleChange}
					className='w-full p-2 border rounded'
					required
				/>
			</div>

			<div>
				<label className='block mb-1'>Quận/Huyện:</label>
				<input
					type='text'
					name='district'
					value={formData.district}
					onChange={handleChange}
					className='w-full p-2 border rounded'
					required
				/>
			</div>

			<div>
				<label className='block mb-1'>Tỉnh/Thành phố:</label>
				<input
					type='text'
					name='city'
					value={formData.city}
					onChange={handleChange}
					className='w-full p-2 border rounded'
					required
				/>
			</div>

			<div className='flex space-x-4'>
				<button
					type='submit'
					className='bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600'
				>
					{initialData.id ? 'Cập nhật' : 'Lưu địa chỉ'}
				</button>
				<button
					type='button'
					onClick={onCancel}
					className='bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600'
				>
					Hủy
				</button>
			</div>
		</form>
	);
}

export default AddressForm;
