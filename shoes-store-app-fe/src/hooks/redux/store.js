import { combineReducers, configureStore } from '@reduxjs/toolkit';
import { persistReducer, persistStore } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

import userReducer from '../auth/authSlice';
import productItemReducer from '../product/productItemSlice';
import { cartReducer } from '../cart/cartSlice';
import { userInfoReducer } from '../user/userSlice';
import { orderReducer } from '../order/orderSlice';
import { filterReducer } from '../filter/filterSlice';
import { bannerReducer } from '../bannerStore';
import { orderProgressReducer } from '../orderProgressStore';

const rootReducer = combineReducers({
	user: userReducer,
	productItem: productItemReducer,
	cart: cartReducer,
	userInfo: userInfoReducer,
	order: orderReducer,
	filter: filterReducer,
	bannerShow: bannerReducer,
	orderStep: orderProgressReducer,
});

const persistConfig = {
	key: 'root',
	storage,
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
	reducer: persistedReducer,
	middleware: (getDefaultMiddleware) =>
		getDefaultMiddleware({
			serializableCheck: false,
		}),
});

export const persistor = persistStore(store);
