package ktpm17ctt.g6.product.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String fieldName, Object value) {
        super(fieldName + " with value " + value + " not found");
    }
}

