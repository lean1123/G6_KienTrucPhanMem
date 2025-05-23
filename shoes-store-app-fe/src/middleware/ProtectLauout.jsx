import { useSelector } from 'react-redux';
import { Navigate, Outlet } from 'react-router';

function ProtectLayout() {
	const { accessToken } = useSelector((state) => state.user);
	const isAuthenticated = !!accessToken;

	return isAuthenticated ? <Outlet /> : <Navigate to='/login' replace />;
}

export default ProtectLayout;
