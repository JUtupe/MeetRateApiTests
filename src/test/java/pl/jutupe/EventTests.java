package pl.jutupe;

import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Date;
import pl.jutupe.object.Location;

import static io.restassured.RestAssured.given;

public class EventTests extends FunctionalTest {

    @Test
    public void testPostEvent() throws JSONException {
        String adminSessionCookie = createAdminCookie();

        String name = RandomStringUtils.randomAlphabetic(8);
        String info = RandomStringUtils.randomAlphanumeric(100);

        JSONObject object = new JSONObject();

        object.put("name", name);
        object.put("img", "#notatka( tu jest zdjęcie )");
        object.put("date", new Date().toString());
        object.put("location", new Location().toString());
        object.put("info", info);

        response = given().header("Content-Type", "application/json")
                .body(object.toString()).log().all()
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());
    }
}
