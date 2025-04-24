const scrollStyles = {
	maxHeight: '300px',
	overflowY: 'auto',
};
function FilterByBrand() {
	return (
		<>
			<div>
				<h1 className='font-bold mt-4 mb-3'>THƯƠNG HIỆU</h1>
			</div>
			<div
				className='search-container bg-white flex flex-col items-start'
				style={scrollStyles}
			>
				<button className='ml-2 mb-2 mt-2'>
					<img
						className='h-10 w-auto'
						src='/ThuongHieu/cros.png'
						alt='Logo'
						width={40}
						height={40}
					/>
				</button>
				<button className='ml-2 font-bold'>Local Brand</button>
				<button className='mb-2'>
					<img
						className='h-12 w-auto'
						src='/ThuongHieu/MLB.png'
						alt='Logo'
						width={40}
						height={40}
					/>
				</button>
				<button className='ml-2 font-bold'>Nike Jordan</button>
				<button className='ml-2 font-bold mt-3'>SaigonSneaker</button>
				<button className='p-3 mt-2'>
					<img
						className='h-8 w-auto'
						src='/ThuongHieu/Adidas.png'
						alt='Logo'
						width={40}
						height={40}
					/>
				</button>
				<button className='p-2'>
					<img
						className='h-10 w-auto ml-2'
						src='/ThuongHieu/NIKE.jpg'
						alt='Logo'
						width={40}
						height={40}
					/>
				</button>
				<button className='p-2'>
					<img
						className='h-14 w-auto'
						src='/ThuongHieu/newbalence.png'
						alt='Logo'
						width={40}
						height={40}
					/>
				</button>
			</div>
		</>
	);
}

export default FilterByBrand;
