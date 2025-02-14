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
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.findAll())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<CategoryResponse> getCategoryById(@PathVariable String id) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.findById(id).orElse(null))
                .build();
    }

    @PostMapping()
    ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.save(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<CategoryResponse> updateCategory(@PathVariable @Valid String id, @RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteById(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/search")
    ApiResponse<List<CategoryResponse>> searchCategory(@RequestParam String keyword) {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.search(keyword))
                .build();
    }

}
