package ktpm17ctt.g6.paymentservice.repositories.httpClients;


import ktpm17ctt.g6.paymentservice.dtos.feinClient.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "http://localhost:8088/internal/orders")
public interface OrderClient {
    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> getOrderById(@PathVariable String id);
}
