export const formatCurrency = (amount, locale = 'vi-VN', currency = 'VND') => {
	return new Intl.NumberFormat(locale, { style: 'currency', currency }).format(
		amount,
	);
};
