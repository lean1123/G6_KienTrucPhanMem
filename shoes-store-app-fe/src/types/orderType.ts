import { AddressType } from './addressType';
import { OrderDetailType } from './orderDetailType';
import { ProductItemType } from './productItemType';

export interface OrderItemType {
	id: string;
	total: number;
	createdDate: Date;
	status: string;
	userId: string;
	paymentMethod: string;
	orderDetails: OrderDetailType[];
	paymentUrl?: string;
	address: AddressType;
	payed: boolean;
}
