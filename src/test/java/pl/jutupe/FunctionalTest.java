package pl.jutupe;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.BeforeClass;
import pl.jutupe.object.Event;
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

    //TODO DODAC ZWYKLY TYP DO POBRANIA
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

    static JsonPath createEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Event event = new Event();

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        return response.jsonPath();
    }

    static JsonPath createEvent(String adminSessionCookie) throws JSONException {
        Event event = new Event();

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        return response.jsonPath();
    }
}
