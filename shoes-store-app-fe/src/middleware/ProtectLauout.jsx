import { useSelector } from 'react-redux';
import { Navigate, Outlet } from 'react-router';

function ProtectLauout() {
	const { accessToken } = useSelector((state) => state.persistedReducer.user);
	const isAuthenticated = !!accessToken;

	return isAuthenticated ? <Outlet /> : <Navigate to='/login' replace />;
}

export default ProtectLauout;
