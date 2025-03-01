package ktpm17ctt.g6.product.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.request.BrandRequest;
import ktpm17ctt.g6.product.dto.response.BrandResponse;
import ktpm17ctt.g6.product.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BrandController {
    BrandService brandService;

    @GetMapping()
    ApiResponse<List<BrandResponse>> getBrands() {
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.findAll())
                .build();
    }

    @GetMapping("/{brandId}")
    ApiResponse<BrandResponse> getBrand(@PathVariable String brandId) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.findById(brandId).orElse(null))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<BrandResponse>> searchBrands(@RequestParam String keyword) {
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.search(keyword))
                .build();
    }

    @PostMapping()
    ApiResponse<BrandResponse> createBrand(@RequestBody @Valid BrandRequest brandRequest) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.save(brandRequest))
                .build();
    }

    @PutMapping("/{brandId}")
    ApiResponse<BrandResponse> updateBrand(@PathVariable String brandId,@RequestBody @Valid BrandRequest brandRequest) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.update(brandId,brandRequest))
                .build();
    }

    @DeleteMapping("/{brandId}")
    ResponseEntity<?> deleteBrand(@PathVariable String brandId) {
        brandService.deleteById(brandId);
        return ResponseEntity.ok().build();
    }


}
