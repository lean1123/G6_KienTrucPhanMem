package ktpm17ctt.g6.cart.dto.response.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Status {
    IN_STOCK("In stock"),
    OUT_OF_STOCK("Out of stock");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    @JsonValue
    public String getValue() {
        return status;
    }

    @JsonCreator
    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.status.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("STATUS_INVALID: " + value);
    }
}
