import PropTypes from 'prop-types';

function FilterByPrice({ onChange }) {
	const handleMinPriceChange = (e) => {
		onChange({ minPrice: e.target.value });
	};

	const handleMaxPriceChange = (e) => {
		onChange({ maxPrice: e.target.value });
	};

	return (
		<div>
			<h2>Filter by Price</h2>
			<input
				className='px-4 py-2 border rounded-md text-black mb-1'
				type='number'
				placeholder='Giá từ...'
				onChange={handleMinPriceChange}
			/>
			<input
				className='px-4 py-2 border rounded-md text-black'
				type='number'
				placeholder='Giá đến...'
				onChange={handleMaxPriceChange}
			/>
		</div>
	);
}

FilterByPrice.propTypes = {
	onChange: PropTypes.func.isRequired,
};

export default FilterByPrice;
