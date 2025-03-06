package ktpm17ctt.g6.product.mapper;

import ktpm17ctt.g6.product.dto.request.ColorRequest;
import ktpm17ctt.g6.product.dto.response.ColorResponse;
import ktpm17ctt.g6.product.entity.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    Color toColor(ColorRequest colorRequest);
    ColorResponse toColorResponse(Color color);
}
