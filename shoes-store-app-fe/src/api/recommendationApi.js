import AdminAxiosClient from './axiosClient';

const recommendationApi = {
	getRecommendationProducts: async (userId) => {
		return AdminAxiosClient.get('/recommend', {
			params: {
				userId,
			},
			withCredentials: true,
		});
	},
};

export default recommendationApi;
