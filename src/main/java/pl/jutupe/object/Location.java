package pl.jutupe.object;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {
    private String city;
    private String place;
    private Double lat;
    private Double lng;

    private JSONObject object;

    //todo losowe
    Location() throws JSONException {
        city = "Zarow";
        place = "placyk";

        lat = 10.0;
        lng = 12.1231231;

        object = new JSONObject();

        object.put("city", city);
        object.put("place", place);
        object.put("lat", lat);
        object.put("lng", lng);
    }

    public JSONObject getObject() {
        return object;
    }

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }

    public String getPlace() {
        return place;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
