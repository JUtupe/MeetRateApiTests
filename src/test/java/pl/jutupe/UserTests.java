package pl.jutupe;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.User;

import static io.restassured.RestAssured.*;


public class UserTests extends FunctionalTest {

    @Test
    public void testValidPostUserSpeaker() throws JSONException {
        RequestSpecification request = RestAssured.given();

        User user = new User(UserType.SPEAKER);

        request.header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE);

        Response response = request.post("v1/user");
        response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();


        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.SPEAKER.getId(), jsonPathEvaluator.getInt("type"));
    }

    @Test
    public void testPostUserAdmin() throws JSONException {
        RequestSpecification request = RestAssured.given();

        User user = new User(UserType.ADMIN);

        request.header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE);

        Response response = request.post("v1/user");
        response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();


        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.ADMIN.getId(), jsonPathEvaluator.getInt("type"));
    }

    @Test
    public void testPostUser() throws JSONException {
        RequestSpecification request = RestAssured.given();

        User user = new User(UserType.USER);

        request.header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE);

        Response response = request.post("v1/user");
        response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();


        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostSuperAdmin() throws JSONException {
        RequestSpecification request = RestAssured.given();

        User user = new User(UserType.SUPER_ADMIN);

        request.header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE);

        Response response = request.post("v1/user");
        response.getBody().prettyPrint();
        JsonPath jsonPathEvaluator = response.jsonPath();


        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.SUPER_ADMIN.getId(), jsonPathEvaluator.getInt("type"));

    }
}
