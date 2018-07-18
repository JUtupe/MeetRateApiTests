package pl.jutupe;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.enums.ErrorType;
import pl.jutupe.enums.UserType;
import static io.restassured.RestAssured.*;

public class QuestionTests extends FunctionalTest {

    //POST
    //todo test post na usunięty talk
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
        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_QUESTION));

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
    public void testSuperAdminPostQuestionIsTooBigForTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String talkId = createTalk(superAdminSessionCookie).get("_id");

        String question = RandomStringUtils.randomAlphanumeric(3000);

        JSONObject object = new JSONObject();
        object.put("question", question);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/question/talk/" + talkId);
        Assert.assertEquals(400, response.getStatusCode());
        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_QUESTION));
    }
}