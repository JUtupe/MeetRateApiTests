package pl.jutupe.object;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Talk {

    private JSONObject object;
    private String title;
    private Date date;

    public Talk(String eventId) throws JSONException {
        object = new JSONObject();

        this.title = RandomStringUtils.randomAlphabetic(8);
        this.date = new Date();

        object.put("eventId", eventId);
        object.put("title", title);
        object.put("date", date.getObject());
    }

    public Talk(String eventId, String title) throws JSONException {
        object = new JSONObject();

        this.title = title;
        this.date = new Date();

        object.put("eventId", eventId);
        object.put("title", title);
        object.put("date", date.getObject());
    }

    public Talk(String eventId, Date date) throws JSONException {
        object = new JSONObject();

        this.title = RandomStringUtils.randomAlphabetic(8);
        this.date = date;

        object.put("eventId", eventId);
        object.put("title", title);
        object.put("date", date.getObject());
    }

    public JSONObject getObject() {
        return object;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
