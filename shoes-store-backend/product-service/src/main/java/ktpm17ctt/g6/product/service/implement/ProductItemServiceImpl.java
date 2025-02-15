package ktpm17ctt.g6.product.service.implement;

import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.entity.ProductItem;
import ktpm17ctt.g6.product.mapper.ProductItemMapper;
import ktpm17ctt.g6.product.mapper.ProductMapper;
import ktpm17ctt.g6.product.repository.ProductItemRepository;
import ktpm17ctt.g6.product.repository.ProductRepository;
import ktpm17ctt.g6.product.service.ProductItemService;
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
public class ProductItemServiceImpl implements ProductItemService {
    ProductItemRepository productItemRepository;
    ProductRepository productRepository;
    ProductItemMapper productItemMapper;
    ProductMapper productMapper;

    @Override
    public ProductItemResponse save(ProductItemRequest productItemRequest) {
        if (productItemRequest.getProductId() == null) {
            throw new IllegalArgumentException("Product id is required");
        }
        if (productRepository.findById(productItemRequest.getProductId()).isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }
        ProductItem productItem = productItemMapper.toProductItem(productItemRequest);
        productItem.setProduct(productRepository.findById(productItemRequest.getProductId()).get());
        return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
    }

    @Override
    public ProductItemResponse update(String id, ProductItemRequest productItemRequest) {
        ProductItem productItem = productItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product item not found"));
        if (productItemRequest.getProductId() != null) {
            if (productRepository.findById(productItemRequest.getProductId()).isEmpty()) {
                throw new IllegalArgumentException("Product not found");
            }
            productItem.setProduct(productRepository.findById(productItemRequest.getProductId()).get());
        }
        return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
    }

    @Override
    public void delete(String id) {
        productItemRepository.deleteById(id);
    }

    @Override
    public Optional<ProductItemResponse> findById(String id) {
        return productItemRepository.findById(id).map(productItemMapper::toProductItemResponse);
    }

    @Override
    public List<ProductItemResponse> findByProductId(String productId) {
        var products = productItemRepository.findByProductId(productId);
        return products.stream().map(productItemMapper::toProductItemResponse).toList();
    }
}
