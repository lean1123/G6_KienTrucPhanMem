package ktpm17ctt.g6.orderservice.controllers;

import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderRequest;
import ktpm17ctt.g6.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    OrderService orderService;

    @PostMapping("/add-new-order")
    public ResponseEntity<?> addNewOrder(@RequestBody OrderCreationRequest request){
        try {
            return ResponseEntity.ok(orderService.save(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
