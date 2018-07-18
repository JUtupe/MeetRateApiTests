package pl.jutupe;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.enums.ErrorType;
import pl.jutupe.enums.UserType;
import pl.jutupe.object.Event;
import pl.jutupe.object.Location;
import static io.restassured.RestAssured.given;

public class ObjectLocationTests extends FunctionalTest{

    private String validLat = Constants.LOCATION_VALID_LAT;
    private String validLng = Constants.LOCATION_VALID_LNG;
    private String validCity = Constants.LOCATION_VALID_CITY;
    private String validPlace = Constants.LOCATION_VALID_PLACE;

    @Test
    public void testPostValidLocation() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Location location = new Location();

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testPostStringLatLngLocation() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Location location = new Location(validCity, validPlace, "piwo", "browar");

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_LOCATION));
    }

    @Test
    public void testPostTooBigCityLocation() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String city = RandomStringUtils.randomAlphabetic(10000);

        Location location = new Location(city, validPlace, validLat, validLng);

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_LOCATION));
    }

    @Test
    public void testPostTooBigCityPlace() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String place = RandomStringUtils.randomAlphabetic(10000);

        Location location = new Location(validCity, place, validLat, validLng);

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_LOCATION));
    }

    @Test
    public void testPostCityWithNumbers() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String city = RandomStringUtils.randomAlphabetic(10);

        Location location = new Location(city + "123", validPlace, validLat, validLng);

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_LOCATION));
    }

    @Test
    public void testPostCityWithNonAlphabeticCharacters() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String city = RandomStringUtils.random(20);


        Location location = new Location(city, validPlace, validLat, validLng);

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_LOCATION));
    }

    @Test
    public void testPostCityWithDash() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Location location = new Location("Bielsko-Czarna", validPlace, validLat, validLng);

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testPostCityWithSpace() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Location location = new Location("Piwo Browar", validPlace, validLat, validLng);

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testPostPlaceWithAsciiCharacters() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String place = RandomStringUtils.randomAscii(20);

        Location location = new Location(validCity, place, validLat, validLng);

        Event event = new Event(location);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }
}