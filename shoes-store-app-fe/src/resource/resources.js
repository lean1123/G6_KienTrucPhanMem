import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

const resources = {
	vi: {
		translation: {
			status: {
				PENDING: 'Đang chờ',
				SUCCESS: 'Thành công',
				CANCELLED: 'Đã hủy',
			},
			payment: {
				VNPAY: 'Ví VNPAY',
				MOMO: 'Ví MoMo',
				COD: 'Thanh toán khi nhận hàng',
				CASH: 'Thanh toán khi nhận hàng',
			},
			// Bạn có thể thêm nhiều nhóm khác nữa
		},
	},
	en: {
		translation: {
			status: {
				PENDING: 'Pending',
				SUCCESS: 'Success',
				CANCELLED: 'Cancelled',
			},
			payment: {
				VNPAY: 'VNPAY Wallet',
				MOMO: 'MoMo Wallet',
				COD: 'Cash on Delivery',
				CASH: 'Cash on Delivery',
			},
		},
	},
};

i18n.use(initReactI18next).init({
	resources,
	lng: 'vi', // mặc định tiếng Việt
	fallbackLng: 'vi', // nếu không tìm thấy sẽ fallback tiếng Việt
	interpolation: {
		escapeValue: false, // React đã tự escape rồi
	},
});

export default i18n;
