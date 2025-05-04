package iuh.fit.se.chatService.feinClients;

import iuh.fit.se.chatService.dtos.common.ApiResponse;
import iuh.fit.se.chatService.dtos.common.PageResponse;
import iuh.fit.se.chatService.dtos.product.ProductItemResponse;
import iuh.fit.se.chatService.dtos.product.Type;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", url = "${PRODUCT_ITEM_URL:http://localhost:8082/product/internal}")
public interface ProductItemClient {
    @GetMapping("/item/search")
    ApiResponse<PageResponse<ProductItemResponse>> searchProductItems(@RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(required = false) String productName,
                                                                      @RequestParam(required = false) Type type,
                                                                      @RequestParam(required = false) String categoryName,
                                                                      @RequestParam(required = false) String colorName,
                                                                      @RequestParam(required = false) Integer size,
                                                                      @RequestParam(required = false) Double minPrice,
                                                                      @RequestParam(required = false) Double maxPrice);
}
