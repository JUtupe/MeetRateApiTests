package pl.jutupe;

public enum UserType {
    USER("0"),
    SPEAKER("1"),
    ADMIN("2"),
    SUPER_ADMIN("3"),
    INVALID_TYPE_1("2.2"),
    INVALID_TYPE_2("piwo"),
    INVALID_TYPE_3("9");

    private final String id;

    UserType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
