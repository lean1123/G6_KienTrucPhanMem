import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setMaxPrice, setMinPrice } from '../../hooks/filter/filterSlice';

function FilterByPrice() {
	const { minPrice, maxPrice } = useSelector((state) => state.filter);

	const [minPriceValue, setMinPriceValue] = useState(minPrice || '');
	const [maxPriceValue, setMaxPriceValue] = useState(maxPrice || '');
	const disPatch = useDispatch();

	const handleMinPriceChange = (e) => {
		const value = e.target.value;

		if (value) {
			setMinPriceValue(value);
			disPatch(setMinPrice(value));
		} else {
			setMinPriceValue('');
			disPatch(setMinPrice(null));
		}
	};

	const handleMaxPriceChange = (e) => {
		const value = e.target.value;

		if (value) {
			setMaxPriceValue(value);
			disPatch(setMaxPrice(value));
		} else {
			setMaxPriceValue('');
			disPatch(setMaxPrice(null));
		}
	};

	return (
		<div>
			<h2 className='mb-1'>Lọc theo giá sản phẩm</h2>
			<input
				className='px-4 py-2 border rounded-md text-black mb-1 w-full'
				type='number'
				placeholder='Giá từ...'
				value={minPriceValue}
				onChange={handleMinPriceChange}
			/>
			<input
				className='px-4 py-2 border rounded-md text-black w-full'
				type='number'
				value={maxPriceValue}
				placeholder='Giá đến...'
				onChange={handleMaxPriceChange}
			/>
		</div>
	);
}

export default FilterByPrice;
