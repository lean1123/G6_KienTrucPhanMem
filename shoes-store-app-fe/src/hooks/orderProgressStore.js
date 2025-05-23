import { createSlice } from '@reduxjs/toolkit';

const orderProgressStep = createSlice({
	name: 'bannerShow',
	initialState: {
		progressSteps: [],
	},
	reducers: {
		setProgress: (state, action) => {
			state.progressSteps = [...state.progressSteps, action.payload];
		},

		initialOrderStep: () => {
			return {
				progressSteps: [],
			};
		},
	},
});

export const { setProgress, initialOrderStep } = orderProgressStep.actions;
export const orderProgressReducer = orderProgressStep.reducer;
