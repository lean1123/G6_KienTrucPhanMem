import { ArrowDownward, ArrowUpward } from '@mui/icons-material';
import React, { ReactNode } from 'react';

interface CardDataStatsProps {
	title: string;
	total: string;
	rate: string;
	levelUp?: boolean;
	levelDown?: boolean;
	children: ReactNode;
}

const CardDataStats: React.FC<CardDataStatsProps> = ({
	title,
	total,
	rate,
	levelUp,
	levelDown,
	children,
}) => {
	return (
		<div className='rounded-md border border-gray-500 bg-white py-6 px-7 shadow-sm'>
			<div className='flex h-11 w-11 items-center justify-center rounded-full bg-white'>
				{children}
			</div>

			<div className='mt-4 flex items-end justify-between'>
				<div>
					<h4 className='text-lg font-bold text-black'>{total}</h4>
					<span className='text-sm font-medium'>{title}</span>
				</div>

				<span
					className={`flex items-center gap-1 text-sm font-medium ${
						levelUp && 'text-green-600-500'
					} ${levelDown && 'text-red-500'} `}
				>
					{rate}

					{levelUp && <ArrowUpward className='w-5 h-5 text-green-600' />}
					{levelDown && <ArrowDownward className='w-5 h-5 text-red-500' />}
				</span>
			</div>
		</div>
	);
};

export default CardDataStats;
