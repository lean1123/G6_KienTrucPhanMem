package ktpm17ctt.g6.product.service.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ktpm17ctt.g6.product.dto.PageResponse;
import ktpm17ctt.g6.product.dto.fein.user.UserResponse;
import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.request.ProductItemUpdationRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.dto.response.ProductItemResponseHasLikes;
import ktpm17ctt.g6.product.entity.ProductItem;
import ktpm17ctt.g6.product.entity.QuantityOfSize;
import ktpm17ctt.g6.product.entity.enums.Type;
import ktpm17ctt.g6.product.exception.AppException;
import ktpm17ctt.g6.product.exception.ErrorCode;
import ktpm17ctt.g6.product.exception.NotFoundException;
import ktpm17ctt.g6.product.mapper.ColorMapper;
import ktpm17ctt.g6.product.mapper.ProductItemMapper;
import ktpm17ctt.g6.product.mapper.ProductMapper;
import ktpm17ctt.g6.product.repository.ColorRepository;
import ktpm17ctt.g6.product.repository.ProductItemRepository;
import ktpm17ctt.g6.product.repository.ProductRepository;
import ktpm17ctt.g6.product.repository.fein.UserClient;
import ktpm17ctt.g6.product.service.ProductItemService;
import ktpm17ctt.g6.product.service.S3Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    UserClient userClient;

    S3Service s3Service;

    @Transactional
    @Override
    public ProductItemResponse save(ProductItemRequest productItemRequest) {
        if (productRepository.findById(productItemRequest.getProductId()).isEmpty()) {
            throw new NotFoundException("Product ID", productItemRequest.getProductId());
        }
        if (colorRepository.findById(productItemRequest.getColorId()).isEmpty()) {
            throw new NotFoundException("Color ID", productItemRequest.getColorId());
        }
        ProductItem productItem = productItemMapper.toProductItem(productItemRequest);
        productItem.setQuantityOfSize(convertJsonToQuantityOfSize(productItemRequest.getQuantityOfSize()));
        productItem.setColor(colorRepository.findById(productItemRequest.getColorId()).get());
        productItem.setProduct(productRepository.findById(productItemRequest.getProductId()).get());
        log.info("Saving productItem: {}", productItem);
        if (productItemRequest.getImages() != null && !productItemRequest.getImages().isEmpty()) {
            log.info("Product item: {}", productItem.getProduct().getId());
            try {
                List<String> uploadResult = s3Service.uploadFiles(productItemRequest.getImages());
                productItem.setImages(uploadResult);
            } catch (Exception e) {
                throw new AppException(ErrorCode.ITEM_IMAGE_CANT_UPLOAD);
            }
        }
        return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
    }

    @Override
    public ProductItemResponse update(String id, ProductItemRequest productItemRequest) {
        ProductItem productItem = productItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Item ID", id));
        if (productItemRequest.getProductId() != null) {
            if (productRepository.findById(productItemRequest.getProductId()).isEmpty()) {
                throw new NotFoundException("Product ID", productItemRequest.getProductId());
            }
            productItem.setProduct(productRepository.findById(productItemRequest.getProductId()).get());
        }
        if (productItemRequest.getPrice() > 0 && productItemRequest.getPrice() != productItem.getPrice()) {
            productItem.setPrice(productItemRequest.getPrice());
        }
        if (productItemRequest.getStatus() != null && productItemRequest.getStatus() != productItem.getStatus()) {
            productItem.setStatus(productItemRequest.getStatus());
        }
        if (productItemRequest.getQuantityOfSize() != null && convertJsonToQuantityOfSize(productItemRequest.getQuantityOfSize()) != productItem.getQuantityOfSize()) {
            productItem.setQuantityOfSize(convertJsonToQuantityOfSize(productItemRequest.getQuantityOfSize()));
        }
        if (productItemRequest.getColorId() != null && !productItemRequest.getColorId().equals(productItem.getColor().getId())) {
            productItem.setColor(colorRepository.findById(productItemRequest.getColorId()).orElseThrow(() -> new NotFoundException("Color ID", productItemRequest.getColorId())));
        }
        // image
        var files = productItemRequest.getImages();

        if (files == null || files.isEmpty()) {
            productItem.setImages(productItem.getImages());
        } else {
            List<MultipartFile> nonEmptyFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    nonEmptyFiles.add(file);
                }
            }

            if (nonEmptyFiles.size() == 0) {
                productItem.setImages(productItem.getImages());
                return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
            }

            try {
                List<String> uploadResult = s3Service.uploadFiles(productItemRequest.getImages());
                productItem.setImages(uploadResult);
            } catch (Exception e) {
                throw new AppException(ErrorCode.ITEM_IMAGE_INVALID);
            }
        }

        productItem.setId(id);

        return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
    }


    @Override
    public ProductItemResponse updateQuantity(String id, ProductItemUpdationRequest productItemRequest) {
        ProductItem productItem = productItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Item ID", id));
        if (productItemRequest.getQuantityOfSize() != null) {
            List<QuantityOfSize> quantityOfSizes = convertJsonToQuantityOfSize(productItemRequest.getQuantityOfSize());
            for (QuantityOfSize quantityOfSize : quantityOfSizes) {
                for (QuantityOfSize existingQuantity : productItem.getQuantityOfSize()) {
                    if (existingQuantity.getSize().equals(quantityOfSize.getSize())) {
                        existingQuantity.setQuantity(quantityOfSize.getQuantity());
                    }
                }
            }
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
    public Optional<ProductItemResponseHasLikes> findProdItemHasLikeById(String id) {
        return productItemRepository.findById(id).map(productItemMapper::toProductItemResponseHasLikes);
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

        log.info("Length of product item list: {}", itemList.size());

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
                .orElseThrow(() -> new RuntimeException("ffh"));

        return productItem.getQuantityOfSize().stream()
                .filter(q -> q.getSize().equals(size))
                .mapToInt(QuantityOfSize::getQuantity)
                .sum();
    }

    @Override
    public PageResponse<ProductItemResponse> newestProductItems(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var itemPage = productItemRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<ProductItemResponse> itemList = itemPage.getContent().stream()
                .map(productItemMapper::toProductItemResponse)
                .toList();
        return PageResponse.<ProductItemResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(itemPage.getTotalPages())
                .totalElements(itemPage.getTotalElements())
                .data(itemList)
                .build();
    }

    @Override
    public List<ProductItemResponse> findAll() {
        var list = productItemRepository.findAll();
        return list.stream().map(productItemMapper::toProductItemResponse).toList();

    }

    private List<QuantityOfSize> convertJsonToQuantityOfSize(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<QuantityOfSize>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Invalid quantityOfSize JSON format", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductItemResponse likeProduct(String id) {

        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String accountId = authentication.getToken().getClaimAsString("accountId");

        UserResponse userResponse = userClient.getUserByAccountId(accountId).getResult();

        ProductItem productItem = productItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Item ID", id));

        List<String> likes = productItem.getLikes();

        if (likes == null){
            likes = new ArrayList<>();
        }

        if (likes.contains(userResponse.getId())) {
            return productItemMapper.toProductItemResponse(productItem);
        } else {
            likes.add(userResponse.getId());
        }

        productItem.setLikes(likes);
        return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
    }

    @Override
    public ProductItemResponse unlikeProduct(String id) {
        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String accountId = authentication.getToken().getClaimAsString("accountId");

        UserResponse userResponse = userClient.getUserByAccountId(accountId).getResult();

        ProductItem productItem = productItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Item ID", id));

        List<String> likes = productItem.getLikes();

        if (likes == null){
            likes = new ArrayList<>();
        }

        if (!likes.contains(userResponse.getId())) {
            return productItemMapper.toProductItemResponse(productItem);
        } else {
            likes.remove(userResponse.getId());
        }

        productItem.setLikes(likes);
        return productItemMapper.toProductItemResponse(productItemRepository.save(productItem));
    }



}
