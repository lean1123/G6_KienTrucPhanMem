package ktpm17ctt.g6.gateway.controller;

import ktpm17ctt.g6.gateway.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping(value="/identity")
    public ApiResponse identityFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Identity Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/user")
    public ApiResponse userFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("User Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/product")
    public ApiResponse productFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Product Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/orders")
    public ApiResponse orderFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Order Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/payments")
    public ApiResponse paymentFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Payment Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/cart")
    public ApiResponse cartFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Cart Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/notification")
    public ApiResponse notificationFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Notification Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/review")
    public ApiResponse reviewFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Review Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/chat")
    public ApiResponse chatFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Chat Service is temporarily unavailable. Please try again later.")
                .build();
    }

    @RequestMapping("/recommendation")
    public ApiResponse recommendationFallback() {
        return ApiResponse.builder()
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message("Recommendation Service is temporarily unavailable. Please try again later.")
                .build();
    }
}
