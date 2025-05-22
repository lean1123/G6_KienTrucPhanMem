package ktpm17ctt.g6.cart.feign;

import ktpm17ctt.g6.cart.configuration.FeignClientConfig;


import java.util.List;


import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.product.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.product.ProductItemResponseHasLikes;
import ktpm17ctt.g6.cart.dto.response.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "${PRODUCT_SERVICE_URL:http://localhost:8082/product}", configuration = FeignClientConfig.class)
public interface ProductFeignClient {
    @GetMapping("/internal/item/{id}")
    ApiResponse<ProductItemResponseHasLikes> getProductItemById(@PathVariable("id") String id);



    @GetMapping("/internal/item/quantity")
    ApiResponse<Integer> getTotalQuantityByProductItemAndSize(
            @RequestParam("id") String id,
            @RequestParam("size") Integer size
    );


    @GetMapping("/internal/item/all")
    ApiResponse<List<ProductItemResponse>> getAllProductItems();

    @GetMapping("/products/{id}")
    ApiResponse<ProductResponse> getProduct(@PathVariable("id") String id);

}
