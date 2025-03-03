package ktpm17ctt.g6.product.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.request.ProductRequest;
import ktpm17ctt.g6.product.dto.response.ProductResponse;
import ktpm17ctt.g6.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ProductController {
    ProductService productService;

    @PostMapping()
    ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        log.info("Create product");
        ProductResponse productResponse = productService.save(productRequest);
        return ApiResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ProductResponse> updateProduct(@PathVariable String id, @RequestBody @Valid ProductRequest productRequest) {
        log.info("Update product");
        ProductResponse productResponse = productService.update(id, productRequest);
        return ApiResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteProduct(@PathVariable String id) {
        log.info("Delete product");
        productService.delete(id);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ProductResponse> getProduct(@PathVariable String id) {
        log.info("Get product");
        ProductResponse productResponse = productService.findById(id).orElse(null);
        return ApiResponse.<ProductResponse>builder()
                .result(productResponse)
                .build();
    }

    @GetMapping()
    ApiResponse<List<ProductResponse>> getAllProducts() {
        log.info("Get all products");
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findAll())
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        log.info("Search products");
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.search(keyword))
                .build();
    }
}
