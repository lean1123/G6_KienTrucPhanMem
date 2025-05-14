import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setCategoryName } from '../../hooks/filter/filterSlice';

function FilterByCategory() {
	const dispatch = useDispatch();
	const { category } = useSelector((state) => state.filter);
	const [selectedCategory, setSelectedCategory] = useState(
		category || 'All Category',
	);

	const categoryOptions = [
		'All Category',
		'Sport Shoes',
		'Sneaker',
		'Western Shoes',
	];

	useEffect(() => {
		setSelectedCategory(category || 'All Category');
	}, [category]);

	const handleCategoryChange = (e) => {
		const value = e.target.value;
		setSelectedCategory(value);
		dispatch(setCategoryName(value === 'All Category' ? null : value));
	};
	return (
		<div className='mb-2'>
			<h2 className='mb-1 font-semibold'>Lọc theo danh mục</h2>
			<select
				className='px-4 py-2 border rounded-md w-full'
				value={selectedCategory}
				onChange={handleCategoryChange}
			>
				{categoryOptions.map((cate) => (
					<option key={cate} value={cate}>
						{cate === 'All Category' ? 'Tất cả' : cate}
					</option>
				))}
			</select>
		</div>
	);
}

export default FilterByCategory;
