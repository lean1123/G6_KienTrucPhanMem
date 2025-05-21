import { Route, Routes } from 'react-router-dom';
import './App.css';

import LoginForm from './components/auth/LoginForm';
import SignUpForm from './components/auth/SignUpForm';
import Cart from './components/cart/Cart';
import Pay from './components/cart/pay/Pay';
import SuccessfullyPayment from './components/cart/pay/SuccessfullyPayment';
import Footer from './components/footer';
import Header from './components/header';
import HomePage from './components/home';
import OrderDetails from './components/order/OrderDetails';
import ProductDetail from './components/product/detail/ProductDetail';
import ListAllProducts from './components/product/ListAllProducts';
import Address from './components/profile/Address';
import Profile from './components/profile/Profile';
import UpdateProfile from './components/profile/UpdateProfile';
import ProtectedLayout from './middleware/ProtectLauout';
import VNPayOrderResult from './components/cart/pay/VNPayOrderResult';
import GoogleLoginCallBack from './components/auth/GoogleLoginCallBack';
import ChatControll from './components/chat';
import About from './components/about';

function App() {
	return (
		<div className='App'>
			<Header />

			<main className='py-32 bg-gradient-to-b from-[#e3f2fd] to-white'>
				<Routes>
					<Route path='/login' element={<LoginForm />} />
					<Route path='/signup' element={<SignUpForm />} />
					<Route path='/' element={<HomePage />} />
					<Route path='/products/:id' element={<ProductDetail />} />
					<Route path='/products' element={<ListAllProducts />} />
					<Route path='/auth/google/callback' element={<GoogleLoginCallBack />} />
					<Route path='/cart' element={<Cart />} />
					<Route path='/about' element={<About />} />

					<Route element={<ProtectedLayout />}>
						<Route path='/pay' element={<Pay />} />
						<Route path='/profile' element={<Profile />} />
						<Route path='/updateProfile' element={<UpdateProfile />} />
						<Route path='/address' element={<Address />} />
						<Route path='/order/:orderId' element={<OrderDetails />} />
						<Route path='/orderSuccess' element={<SuccessfullyPayment />} />
						<Route path='/orderFail' element={<SuccessfullyPayment />} />
						<Route path='/vnpayOrderResult' element={<VNPayOrderResult />} />
					</Route>

					<Route path='*' element={<h1>404 Not Found</h1>} />
				</Routes>
				<ChatControll />
			</main>

			<Footer />
		</div>
	);
}

export default App;
