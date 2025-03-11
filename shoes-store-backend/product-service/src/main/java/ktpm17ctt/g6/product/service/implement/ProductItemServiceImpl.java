    package ktpm17ctt.g6.product.service.implement;

    import ktpm17ctt.g6.product.dto.PageResponse;
    import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
    import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
    import ktpm17ctt.g6.product.entity.ProductItem;
    import ktpm17ctt.g6.product.entity.QuantityOfSize;
    import ktpm17ctt.g6.product.entity.enums.Type;
    import ktpm17ctt.g6.product.exception.AppException;
    import ktpm17ctt.g6.product.exception.ErrorCode;
    import ktpm17ctt.g6.product.mapper.ColorMapper;
    import ktpm17ctt.g6.product.mapper.ProductItemMapper;
    import ktpm17ctt.g6.product.mapper.ProductMapper;
    import ktpm17ctt.g6.product.repository.ColorRepository;
    import ktpm17ctt.g6.product.repository.ProductItemRepository;
    import ktpm17ctt.g6.product.repository.ProductRepository;
    import ktpm17ctt.g6.product.service.ProductItemService;
    import lombok.AccessLevel;
    import lombok.RequiredArgsConstructor;
    import lombok.experimental.FieldDefaults;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
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
        ColorRepository colorRepository;
        ProductItemMapper productItemMapper;
        ProductMapper productMapper;
        ColorMapper colorMapper;

        @Override
        public ProductItemResponse save(ProductItemRequest productItemRequest) {
            if (productItemRequest.getProductId() == null) {
                throw new AppException(ErrorCode.PRODUCT_ITEM_NOT_FOUND);
            }
            if (productRepository.findById(productItemRequest.getProductId()).isEmpty()) {
                throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
            }
            if (colorRepository.findById(productItemRequest.getColorId()).isEmpty()) {
                throw new AppException(ErrorCode.COLOR_NOT_FOUND);
            }
            ProductItem productItem = productItemMapper.toProductItem(productItemRequest);
            productItem.setColor(colorRepository.findById(productItemRequest.getColorId()).get());
            productItem.setProduct(productRepository.findById(productItemRequest.getProductId()).get());
            return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
        }

        @Override
        public ProductItemResponse update(String id, ProductItemRequest productItemRequest) {
            ProductItem productItem = productItemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ITEM_NOT_FOUND));
            if (productItemRequest.getProductId() != null) {
                if (productRepository.findById(productItemRequest.getProductId()).isEmpty()) {
                    throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
                }
                productItem.setProduct(productRepository.findById(productItemRequest.getProductId()).get());
            }
            if (productItemRequest.getPrice() > 0 && productItemRequest.getPrice() != productItem.getPrice()) {
                productItem.setPrice(productItemRequest.getPrice());
            }
            if (productItemRequest.getStatus() != null && productItemRequest.getStatus() != productItem.getStatus()) {
                productItem.setStatus(productItemRequest.getStatus());
            }
            if (productItemRequest.getQuantityOfSize() != null && productItemRequest.getQuantityOfSize() != productItem.getQuantityOfSize()) {
                productItem.setQuantityOfSize(productItemRequest.getQuantityOfSize());
            }
            if (productItemRequest.getColorId() != null && !productItemRequest.getColorId().equals(productItem.getColor().getId())) {
                productItem.setColor(colorRepository.findById(productItemRequest.getColorId()).orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND)));
            }
            // image
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
            var products = productItemRepository.findByProduct_Id(productId);
            return products.stream().map(productItemMapper::toProductItemResponse).toList();
        }

        @Override
        public PageResponse<ProductItemResponse> search(Integer page, String productName, Type type, String categoryName, String colorName, Integer size, Double minPrice, Double maxPrice) {
            Pageable pageable = PageRequest.of(page - 1, 10);
            var itemPage = productItemRepository.search(productName, type, categoryName, colorName, size, minPrice, maxPrice, pageable);

            List<ProductItemResponse> itemList = itemPage.getContent().stream()
                    .map(productItemMapper::toProductItemResponse)
                    .toList();

            return PageResponse.<ProductItemResponse>builder()
                    .currentPage(page)
                    .pageSize(10)
                    .totalPages(itemPage.getTotalPages())
                    .totalElements(itemPage.getTotalElements())
                    .data(itemList)
                    .build();
        }

        @Override
        public int getTotalQuantityByProductAndSize(String id, Integer size) {
            ProductItem productItem = productItemRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ITEM_NOT_FOUND));

            return productItem.getQuantityOfSize().stream()
                    .filter(q -> q.getSize().equals(size))
                    .mapToInt(QuantityOfSize::getQuantity)
                    .sum();
        }


    }
