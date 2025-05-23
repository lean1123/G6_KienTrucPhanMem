import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setColor } from '../../hooks/filter/filterSlice';
import productItemApi from '../../api/productItemApi';
import { useTranslation } from 'react-i18next';

function FilterByColor() {
	const { color } = useSelector((state) => state.filter);
	const [selectedColor, setSelectedColor] = useState(color || 'All Color');
	const [listColors, setListColors] = useState([]);
	const disPatch = useDispatch();
	const { t } = useTranslation();

	useEffect(() => {
		const fetchColors = async () => {
			try {
				const response = await productItemApi.getAllColors();
				if (response.status !== 200) {
					console.error('Failed to fetch colors');
					return;
				}
				if (response.data.status === 503) {
					console.error('Service Product unavailable');
					return;
				}

				setListColors(response.data.result);
			} catch (error) {
				throw new Error('Error fetching colors');
			}
		};
		fetchColors();
	}, []);

	useEffect(() => {
		setSelectedColor(color || 'All Color');
	}, [color]);

	const handleColorChange = (e) => {
		const value = e.target.value;
		setSelectedColor(value);
		if (value === 'All Color') {
			disPatch(setColor(null));
		} else {
			disPatch(setColor(value));
		}
	};

	const isSelected = (value) => {
		return value === selectedColor;
	};

	return (
		<div className='mb-2'>
			<h2 className='mb-1'>Lọc theo màu sắc:</h2>
			<select
				className='px-4 py-2 border rounded-md w-full'
				onChange={handleColorChange}
				value={selectedColor}
			>
				<option value='All Color'>Tất cả</option>
				{listColors?.map((colorOption) => (
					<option key={colorOption.id} value={colorOption.name}>
						{t('color.' + colorOption.name)}
					</option>
				))}
			</select>
		</div>
	);
}

export default FilterByColor;
