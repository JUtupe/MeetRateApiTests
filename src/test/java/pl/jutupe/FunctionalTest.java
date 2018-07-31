package pl.jutupe;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.BeforeClass;
import pl.jutupe.object.Event;
import pl.jutupe.object.Talk;
import pl.jutupe.object.User;
import pl.jutupe.enums.UserType;

import static io.restassured.RestAssured.given;

public class FunctionalTest {
    private static String ADMIN_SESSION_COOKIE;
    static Response response;

    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = "http://localhost/api/";
        RestAssured.port = 3000;

        response = given()
                .header("email", "co@co.pl")
                .header("password_hash", "$2b$10$5NYolQ77qlRQVkjkhfKxM.VVpxO2998nqEtA7lcfQ0PklMqb5wWse")
        .when().get("v1/login");

        Assert.assertEquals(200, response.getStatusCode());

        ADMIN_SESSION_COOKIE = response.cookie("connect.sid");
    }

    static String createUserCookie(UserType type) throws JSONException {
        if(type == UserType.USER){
            return given().get("../").cookie("connect.sid");
        }

        User user = new User(type);

        //rejestracja
        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", ADMIN_SESSION_COOKIE).post("v1/user");

        return given().header("email", user.getEmail()).header("password_hash", response.jsonPath().get("password_hash"))
                .when().get("v1/login")
                .then().extract().cookie("connect.sid");
    }

    static JsonPath createEvent(String sessionCookie) throws JSONException {
        Event event = new Event();

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", sessionCookie).post("v1/event");

        Assert.assertEquals(201, response.getStatusCode());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response.jsonPath();
    }

    static JsonPath createTalk(String sessionCookie) throws JSONException {
        String eventId = createEvent(sessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", sessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());

        return response.jsonPath();
    }
}
