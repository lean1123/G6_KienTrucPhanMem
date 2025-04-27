import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { loginSocialCallback } from '../../hooks/auth/authSlice';
import { unwrapResult } from '@reduxjs/toolkit';
import { fetchUser } from '../../hooks/user/userSlice';
import { enqueueSnackbar } from 'notistack';

const GoogleLoginCallBack = () => {
	const [params] = useSearchParams(); // Sửa đúng cú pháp
	const router = useNavigate();
	const dispatch = useDispatch();

	useEffect(() => {
		const handleVerifyCode = async () => {
			const code = params.get('code');
			const error = params.get('error');

			if (error) {
				console.log('Error:', error);
				router('/login');
				return;
			}

			if (code) {
				try {
					// debugger;
					const action = loginSocialCallback({ provider: 'google', code }); // Payload đúng dạng object
					const result = await dispatch(action);
					const resultUnwrapped = unwrapResult(result);
					console.log('Result:', resultUnwrapped);
					if (resultUnwrapped?.token) {
						await dispatch(fetchUser(resultUnwrapped.userId));
						enqueueSnackbar('Đăng nhập thành công', { variant: 'success' });
						router('/');
						return;
					}

					enqueueSnackbar('Đăng nhập thất bại', { variant: 'error' });
					return;
				} catch (error) {
					// console.error('Login social callback error:', error);
					router('/login');
				}
			}
		};

		handleVerifyCode();
	}, [params, dispatch, router]);

	return <div>Đang đăng nhập Google...</div>;
};

export default GoogleLoginCallBack;
