import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import * as yup from 'yup';
import { login } from '../../hooks/auth/authSlice';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import '@fortawesome/fontawesome-free/css/all.min.css';
import { enqueueSnackbar } from 'notistack';
import { unwrapResult } from '@reduxjs/toolkit';
import { fetchUser } from '../../hooks/user/userSlice';

LoginForm.propTypes = {};

function LoginForm() {
	const dispatch = useDispatch();
	const navigate = useNavigate();

	const schema = yup.object().shape({
		email: yup.string().email().required(),
		password: yup.string().required(),
	});

	const form = useForm({
		defaultValues: {
			email: '',
			password: '',
		},
		resolver: yupResolver(schema),
	});

	const handleOnSubmit = async (values) => {
		try {
			const action = login(values);
			const result = await dispatch(action);
			const resultUnwrapped = unwrapResult(result);
			console.log('Result:', resultUnwrapped);

			if (resultUnwrapped?.token) {
				await dispatch(fetchUser(resultUnwrapped.userId));
				enqueueSnackbar('Đăng nhập thành công', { variant: 'success' });
				navigate('/');
				return;
			}

			enqueueSnackbar('Đăng nhập thất bại', { variant: 'error' });
			return;
		} catch (error) {
			console.log('Error: ', error);
			enqueueSnackbar('Đăng nhập thất bại', { variant: 'error' });
			return;
		}
	};

	return (
		<>
			<div className='flex justify-center p-3 text-black'>
				<h1 className='font-bold text-2xl'>Tài Khoản</h1>
			</div>
			<div className='flex justify-center'>
				<div className='boder-login mr-10 p-5'>
					<form
						className='flex-col items-start'
						onSubmit={form.handleSubmit(handleOnSubmit)}
					>
						<p className='pt-4 pb-5 text-sl font-bold'>ĐĂNG NHẬP</p>
						<p className='pb-5 text-sl font-calibri'>
							Nếu bạn có tài khoản, vui lòng đăng nhập.
						</p>
						<div className='w-full mb-4 search-container'>
							<input
								className='w-full boder no-border py-1 px-20 input-field'
								type='email'
								id='email'
								name='email'
								placeholder='Email'
								{...form.register('email')}
							/>
						</div>
						<div className='mb-4 search-container'>
							<input
								type='password'
								id='password'
								name='password'
								placeholder='Password'
								className='w-full boder no-border py-1 px-20 input-field'
								{...form.register('password')}
							/>
						</div>
						<div className='flex justify-center'>
							<button
								type='submit'
								className='bg-red py-2 px-4 text-white hover:bg-black'
							>
								Đăng Nhập
							</button>
						</div>
						<div className='flex justify-center text-sl mt-3 hover:text-red-500'>
							<button>Bạn quên mật khẩu?</button>
						</div>
					</form>
				</div>
				<div className='boder-login p-5'>
					<form
						className='flex-col items-start'
						onSubmit={form.handleSubmit(handleOnSubmit)}
					>
						<p className='pt-4 pb-5 text-sl font-bold'>BẠN LÀ KHÁCH HÀNG MỚI?</p>
						<div className='flex flex-col mt-2 text-sm'>
							<div className='font-calibri text-sl'>
								Đăng ký tài khoản trên trang web này giúp bạn theo
							</div>
							<div className='font-calibri'>
								dõi tình trạng và lịch sử đơn hàng của mình. Chúng
							</div>
							<div className='font-calibri'>
								tôi sẽ nhanh chóng tạo một tài khoản mới cho bạn.
							</div>
							<div className='font-calibri'>
								Để làm điều này,chúng tôi chỉ yêu cầu thông tin cần
							</div>
							<div className='font-calibri'>
								thiết để giúp quá trình mua hàng nhanh chóng và dễ
							</div>
							<div className='font-calibri'>dàng hơn.</div>
						</div>
						<div className='flex justify-center pt-14'>
							<button
								onClick={() => navigate('/signup')}
								type='submit'
								className='bg-red py-2 px-4 text-white hover:bg-black'
							>
								Tạo Một Tài Khoản
							</button>
						</div>
					</form>
				</div>
			</div>
		</>
	);
}

export default LoginForm;
