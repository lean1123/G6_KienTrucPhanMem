import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setCategoryName } from '../../hooks/filter/filterSlice';
import productItemApi from '../../api/productItemApi';
import { useTranslation } from 'react-i18next';

function FilterByCategory() {
	const { t } = useTranslation();
	const dispatch = useDispatch();
	const { categoryName } = useSelector((state) => state.filter);
	const [selectedCategory, setSelectedCategory] = useState(
		categoryName || 'All Category',
	);

	const [listCategories, setListCategories] = useState([]);

	useEffect(() => {
		setSelectedCategory(categoryName || 'All Category');
	}, [categoryName]);

	useEffect(() => {
		const fetchCategories = async () => {
			try {
				const response = await productItemApi.getAllCategories();
				if (response.status !== 200) {
					console.error('Failed to fetch colors');
					return;
				}
				if (response.data.status === 503) {
					console.error('Service Product unavailable');
					return;
				}

				setListCategories(response.data.result);
			} catch (error) {
				throw new Error('Error fetching colors');
			}
		};

		fetchCategories();
	}, []);

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
				<option value={'All Category'}>Tất cả</option>
				{listCategories?.map((cate) => (
					<option key={cate.id} value={cate.name}>
						{t('category.' + cate.name)}
					</option>
				))}
			</select>
		</div>
	);
}

export default FilterByCategory;
