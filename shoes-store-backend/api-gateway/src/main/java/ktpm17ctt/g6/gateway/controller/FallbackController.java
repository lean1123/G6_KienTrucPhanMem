package ktpm17ctt.g6.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/identity")
    public ResponseEntity<String> identityFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Identity Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/product")
    public ResponseEntity<String> productFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Product Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/order")
    public ResponseEntity<String> orderFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Order Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/payment")
    public ResponseEntity<String> paymentFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Payment Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/cart")
    public ResponseEntity<String> cartFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Cart Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/notification")
    public ResponseEntity<String> notificationFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Notification Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/review")
    public ResponseEntity<String> reviewFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Review Service is temporarily unavailable. Please try again later.");
    }
}
