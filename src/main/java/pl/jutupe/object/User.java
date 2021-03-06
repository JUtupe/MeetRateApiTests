package pl.jutupe.object;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import pl.jutupe.enums.UserType;

public class User {
    private JSONObject object;
    private String name;
    private String email;

    public User(UserType type) throws JSONException {
        object = new JSONObject();

        name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        email = RandomStringUtils.randomAlphabetic(8) + "@co.pl";

        object.put("name", name);
        object.put("email", email);
        object.put("type", type.getId());
    }

    public User(UserType type, String name, String email) throws JSONException {
        object = new JSONObject();

        object.put("name", name);
        object.put("email", email);
        object.put("type", type.getId());
    }

    public JSONObject getObject() {
        return object;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
