package ktpm17ctt.g6.cart.feign;

import ktpm17ctt.g6.cart.configuration.FeignClientConfig;
import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.ProductResponse;
import ktpm17ctt.g6.cart.dto.response.QuantityOfSizeResponse;

import java.util.List;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "http://localhost:8082/product", configuration = FeignClientConfig.class)
public interface ProductFeignClient {
    @GetMapping("/item/{id}")
    ApiResponse<ProductItemResponse> getProductItemById(@PathVariable("id") String id);



    @GetMapping("/item/quantity")
    ApiResponse<Integer> getTotalQuantityByProductItemAndSize(
            @RequestParam("id") String id,
            @RequestParam("size") Integer size
    );


    @GetMapping("/item/all")
    ApiResponse<List<ProductItemResponse>> getAllProductItems();

    @GetMapping("/products/{id}")
    ApiResponse<ProductResponse> getProduct(@PathVariable("id") String id);

}
