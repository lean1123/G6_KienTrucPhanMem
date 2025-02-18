package ktpm17ctt.g6.orderservice.repositories.httpClients;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.ktpm17ctt.g6.commondto.dtos.ApiResponse;
import java.ktpm17ctt.g6.commondto.dtos.requests.ProductItemRequest;
import java.ktpm17ctt.g6.commondto.dtos.responses.ProductItemResponse;

@FeignClient(name = "product-service", url = "http://localhost:8083/product/item")
public interface ProductItemClient {
    @GetMapping("/internal/{id}")
    ApiResponse<ProductItemResponse> getProductItem(@PathVariable String id);

    @PutMapping("/internal/update/{id}")
    ApiResponse<ProductItemResponse> updateProductItem(@PathVariable String id,
                                                       @RequestBody @Valid ProductItemRequest productItemRequest);
}
