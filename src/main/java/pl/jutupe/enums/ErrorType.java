package pl.jutupe.enums;

public enum ErrorType {
    INVALID_NAME(0),
    INVALID_EMAIL(1),
    INVALID_ACCOUNT_TYPE(2),
    INVALID_OBJECTID(3),
    INVALID_EVENT_NAME(4),
    INVALID_IMG(5),
    INVALID_LOCATION(6),
    INVALID_INFO(7),
    INVALID_DATE(8),
    INVALID_RATING(9),
    INVALID_RATING_CONTENT(10),
    INVALID_QUESTION(11),
    INVALID_TYPE(12),
    INVALID_REQUEST_FORMAT(13);

    private final int id;

    ErrorType (int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
