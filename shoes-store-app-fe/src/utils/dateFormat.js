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
