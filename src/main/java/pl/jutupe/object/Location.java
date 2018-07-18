package pl.jutupe.object;

import org.json.JSONException;
import org.json.JSONObject;
import pl.jutupe.Constants;

public class Location {
    private String city;
    private String place;
    private String lat;
    private String lng;

    private JSONObject object;

    //todo losowe
    public Location() throws JSONException {
        object = new JSONObject();

        city = Constants.LOCATION_VALID_CITY;
        place = Constants.LOCATION_VALID_PLACE;
        lat = Constants.LOCATION_VALID_LAT;
        lng = Constants.LOCATION_VALID_LNG;

        object.put("city", city);
        object.put("place", place);
        object.put("lat", lat);
        object.put("lng", lng);
    }

    public Location(String city, String place, String lat, String lng) throws JSONException {
        object = new JSONObject();

        object.put("city", city);
        object.put("place", place);
        object.put("lat", lat);
        object.put("lng", lng);
    }

    public JSONObject getObject() {
        return object;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
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
