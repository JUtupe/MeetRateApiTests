package pl.jutupe;

import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.enums.ErrorType;
import pl.jutupe.object.Date;
import pl.jutupe.object.Talk;
import pl.jutupe.enums.UserType;

import static io.restassured.RestAssured.*;

public class TalkTests extends FunctionalTest {

    //POST

    @Test
    public void  testAdminGetTalkWhenEventIsDeleted() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        //post

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");
        Assert.assertEquals(201, response.getStatusCode());

        JsonPath jsonPath = response.jsonPath();
        String talkId = jsonPath.get("_id");

        //delete

        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/event/" + eventId);
        Assert.assertEquals(200, response.getStatusCode());


        response = given().cookie("connect.sid", adminSessionCookie).get("v1/talk/" + talkId);
        Assert.assertEquals(200, response.getStatusCode());

    }

    @Test
    public void  testAdminPostTalkWhenEventIsDeleted() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = createEvent(adminSessionCookie).get("_id");

        //delete

        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/event/" + eventId);
        Assert.assertEquals(200, response.getStatusCode());

        //post

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void  testUserPostTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String eventId = createEvent(adminSessionCookie).get("_id");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/talk");


        Assert.assertEquals(403, response.getStatusCode());
    }

    @Test
    public void  testSpeakerPostTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);
        String eventId = createEvent(adminSessionCookie).get("_id");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", speakerSessionCookie).post("v1/talk");



        Assert.assertEquals(403, response.getStatusCode());
    }

    @Test
    public void  testAdminPostTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = createEvent(adminSessionCookie).get("_id");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void  testSuperAdminPostTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);

        String eventId = createEvent(superAdminSessionCookie).get("_id");
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/talk");



        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostTalkWhenEventIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        String eventId = "5b421e9667e43f05ccaca67d";
        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testAdminPostTalkWhenTitleIsTooBig() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String title = RandomStringUtils.randomAlphabetic(100000);

        Talk talk = new Talk(eventId, title);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(400, response.getStatusCode());
        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_EVENT_NAME));

    }

    @Test
    public void testAdminPostTalkWhenDateIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Date date = new Date("9L", "102L");
        Talk talk = new Talk(eventId, date);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(400, response.getStatusCode());
        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_DATE));
    }

    @Test
    public void testAdminPostTalkWhenTitleIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String title = RandomStringUtils.random(15);

        Talk talk = new Talk(eventId, title);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_EVENT_NAME));
    }

    @Test
    public void testAdminPostTalkWhenEventDoNotBelongToSpecificAdmin() throws JSONException {
        String firstAdminSessionCookie = createUserCookie(UserType.ADMIN);
        String secondAdminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(firstAdminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", secondAdminSessionCookie).post("v1/talk");

        Assert.assertEquals( 403, response.getStatusCode());
    }

    @Test
    public void testAdminPostTalkWhenTitleIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String title = "";

        Talk talk = new Talk(eventId, title);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(400, response.getStatusCode());
        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));

    }

    @Test
    public void testAdminPostTalkWhenDateIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Date date = new Date("", "");

        System.out.println(date.toString());

        Talk talk = new Talk(eventId, date);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(400, response.getStatusCode());
        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_DATE));

    }

    @Test
    public void testAdminPostTalkWhenTitleIsAscii() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String title = RandomStringUtils.randomAscii(20);

        Talk talk = new Talk(eventId, title);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");



        Assert.assertEquals(201, response.getStatusCode());
    }

    //GET

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
    public void testSuperAdminGetTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String eventId = createEvent(superAdminSessionCookie).get("_id");
        String firstTitle = RandomStringUtils.randomAlphabetic(10);

        Talk talk = new Talk(eventId, firstTitle);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath firstJsonPath = response.jsonPath();

        String talkId = firstJsonPath.get("_id");

        /*Tutaj robię dopiero get*/

        response = given().cookie("connect.sid", superAdminSessionCookie).get("v1/talk/" + talkId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String secondTitle = secondJsonPath.get("title");

        Assert.assertEquals(firstTitle, secondTitle);
    }

    //PATCH

    @Test
    public void testUserPatchTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
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
                .cookie("connect.sid", userSessionCookie).patch("v1/talk/" + talkId);

        Assert.assertEquals(403, response.getStatusCode());

    }

    @Test
    public void testSpeakerPatchTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);
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
                .cookie("connect.sid", speakerSessionCookie).patch("v1/talk/" + talkId);

        Assert.assertEquals(403, response.getStatusCode());

    }

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

    @Test
    public void testSuperAdminPatchTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String eventId = createEvent(superAdminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/talk");

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
                .cookie("connect.sid", superAdminSessionCookie).patch("v1/talk/" + talkId);

        Assert.assertEquals(200, response.getStatusCode());

        //get
        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", superAdminSessionCookie).get("v1/talk/" + talkId);


        Assert.assertEquals(200, response.getStatusCode());
    }

    //DELETE

    @Test
    public void testUserDeleteTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String eventId = createEvent(superAdminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();

        String talkId = jsonPath.get("_id");

        //delete
        response = given().cookie("connect.sid", userSessionCookie).delete("v1/talk/" + talkId);
        Assert.assertEquals(403, response.getStatusCode());
    }

    @Test
    public void testSpeakerDeleteTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);
        String eventId = createEvent(superAdminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();

        String talkId = jsonPath.get("_id");

        //delete
        response = given().cookie("connect.sid", speakerSessionCookie).delete("v1/talk/" + talkId);
        Assert.assertEquals(403, response.getStatusCode());
    }

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

    @Test
    public void testSuperAdminDeleteTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String eventId = createEvent(superAdminSessionCookie).get("_id");

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/talk");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();

        String talkId = jsonPath.get("_id");

        //delete
        response = given().cookie("connect.sid", superAdminSessionCookie).delete("v1/talk/" + talkId);
        Assert.assertEquals(200, response.getStatusCode());

        //get
        response = given().cookie("connect.sid", superAdminSessionCookie).get("v1/talk/" + talkId);
        Assert.assertEquals(404, response.getStatusCode());
    }




}
