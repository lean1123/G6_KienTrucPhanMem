import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import productItemApi from '../../api/productItemApi';

export const fetchProductItem = createAsyncThunk(
	'productItem/fetchProductItem',
	async (productItemId, { rejectWithValue }) => {
		try {
			const response = await productItemApi.getProductItemById(productItemId);
			if (!response.status === 200) {
				return rejectWithValue(
					`Failed to fetch product item. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			return response.data?.data;
		} catch (error) {
			console.error('Error fetching product item: ', error);
			return rejectWithValue('Error fetching product item');
		}
	},
);

export const getProductItemByColorAndSize = createAsyncThunk(
	'productItem/getProductItemByColorAndSize',
	async ({ productId, color, size }, { rejectWithValue }) => {
		try {
			const response = await productItemApi.getProductItemByColorAndSize(
				productId,
				color,
				size,
			);
			if (!response.status === 200) {
				return rejectWithValue(
					`Failed to fetch product item. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}
			return response.data?.data;
		} catch (error) {
			console.error('Error fetching product item by color and size: ', error);
			return rejectWithValue('Error fetching product item by color and size');
		}
	},
);

const ProductItemSlice = createSlice({
	name: 'productItem',
	initialState: {
		productItem: null,
		error: null,
		loading: false,
	},
	reducers: {
		setProductItem: (state, action) => {
			state.productItem = action.payload;
		},
	},
	extraReducers: (builder) => {
		builder
			.addCase(fetchProductItem.fulfilled, (state, action) => {
				state.productItem = action.payload;
				state.loading = false;
			})
			.addCase(fetchProductItem.rejected, (state, action) => {
				state.error = action.payload;
				state.loading = false;
			})
			.addCase(getProductItemByColorAndSize.fulfilled, (state, action) => {
				if (action.payload) {
					state.productItem = action.payload;
					state.loading = false;
				}
			})
			.addCase(getProductItemByColorAndSize.rejected, (state, action) => {
				state.error = action.payload;
				state.loading = false;
			});
	},
});

export const { setProductItem } = ProductItemSlice.actions;
export default ProductItemSlice.reducer;
