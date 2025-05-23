import SearchSharpIcon from '@mui/icons-material/SearchSharp';
import { Input, Pagination } from '@mui/material';
import { debounce } from 'lodash';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
	onSearch,
	setCurrentPage,
	setProductName,
} from '../../hooks/filter/filterSlice';
import FilterByColor from '../filters/FilterByColor';
import FilterByPrice from '../filters/FilterByPrice';
import FilterBySize from '../filters/FilterBySize';
import ListProduct from './ListProduct';
import FilterByCategory from '../filters/FilterByCategory';

function ListAllProducts() {
	const dispatch = useDispatch();

	const {
		color,
		size,
		minPrice,
		maxPrice,
		productName,
		returnProducts,
		categoryName,
		page,
		totalPage,
	} = useSelector((state) => state.filter);
	const [productNameValue, setProductNameValue] = useState(productName || '');

	useEffect(() => {
		const fetchProductItemsByFilter = () => {
			dispatch(
				onSearch({
					color,
					size,
					minPrice,
					maxPrice,
					productName,
					categoryName,
					page,
				}),
			);
		};

		const debouncedFetch = debounce(fetchProductItemsByFilter, 800);
		debouncedFetch();

		return () => {
			debouncedFetch.cancel();
		};
	}, [
		dispatch,
		color,
		size,
		minPrice,
		maxPrice,
		productName,
		categoryName,
		page,
	]);

	const handleProductNameChange = (e) => {
		const value = e.target.value;
		setProductNameValue(value);
		dispatch(setProductName(value.trim() === '' ? null : value));
	};

	return (
		<div className='topSale-container px-4'>
			<div className='flex justify-center items-center mt-5'>
				<div className='p-2 border rounded-md border-slate-800 shadow-md bg-slate-50'>
					<SearchSharpIcon sx={{ cursor: 'pointer' }} />
					<Input
						sx={{ paddingX: '10px' }}
						placeholder='Nhập tên sản phẩm'
						onChange={handleProductNameChange}
						value={productNameValue}
						disableUnderline={true}
					/>
				</div>
			</div>
			<div className='flex mt-4'>
				<div
					className='w-1/5 bg-white ml-5 items-center text-base font-calibri font-semibold 
						p-2 rounded-md shadow-md border h-fit'
				>
					<FilterBySize />
					<FilterByColor />
					<FilterByCategory />
					<FilterByPrice />
				</div>
				<div className='w-4/5 mb-4'>
					<ListProduct items={returnProducts} />
				</div>
			</div>
			<div className='flex justify-center items-center mt-5'>
				<Pagination
					size='small'
					count={totalPage}
					page={page}
					onChange={(e, p) => dispatch(setCurrentPage(p))}
				/>
			</div>
		</div>
	);
}

export default ListAllProducts;
