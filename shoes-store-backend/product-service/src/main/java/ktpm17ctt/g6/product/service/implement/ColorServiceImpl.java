package ktpm17ctt.g6.product.service.implement;

import ktpm17ctt.g6.product.dto.request.ColorRequest;
import ktpm17ctt.g6.product.dto.response.ColorResponse;
import ktpm17ctt.g6.product.entity.Color;
import ktpm17ctt.g6.product.mapper.ColorMapper;
import ktpm17ctt.g6.product.repository.ColorRepository;
import ktpm17ctt.g6.product.service.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class ColorServiceImpl implements ColorService {
    ColorRepository colorRepository;
    ColorMapper colorMapper;

    @Override
    public ColorResponse save(ColorRequest colorRequest) {
        Color color = colorMapper.toColor(colorRequest);
        color = colorRepository.save(color);
        return colorMapper.toColorResponse(color);
    }

    @Override
    public ColorResponse update(String id, ColorRequest colorRequest) {
        Color color = colorRepository.findById(id).orElse(null);
        if (color == null) {
            return null;
        }
        color = colorMapper.toColor(colorRequest);
        color.setId(id);
        color = colorRepository.save(color);
        return colorMapper.toColorResponse(color);
    }

    @Override
    public void delete(String id) {
        colorRepository.deleteById(id);
    }

    @Override
    public Optional<ColorResponse> findById(String id) {
        Color color = colorRepository.findById(id).orElse(null);
        if (color == null) {
            return Optional.empty();
        }
        return Optional.of(colorMapper.toColorResponse(color));
    }

    @Override
    public List<ColorResponse> findAll() {
        var colors = colorRepository.findAll();
        return colors.stream().map(colorMapper::toColorResponse).toList();
    }

    @Override
    public List<ColorResponse> search(String keyword) {
        var colors = colorRepository.searchByNameOrCode(keyword);
        return colors.stream().map(colorMapper::toColorResponse).toList();
    }
}
