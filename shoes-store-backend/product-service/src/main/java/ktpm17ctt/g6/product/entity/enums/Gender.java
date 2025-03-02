package ktpm17ctt.g6.product.entity.enums;

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
