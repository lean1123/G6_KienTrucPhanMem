import SearchSharpIcon from '@mui/icons-material/SearchSharp';
import { Input } from '@mui/material';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
	onSearch,
	setColor,
	setMaxPrice,
	setMinPrice,
	setProductName,
	setSize,
} from '../../hooks/filter/filterSlice';
import FilterByColor from '../filters/FilterByColor';
import FilterByPrice from '../filters/FilterByPrice';
import FilterBySize from '../filters/FilterBySize';
import ListProduct from './ListProduct';

function ListAllProducts() {
	const dispatch = useDispatch();

	const {
		color,
		size,
		minPrice,
		maxPrice,
		productName,
		returnProducts,
		error,
		loading,
	} = useSelector((state) => state.filter);

	useEffect(() => {
		const fetchProductItemsByFilter = async () => {
			await dispatch(onSearch({ color, size, minPrice, maxPrice, productName }));
		};
		fetchProductItemsByFilter();
	}, [dispatch, color, size, minPrice, maxPrice, productName]);

	const handleProductNameChange = (newName) => {
		if (newName.target.value) {
			const productName = newName.target.value;

			dispatch(setProductName(productName.length > 0 ? productName : null));
		}
	};

	const handleSizeChange = (newSize) => {
		if (newSize.size) {
			if (newSize.size === 'All Size') {
				console.log('newSize', newSize.size);

				dispatch(setSize(null));
				return;
			}

			dispatch(setSize(newSize.size));
		}
	};

	const handleColorChange = (newColor) => {
		if (newColor.color) {
			if (newColor.color === 'All Color') {
				dispatch(setColor(null));
				return;
			}

			dispatch(setColor(newColor.color));
		}
	};

	const handlePriceChange = (newPrice) => {
		if (newPrice.minPrice) {
			dispatch(setMinPrice(newPrice.minPrice));
		} else {
			dispatch(setMaxPrice(newPrice.maxPrice));
		}
	};

	return (
		<div className='topSale-container'>
			<div className='flex justify-center items-center mt-5'>
				<div className='p-2 border rounded-md border-slate-800 shadow-md bg-slate-50'>
					<SearchSharpIcon sx={{ cursor: 'pointer' }} />
					<Input
						sx={{ paddingX: '10px' }}
						placeholder='Nhập tên sản phẩm'
						onChange={handleProductNameChange}
						disableUnderline={true}
					/>
				</div>
			</div>
			<div className='flex mt-4'>
				<div
					className='w-1/5 bg-white ml-5 items-center text-base font-calibri font-semibold 
						p-2 rounded-md shadow-md border h-fit'
				>
					<FilterBySize onChange={handleSizeChange} />
					<FilterByColor onChange={handleColorChange} />
					<FilterByPrice onChange={handlePriceChange} />
				</div>
				<div className='w-4/5 mb-4'>
					{loading && <p>Loading...</p>}
					{error && <p>{error.message || JSON.stringify(error)}</p>}
					<ListProduct items={returnProducts} />
				</div>
			</div>
		</div>
	);
}

export default ListAllProducts;
