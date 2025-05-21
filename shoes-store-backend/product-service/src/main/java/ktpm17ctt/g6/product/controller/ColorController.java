package ktpm17ctt.g6.product.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.request.ColorRequest;
import ktpm17ctt.g6.product.dto.response.ColorResponse;
import ktpm17ctt.g6.product.service.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colors")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ColorController {
    ColorService colorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<ColorResponse> createColor(@RequestBody @Valid ColorRequest colorRequest) {
        log.info("Create new color: {}", colorRequest.getName());
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.save(colorRequest))
                .build();
    }

    @PutMapping("/{colorId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<ColorResponse> updateColor(@PathVariable String colorId,@RequestBody @Valid ColorRequest colorRequest) {
        log.info("Update color with ID: {}", colorId);
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.update(colorId, colorRequest))
                .build();
    }

    @DeleteMapping("/{colorId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> deleteColor(@PathVariable String colorId) {
        log.info("Delete color with ID: {}", colorId);
        colorService.delete(colorId);
        return ApiResponse.<String>builder().result("Color has been deleted").build();
    }

    @GetMapping("/{colorId}")
    ApiResponse<ColorResponse> getColor(@PathVariable String colorId) {
        log.info("Get color with ID: {}", colorId);
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.findById(colorId).orElse(null))
                .build();
    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<ColorResponse>> searchColors(@RequestParam String keyword) {
        log.info("Search colors with keyword: {}", keyword);
        return ApiResponse.<List<ColorResponse>>builder()
                .result(colorService.search(keyword))
                .build();
    }

}
