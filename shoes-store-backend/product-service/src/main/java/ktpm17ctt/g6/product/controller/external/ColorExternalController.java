package ktpm17ctt.g6.product.controller.external;

import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.response.ColorResponse;
import ktpm17ctt.g6.product.service.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/external/colors")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ColorExternalController {
    ColorService colorService;

    //    public endpoints
    @GetMapping
    ApiResponse<List<ColorResponse>> getColors() {
        return ApiResponse.<List<ColorResponse>>builder()
                .result(colorService.findAll())
                .build();
    }
}
