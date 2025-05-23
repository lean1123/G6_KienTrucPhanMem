import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import productItemApi from '../../api/productItemApi';
import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';
import { setCategoryName } from '../../hooks/filter/filterSlice';

function CategoryNav() {
	const { t } = useTranslation();
	const navigate = useNavigate();
	const [listCategories, setListCategories] = useState([]);
	const dispatch = useDispatch();

	useEffect(() => {
		const fetchCategories = async () => {
			try {
				const response = await productItemApi.getAllCategories();
				if (response.status !== 200) {
					console.error('Failed to fetch categories');
					return;
				}
				if (response.data.status === 503) {
					console.error('Service Product unavailable');
					return;
				}
				setListCategories(response.data.result);
			} catch (error) {
				console.error('Error fetching categories', error);
			}
		};

		fetchCategories();
	}, []);

	return (
		<div className='relative group h-full flex items-center'>
			{/* Button chính */}
			<button
				onClick={() => navigate('/categories')}
				className='text-black text-ml font-medium px-3 py-2 rounded-md flex items-center'
			>
				{t('category.title', 'DANH MỤC')}
				<KeyboardArrowDownIcon fontSize='small' />
			</button>

			{/* Dropdown list */}
			<ul className='absolute top-12 left-0 z-50 bg-white shadow-md rounded-md mt-2 w-48 hidden group-hover:block'>
				{listCategories.map((category, index) => (
					<li
						key={index}
						className='px-4 py-2 hover:bg-gray-100 cursor-pointer text-sm text-gray-700'
						onClick={() => {
							dispatch(setCategoryName(category.name));
							navigate(`/products`);
						}}
					>
						{t(`category.${category.name}`, category.name)}
					</li>
				))}
			</ul>
		</div>
	);
}

export default CategoryNav;
