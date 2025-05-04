import { Assistant, Chat, Send } from '@mui/icons-material';
import React from 'react';
import ChatContent from './ChatContent';
import { useDispatch } from 'react-redux';
import {
	addMessage,
	sendMessage,
	setCurrent,
} from '../../hooks/chat/messageSlice';
import { unwrapResult } from '@reduxjs/toolkit';
import { enqueueSnackbar } from 'notistack';
import { CircularProgress } from '@mui/material';

function ChatControll() {
	const [isOpen, setIsOpen] = React.useState(false);
	const [message, setMessage] = React.useState('');
	const [isSending, setIsSending] = React.useState(false);
	const dispatch = useDispatch();

	const handleSendMessage = async () => {
		if (message.trim() === '') return;

		try {
			setIsSending(true);
			dispatch(setCurrent({ message, isAI: false }));
			dispatch(addMessage({ message, isAI: false }));
			const response = await dispatch(sendMessage(message));
			if (response.error) {
				console.error('Error sending message:', response.error.message);
				enqueueSnackbar('Gửi tin nhắn thất bại', {
					variant: 'error',
				});
				return;
			}
			dispatch(setCurrent(unwrapResult(response)));

			setMessage('');
			return;
		} catch (error) {
			console.error('Error sending message:', error.message);
			enqueueSnackbar('Gửi tin nhắn thất bại', {
				variant: 'error',
			});
			return;
		} finally {
			setIsSending(false);
		}
	};
	return (
		<div className='fixed top-3/4 right-9 z-50'>
			<div
				className='bg-blue-500 rounded-full shadow-md border p-2 cursor-pointer hover:translate-y-1
            hover:scale-110 transition-all duration-300 ease-in-out'
				onClick={() => setIsOpen((prev) => !prev)}
			>
				<Assistant sx={{ width: 32, height: 32, color: 'white' }} />
			</div>
			{isOpen && (
				<div className='bg-white rounded-lg shadow-md p-4 mt-2 absolute right-12 bottom-14 w-96 h-96'>
					<div className='flex flex-col items-center justify-center mb-2'>
						<Chat sx={{ width: 24, height: 24, color: 'blue' }} />
						<span className='ml-2 text-lg font-semibold'>
							Gợi ý sản phẩm với trợ lý AI
						</span>
					</div>
					<ChatContent />
					<div className='absolute bottom-4 left-4 right-4 flex items-center'>
						<input
							type='text'
							placeholder='Nhập tin nhắn của bạn...'
							className='border border-gray-300 p-2 pr-10 w-full rounded-full focus:outline-none'
							value={message}
							onChange={(e) => setMessage(e.target.value)}
						/>
						{
							<button
								className='absolute right-2 top-1/2 transform -translate-y-1/2 bg-blue-500 text-white 
                        w-8 h-8 rounded-full flex items-center justify-center hover:bg-blue-600'
								onClick={handleSendMessage}
								disabled={isSending}
							>
								{!isSending ? (
									<Send sx={{ width: 16, height: 16 }} />
								) : (
									<CircularProgress size={16} sx={{ color: 'white' }} />
								)}
							</button>
						}
					</div>
				</div>
			)}
		</div>
	);
}

export default ChatControll;
