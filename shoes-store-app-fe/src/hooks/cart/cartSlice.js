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

			console.log('response of view cart', response);
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
	async ({ cartId, productId, quantity }, { rejectWithValue }) => {
		try {
			const response = await cartApi.updateQuantity({
				cartId,
				productId,
				quantity,
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
	async (productId, { rejectWithValue }) => {
		try {
			console.log('Product id', productId);
			const response = await cartApi.deleteCartDetail(productId);

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
			});
	},
});

export const { clearCart } = cartSlice.actions;

export const cartReducer = cartSlice.reducer;
