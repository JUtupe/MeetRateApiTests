package pl.jutupe.object;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Feedback {
    private JSONObject object;
    private String rating;
    private String content;

    public Feedback() throws JSONException {
        object = new JSONObject();

        this.content = RandomStringUtils.randomAlphabetic(30);
        this.rating = "1";

        object.put("content", content);
        object.put("rating", rating);
    }

    public Feedback(String rating) throws JSONException {
        object = new JSONObject();

        this.content = RandomStringUtils.randomAlphabetic(30);
        this.rating = rating;

        object.put("content", content);
        object.put("rating", rating);
    }

    public Feedback(String rating, String content) throws JSONException {
        object = new JSONObject();

        this.rating = rating;
        this.content = content;

        object.put("content", content);
        object.put("rating", rating);
    }

    public String getContent() {
        return content;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}

