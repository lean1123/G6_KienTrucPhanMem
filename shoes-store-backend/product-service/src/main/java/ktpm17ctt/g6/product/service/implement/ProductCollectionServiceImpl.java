package ktpm17ctt.g6.product.service.implement;

import ktpm17ctt.g6.product.dto.request.ProductCollectionRequest;
import ktpm17ctt.g6.product.dto.response.ProductCollectionResponse;
import ktpm17ctt.g6.product.entity.Brand;
import ktpm17ctt.g6.product.entity.ProductCollection;
import ktpm17ctt.g6.product.mapper.BrandMapper;
import ktpm17ctt.g6.product.mapper.ProductCollectionMapper;
import ktpm17ctt.g6.product.repository.BrandRepository;
import ktpm17ctt.g6.product.repository.ProductCollectionRepository;
import ktpm17ctt.g6.product.service.ProductCollectionService;
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
public class ProductCollectionServiceImpl implements ProductCollectionService {
    ProductCollectionRepository productCollectionRepository;
    BrandRepository brandRepository;
    ProductCollectionMapper productCollectionMapper;
    BrandMapper brandMapper;

    @Override
    public ProductCollectionResponse save(ProductCollectionRequest request) {
        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow();
        ProductCollection productCollection = productCollectionMapper.toProductCollection(request);
        productCollection.setBrand(brand);
        return productCollectionMapper.toProductCollectionResponse(productCollectionRepository.save(productCollection));
    }

    @Override
    public ProductCollectionResponse update(String id, ProductCollectionRequest request) {
        ProductCollection productCollection = productCollectionRepository.findById(id).orElseThrow();
        productCollection = productCollectionMapper.toProductCollection(request);
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow();
            productCollection.setBrand(brand);
        }
        return productCollectionMapper.toProductCollectionResponse(productCollectionRepository.save(productCollection));
    }

    @Override
    public void delete(String id) {
        productCollectionRepository.deleteById(id);
    }

    @Override
    public Optional<ProductCollectionResponse> findById(String id) {
        return productCollectionRepository.findById(id).map(productCollectionMapper::toProductCollectionResponse);
    }

    @Override
    public List<ProductCollectionResponse> findAll() {
        var productCollections = productCollectionRepository.findAll();
        return productCollections.stream().map(productCollectionMapper::toProductCollectionResponse).toList();
    }

    @Override
    public List<ProductCollectionResponse> findAllByBrandId(String brandId) {
        var productCollections = productCollectionRepository.findByBrand_Id(brandId);
        return productCollections.stream().map(productCollectionMapper::toProductCollectionResponse).toList();
    }

    @Override
    public List<ProductCollectionResponse> search(String keyword) {
        return List.of();
    }
}
