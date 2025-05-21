package ktpm17ctt.g6.product.service.implement;

import ktpm17ctt.g6.product.dto.request.CategoryRequest;
import ktpm17ctt.g6.product.dto.response.CategoryResponse;
import ktpm17ctt.g6.product.entity.Category;
import ktpm17ctt.g6.product.mapper.CategoryMapper;
import ktpm17ctt.g6.product.repository.CategoryRepository;
import ktpm17ctt.g6.product.repository.ProductRepository;
import ktpm17ctt.g6.product.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    ProductRepository productRepository;

    @Override
    public Optional<CategoryResponse> findById(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return Optional.empty();
        }
        return Optional.of(categoryMapper.toCategoryResponse(category));
    }

    @Override
    public CategoryResponse save(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toCategory(categoryRequest);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse update(String id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        category = categoryMapper.toCategory(categoryRequest);
        category.setId(id);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public boolean deleteById(String id) {
        var category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return false;
        }
        var products = productRepository.findByCategory_Id(id);
        if (!products.isEmpty()) {
            log.warn("Cannot delete category with ID: {} because it has products", id);
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CategoryResponse> findAll() {
        var categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::toCategoryResponse).toList();
    }

    @Override
    public List<CategoryResponse> search(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword).stream().map(categoryMapper::toCategoryResponse).toList();
    }
}
