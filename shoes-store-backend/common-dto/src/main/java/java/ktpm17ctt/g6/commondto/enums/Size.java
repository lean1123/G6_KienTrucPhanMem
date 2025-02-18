package java.ktpm17ctt.g6.commondto.enums;

public enum Size {
    SIZE_30(30),
    SIZE_32(32),
    SIZE_34(34),
    SIZE_36(36),
    SIZE_38(38),
    SIZE_40(40),
    SIZE_42(42),
    SIZE_44(44);

    private final int value;

    Size(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
