import PropTypes from 'prop-types';

function FilterBySize({ onChange }) {
	const handleSizeChange = (e) => {
		onChange({ size: e.target.value });
	};

	return (
		<div className='mb-2'>
			<h2 className='mb-1'>Lọc theo kích cỡ</h2>
			<select
				className='px-4 py-2 border rounded-md w-full'
				onChange={handleSizeChange}
			>
				<option value='All Size'>Tất cả</option>
				<option value='35'>35</option>
				<option value='36'>36</option>
				<option value='37'>37</option>
				<option value='38'>38</option>
				<option value='39'>39</option>
				<option value='40'>40</option>
				<option value='41'>41</option>
				<option value='42'>42</option>
				<option value='43'>43</option>
				<option value='44'>44</option>
			</select>
		</div>
	);
}

FilterBySize.propTypes = {
	onChange: PropTypes.func.isRequired,
};

export default FilterBySize;
