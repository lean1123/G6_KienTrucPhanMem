import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setSize } from '../../hooks/filter/filterSlice';

function FilterBySize() {
	const { size } = useSelector((state) => state.filter);
	const [selectedSize, setSelectedSize] = useState(size || 'All Size');
	const dispatch = useDispatch();

	useEffect(() => {
		setSelectedSize(size || 'All Size');
	}, [size]);

	const handleSizeChange = (e) => {
		const value = e.target.value;
		setSelectedSize(value);
		dispatch(setSize(value === 'All Size' ? null : value));
	};

	const sizeOptions = [
		'35',
		'36',
		'37',
		'38',
		'39',
		'40',
		'41',
		'42',
		'43',
		'44',
		'45',
	];

	return (
		<div className='mb-2'>
			<h2 className='mb-1 font-semibold'>Lọc theo kích cỡ</h2>
			<select
				className='px-4 py-2 border rounded-md w-full'
				value={selectedSize}
				onChange={handleSizeChange}
			>
				<option value={'All Size'}>Tất cả</option>
				{sizeOptions.map((sz) => (
					<option key={sz} value={sz}>
						{sz}
					</option>
				))}
			</select>
		</div>
	);
}

export default FilterBySize;
