package pl.jutupe;

import io.restassured.path.json.JsonPath;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.User;
import static io.restassured.RestAssured.*;


public class UserTests extends FunctionalTest {
    @Test
    public void testValidPostUserSpeaker() throws JSONException {
        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE).post("v1/user");

        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.SPEAKER.getId(), jsonPathEvaluator.getInt("type"));
    }

    @Test
    public void testPostUserAdmin() throws JSONException {
        User user = new User(UserType.ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE).post("v1/user");

        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.ADMIN.getId(), jsonPathEvaluator.getInt("type"));
    }

    @Test
    public void testPostUser() throws JSONException {
        User user = new User(UserType.USER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostSuperAdmin() throws JSONException {
        User user = new User(UserType.SUPER_ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE).post("v1/user");

        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.SUPER_ADMIN.getId(), jsonPathEvaluator.getInt("type"));
    }
}
