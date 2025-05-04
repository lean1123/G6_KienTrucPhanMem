import AdminAxiosClient from './axiosClient';

const ChatApi = {
	sendMessage: async (message) => {
		return AdminAxiosClient.post(
			'/chat/analyze',
			{ message },
			{ withCredentials: true },
		);
	},
};

export default ChatApi;
