package pl.jutupe;

public enum UserType {
    USER(0),
    SPEAKER(1),
    ADMIN(2),
    SUPER_ADMIN(3);

    private final int id;

    UserType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
