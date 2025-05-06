package iuh.fit.se.chatService.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import iuh.fit.se.chatService.dtos.AIResponse;
import iuh.fit.se.chatService.dtos.SearchIntentDTO;
import iuh.fit.se.chatService.dtos.common.ApiResponse;
import iuh.fit.se.chatService.dtos.common.PageResponse;
import iuh.fit.se.chatService.dtos.product.ProductItemResponse;
import iuh.fit.se.chatService.services.GeminiAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final GeminiAIService geminiAIService;

    @PostMapping("/analyze")
    public ResponseEntity<AIResponse> analyze(@RequestBody Map<String, String> body) {
        AIResponse aiResponse = null;
        aiResponse = geminiAIService.searchProductsByIntent(body.get("message"));
        return ResponseEntity.ok(aiResponse);
    }
}
