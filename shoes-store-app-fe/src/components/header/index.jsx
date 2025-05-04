import '@fortawesome/fontawesome-free/css/all.min.css';
import { Box } from '@mui/material';
import { useNavigate } from 'react-router';

import NavigationButton from './NavigationButton';
import './style.css';
import UserButton from './UserButton';
import CartButton from './CartButton';

function Header() {
	const navigate = useNavigate();

	return (
		<Box
			sx={{
				backgroundColor: 'rgba(255, 255, 255, 0.5)',
				position: 'fixed',
				width: '100%',
				zIndex: 10000,
				backdropFilter: 'blur(2px)',
				marginBottom: 40,
			}}
		>
			<nav className=''>
				<div className='mx-auto max-w-7xl px-2 sm:px-6 lg:px-8'>
					<div className='relative flex h-16 items-center justify-between'>
						<div className='flex flex-1 items-center justify-center sm:items-stretch sm:justify-start'>
							<div
								className='flex-shrink-0 hover:cursor-pointer'
								onClick={() => navigate('/')}
							>
								<img
									className='block h-16 w-auto'
									src='/logo.png'
									alt='Logo'
									width={40}
									height={40}
								/>
							</div>
							<div className='hidden sm:flex justify-center items-center mx-auto'>
								<div className='flex space-x-4 h-full items-center'>
									<NavigationButton path='/products' title='TẤT CẢ SẢN PHẨM' />
									<NavigationButton path='/about' title='VỀ CHÚNG TÔI' />
									<NavigationButton path='/categories' title='DANH MỤC' />
									<NavigationButton path='/categoryforBoy' title='NAM' />
									<NavigationButton path='/categoryforGirl' title='NỮ' />
								</div>
							</div>
						</div>
						<div className='relative ml-3'>
							<div>
								<div className='flex items-center'>
									<UserButton />
									<CartButton />
								</div>
							</div>
						</div>
					</div>
				</div>
			</nav>
		</Box>
	);
}

export default Header;
