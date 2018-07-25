package pl.jutupe;


import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Feedback;
import pl.jutupe.enums.UserType;
import pl.jutupe.object.Talk;

import static io.restassured.RestAssured.*;

public class FeedbackTests extends FunctionalTest {

    //POST EVENT

    @Test
    public void testUserPostFeedbackForDeletedEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String eventId = createEvent(adminSessionCookie).get("_id");

        //delete event

        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/event/" + eventId);
        Assert.assertEquals(200, response.getStatusCode());

        //post feedback

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testUserPostFeedbackForEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testSpeakerPostFeedbackForEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", speakerSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testSuperAdminPostFeedbackForEvent() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(superAdminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForEventWhenRatingIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String rating = "16";

        Feedback feedback = new Feedback(rating);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForEventWhenRatingIsValid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String rating = "5";

        Feedback feedback = new Feedback(rating);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForEventWhenContentIsTooBig() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String content = RandomStringUtils.randomAlphabetic(501);
        String rating = "1";

        Feedback feedback = new Feedback(rating,content);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForEventWhenContentIsNull() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String rating = "1";

        Feedback feedback = new Feedback(rating, null);

        System.out.println(feedback.toString());

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForEventWhenContentIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String rating = "1";

        Feedback feedback = new Feedback(rating, " ");

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForEventWhenContentIsAscii() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String content = RandomStringUtils.randomAscii(20);

        String rating = "1";

        Feedback feedback = new Feedback(rating, content);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    //POST TALK

    @Test
    public void testUserPostFeedbackForTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String talkId = createTalk(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testUserPostFeedbackForDeletedTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String talkId = createTalk(adminSessionCookie).get("_id");

        //delete talk

        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/talk/" + talkId);
        Assert.assertEquals(200, response.getStatusCode());

        //post feedback

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testUserPostFeedbackForTalkWithDeletedEvent() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String eventId = createEvent(adminSessionCookie).get("_id");

        //post talk

        Talk talk = new Talk(eventId);

        response = given().header("Content-Type", "application/json")
                .body(talk.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/talk");

        //delete event

        response = given().cookie("connect.sid", adminSessionCookie).delete("v1/event/" + eventId);
        Assert.assertEquals(200, response.getStatusCode());

        //post feedback

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testSpeakerPostFeedbackForTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String speakerSessionCookie = createUserCookie(UserType.SPEAKER);
        String talkId = createTalk(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", speakerSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForTalk() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String talkId = createTalk(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testSuperAdminPostFeedbackForTalk() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String talkId = createTalk(superAdminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForTalkWhenRatingIsInvalid() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String talkId = createTalk(adminSessionCookie).get("_id");
        String rating = "5";

        Feedback feedback = new Feedback(rating);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(400, response.getStatusCode());
    }
  
    @Test
    public void testUserPostFeedbackForTalkWhenContentIsTooBig() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String userSessionCookie = createUserCookie(UserType.USER);
        String talkId = createTalk(adminSessionCookie).get("_id");
        String content = RandomStringUtils.randomAlphabetic(3000);
        String rating = "1";

        Feedback feedback = new Feedback(rating, content);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", userSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForTalkWhenContentIsNull() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String talkId = createTalk(adminSessionCookie).get("_id");
        String rating = "1";

        Feedback feedback = new Feedback(rating, null);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForTalkWhenContentIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String talkId = createTalk(adminSessionCookie).get("_id");
        String rating = "1";

        Feedback feedback = new Feedback(rating, "");

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminPostFeedbackForTalkWhenContentIsAscii() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String talkId = createTalk(adminSessionCookie).get("_id");
        String content = RandomStringUtils.randomAscii(20);
        String rating = "1";

        Feedback feedback = new Feedback(rating, content);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testAdminGetFeedback() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).get("v1/event/" + eventId);

        Assert.assertEquals(200, response.getStatusCode());
    }
}
