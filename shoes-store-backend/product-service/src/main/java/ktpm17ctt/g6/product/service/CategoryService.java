package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.request.CategoryRequest;
import ktpm17ctt.g6.product.dto.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<CategoryResponse> findById(String id);
    CategoryResponse save(CategoryRequest categoryRequest);
    CategoryResponse update(String id, CategoryRequest categoryRequest);
    void deleteById(String id);
    List<CategoryResponse> findAll();
    List<CategoryResponse> search(String keyword);
}
