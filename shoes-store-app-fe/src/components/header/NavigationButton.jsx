import PropTypes from 'prop-types';
import { useState } from 'react';
import { useNavigate } from 'react-router';

function NavigationButton({ path, title }) {
	const navigate = useNavigate();
	const [listCategories, setListCategories] = useState([
		'Giày Thể Thao',
		'Giày Tây',
		'Giày Sneaker',
	]);
	return (
		<div className='hover:border-b-2 hover:border-black hover:duration-75 h-full flex items-center'>
			<button
				onClick={() => navigate(path)}
				className='text-black text-ml font-medium px-3 py-2 rounded-md '
			>
				{title}
			</button>
		</div>
	);
}

export default NavigationButton;

NavigationButton.propTypes = {
	path: PropTypes.string.isRequired,
	title: PropTypes.string.isRequired,
};
