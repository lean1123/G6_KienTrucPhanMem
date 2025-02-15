package ktpm17ctt.g6.product.service.implement;

import ktpm17ctt.g6.product.dto.request.BrandRequest;
import ktpm17ctt.g6.product.dto.response.BrandResponse;
import ktpm17ctt.g6.product.entity.Brand;
import ktpm17ctt.g6.product.mapper.BrandMapper;
import ktpm17ctt.g6.product.repository.BrandRepository;
import ktpm17ctt.g6.product.service.BrandService;
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
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;
    BrandMapper brandMapper;

    @Override
    public Optional<BrandResponse> findById(String id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if (brand == null) {
            log.error("Brand with id {} not found", id);
        }
        return Optional.ofNullable(brandMapper.toBrandResponse(brand));
    }

    @Override
    public Optional<BrandResponse> findByName(String name) {
        Brand brand = brandRepository.findByName(name);
        if (brand == null) {
            log.error("Brand with name {} not found", name);
        }
        return Optional.ofNullable(brandMapper.toBrandResponse(brand));
    }

    @Override
    public BrandResponse save(BrandRequest brandRequest) {
        Brand brand = brandMapper.toBrand(brandRequest);
        brand = brandRepository.save(brand);
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public BrandResponse update(String id, BrandRequest brandRequest) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if (brand == null) {
            log.error("Brand with id {} not found", id);
            return null;
        }
        brand = brandMapper.toBrand(brandRequest);
        brand.setId(id);
        brand = brandRepository.save(brand);
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public void deleteById(String id) {
        brandRepository.deleteById(id);
    }

    @Override
    public List<BrandResponse> findAll() {
        var brands = brandRepository.findAll();
        return brands.stream().map(brandMapper::toBrandResponse).toList();
    }

    @Override
    public List<BrandResponse> search(String keyword) {
        return List.of();
    }
}
