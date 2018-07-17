package pl.jutupe;

import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Event;
import pl.jutupe.enums.UserType;

import static io.restassured.RestAssured.given;

public class EventTests extends FunctionalTest {

    //POST

    @Test
    public void testAdminPostEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Event event = new Event();

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostEventWithTooBigName() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(10000);
        String info = RandomStringUtils.randomAlphabetic(30);

        Event event = new Event(name, info);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testAdminPostEventWithTooBigInfo() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(30);
        String info = RandomStringUtils.randomAlphabetic(10000);

        Event event = new Event(name, info);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testAdminPostEventWithAsciiName() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAscii(50);
        String info = RandomStringUtils.randomAlphabetic(30);

        Event event = new Event(name, info);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testSuperAdminPostEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);

        Event event = new Event();

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }

    //GET

    @Test
    public void testUserGetEvent() throws JSONException {
        String sessionCookie = createUserCookie(UserType.ADMIN);

        JsonPath jsonPath = createEvent(sessionCookie);
        String eventId = jsonPath.get("_id");

        //

        response = given().get("v1/event/" + eventId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testUserGetAllEvents(){
        response = given().get("v1/event");

        Assert.assertEquals(200, response.getStatusCode());
    }

    //PATCH

    //todo testy każdego parametru
    @Test
    public void testAdminPatchEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        JsonPath eventJson = createEvent(adminSessionCookie);
        String eventId = eventJson.get("_id");
        String oldName = eventJson.get("name");

        String newName = RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", adminSessionCookie).patch("v1/event/" + eventId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath responseJson = response.jsonPath();
        Assert.assertEquals(oldName, responseJson.get("name"));
    }

    @Test
    public void testSuperAdminPatchEvent() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        JsonPath eventJson = createEvent(superAdminSessionCookie);
        String eventId = eventJson.get("_id");
        String oldName = eventJson.get("name");

        String newName = RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminSessionCookie).patch("v1/event/" + eventId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath responseJson = response.jsonPath();
        Assert.assertEquals(oldName, responseJson.get("name"));
    }

    //DELETE

    @Test
    public void testAdminDeleteEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        JsonPath jsonPath = createEvent(adminSessionCookie);
        String eventId = jsonPath.get("_id");

        //delete

        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/event/" + eventId);
        response.prettyPrint();
        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().get("v1/event/" + eventId);

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testSuperAdminDeleteEvent() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);

        JsonPath jsonPath = createEvent(superAdminSessionCookie);
        String eventId = jsonPath.get("_id");

        //delete

        response = given().cookie("connect.sid", superAdminSessionCookie).delete("v1/event/" + eventId);
        response.prettyPrint();
        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().get("v1/event/" + eventId);

        Assert.assertEquals(404, response.getStatusCode());
    }
}