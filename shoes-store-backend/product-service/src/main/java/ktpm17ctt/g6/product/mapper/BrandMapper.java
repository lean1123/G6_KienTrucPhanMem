package ktpm17ctt.g6.product.mapper;

import ktpm17ctt.g6.product.dto.request.BrandRequest;
import ktpm17ctt.g6.product.dto.response.BrandResponse;
import ktpm17ctt.g6.product.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandRequest brandRequest);
    BrandResponse toBrandResponse(Brand brand);
}
