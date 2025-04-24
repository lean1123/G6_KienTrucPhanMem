import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import PropTypes from 'prop-types';
import { useState } from 'react';

function SubProductDetail({ title = '', path = '', content = '' }) {
	const [showContent, setShowContent] = useState(false);

	return (
		<div className='w-full'>
			<div
				onClick={() => setShowContent((prev) => !prev)}
				className='flex justify-between items-center cursor-pointer py-2'
			>
				<p className='text-base font-semibold text-slate-900'>{title}</p>
				<KeyboardArrowDownIcon
					className={`transition-transform duration-300 ${showContent ? 'rotate-180' : ''}`}
				/>
			</div>

			{showContent && (
				<div className='mt-2 text-sm text-slate-700 transition-all duration-300 ease-in-out'>
					{content}
				</div>
			)}
		</div>
	);
}

SubProductDetail.propTypes = {
	title: PropTypes.string,
	path: PropTypes.string,
	content: PropTypes.string,
};

export default SubProductDetail;
