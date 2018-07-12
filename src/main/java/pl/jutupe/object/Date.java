package pl.jutupe.object;

import org.json.JSONException;
import org.json.JSONObject;

public class Date {
    private String start;
    private String end;
    private JSONObject object;

    //todo na podst aktualnego czasu
  
    public Date() throws JSONException {
        start = Long.toString(System.currentTimeMillis()/1000);
        end = Long.toString((System.currentTimeMillis()/1000)+30000);

        object = new JSONObject();
        object.put("start", start);
        object.put("end", end);
    }

    public Date(String start, String end) throws JSONException {
        object = new JSONObject();

        object.put("start", start);
        object.put("end", end);

    }

    public JSONObject getObject() {
        return object;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
