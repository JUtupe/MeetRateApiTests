package pl.jutupe;



import io.restassured.path.json.JsonPath;
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

        String eventId = createEvent().get("eventId");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testPostTalkByUser() throws JSONException {
        String speakerSessionCookie = createUserCookie(UserType.USER);

        String eventId = createEvent().get("eventId");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", speakerSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());
    }


    @Test
    public void testPostTalkBySpeaker() throws JSONException {
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);

        String eventId = createEvent().get("eventId");
        System.out.println(eventId);
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", speakerSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        //todo jaki kod błedu?
        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostTalkWhenEventIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = "piwo";
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
        String eventId = createEvent().get("eventId");
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
        String eventId = createEvent().get("eventId");

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
        String eventId = createEvent().get("eventId");
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
        String eventId = createEvent(firstAdminSessionCookie).get("eventId");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", secondAdminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();
        Assert.assertEquals( 401, response.getStatusCode());
    }

    //todo testy get /talk

    @Test
    public void testGetTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent().get("eventId");
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
