package pl.jutupe;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Event;

import static io.restassured.RestAssured.given;

public class EventTests extends FunctionalTest {

    @Test
    public void testPostEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Event event = new Event();

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        response.prettyPrint();

        Assert.assertEquals(201, response.getStatusCode());
    }
}