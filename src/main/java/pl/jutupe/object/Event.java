package pl.jutupe.object;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Event {
    private JSONObject object;
    private String name;
    private String info;

    public Event() throws JSONException {
        object = new JSONObject();

        name = RandomStringUtils.randomAlphabetic(8);
        info = RandomStringUtils.randomAlphanumeric(100);

        object.put("name", name);
        object.put("img", "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");
        object.put("date", new Date().getObject());
        object.put("location", new Location().getObject());
        object.put("info", info);
    }

    public Event(String name, String info) throws JSONException {
        object = new JSONObject();

        object.put("name", name);
        object.put("img", "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");
        object.put("date", new Date().getObject());
        object.put("location", new Location().getObject());
        object.put("info", info);
    }

    public Event(Date date) throws JSONException {
        object = new JSONObject();

        name = RandomStringUtils.randomAlphabetic(8);
        info = RandomStringUtils.randomAlphanumeric(100);

        object.put("name", name);
        object.put("img", "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");
        object.put("date", date.getObject());
        object.put("location", new Location().getObject());
        object.put("info", info);
    }

    public Event(Location location) throws JSONException {
        object = new JSONObject();

        name = RandomStringUtils.randomAlphabetic(8);
        info = RandomStringUtils.randomAlphanumeric(100);

        object.put("name", name);
        object.put("img", "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");
        object.put("date", new Date().getObject());
        object.put("location", location);
        object.put("info", info);
    }

    public JSONObject getObject() {
        return object;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
