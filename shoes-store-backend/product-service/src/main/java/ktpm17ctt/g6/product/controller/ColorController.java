package ktpm17ctt.g6.product.controller;

import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.request.ColorRequest;
import ktpm17ctt.g6.product.dto.response.ColorResponse;
import ktpm17ctt.g6.product.entity.Color;
import ktpm17ctt.g6.product.mapper.ColorMapper;
import ktpm17ctt.g6.product.service.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    ApiResponse<ColorResponse> createColor(@RequestBody ColorRequest colorRequest) {
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.save(colorRequest))
                .build();
    }

    @PutMapping("/{colorId}")
    ApiResponse<ColorResponse> updateColor(@PathVariable String colorId,@RequestBody ColorRequest colorRequest) {
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.update(colorId, colorRequest))
                .build();
    }

    @DeleteMapping("/{colorId}")
    ApiResponse<String> deleteColor(@PathVariable String colorId) {
        colorService.delete(colorId);
        return ApiResponse.<String>builder().result("Color has been deleted").build();
    }

    @GetMapping("/{colorId}")
    ApiResponse<ColorResponse> getColor(@PathVariable String colorId) {
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.findById(colorId).orElse(null))
                .build();
    }

    @GetMapping
    ApiResponse<List<ColorResponse>> getColors() {
        return ApiResponse.<List<ColorResponse>>builder()
                .result(colorService.findAll())
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<ColorResponse>> searchColors(@RequestParam String keyword) {
        return ApiResponse.<List<ColorResponse>>builder()
                .result(colorService.findByName(keyword))
                .build();
    }
}
