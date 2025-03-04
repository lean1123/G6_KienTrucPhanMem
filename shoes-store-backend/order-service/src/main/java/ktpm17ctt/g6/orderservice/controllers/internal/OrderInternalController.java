package ktpm17ctt.g6.orderservice.controllers.internal;

import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderInternalController {
    OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(orderService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
