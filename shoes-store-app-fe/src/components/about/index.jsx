import React from 'react';
import { Carousel } from 'react-bootstrap';

function About() {
	return (
		<div className='font-sans text-gray-800'>
			{/* Hero Section */}
			<section
				className='h-screen bg-cover bg-center flex items-center justify-center text-white'
				style={{
					backgroundImage:
						"url('https://file.hstatic.net/1000230642/file/banner-hero_cabb9ffa6e404fcb8a32b0ef10dd2f41.jpg')",
				}}
			></section>

			{/* Giới thiệu */}
			<section className='py-16 px-4 max-w-4xl mx-auto text-center'>
				<div className='bg-black bg-opacity-10 p-10 rounded-xl text-center'>
					<h1 className='text-4xl md:text-6xl font-bold mb-4'>
						<span className='text-sky-400'>Len</span>
						<span className='text-orange-400'>Dom</span>
					</h1>
				</div>
				<p className='text-gray-600 text-lg text-justify mt-2 font-calibri'>
					LenDom là thương hiệu giày dép mang sứ mệnh tôn vinh phong cách sống năng
					động và hiện đại. Mỗi đôi giày là sự kết hợp giữa thiết kế thời thượng và
					chất liệu chất lượng, mang lại trải nghiệm thoải mái cho mọi hành trình của
					bạn.
				</p>
			</section>

			{/* Hình ảnh sản phẩm */}
			<section className='grid grid-cols-1 md:grid-cols-3 gap-6 px-6 pb-16'>
				{[
					{
						name: 'white',
						img: 'https://file.hstatic.net/1000230642/file/asset_3_2x-100_2de08353866747d39c73a7069466141d.jpg',
					},
					{
						name: 'leather',
						img: 'https://file.hstatic.net/1000230642/file/asset-1_2x-100_6006cd5edd7141f58ab0a54496c13c93.jpg',
					},
					{
						name: 'sneaker',
						img: 'https://file.hstatic.net/1000230642/file/asset-2_2x-100_1216ccc0d5724cb2b8e6933de16b01ab.jpg',
					},
				].map((item, i) => (
					<img
						key={i}
						src={item.img}
						alt={`Giày ${item.name}`}
						className='rounded-xl shadow-md hover:scale-105 transition-transform'
					/>
				))}
			</section>

			{/* Sứ mệnh */}
			<section className='bg-blue-50 py-16 px-4 text-center'>
				<h1 className='text-4xl md:text-6xl font-bold mb-4'>
					<span className='text-blue-950'>Sứ Mệnh Của </span>
					<span className='text-sky-400'>Len</span>
					<span className='text-orange-400'>Dom</span>
				</h1>
				<p className='text-gray-700 text-lg max-w-3xl mx-auto text-justify font-calibri'>
					Tại LenDom, chúng tôi không chỉ sản xuất giày – chúng tôi kiến tạo phong
					cách sống. Chúng tôi hướng tới việc tạo ra những sản phẩm vừa đẹp mắt, vừa
					thân thiện với môi trường, đồng hành cùng thế hệ trẻ trong từng bước chuyển
					mình.
				</p>
			</section>
			<Carousel>
				<Carousel.Item>
					<img
						src='https://file.hstatic.net/1000230642/file/asset_3_2x-100_1b31832c497843b888be5f8c0b5f0bcb.jpg'
						className='w-full h-full'
					/>
				</Carousel.Item>
				<Carousel.Item>
					<img
						src='https://file.hstatic.net/1000230642/file/asset_4_2x-100_b72f4dbdffc94309990714e1fcff32fb.jpg'
						className='w-full h-full'
					/>
				</Carousel.Item>
				<Carousel.Item>
					<img
						src='https://file.hstatic.net/1000230642/file/asset_5_2x-100_839dfe6d64184b0a96baa85486315594.jpg'
						className='w-full h-full'
					/>
				</Carousel.Item>
			</Carousel>
			<section className='py-6 px-6 bg-gray-100 text-center mt-6'>
				<h2 className='text-3xl font-bold mb-8 text-blue-950'>
					Đơn Vị Cùng Đồng Hành
				</h2>
				<div className='flex flex-wrap justify-center items-center gap-8'>
					{[
						{
							name: 'ChatGPT',
							logo:
								'https://accesstrade.vn/wp-content/uploads/2024/03/chat-gpt-la-gi.jpeg',
						},
						{
							name: 'Gemini',
							logo: 'https://cloud-ace.vn/wp-content/uploads/2024/06/Gemini.jpg',
						},
						{
							name: 'Grok 3',
							logo: 'https://augmentium.ai/images/blog/grok3-hero.jpg',
						},
						{
							name: 'DeepSeek',
							logo:
								'https://framerusercontent.com/images/xxjR2ZsbtvOGbyUhghLRvCmb54.jpeg',
						},
						{
							name: 'Claude',
							logo: 'https://hoaianz.com/wp-content/uploads/2024/08/claude-ai.jpg',
						},
					].map((brand, index) => (
						<div
							key={index}
							className='bg-white rounded-xl shadow px-4 py-3 hover:scale-105 transition-transform'
						>
							<img
								src={brand.logo}
								alt={brand.name}
								className='h-12 md:h-16 object-contain mx-auto'
							/>
						</div>
					))}
				</div>
			</section>
		</div>
	);
}

export default About;
