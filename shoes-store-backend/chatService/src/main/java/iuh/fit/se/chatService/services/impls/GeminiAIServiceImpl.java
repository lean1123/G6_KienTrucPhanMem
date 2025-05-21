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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAIServiceImpl implements GeminiAIService {
    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;
    private final ProductItemClient productItemClient;

    @Value("${promts.path}")
    private String PROMTS_PATH;


    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public AIResponse searchProductsByIntent(String userMessage) {
        SearchIntentDTO intentDTO = null;
        try {
            intentDTO = this.extractIntent(userMessage);
            log.info("Extracted intent: {}", intentDTO);
        } catch (IOException e) {
            log.error("Error processing JSON: {}", e.getMessage());
        } finally {
            if (intentDTO == null) {
                return null;
            }
        }

        List<ProductItemResponse> productItemResponses = new ArrayList<>();

        if (intentDTO.getIntent().equals("search_product")) {
            productItemResponses = productItemClient.searchProductItems(1, null,
                    !intentDTO.getType().trim().isEmpty() ? Type.valueOf(intentDTO.getType()) : null,
                    intentDTO.getCategoryName().trim().isEmpty() ? null : intentDTO.getCategoryName(),
                    !intentDTO.getColor().trim().isEmpty() ? intentDTO.getColor() : null,
                    !intentDTO.getSize().isEmpty() ? Integer.valueOf(intentDTO.getSize().get(0)) : null,
                    intentDTO.getPriceMin() == 0 ? null : Double.valueOf(String.valueOf(intentDTO.getPriceMin())),
                    intentDTO.getPriceMax() == 0 ? null : Double.valueOf(String.valueOf(intentDTO.getPriceMax()))).getResult().getData();
        }

        return AIResponse.builder()
                .results(productItemResponses)
                .message(intentDTO.getMessage())
                .intent(intentDTO.getIntent())
                .build();
    }

    private SearchIntentDTO extractIntent(String userMessage) throws IOException {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + GEMINI_API_KEY;
        Map<String, Object> requestBody = Map.of("contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", prompt(userMessage))))));

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

    private String prompt(String userMessage) {
        return String.format("""
                 Bạn là một trợ lý AI giúp người dùng tìm kiếm giày và tư vấn chọn size.
                Dưới đây là câu hỏi hoặc thông tin từ người dùng: "%s"
                Bạn sẽ được cung cấp câu hỏi của người dùng do hệ thống gửi kèm. Khi người dùng
                đề cập đến việc chọn size (ví dụ: hỏi size phù hợp với chiều dài chân, size quốc tế, size
                US/EU, cm, v.v.), hãy sử dụng thông tin từ ảnh đó để gợi ý size phù hợp nhất.
                Trong trường hợp người dùng thông báo rằng sản phẩm mà bạn đã gợi hết hàng thì bạn nên
                gợi ý người dùng tham khảo các sản phẩm mà tôi đã gửi cùng tin nhắn của bạn
                Theo tôi nghĩ việc gợi ý size thì nên trả ra nhiều size ở message nhưng tôi khuyên bạn người
                dùng cần sự gợi ý chính xác nên hãy ưu tiên 1 size duy nhất
                Hãy phân tích và trả về kết quả dưới dạng JSON CHÍNH XÁC theo định dạng sau (không ghi
                thêm gì ngoài JSON):
                { 
                    "intent": "'search_product' hay 'assist'", // chú ý: 'search_product' nếu người dùng nhập có từ 'tìm' hoặc 'mua' hay tùy thuộc vào ngữ cảnh mà bạn phân tích
                    "color": "màu của sản phẩm", // ví dụ: “RED”, “WHITE”,...
                    "priceMax": số_nguyên,
                    "priceMin": số_nguyên,
                    "size": ["danh sách kích cỡ có thể gợi ý"], // Chú ý: chi hiển thị một size duy nhất, ví dụ: [“41”], hoặc [] nếu khách hàng không có để cập tới
                    "type": "MALE" | "FEMALE" | "CHILDREN" | "",
                    "message": "Thông điệp hiển thị cho người dùng", "sizeResult": "Gợi ý size cụ thể nếu có, ví dụ: 'Size 41'"
                    "categoryName": "Chỉ gợi ý một trong 3 loại danh mục sau: 'Sport Shoes', 'Western Shoes' và 'Sneaker' hoặc '' nếu khách hàng không đề cập"
                }
                Lưu ý:
                • Nếu người dùng không nói gì về size, để sizeResult là chuỗi rỗng "".
                • Nếu không nói đến màu, để color là "".
                • Nếu không nhắc đến giá, đặt priceMin và priceMax là 0.
                • message phải rõ ràng, dễ hiểu và phù hợp với nội dung phản hồi.
                Ảnh hướng dẫn chọn size sẽ được cung cấp bên dưới(AI không cần yêu cầu ảnh, chỉ
                cần sử dụng khi có thông tin cần thiết).
                """, userMessage);
    }

//    private String prompt(String userMessage) throws IOException {
//        log.info("User message: {}", userMessage);
//        String promptFromPdf = readPromptFromPdf(PROMTS_PATH);
//        log.info("Prompt from PDF: {}", promptFromPdf);
//        return String.format(promptFromPdf, userMessage);
//    }
//
//    public static String readPromptFromPdf(String path) throws IOException {
//        File file = new File(path);
//        if (!file.exists()) {
//            throw new FileNotFoundException("Không tìm thấy file: " + file.getAbsolutePath());
//        }
//        try (PDDocument document = Loader.loadPDF(file)) {
//            PDFTextStripper stripper = new PDFTextStripper();
//            return stripper.getText(document).trim();
//        } catch (IOException e) {
//            throw new RuntimeException("Không đọc được PDF prompt", e);
//        }
//    }


}
