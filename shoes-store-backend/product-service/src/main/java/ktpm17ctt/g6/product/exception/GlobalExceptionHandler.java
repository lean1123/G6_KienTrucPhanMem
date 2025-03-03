package ktpm17ctt.g6.product.exception;


import jakarta.validation.ConstraintViolation;
import ktpm17ctt.g6.product.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
//        String enumKey = exception.getFieldError().getDefaultMessage();
//
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//        Map<String, Object> attributes = null;
//        try {
//            errorCode = ErrorCode.valueOf(enumKey);
//
//            var constraintViolation =
//                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
//
//            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
//
//            log.info(attributes.toString());
//
//        } catch (IllegalArgumentException e) {
//
//        }
//
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(
//                Objects.nonNull(attributes)
//                        ? mapAttribute(errorCode.getMessage(), attributes)
//                        : errorCode.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        List<Map<String, Object>> errorList = new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            Map<String, Object> errorDetails = new HashMap<>();

            // Lấy key lỗi từ annotation message
            String enumKey = error.getDefaultMessage();
            ErrorCode errorCode = ErrorCode.INVALID_KEY;
            Map<String, Object> attributes = null;

            try {
                errorCode = ErrorCode.valueOf(enumKey);

                var constraintViolation = error.unwrap(ConstraintViolation.class);
                attributes = constraintViolation.getConstraintDescriptor().getAttributes();
                log.info(attributes.toString());
            } catch (IllegalArgumentException e) {
                // Nếu lỗi không khớp enum, giữ INVALID_KEY
            }

            // Thêm thông tin lỗi vào danh sách
            errorDetails.put("field", error instanceof FieldError ? ((FieldError) error).getField() : "global");
            errorDetails.put("code", errorCode.getCode());
            errorDetails.put("message",
                    Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());

            errorList.add(errorDetails);
        }

        // Tạo API Response chứa danh sách lỗi
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode()); // Mã lỗi chung cho validation
        apiResponse.setMessage("Validation failed");
        apiResponse.setResult(errorList); // Trả về danh sách lỗi

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
