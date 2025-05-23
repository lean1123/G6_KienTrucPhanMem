export interface ProductItemType {
	id: string;
	price: number;
	images: string[];
	color: {
		id: string;
		name: string;
		code: string;
	};
	quantityOfSize: [
		{
			size: number;
			quantity: number;
		},
	];
	product: {
		id: string;
		name: string;
		description: string;
		warrantyInformation: string;
		returnInformation: string;
		avatar: string;
		shippingInformation: string;
		rating: number;
		createdDate: string;
	};
	likes: string[];
	isActive: boolean;
}
