import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import { Link } from '@mui/material';
import PropTypes from 'prop-types';

function SubProductDetail({ title = '', path = '' }) {
	return (
		<Link
			href={path}
			className='w-full flex justify-between'
			underline='none'
			color='textPrimary'
		>
			<p className='text-base text-slate-900 font-bold mr-40'>{title}</p>
			<span>
				<KeyboardArrowDownIcon />
			</span>
		</Link>
	);
}

SubProductDetail.propTypes = {
	title: PropTypes.string,
	path: PropTypes.string,
};

export default SubProductDetail;
