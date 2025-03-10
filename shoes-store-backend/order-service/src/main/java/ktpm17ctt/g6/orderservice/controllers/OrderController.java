package ktpm17ctt.g6.orderservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderRequest;
import ktpm17ctt.g6.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@FieldDefaults(makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/add-new-order")
    public ResponseEntity<?> addNewOrder(@RequestBody OrderCreationRequest request,
                                         HttpServletRequest httpServletRequest){
        try {
            return ResponseEntity.ok(orderService.save(request, httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/canceling-order/{id}")
    public ResponseEntity<?> cancelingOrder(@PathVariable String id, HttpServletRequest httpServletRequest){
        try {
            return ResponseEntity.ok(orderService.canclingOrder(id, httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
