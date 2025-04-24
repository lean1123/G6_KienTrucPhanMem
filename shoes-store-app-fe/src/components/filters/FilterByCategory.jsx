const scrollStyles = {
	maxHeight: '300px',
	overflowY: 'auto',
};

function FilterByCategory() {
	return (
		<>
			return{' '}
			<div>
				<h1 className='font-bold mt-4'>NỔI BẬT</h1>
			</div>
			<div
				className='search-container bg-white flex flex-col items-start mt-3'
				style={scrollStyles}
			>
				<button className='font-bold mb-3 text-s'>All Black</button>
				<button className='font-bold mb-3 text-s'>All White</button>
				<button className='font-bold mb-3 text-s'>Cổ cao</button>
				<button className='font-bold mb-3 text-s'>Collab</button>
				<button className='font-bold mb-3 text-s'>Đế cao </button>
				<button className='font-bold mb-3 text-s'>Limited</button>
				<button className='font-bold mb-3 text-s'>Luxury brand</button>
				<button className='font-bold mb-3 text-s'>Phản quang</button>
				<button className='font-bold mb-3 text-s'>Retro</button>
				<button className='font-bold mb-3 text-s'>Vintage</button>
				<button className='font-bold mb-3 text-s'>Cổ thấp</button>
			</div>
		</>
	);
}

export default FilterByCategory;
