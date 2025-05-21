package ktpm17ctt.g6.product.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.request.CategoryRequest;
import ktpm17ctt.g6.product.dto.response.CategoryResponse;
import ktpm17ctt.g6.product.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @GetMapping()
    ApiResponse<List<CategoryResponse>> getAllCategories() {
        log.info("Get all categories");
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.findAll())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<CategoryResponse> getCategoryById(@PathVariable String id) {
        log.info("Get category with ID: {}", id);
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.findById(id).orElse(null))
                .build();
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        log.info("Create new category: {}", request.getName());
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.save(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<CategoryResponse> updateCategory(@PathVariable @Valid String id, @RequestBody @Valid CategoryRequest request) {
        log.info("Update category with ID: {}", id);
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteCategory(@PathVariable String id) {
        log.info("Delete category with ID: {}", id);
        var result = categoryService.deleteById(id);
        if (!result) {
            return ApiResponse.<Void>builder()
                    .code(400)
                    .message("Category have products, cannot delete")
                    .build();
        }
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<CategoryResponse>> searchCategory(@RequestParam String keyword) {
        log.info("Search category with keyword: {}", keyword);
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.search(keyword))
                .build();
    }

}
