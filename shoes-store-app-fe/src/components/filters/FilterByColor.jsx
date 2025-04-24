import PropTypes from 'prop-types';

function FilterByColor({ onChange }) {
	const handleColorChange = (e) => {
		onChange({ color: e.target.value });
	};

	return (
		<div className='mb-1'>
			<h2 className='mb-1'>Lọc theo màu sắc:</h2>
			<select className='px-4 py-2 border rounded-md' onChange={handleColorChange}>
				<option value='All Color'>Tất Cả</option>
				<option className='bg-red-400' value='RED'>
					Đỏ
				</option>
				<option className='bg-sky-500' value='BLUE'>
					Xanh Dương
				</option>
				<option className='bg-green-500' value='GREEN'>
					Xanh Lá
				</option>
				{/* Add more colors as needed */}
			</select>
		</div>
	);
}

FilterByColor.propTypes = {
	onChange: PropTypes.func.isRequired,
};

export default FilterByColor;
