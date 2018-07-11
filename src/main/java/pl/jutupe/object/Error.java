package pl.jutupe.object;

import io.restassured.path.json.JsonPath;
import org.json.JSONObject;

import java.util.Objects;

public class Error {

    private Long code;
    private String message;

    public Error(JsonPath path) {
        this.code = path.get("code");
        this.message = path.get("message");
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return Objects.equals(code, error.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code);
    }
}
