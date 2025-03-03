package ktpm17ctt.g6.product.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Type {
    MALE("Male"),
    FEMALE("Female"),
    CHILDREN("Children");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static Type fromString(String value) {
        for (Type type : Type.values()) {
            if (type.name().equalsIgnoreCase(value)) { // Chấp nhận "Male", "male", "MALE"
                return type;
            }
        }
        throw new IllegalArgumentException("PRODUCT_TYPE_INVALID: " + value);
    }
}
