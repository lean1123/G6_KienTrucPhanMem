import { createSlice } from '@reduxjs/toolkit';

const bannerSlice = createSlice({
	name: 'bannerShow',
	initialState: {
		isBannerShow: true,
	},
	reducers: {
		setBannerShow: (state, action) => {
			state.isBannerShow = action.payload;
		},
		bannerShowInitialState: () => {
			return {
				isBannerShow: true,
			};
		},
	},
});

export const { setBannerShow, bannerShowInitialState } = bannerSlice.actions;
export const bannerReducer = bannerSlice.reducer;
