package ktpm17ctt.g6.product.controller.internal;

import jakarta.validation.Valid;
import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.service.ProductItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductItemInternalController {
    ProductItemService productItemService;

    @GetMapping("/internal/{id}")
    ApiResponse<ProductItemResponse> getProductItem(@PathVariable String id) {
        log.info("Get product item: {}", id);
        return ApiResponse.<ProductItemResponse>builder()
                .result(productItemService.findById(id).orElse(null))
                .build();
    }

    @PutMapping("/internal/update/{id}")
    ApiResponse<ProductItemResponse> updateProductItem(@PathVariable String id, @RequestBody @Valid ProductItemRequest productItemRequest) {
        log.info("Update product item: {}", productItemRequest);
        ProductItemResponse productItemResponse = productItemService.update(id, productItemRequest);
        return ApiResponse.<ProductItemResponse>builder()
                .result(productItemResponse)
                .build();
    }
}
