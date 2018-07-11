package pl.jutupe;



import io.restassured.path.json.JsonPath;
import io.restassured.path.json.exception.JsonPathException;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Date;
import pl.jutupe.object.Talk;
import static io.restassured.RestAssured.*;

public class TalkTests extends FunctionalTest {

    //todo testy post /talk

    @Test
    public void  testPostTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = createEvent().get("_id");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.prettyPrint();

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testPostTalkBySpeaker() throws JSONException {
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);

        String eventId = createEvent().get("_id");
        System.out.println(eventId);
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", speakerSessionCookie).post("v1/talk");

        //todo jaki kod błedu?
        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostTalkWhenEventIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = "5b421e9667e43f05ccaca67d";
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testPostTalkWhenTitleIsTooBig() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent().get("_id");
        String title = RandomStringUtils.randomAlphabetic(100000);

        Talk talk = new Talk(eventId, title);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());

    }

    @Test
    public void testPostTalkWhenDateIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent().get("_id");

        Date date = new Date("9L", "102L");
        Talk talk = new Talk(eventId, date);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());

    }

    @Test
    public void testPostTalkWhenTitleIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent().get("_id");
        String title = RandomStringUtils.random(15);

        Talk talk = new Talk(eventId, title);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostTalkWhenEventDoNotBelongToSpecificAdmin() throws JSONException {
        String firstAdminSessionCookie = createUserCookie(UserType.ADMIN);
        String secondAdminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(firstAdminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", secondAdminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();
        Assert.assertEquals( 401, response.getStatusCode());
    }

    @Test
    public void testPostTalkWhenTitleIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent().get("_id");
        String title = "";

        Talk talk = new Talk(eventId, title);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostTalkWhenDateIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent().get("_id");

        Date date = new Date("", "");
        Talk talk = new Talk(eventId, date);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());
    }


    //todo testy get /talk

    @Test
    public void testGetTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent().get("_id");
        String firstTitle = RandomStringUtils.randomAlphabetic(10);

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        JsonPath firstJsonPath = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());

        String talkId = firstJsonPath.get("talkId");

        /*Tutaj robię dopiero get*/

        response = given().cookie("connect.sid", adminSessionCookie).get("v1/talk/" + talkId);

        JsonPath secondJsonPath = response.jsonPath();

        String secondTitle = secondJsonPath.get("title");

        Assert.assertEquals(firstTitle, secondTitle);
    }

    //todo testy patch /talk
}
