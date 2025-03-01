package ktpm17ctt.g6.product.mapper;

import ktpm17ctt.g6.product.dto.request.ProductCollectionRequest;
import ktpm17ctt.g6.product.dto.response.ProductCollectionResponse;
import ktpm17ctt.g6.product.entity.ProductCollection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCollectionMapper {
    ProductCollection toProductCollection(ProductCollectionRequest productCollectionRequest);
    ProductCollectionResponse toProductCollectionResponse(ProductCollection productCollection);
}
