package pl.jutupe;


import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Feedback;
import pl.jutupe.enums.UserType;

import static io.restassured.RestAssured.*;


public class FeedbackTests extends FunctionalTest {

    @Test
    public void testPostFeedbackForEventByAdmin() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testPostFeedbackForEventBySpeaker() throws JSONException {
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
    public void testPostFeedbackForEventByUser() throws JSONException {
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
    public void testPostFeedbackForEventWhenRatingIsInvalid() throws JSONException {
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
    public void testPostFeedbackForEventWhenContentIsTooBig() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String content = RandomStringUtils.randomAlphabetic(3000);
        String rating = "1";

        Feedback feedback = new Feedback(rating,content);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostFeedbackForTalkByAdmin() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String talkId = createTalk(adminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/talk/" + talkId);

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Test
    public void testPostFeedbackForTalkBySpeaker() throws JSONException {
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
    public void testPostFeedbackForTalkByUser() throws JSONException {
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
    public void testPostFeedbackForTalkByUserWhenContentIsTooBig() throws JSONException {
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

    //todo test get /feedback

    @Test
    public void testGetFeedback() throws JSONException {
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

        response.prettyPrint();
    }
}
