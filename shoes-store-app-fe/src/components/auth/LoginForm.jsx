import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import * as yup from 'yup';
import { login, loginWithGoogle } from '../../hooks/auth/authSlice';
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
				await dispatch(fetchUser());
				enqueueSnackbar('Đăng nhập thành công', { variant: 'success' });
				navigate('/');
				return;
			}

			enqueueSnackbar('Đăng nhập thất bại', { variant: 'error' });
			return;
		} catch (error) {
			console.error('Error in Login Form: ', error);
			enqueueSnackbar(`Đăng nhập thất bại: ${error?.response?.data?.message}`, {
				variant: 'error',
			});
			return;
		}
	};

	const handleGoogleLogin = async () => {
		try {
			const action = loginWithGoogle();
			const result = await dispatch(action);
			const resultUnwrapped = unwrapResult(result);
			if (resultUnwrapped) {
				console.log(resultUnwrapped);
				window.location.href = resultUnwrapped;
				return;
			}
		} catch (error) {
			console.error('Error in loginWithGoogle', error);
			enqueueSnackbar('Đăng nhập thất bại', { variant: 'error' });
			return;
		}
	};

	return (
		<div className='py-4'>
			<div className='flex justify-center'>
				<div className='bg-white shadow-lg rounded-lg p-6 mr-10 w-[400px]'>
					<form
						className='flex-col items-start'
						onSubmit={form.handleSubmit(handleOnSubmit)}
					>
						<p className='pt-4 pb-5 text-sl font-bold text-center'>ĐĂNG NHẬP</p>
						<p className='pb-5 text-sl font-calibri text-center'>
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
						<div className='flex flex-col justify-center items-center'>
							<button
								type='submit'
								className='bg-blue-600 hover:bg-blue-700 text-white font-semibold px-4 py-2 rounded-md shadow mb-2 w-full'
							>
								Đăng Nhập
							</button>
							<button
								type='button'
								className='bg-blue-600 hover:bg-blue-700 text-white font-semibold px-4 py-2 rounded-md shadow w-full'
								onClick={handleGoogleLogin}
							>
								Đăng Nhập Với Google
							</button>
						</div>
					</form>
				</div>
				<div className='bg-white shadow-lg rounded-lg p-6 mr-10 w-[400px]'>
					<form
						className='flex-col items-start'
						onSubmit={form.handleSubmit(handleOnSubmit)}
					>
						<p className='pb-5 text-sl font-bold text-center'>
							BẠN LÀ KHÁCH HÀNG MỚI?
						</p>
						<div className='flex flex-col items-center'>
							<div className='text-justify font-calibri text-sl max-w-[300px]'>
								<p>
									Đăng ký tài khoản trên trang web này giúp bạn theo dõi tình trạng và
									lịch sử đơn hàng của mình.
								</p>
								<p>Chúng tôi sẽ nhanh chóng tạo một tài khoản mới cho bạn.</p>
								<p>
									Để làm điều này, chúng tôi chỉ yêu cầu thông tin cần thiết để giúp quá
									trình mua hàng nhanh chóng và dễ dàng hơn.
								</p>
							</div>
						</div>
						<div className='flex justify-center pt-4'>
							<button
								onClick={() => navigate('/signup')}
								type='submit'
								className='bg-blue-600 hover:bg-blue-700 text-white font-semibold px-4 py-2 rounded-md shadow'
							>
								Tạo Một Tài Khoản
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	);
}

export default LoginForm;
