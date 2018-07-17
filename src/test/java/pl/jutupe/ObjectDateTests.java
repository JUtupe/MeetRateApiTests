package pl.jutupe;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.Date;
import pl.jutupe.object.Event;
import pl.jutupe.enums.ErrorType;
import pl.jutupe.enums.UserType;

import static io.restassured.RestAssured.given;

/**
 * Testowanie daty odbywa się tylko na eventach
 */
public class ObjectDateTests extends FunctionalTest {

    @Test
    public void testPostStringDate() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        Date date = new Date("browar", "piwo");

        Event event = new Event(date);

        response = given().header("Content-Type", "application/json")
                .body(event.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/event");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());

        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_DATE));
    }
}
