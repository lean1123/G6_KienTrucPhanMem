package ktpm17ctt.g6.orderservice.controllers.internal;

import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

    @PostMapping("/update-order-for-payment-failed/{orderId}")
    public ResponseEntity<OrderResponse> handleUpdateOrderForPaymentFailed(@PathVariable String orderId) {
        try {
            return ResponseEntity.ok(orderService.handleUpdateOrderForPaymentFailed(orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/update-payment-status/{orderId}")
    public ResponseEntity<OrderResponse> updatePaymentStatusForOrder(@PathVariable String orderId, @RequestParam boolean isPayed) {
        try {
            return ResponseEntity.ok(orderService.updatePaymentStatusForOrder(orderId, isPayed));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
