package ktpm17ctt.g6.cart.feign;

import ktpm17ctt.g6.cart.dto.response.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.QuantityOfSizeResponse;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8083/api/product")
public interface ProductFeignClient {
    @GetMapping("/{id}")
    ProductItemResponse getProductItemById(@PathVariable("id") String productItemId);

    @GetMapping("/{id}/quantityOfSize")
    List<QuantityOfSizeResponse> getQuantityOfSize(@PathVariable("id") String productItemId);
}
