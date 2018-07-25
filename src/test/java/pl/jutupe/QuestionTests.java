package pl.jutupe;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.enums.UserType;
import pl.jutupe.object.Talk;

import static io.restassured.RestAssured.*;

public class QuestionTests extends FunctionalTest {

    //POST
    @Test
    public void testUserPostQuestionForTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String talkId = createTalk(adminSessionCookie).get("_id");

        String question = RandomStringUtils.randomAlphabetic(30);

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testUserPostQuestionForDeletedTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String eventId = createEvent(adminSessionCookie).get("_id");

        //post talk

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");
        Assert.assertEquals(201, response.getStatusCode());

        String talkId = response.jsonPath().get("_id");

        //delete event

        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/event/" + eventId);
        response.prettyPrint();
        Assert.assertEquals(200, response.getStatusCode());

        //post

        String question = RandomStringUtils.randomAlphabetic(30);
        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testSpeakerPostQuestionForTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);
        String talkId = createTalk(adminSessionCookie).get("_id");

        String question = RandomStringUtils.randomAlphabetic(30);

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", speakerSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(201, response.getStatusCode());

    }

    @Test
    public void testAdminPostQuestionForTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String talkId = createTalk(adminSessionCookie).get("_id");

        String question = RandomStringUtils.randomAlphabetic(30);

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(201, response.getStatusCode());

    }

    @Test
    public void testSuperAdminPostQuestionForTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String talkId = createTalk(superAdminSessionCookie).get("_id");

        String question = RandomStringUtils.randomAlphabetic(30);

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(201, response.getStatusCode());

    }

    @Test
    public void testSuperAdminPostEmptyQuestionForTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String talkId = createTalk(superAdminSessionCookie).get("_id");

        String question = "";

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testSuperAdminPostAsciiQuestionForTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String talkId = createTalk(superAdminSessionCookie).get("_id");

        String question = RandomStringUtils.randomAscii(35);

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(201, response.getStatusCode());

    }

    @Test
    public void testSuperAdminPostTooBigQuestion() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String talkId = createTalk(superAdminSessionCookie).get("_id");

        String question = RandomStringUtils.randomAlphanumeric(501);

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(400, response.getStatusCode());
    }
}