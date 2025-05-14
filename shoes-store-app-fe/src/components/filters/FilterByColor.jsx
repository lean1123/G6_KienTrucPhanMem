import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setColor } from '../../hooks/filter/filterSlice';

function FilterByColor() {
	const { color } = useSelector((state) => state.filter);
	const [selectedColor, setSelectedColor] = useState(color || 'All Color');
	const disPatch = useDispatch();

	useEffect(() => {
		setSelectedColor(color || 'All Color');
	}, [color]);

	const colorOptions = ['All Color', 'RED', 'BLUE', 'GREEN', 'WHITE', 'BLACK'];

	const handleColorChange = (e) => {
		const value = e.target.value;
		setSelectedColor(value);
		if (value === 'All Color') {
			disPatch(setColor(null));
		} else {
			disPatch(setColor(value));
		}
	};

	return (
		<div className='mb-2'>
			<h2 className='mb-1'>Lọc theo màu sắc:</h2>
			<select
				className='px-4 py-2 border rounded-md w-full'
				onChange={handleColorChange}
				value={selectedColor}
			>
				{colorOptions.map((colorOption) => (
					<option key={colorOption} value={colorOption}>
						{colorOption === 'All Color' ? 'Tất cả' : colorOption}
					</option>
				))}
			</select>
		</div>
	);
}

export default FilterByColor;
