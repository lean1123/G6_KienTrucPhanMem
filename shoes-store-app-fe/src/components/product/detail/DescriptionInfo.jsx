import { useParams } from 'react-router';
import useProductItem from '../../../hooks/product/useProductItem';

function DescriptionInfo() {
	const { id } = useParams();

	const { isLoading, productItem } = useProductItem(id);

	if (isLoading) return <div>Loading...</div>;

	return <div>{productItem?.product.description}</div>;
}

export default DescriptionInfo;
