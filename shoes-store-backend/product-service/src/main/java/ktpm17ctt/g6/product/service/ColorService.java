package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.request.ColorRequest;
import ktpm17ctt.g6.product.dto.response.ColorResponse;

import java.util.List;
import java.util.Optional;

public interface ColorService {
    ColorResponse save(ColorRequest colorRequest);
    ColorResponse update(String id, ColorRequest colorRequest);
    boolean delete(String id);
    Optional<ColorResponse> findById(String id);
    List<ColorResponse> findAll();
    List<ColorResponse> search(String keyword);
}
