package ktpm17ctt.g6.orderservice.dto.feinClient.product;

public enum Gender {
    MEN("Men"),
    WOMAN("Woman"),
    UNISEX("Unisex");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
