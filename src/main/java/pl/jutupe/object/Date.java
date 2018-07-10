package pl.jutupe.object;

import org.json.JSONException;
import org.json.JSONObject;

public class Date {
    private Long start;
    private Long end;
    private JSONObject object;

    //todo na podst aktualnego czasu
    public Date() throws JSONException {
        start = 10L;
        end = 100L;

        object = new JSONObject();
        object.put("start", start);
        object.put("end", end);
    }

    public JSONObject getObject() {
        return object;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
