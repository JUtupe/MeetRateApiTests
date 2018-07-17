package pl.jutupe;

import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Date;
import pl.jutupe.object.Talk;
import pl.jutupe.enums.UserType;

import static io.restassured.RestAssured.*;

public class TalkTests extends FunctionalTest {

    //todo testy post /talk

    @Test
    public void testPostTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = createEvent(adminSessionCookie).get("_id");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.prettyPrint();

        Assert.assertEquals(201, response.getStatusCode());
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
        String eventId = createEvent(adminSessionCookie).get("_id");
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
        String eventId = createEvent(adminSessionCookie).get("_id");

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
        String eventId = createEvent(adminSessionCookie).get("_id");
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
        Assert.assertEquals( 403, response.getStatusCode());
    }

    @Test
    public void testPostTalkWhenTitleIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
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
        String eventId = createEvent(adminSessionCookie).get("_id");

        Date date = new Date("", "");

        System.out.println(date.toString());

        Talk talk = new Talk(eventId, date);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        response.getBody().prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());
    }


    //todo testy get /talk

    @Test
    public void testAdminGetTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String firstTitle = RandomStringUtils.randomAlphabetic(10);

        Talk talk = new Talk(eventId, firstTitle);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath firstJsonPath = response.jsonPath();

        String talkId = firstJsonPath.get("_id");

        /*Tutaj robię dopiero get*/

        response = given().cookie("connect.sid", adminSessionCookie).get("v1/talk/" + talkId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String secondTitle = secondJsonPath.get("title");

        Assert.assertEquals(firstTitle, secondTitle);
    }

    @Test
    public void testSpeakerGetTalk() throws JSONException {
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = createEvent(adminSessionCookie).get("_id");
        String firstTitle = RandomStringUtils.randomAlphabetic(10);

        Talk talk = new Talk(eventId, firstTitle);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath firstJsonPath = response.jsonPath();

        String talkId = firstJsonPath.get("_id");

        /*Tutaj robię dopiero get*/

        response = given().cookie("connect.sid", speakerSessionCookie).get("v1/talk/" + talkId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String secondTitle = secondJsonPath.get("title");

        Assert.assertEquals(firstTitle, secondTitle);
    }

    @Test
    public void testUserGetTalk() throws JSONException {
        String userSessionCookie = createUserCookie(UserType.USER);
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = createEvent(adminSessionCookie).get("_id");
        String firstTitle = RandomStringUtils.randomAlphabetic(10);

        Talk talk = new Talk(eventId, firstTitle);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath firstJsonPath = response.jsonPath();

        String talkId = firstJsonPath.get("_id");

        /*Tutaj robię dopiero get*/

        response = given().cookie("connect.sid", userSessionCookie).get("v1/talk/" + talkId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String secondTitle = secondJsonPath.get("title");

        Assert.assertEquals(firstTitle, secondTitle);
    }

    //todo testy patch /talk

    @Test
    public void testAdminPatchTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();
        String talkId = jsonPath.get("_id");

        //patch
        JSONObject object = new JSONObject();
        String newTitle = RandomStringUtils.randomAlphabetic(8);
        object.put("title", newTitle);


        System.out.println(newTitle);
        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", adminSessionCookie).patch("v1/talk/" + talkId);

        Assert.assertEquals(200, response.getStatusCode());

        //get
        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).get("v1/talk/" + talkId);


        Assert.assertEquals(200, response.getStatusCode());
    }

    //todo testy delete /talk

    @Test
    public void testAdminDeleteTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();

        String talkId = jsonPath.get("_id");

        //delete
        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/talk/" + talkId);
        Assert.assertEquals(200, response.getStatusCode());

        //get
        response = given().cookie("connect.sid", adminSessionCookie).get("v1/talk/" + talkId);
        Assert.assertEquals(404, response.getStatusCode());
    }

}
