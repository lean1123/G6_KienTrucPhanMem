import { useEffect, useRef } from 'react';
import AIResponse from './AIResponse';
import MyMessage from './MyMessage';
import { useSelector } from 'react-redux';
import { Assistant } from '@mui/icons-material';

function ChatContent() {
	const messages = useSelector((state) => state.chat.messages);
	const bottomRef = useRef(null);

	useEffect(() => {
		if (bottomRef.current) {
			bottomRef.current.scrollIntoView({ behavior: 'smooth' });
		}
	}, [messages]);

	return (
		<div className='overflow-y-scroll custom-scrollbar h-56 text-justify'>
			{messages.length === 0 ? (
				<div className='flex items-center mb-2 justify-start'>
					<div className='flex items-center justify-center bg-slate-100 p-2 rounded-full shadow-md border mr-1'>
						<Assistant sx={{ width: 14, height: 14, color: '#3b82f6' }} />
					</div>
					<div className='bg-slate-100 p-2 rounded-md shadow-md'>
						<p className='text-sm'>Bạn cần tìm kiếm sản phẩm như thế nào?y</p>
					</div>
				</div>
			) : (
				messages.map((message, index) => {
					if (message.isAI) {
						return <AIResponse key={index} item={message} />;
					}
					return <MyMessage key={index} item={message} />;
				})
			)}
			<div ref={bottomRef} />
		</div>
	);
}

export default ChatContent;
