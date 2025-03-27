package ktpm17ctt.g6.product.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.PageResponse;
import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.entity.enums.Type;
import ktpm17ctt.g6.product.service.ProductItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class ProductItemController {
    ProductItemService productItemService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<ProductItemResponse> createProductItem(@RequestBody @Valid ProductItemRequest productItemRequest) {
        log.info("Create product item: {}", productItemRequest);
        ProductItemResponse productItemResponse = productItemService.save(productItemRequest);
        return ApiResponse.<ProductItemResponse>builder()
                .result(productItemResponse)
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<ProductItemResponse> updateProductItem(@PathVariable String id, @RequestBody @Valid ProductItemRequest productItemRequest) {
        log.info("Update product item: {}", productItemRequest);
        ProductItemResponse productItemResponse = productItemService.update(id, productItemRequest);
        return ApiResponse.<ProductItemResponse>builder()
                .result(productItemResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteProductItem(@PathVariable String id) {
        log.info("Delete product item: {}", id);
        productItemService.delete(id);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ProductItemResponse> getProductItem(@PathVariable String id) {
        log.info("Get product item: {}", id);
        return ApiResponse.<ProductItemResponse>builder()
                .result(productItemService.findById(id).orElse(null))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<List<ProductItemResponse>> getProductItemsByProductId(@PathVariable String productId) {
        log.info("Get product items by product id: {}", productId);
        return ApiResponse.<List<ProductItemResponse>>builder()
                .result(productItemService.findByProductId(productId))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<PageResponse<ProductItemResponse>> searchProductItems(@RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(required = false) String productName,
                                                                      @RequestParam(required = false) Type type,
                                                                      @RequestParam(required = false) String categoryName,
                                                                      @RequestParam(required = false) String colorName,
                                                                      @RequestParam(required = false) Integer size,
                                                                      @RequestParam(required = false) Double minPrice,
                                                                      @RequestParam(required = false) Double maxPrice) {
        log.info("Search product items: productName={}, type={}, categoryName={}, colorName={}, size={}, minPrice={}, maxPrice={}",
                productName, type, categoryName, colorName, size, minPrice, maxPrice);
        return ApiResponse.<PageResponse<ProductItemResponse>>builder()
                .result(productItemService.search(page, productName, type, categoryName, colorName, size, minPrice, maxPrice))
                .build();
    }

    @GetMapping("/quantity")
    ApiResponse<Integer> getTotalQuantityByProductItemAndSize(
            @RequestParam String id,
            @RequestParam Integer size) {
        log.info("Get total quantity for productItemId={} and size={}", id, size);
        int totalQuantity = productItemService.getTotalQuantityByProductAndSize(id,size);
        return ApiResponse.<Integer>builder()
                .result(totalQuantity)
                .build();
    }

    @GetMapping("/all")
    ApiResponse<List<ProductItemResponse>> getAllProductItems() {
        log.info("Get all product items");
        return ApiResponse.<List<ProductItemResponse>>builder()
                .result(productItemService.findAll())
                .build();
    }

}
