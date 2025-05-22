package ktpm17ctt.g6.orderservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponseHasUser;
import ktpm17ctt.g6.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@FieldDefaults(makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/add-new-order")
    public ResponseEntity<?> addNewOrder(@Valid @RequestBody OrderCreationRequest request,
                                         BindingResult result,
                                         HttpServletRequest httpServletRequest){
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            DefaultMessageSourceResolvable::getDefaultMessage
                    ));

            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(orderService.save(request, httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/canceling-order/{id}")
    public ResponseEntity<?> cancelingOrder(@PathVariable String id, HttpServletRequest httpServletRequest){
        try {
            return ResponseEntity.ok(orderService.cancelingOrder(id, httpServletRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-order-by-id/{id}")
    public  ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/get-order-by-id-has-user/{id}")
    public  ResponseEntity<OrderResponseHasUser> getOrderHasUserById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(orderService.findOrderHasUserById(id).get());
    }

    @GetMapping("/get-my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders() throws Exception {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PutMapping("/update-order-status/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestParam String status, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(orderService.updateStatusForOrder(orderId, status, request));
    }


    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseHasUser>> getAllOrders() throws Exception {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
