package iuh.fit.se.chatService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import iuh.fit.se.chatService.dtos.AIResponse;
import iuh.fit.se.chatService.dtos.SearchIntentDTO;
import iuh.fit.se.chatService.dtos.common.ApiResponse;
import iuh.fit.se.chatService.dtos.common.PageResponse;
import iuh.fit.se.chatService.dtos.product.ProductItemResponse;

import java.util.List;

public interface GeminiAIService {
    AIResponse searchProductsByIntent(String userMessage);
}
