package ktpm17ctt.g6.product.service.implement;

import ktpm17ctt.g6.product.dto.request.ProductRequest;
import ktpm17ctt.g6.product.dto.response.ProductResponse;
import ktpm17ctt.g6.product.entity.Category;
import ktpm17ctt.g6.product.entity.Product;
import ktpm17ctt.g6.product.entity.ProductCollection;
import ktpm17ctt.g6.product.mapper.CategoryMapper;
import ktpm17ctt.g6.product.mapper.ProductCollectionMapper;
import ktpm17ctt.g6.product.mapper.ProductMapper;
import ktpm17ctt.g6.product.repository.CategoryRepository;
import ktpm17ctt.g6.product.repository.ProductCollectionRepository;
import ktpm17ctt.g6.product.repository.ProductRepository;
import ktpm17ctt.g6.product.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductCollectionRepository productCollectionRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    ProductCollectionMapper productCollectionMapper;
    CategoryMapper categoryMapper;
    @Override
    public ProductResponse save(ProductRequest productRequest) {
        ProductCollection productCollection = productCollectionRepository.findById(productRequest.getCollectionId()).orElseThrow();
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow();
        Product product = productMapper.toProduct(productRequest);
        product.setCategory(category);
        product.setCollection(productCollection);
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse update(String id, ProductRequest productRequest) {
        ProductCollection productCollection = productCollectionRepository.findById(productRequest.getCollectionId()).orElseThrow();
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow();
        Product product = productRepository.findById(id).orElseThrow();
        product = productMapper.toProduct(productRequest);
        product.setCategory(category);
        product.setCollection(productCollection);
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<ProductResponse> findById(String id) {
        Product product = productRepository.findById(id).orElseThrow();
        return Optional.of(productMapper.toProductResponse(product));
    }

    @Override
    public List<ProductResponse> findAll() {
        var products = productRepository.findAll();
        return products.stream().map(productMapper::toProductResponse).toList();
    }
}
