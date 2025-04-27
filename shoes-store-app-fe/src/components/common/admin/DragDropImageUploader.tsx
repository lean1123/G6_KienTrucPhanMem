import React, { useRef, useState } from 'react';

interface DragDropImageUploaderProps {
	onImagesChange: (images: File[]) => void;
}

function DragDropImageUploader({ onImagesChange }: DragDropImageUploaderProps) {
	const [images, setImages] = useState<File[]>([]);
	const [dragging, setDragging] = useState(false);
	const fileInputRef = useRef<HTMLInputElement>(null);

	function selectFiles() {
		if (fileInputRef.current) {
			fileInputRef.current.click();
		}
	}

	function onFileSelect(e: React.ChangeEvent<HTMLInputElement>) {
		if (e.target.files) {
			const newImages = [...images, ...Array.from(e.target.files)];
			setImages(newImages);
			onImagesChange(newImages); // Gửi dữ liệu lên component cha
		}
	}

	function deleteImage(index: number) {
		const newImages = images.filter((_, i) => i !== index);
		setImages(newImages);
		onImagesChange(newImages); // Gửi dữ liệu cập nhật lên component cha
	}

	function onDragOver(e: React.DragEvent<HTMLDivElement>) {
		e.preventDefault();
		setDragging(true);
		e.dataTransfer.dropEffect = 'copy';
	}

	function onDragLeave(e: React.DragEvent<HTMLDivElement>) {
		e.preventDefault();
		setDragging(false);
	}

	function onDrop(e: React.DragEvent<HTMLDivElement>) {
		e.preventDefault();
		setDragging(false);
		if (e.dataTransfer.files) {
			const newImages = [...images, ...Array.from(e.dataTransfer.files)];
			setImages(newImages);
			onImagesChange(newImages); // Gửi dữ liệu lên component cha
		}
	}

	return (
		<div className='p-3 shadow-md rounded-md overflow-hidden bg-white'>
			<div className='text-left'>
				<p className='font-bold text-blue-500'>List Image Item</p>
			</div>
			<div className='flex flex-row gap-6'>
				<div
					className='h-[150px] w-[400px] rounded border-2 border-dashed border-blue-500 text-blue-500 flex justify-center items-center select-none mt-2'
					onDragOver={onDragOver}
					onDragLeave={onDragLeave}
					onDrop={onDrop}
				>
					{dragging ? (
						<span className='ml-1 text-blue-800 cursor-pointer transition hover:opacity-60'>
							Drop image here
						</span>
					) : (
						<>
							Drag & Drop image here or{' '}
							<span
								className='ml-1 text-blue-800 cursor-pointer transition hover:opacity-60'
								role='button'
								onClick={selectFiles}
							>
								Browse
							</span>
						</>
					)}
					<input
						name='file'
						type='file'
						multiple
						className='hidden'
						ref={fileInputRef}
						onChange={onFileSelect}
					/>
				</div>
				<div className='w-full h-auto flex justify-start items-start flex-wrap max-h-[150px] overflow-y-auto overflow-x-scroll no-scrollbar mt-2'>
					{images.map((image, index) => (
						<div key={index} className='w-[100px] mr-1 h-[100px] relative mb-2'>
							<span
								className='absolute -top-1 right-2 text-lg cursor-pointer z-[999]'
								onClick={() => deleteImage(index)}
							>
								&times;
							</span>
							<img
								src={URL.createObjectURL(image)}
								alt=''
								className='w-full h-full rounded'
							/>
						</div>
					))}
				</div>
			</div>
		</div>
	);
}

export default DragDropImageUploader;
