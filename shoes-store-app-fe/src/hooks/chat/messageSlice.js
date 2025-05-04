import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import ChatApi from '../../api/chatApi';

export const sendMessage = createAsyncThunk(
	'chat/analyze',
	async (message, { rejectWithValue }) => {
		try {
			const response = await ChatApi.sendMessage(message);

			console.log('response', response);

			if (response.status !== 200 || !Array.isArray(response.data)) {
				return rejectWithValue(
					`Failed to send message. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			if (response.data.code === 503) {
				return rejectWithValue(
					`Failed to send message. Status: ${response.status}, Message: ${response.statusText}`,
				);
			}

			const data = response?.data;

			return {
				result: data,
				isAI: true,
			};
		} catch (error) {
			console.error('Error sending message:', error);
			return rejectWithValue('Error sending message');
		}
	},
);

const messageSlice = createSlice({
	name: 'chat',
	initialState: {
		messages: [],
		current: {},
		error: null,
	},
	reducers: {
		setCurrent: (state, action) => {
			state.current = action.payload;
		},
		chatInitialize: () => {
			return {
				messages: [],
				current: {},
				error: null,
			};
		},
		addMessage: (state, action) => {
			state.messages.push(action.payload);
		},
	},
	extraReducers: (builder) => {
		builder
			.addCase(sendMessage.pending, (state) => {
				state.messages.push({ isAI: true, isLoading: true });
			})
			.addCase(sendMessage.fulfilled, (state, action) => {
				state.messages = state.messages.filter((msg) => !msg.isLoading);
				state.messages.push(action.payload);
			})
			.addCase(sendMessage.rejected, (state, action) => {
				state.error = action.payload;
			});
	},
});

export const { setCurrent, chatInitialize, addMessage } = messageSlice.actions;
export const messageReducer = messageSlice.reducer;
