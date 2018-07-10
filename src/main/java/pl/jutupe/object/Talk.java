package pl.jutupe.object;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Talk {

    private JSONObject object;
    private String title;
    private Date date

    public Talk() throws JSONException {
        object = new JSONObject();

        title = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        object.put("title", title);
        object.put("date", date);

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
