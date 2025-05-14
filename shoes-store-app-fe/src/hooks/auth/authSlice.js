import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import AuthAPI from '../../api/authApi';

export const login = createAsyncThunk(
	'/login',
	async (payload, { rejectWithValue }) => {
		try {
			const response = await AuthAPI.login(payload);

			if (response.status.valueOf() !== 200) return rejectWithValue(response.data);

			const accessToken = response.data.result.token;
			const refreshToken = response.data.result.refreshToken;

			localStorage.setItem('accessToken', accessToken);
			localStorage.setItem('refreshToken', refreshToken);

			return response.data.result;
		} catch (error) {
			console.error('Error in login', error);
			return rejectWithValue(error);
		}
	},
);

export const register = createAsyncThunk(
	'/register',
	async (payload, { rejectWithValue }) => {
		try {
			const response = await AuthAPI.register(payload);

			if (response.status.valueOf() !== 200) {
				return rejectWithValue(response.data);
			}

			return response.data.result;
		} catch (error) {
			console.error('Error in register', error);
			return rejectWithValue(error);
		}
	},
);

export const logout = createAsyncThunk(
	'/logout',
	async (_, { rejectWithValue }) => {
		try {
			const accessToken = localStorage.getItem('accessToken');
			const refreshToken = localStorage.getItem('refreshToken');
			if (!accessToken || !refreshToken) {
				return rejectWithValue('No access token or refresh token found');
			}
			const response = await AuthAPI.logout(accessToken, refreshToken);

			if (response.status.valueOf() !== 200) {
				return rejectWithValue(response.data);
			}

			return response.data;
		} catch (error) {
			// console.error('Error in logout', error);
			return rejectWithValue(error);
		}
	},
);

export const loginWithGoogle = createAsyncThunk(
	'/loginWithGoogle',
	async (_, { rejectWithValue }) => {
		try {
			const response = await AuthAPI.loginWithGoogle();

			if (response.status.valueOf() !== 200) {
				return rejectWithValue(response.data);
			}

			return response.data.result;
		} catch (error) {
			// console.error('Error in loginWithGoogle', error);
			return rejectWithValue(error);
		}
	},
);

export const loginSocialCallback = createAsyncThunk(
	'/loginSocialCallback',
	async (payload, { rejectWithValue }) => {
		try {
			const response = await AuthAPI.loginSocialCallback(
				payload.provider,
				payload.code,
			);

			if (response.status.valueOf() !== 200) {
				return rejectWithValue(response.data);
			}
			if (response.data.code === 503) {
				return rejectWithValue(response.data);
			}
			const accessToken = response.data.result.token;
			const refreshToken = response.data.result.refreshToken;

			localStorage.setItem('accessToken', accessToken);
			localStorage.setItem('refreshToken', refreshToken);
			return response.data.result;
		} catch (error) {
			// console.error('Error in loginSocialCallback', error);
			return rejectWithValue(error);
		}
	},
);

const AuthSlice = createSlice({
	name: 'user',
	initialState: {
		accessToken: null,
		userId: null,
	},
	reducers: {
		setAccessToken: (state, action) => {
			state.accessToken = action.payload;
		},
		setUserId: (state, action) => {
			state.userId = action.payload;
		},
		authInitialState: () => {
			return {
				accessToken: null,
				userId: null,
			};
		},
	},
	extraReducers: (builder) => {
		builder
			.addCase(register.fulfilled, (state, action) => {
				state.accessToken = action.payload.token;
				state.userId = action.payload.userId;
			})
			.addCase(login.fulfilled, (state, action) => {
				state.accessToken = action.payload.token;
				state.userId = action.payload.userId;
			})
			.addCase(register.rejected, (state, action) => {
				console.error('Error in register', action.payload);
			})
			.addCase(login.rejected, (state, action) => {
				console.error('Error in login', action.payload);
			})
			.addCase(logout.fulfilled, (state) => {
				state.accessToken = null;
				state.userId = null;
			})
			.addCase(logout.rejected, (state, action) => {
				state.accessToken = null;
				state.userId = null;
				console.error('Error in logout', action.payload);
			})
			.addCase(loginSocialCallback.fulfilled, (state, action) => {
				state.accessToken = action.payload.token;
				state.userId = action.payload.userId;
			})
			.addCase(loginSocialCallback.rejected, (state) => {
				state.accessToken = null;
				state.userId = null;
			});
	},
});

export const { setAccessToken, setUserId, authInitialState } =
	AuthSlice.actions;
export default AuthSlice.reducer;
