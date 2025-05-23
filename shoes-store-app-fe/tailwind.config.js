/** @type {import('tailwindcss').Config} */
// import plugin from 'tailwind-scrollbar';
export default {
	content: [
		'./index.html',
		'./src/**/*.{js,ts,jsx,tsx}',
		'./src/**/**/*.{js,ts,jsx,tsx}',
	],
	theme: {
		extend: {
			fontFamily: {
				calibri: ['Calibri', 'sans-serif'],
			},
			display: ['group-hover'],
		},
	},
	plugins: [
		// function ({ addUtilities }) {
		// 	const newUtilities = {
		// 		'.scroll-thin': {
		// 			scrollbarWidth: 'thin',
		// 			scrollbarColor: 'rgb(31 29 29) white',
		// 		},
		// 		'.scrollbar-webkit': {
		// 			'&::-webkit-scrollbar': {
		// 				width: '8px',
		// 			},
		// 			'&::-webkit-scrollbar-thumb': {
		// 				backgroundColor: 'green',
		// 				borderRadius: '8px',
		// 			},
		// 			'&::-webkit-scrollbar-track': {
		// 				backgroundColor: 'white',
		// 				borderRadius: '8px',
		// 			},
		// 		},
		// 	};
		// 	addUtilities(newUtilities, ['responsive', 'hover']);
		// },
		// plugin,
	],
};
