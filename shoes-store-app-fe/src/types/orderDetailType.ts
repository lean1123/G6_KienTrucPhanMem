import { ProductItemType } from './productItemType';

export interface OrderDetailType {
	id: string;
	quantity: number;
	price: number;
	productItem: ProductItemType;
	orderId: string;
	size: number;
}
