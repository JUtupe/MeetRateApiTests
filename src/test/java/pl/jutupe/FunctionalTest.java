package pl.jutupe;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.junit.BeforeClass;
import pl.jutupe.object.User;

import static io.restassured.RestAssured.given;

public class FunctionalTest {
    static String ADMIN_SESSION_COOKIE;
    static Response response;

    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = "http://206.142.240.132/api/";
        RestAssured.port = 80;

        ADMIN_SESSION_COOKIE = given().header("email", "tester").header("password_hash", "$2b$10$.l5sh.UWxrUsYVRUpmuyB.jkQLUFOVbCONYz2C7/9gfDgTJGRGdl6")
                .when().get("v1/login")
                .then().extract().cookie("connect.sid");
    }

    public static JsonPath createSpeaker() throws JSONException {
        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE).post("v1/user");

        response.getBody().prettyPrint();
        return response.jsonPath();
    }

    static String createAdminCookie() throws JSONException {
        RequestSpecification request = RestAssured.given();

        User user = new User(UserType.SPEAKER);

        request.header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE);

        Response response = request.post("v1/user");

        return given().header("email", user.getEmail()).header("password_hash", response.jsonPath().get("password_hash"))
                .when().get("v1/login")
                .then().extract().cookie("connect.sid");
    }
}
