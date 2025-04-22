package ktpm17ctt.g6.orderservice.repositories.httpClients;

import jakarta.validation.Valid;
import ktpm17ctt.g6.orderservice.dto.common.ApiResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemRequest;
import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", url = "${PRODUCT_ITEM_URL:http://localhost:8082/product/internal}")
public interface ProductItemClient {
    @GetMapping("/item/{id}")
    ApiResponse<ProductItemResponse> getProductItem(@PathVariable String id);

    @PutMapping("/item/update/{id}")
    ApiResponse<ProductItemResponse> updateProductItem(@PathVariable String id,
                                                       @RequestBody @Valid ProductItemRequest productItemRequest);
}
