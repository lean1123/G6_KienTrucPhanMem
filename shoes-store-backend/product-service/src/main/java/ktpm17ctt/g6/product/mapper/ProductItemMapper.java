package ktpm17ctt.g6.product.mapper;

import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.entity.ProductItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductItemMapper {
    ProductItem toProductItem(ProductItemRequest productItemRequest);
    ProductItemResponse toProductItemResponse(ProductItem productItem);
}
