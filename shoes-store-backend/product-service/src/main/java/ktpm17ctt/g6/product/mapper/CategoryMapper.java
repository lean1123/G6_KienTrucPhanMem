package ktpm17ctt.g6.product.mapper;

import ktpm17ctt.g6.product.dto.request.CategoryRequest;
import ktpm17ctt.g6.product.dto.response.CategoryResponse;
import ktpm17ctt.g6.product.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest categoryRequest);
    CategoryResponse toCategoryResponse(Category category);
}
