import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import cartApi from '../../api/cartApi';

// Fetch cart details
export const viewCart = createAsyncThunk(
	'cart/viewCart',
	async (_, { rejectWithValue }) => {
		try {
			const response = await cartApi.viewCart();

			if (response.status !== 200) {
				return rejectWithValue(
					`Failed to fetch cart. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			return response.data;
		} catch (error) {
			console.error('Error viewing cart:', error);
			return rejectWithValue('Error viewing cart');
		}
	},
);

// Add an item to the cart
export const addToCart = createAsyncThunk(
	'cart/addToCart',
	async (cartDetailRequest, { rejectWithValue }) => {
		try {
			const response = await cartApi.addToCart(cartDetailRequest);

			if (response.status !== 200) {
				return rejectWithValue(
					`Failed to add item to cart. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			return response.data;
		} catch (error) {
			console.error('Error adding item to cart:', error);
			return rejectWithValue('Error adding item to cart');
		}
	},
);

// Update the quantity of an item in the cart
export const updateQuantity = createAsyncThunk(
	'cart/updateQuantity',
	async ({ cartId, productItemId, quantity, size }, { rejectWithValue }) => {
		try {
			const response = await cartApi.updateQuantity({
				cartId,
				productItemId,
				quantity,
				size,
			});

			if (response.status !== 200) {
				return rejectWithValue(
					`Failed to update quantity. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			console.log('response of update quantity', response);

			return response.data;
		} catch (error) {
			console.error('Error updating quantity:', error);
			return rejectWithValue('Error updating quantity');
		}
	},
);

export const deleteCartDetail = createAsyncThunk(
	'cart/deleteCartDetail',
	async ({ productItemId, size }, { rejectWithValue }) => {
		try {
			console.log('Product id', productItemId);
			const response = await cartApi.deleteCartDetail(productItemId, size);

			if (response.status !== 200) {
				return rejectWithValue(
					`Failed to delete item. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			return response.data;
		} catch (error) {
			console.error('Error deleting cart item:', error);
			return rejectWithValue('Error deleting cart item');
		}
	},
);

// Clear the cart
export const clearCartFromSession = createAsyncThunk(
	'cart/clearCart',
	async (_, { rejectWithValue }) => {
		try {
			const response = await cartApi.clearCart();
			if (response.status !== 200) {
				return rejectWithValue(
					`Failed to delete item. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			return response.data;
		} catch (error) {
			console.error('Error deleting cart item:', error);
			return rejectWithValue('Error deleting cart item');
		}
	},
);

const cartSlice = createSlice({
	name: 'cart',
	initialState: {
		cartItems: [],
		totalPrice: 0,
		totalQuantity: 0,
		loading: false,
		error: null,
	},
	reducers: {
		clearCart: (state) => {
			state.cartItems = [];
			state.totalPrice = 0;
			state.totalQuantity = 0;
		},
		setTotalPrice: (state) => {
			state.totalPrice = state.cartItems.reduce((total, item) => {
				return (total += item.productItem.price * item.quantity);
			}, 0);
		},
		cartInitializeState: () => {
			return {
				cartItems: [],
				totalPrice: 0,
				totalQuantity: 0,
				loading: false,
				error: null,
			};
		},
	},
	extraReducers: (builder) => {
		builder
			.addCase(viewCart.pending, (state) => {
				state.loading = true;
				state.error = null;
			})
			.addCase(viewCart.fulfilled, (state, action) => {
				state.loading = false;
				state.cartItems = action.payload.data;
				state.totalPrice = action.payload.data.reduce((total, item) => {
					return (total += item.productItem.price * item.quantity);
				}, 0);
				state.totalQuantity = action.payload.data.length;
			})
			.addCase(viewCart.rejected, (state, action) => {
				state.loading = false;
				state.error = action.payload;
				state.cartItems = [];
				state.totalPrice = 0;
				state.totalQuantity = 0;
			})
			.addCase(addToCart.fulfilled, (state, action) => {
				state.cartItems = action.payload.data;
			})
			.addCase(updateQuantity.fulfilled, (state, action) => {
				state.cartItems = action.payload.data;
			})
			.addCase(deleteCartDetail.fulfilled, (state, action) => {
				state.cartItems = action.payload.data;
			})
			.addCase(clearCartFromSession.fulfilled, (state) => {
				state.cartItems = [];
				state.totalPrice = 0;
				state.totalQuantity = 0;
			})
			.addCase(clearCartFromSession.rejected, (state, action) => {
				state.error = action.payload;
			});
	},
});

export const { clearCart, cartInitializeState } = cartSlice.actions;

export const cartReducer = cartSlice.reducer;
