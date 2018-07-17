package pl.jutupe;


import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Feedback;
import static io.restassured.RestAssured.*;


public class FeedbackTests extends FunctionalTest {

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
    public void testAdminPostFeedbackForEventWhenContentIsTooBig() throws JSONException {
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
    public void testPostFeedbackForEventWhenContentIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String content = "";
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
    public void testPostFeedbackForTalkSuperByAdmin() throws JSONException {
        String superAdminSessionCookie = createUserCookie(UserType.SUPER_ADMIN);
        String talkId = createTalk(superAdminSessionCookie).get("_id");

        Feedback feedback = new Feedback();

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", superAdminSessionCookie).post("v1/feedback/talk/" + talkId);

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

    @Test
    public void testPostFeedbackForTooWhenContentIsEmpty() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);
        String eventId = createEvent(adminSessionCookie).get("_id");
        String content = "";
        String rating = "1";

        Feedback feedback = new Feedback(rating,content);

        response = given().header("Content-Type", "application/json")
                .body(feedback.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/feedback/event/" + eventId);

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
