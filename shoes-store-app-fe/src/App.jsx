import { Route, Routes, useLocation } from 'react-router-dom';
import './App.css';

import Header from './components/header';
import Footer from './components/footer';
import LoginForm from './components/auth/LoginForm';
import SignUpForm from './components/auth/SignUpForm';
import Cart from './components/cart/Cart';
import Pay from './components/cart/pay/Pay';
import SuccessfullyPayment from './components/cart/pay/SuccessfullyPayment';
import HomePage from './components/home';
import ProductDetail from './components/product/detail/ProductDetail';
import ListAllProducts from './components/product/ListAllProducts';
import Profile from './components/profile/Profile';
import UpdateProfile from './components/profile/UpdateProfile';
import OrderDetails from './components/order/OrderDetails';
import Address from './components/profile/Address';
import ProtectedLayout from './middleware/ProtectLauout';

function App() {
	const location = useLocation();

	return (
		<div className='App'>
			{!location.pathname.includes('/login') && <Header />}

			<Routes>
				{/* Public routes */}
				<Route path='/login' element={<LoginForm />} />
				<Route path='/signup' element={<SignUpForm />} />
				<Route path='/' element={<HomePage />} />
				<Route path='/products/:id' element={<ProductDetail />} />
				<Route path='/products' element={<ListAllProducts />} />

				{/* Protected routes */}
				<Route element={<ProtectedLayout />}>
					<Route path='/cart' element={<Cart />} />
					<Route path='/pay' element={<Pay />} />
					<Route path='/profile' element={<Profile />} />
					<Route path='/updateProfile' element={<UpdateProfile />} />
					<Route path='/address' element={<Address />} />
					<Route path='/order/:orderId' element={<OrderDetails />} />
				</Route>

				{/* Kết quả thanh toán (cho cả thành công và thất bại) */}
				<Route path='/orderSuccess' element={<SuccessfullyPayment />} />
				<Route path='/orderFail' element={<SuccessfullyPayment />} />

				{/* Not found fallback */}
				<Route path='*' element={<h1>404 Not Found</h1>} />
			</Routes>

			{location.pathname !== '/cart' &&
				location.pathname !== '/cart/pay' &&
				!location.pathname.includes('/admin') && <Footer />}
		</div>
	);
}

export default App;
