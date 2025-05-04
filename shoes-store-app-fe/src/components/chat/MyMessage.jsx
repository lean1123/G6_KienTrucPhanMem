import { People } from '@mui/icons-material';
import PropTypes from 'prop-types';

function MyMessage({ item }) {
	return (
		<div className='flex items-center justify-end mb-2'>
			<span className='bg-blue-400 p-1 rounded-md text-white text-sm'>
				{item.message}
			</span>
			<div className='bg-blue-500 flex justify-center items-center rounded-full shadow-md border p-2 ml-1'>
				<People sx={{ width: 14, height: 14, color: 'white' }} />
			</div>
		</div>
	);
}

export default MyMessage;

MyMessage.propTypes = {
	item: PropTypes.shape({
		message: PropTypes.string,
		isAI: PropTypes.bool,
	}).isRequired,
};
