export function convertTimestampToDateTime(ts) {
	const [year, month, day, hour, minute, second, millisecond] = [
		ts[0], // yyyy
		ts[1], // MM
		ts[2], // dd
		ts[3], // HH
		ts[4], // mm
		ts[5], // ss
		ts[6], // SSS
	];
	const date = new Date(
		parseInt(year),
		parseInt(month) - 1,
		parseInt(day),
		parseInt(hour),
		parseInt(minute),
		parseInt(second),
		parseInt(millisecond),
	);

	return date.toISOString().replace('T', ' ').slice(0, 23);
}

export const formatVNPayDate = (dateString) => {
	// Kiểm tra nếu chuỗi dateString hợp lệ
	if (!dateString || dateString.length !== 14) return 'Không có thông tin';

	// Chuyển đổi chuỗi theo định dạng yyyyMMddHHmmss
	const year = dateString.substring(0, 4);
	const month = dateString.substring(4, 6);
	const day = dateString.substring(6, 8);
	const hour = dateString.substring(8, 10);
	const minute = dateString.substring(10, 12);
	const second = dateString.substring(12, 14);

	// Trả về định dạng mới như "2025-04-27 15:23:06"
	return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
};

export const formatISODate = (isoString) => {
	if (!isoString) return '';

	const date = new Date(isoString);

	const day = String(date.getDate()).padStart(2, '0');
	const month = String(date.getMonth() + 1).padStart(2, '0'); // tháng bắt đầu từ 0
	const year = date.getFullYear();

	const hours = String(date.getHours()).padStart(2, '0');
	const minutes = String(date.getMinutes()).padStart(2, '0');
	const seconds = String(date.getSeconds()).padStart(2, '0');

	// bạn muốn đổi format ở đây
	return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
};
