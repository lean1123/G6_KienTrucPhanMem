package ktpm17ctt.g6.product.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.request.ProductCollectionRequest;
import ktpm17ctt.g6.product.dto.response.ProductCollectionResponse;
import ktpm17ctt.g6.product.service.ProductCollectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ProductCollectionController {
    ProductCollectionService productCollectionService;

    @PostMapping
    ApiResponse<ProductCollectionResponse> createCollection(@RequestBody @Valid ProductCollectionRequest request) {
        return ApiResponse.<ProductCollectionResponse>builder()
                .result(productCollectionService.save(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ProductCollectionResponse> updateCollection(@PathVariable String id, @RequestBody @Valid ProductCollectionRequest request) {
        return ApiResponse.<ProductCollectionResponse>builder()
                .result(productCollectionService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteCollection(@PathVariable String id) {
        productCollectionService.delete(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/{id}")
    ApiResponse<ProductCollectionResponse> getCollection(@PathVariable String id) {
        return ApiResponse.<ProductCollectionResponse>builder()
                .result(productCollectionService.findById(id).orElse(null))
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductCollectionResponse>> getAllCollections() {
        return ApiResponse.<List<ProductCollectionResponse>>builder()
                .result(productCollectionService.findAll())
                .build();
    }

    @GetMapping("/brand/{brandId}")
    ApiResponse<List<ProductCollectionResponse>> getCollectionsByBrand(@PathVariable String brandId) {
        return ApiResponse.<List<ProductCollectionResponse>>builder()
                .result(productCollectionService.findAllByBrandId(brandId))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<ProductCollectionResponse>> searchCollections(@RequestParam String keyword, @RequestParam String brandId) {
        return ApiResponse.<List<ProductCollectionResponse>>builder()
                .result(productCollectionService.search(keyword))
                .build();
    }
}
