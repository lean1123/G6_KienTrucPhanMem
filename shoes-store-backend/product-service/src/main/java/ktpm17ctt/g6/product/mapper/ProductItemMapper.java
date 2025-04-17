package ktpm17ctt.g6.product.mapper;

import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.entity.ProductItem;
import ktpm17ctt.g6.product.entity.QuantityOfSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductItemMapper {
//    @Mapping(target = "quantityOfSize", source = "quantityOfSize", qualifiedByName = "mapQuantityOfSizeList")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "quantityOfSize", ignore = true)
    ProductItem toProductItem(ProductItemRequest productItemRequest);
    ProductItemResponse toProductItemResponse(ProductItem productItem);

    @Named("mapQuantityOfSizeList")
    static List<QuantityOfSize> mapQuantityOfSizeList(List<QuantityOfSize> quantityOfSizeList) {
        return quantityOfSizeList;
    }
}
