package iuh.fit.se.chatService.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.chatService.dtos.AIResponse;
import iuh.fit.se.chatService.dtos.SearchIntentDTO;
import iuh.fit.se.chatService.dtos.common.ApiResponse;
import iuh.fit.se.chatService.dtos.common.PageResponse;
import iuh.fit.se.chatService.dtos.product.ProductItemResponse;
import iuh.fit.se.chatService.dtos.product.Type;
import iuh.fit.se.chatService.feinClients.ProductItemClient;
import iuh.fit.se.chatService.services.GeminiAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAIServiceImpl implements GeminiAIService {
    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;
    private final ProductItemClient productItemClient;


    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public AIResponse searchProductsByIntent(String userMessage) {
        SearchIntentDTO intentDTO = null;
        try {
            intentDTO = this.extractIntent(userMessage);
            log.info("Extracted intent: {}", intentDTO);
        }catch (IOException e) {
            log.error("Error processing JSON: {}", e.getMessage());
        }finally {
            if (intentDTO == null) {
                return null;
            }
        }

        List<ProductItemResponse> productItemResponses = productItemClient.searchProductItems(
                1,
                null,
                !intentDTO.getType().trim().isEmpty() ? Type.valueOf(intentDTO.getType()) : null,
                null,
                !intentDTO.getColor().trim().isEmpty() ? intentDTO.getColor() : null,
                !intentDTO.getSize().isEmpty() ? Integer.valueOf(intentDTO.getSize().get(0)) : null,
                intentDTO.getPriceMin() == 0 ? null : Double.valueOf(String.valueOf(intentDTO.getPriceMin())),
                intentDTO.getPriceMax() == 0 ? null : Double.valueOf(String.valueOf(intentDTO.getPriceMax()))
        ).getResult().getData();

        return AIResponse.builder()
                .results(productItemResponses)
                .message(intentDTO.getMessage())
                .build();
    }

    private SearchIntentDTO extractIntent(String userMessage) throws IOException {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + GEMINI_API_KEY;
        Map<String, Object> requestBody = Map.of("contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text",
                prompt(userMessage))))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

        // Parse kết quả từ Gemini
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
        String json = parts.get(0).get("text");
        String cleanedJson = json.replaceAll("(?s)```json\\s*", "")  // xóa phần mở đầu ```json
                .replaceAll("(?s)```", "")          // xóa phần kết thúc ```
                .trim();
        log.info("Cleaned JSON: {}", cleanedJson);
        ObjectMapper mapper = new ObjectMapper();
        SearchIntentDTO intent = mapper.readValue(cleanedJson, SearchIntentDTO.class);


        return intent;
    }

//    private String prompt(String userMessage) {
//        return String.format("""
//                Bạn là một trợ lý AI giúp người dùng tìm kiếm giày.
//
//                Câu hỏi: "%s"
//
//                Hãy phân tích câu hỏi trên và trả về kết quả dưới dạng JSON **CHÍNH XÁC** theo định dạng sau (đừng ghi thêm gì ngoài JSON):
//
//                    {
//                     "intent": "search_product",           	// luôn là 'search_product'
//                     "color": "màu của sản phẩm",          	// ví dụ: "RED", "WHITE" (luôn là chữ in và viết bằng tiếng anh) và "" nếu không có
//                     "priceMax": số_nguyên,                	// nếu không có thì trả về 0
//                     "priceMin": số_nguyên,              	// nếu không có thì trả về 0
//                     "size": ["danh sách kích cỡ có thể gợi ý"],    		// luôn là mảng, có thể rỗng
//                     "type": loại sản phầm dành cho nam hay nữ	// Chỉ trả về giá trị thuộc "MALE", "FEMALE", "CHILDREN" hoặc ""
//                   }
//
//                Chỉ trả về JSON hợp lệ.
//                """, userMessage);
//    }

    private String prompt(String userMessage) throws IOException {
        log.info("User message: {}", userMessage);
        String promptFromPdf = readPromptFromPdf("chatService/promts/product-assistant-promt.pdf");
        log.info("Prompt from PDF: {}", promptFromPdf);
        return String.format(promptFromPdf, userMessage);
    }

    public static String readPromptFromPdf(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Không tìm thấy file: " + file.getAbsolutePath());
        }
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được PDF prompt", e);
        }
    }


}
