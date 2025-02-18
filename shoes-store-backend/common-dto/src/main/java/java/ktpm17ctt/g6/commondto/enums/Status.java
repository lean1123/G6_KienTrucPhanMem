package java.ktpm17ctt.g6.commondto.enums;

public enum Status {
    INSTOCK("In stock"),
    OUTOFSTOCK("Out of stock");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
