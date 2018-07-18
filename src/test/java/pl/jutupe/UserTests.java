package pl.jutupe;

import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.enums.ErrorType;
import pl.jutupe.object.User;
import pl.jutupe.enums.UserType;
import static io.restassured.RestAssured.*;


public class UserTests extends FunctionalTest {

    //POST

    @Test
    public void testAdminPostSpeaker() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.SPEAKER.getId(), jsonPath.getString("type"));
    }

    @Test
    public void testAdminPostSuperAdmin() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);
        User user = new User(UserType.SUPER_ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(401, response.getStatusCode());
    }

    @Test
    public void testAdminPostUserWithDoubleType() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.INVALID_TYPE_1);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_ACCOUNT_TYPE));
    }

    @Test
    public void testAdminPostUserWithStringType() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.INVALID_TYPE_2);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_ACCOUNT_TYPE));
    }

    @Test
    public void testAdminPostSpeakerWithDotEndedType() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.INVALID_TYPE_4);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_ACCOUNT_TYPE));
    }

    @Test
    public void testAdminPostSpeakerWithNameWithoutSpace() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(7) + "co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostSpeakerWithSpaceAfterFirstName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(10) + " ";

        String email = RandomStringUtils.randomAlphabetic(7) + "co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostSpeakerWithNameWithTwoSpaces() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(7) + " " + RandomStringUtils.randomAlphabetic(7) + " " + RandomStringUtils.randomAlphabetic(7);
        String email = RandomStringUtils.randomAlphabetic(7) + "co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostSpeakerWithBadEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.randomAlphabetic(10);

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());


        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_EMAIL));
    }

    @Test
    public void testAdminPostSpeakerWithLargeName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(10000) + " " + RandomStringUtils.randomAlphabetic(10000);
        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());


        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostUserWithLargeEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.randomAlphabetic(10000) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_EMAIL));
    }

    @Test
    public void testAdminPostUserWithNumberInName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomNumeric(8) + " " + RandomStringUtils.randomNumeric(8);
        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostUserWithNullName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";
        User user = new User(UserType.SPEAKER, null, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostUserWithEmptyName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";
        User user = new User(UserType.SPEAKER, "", email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostUserWithNullEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        User user = new User(UserType.SPEAKER, name, null);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_EMAIL));
    }

    @Test
    public void testAdminPostUserWithEmptyEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        User user = new User(UserType.SPEAKER, name, "");

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_EMAIL));
    }

    @Test
    public void testAdminPostUserWhenEmailIsInDatabase() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name1 = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String name2 = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user1 = new User(UserType.SPEAKER, name1, email);

        response = given().header("Content-Type", "application/json")
                .body(user1.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        User user2 = new User(UserType.SPEAKER, name2, email);

        response = given().header("Content-Type", "application/json")
                .body(user2.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(409, response.getStatusCode());
    }

    @Test
    public void testAdminPostUserWhenNameHasNotAlphanumericCharacters() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.random(8) + " " + RandomStringUtils.random(8);

        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_NAME));
    }

    @Test
    public void testAdminPostUserWhenEmailHasNotAlphanumericCharacters() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.random(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_EMAIL));
    }

    @Test
    public void testSuperAdminPostUser() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.USER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_ACCOUNT_TYPE));
    }

    @Test
    public void testSuperAdminPostAdmin() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.ADMIN.getId(), jsonPath.getString("type"));
    }

    @Test
    public void testSuperAdminPostSuperAdmin() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.SUPER_ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.SUPER_ADMIN.getId(), jsonPath.getString("type"));
    }

    @Test
    public void testSuperAdminPostUserWithTooBigType() throws JSONException {
        String adminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.INVALID_TYPE_3);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        response.prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());

        ErrorChecker checker = new ErrorChecker(response.jsonPath());
        Assert.assertTrue(checker.checkForError(ErrorType.INVALID_ACCOUNT_TYPE));
    }

    @Test
    public void testSuperAdminPostSpeaker() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals(UserType.SPEAKER.getId(), jsonPath.getString("type"));
    }

    //GET

    @Test
    public void testUserGetSpeakerById() throws JSONException {
        String userCookie = createUserCookie(UserType.USER);
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath firstJsonPath = response.jsonPath();
        String userId = firstJsonPath.get("_id");

        //

        response = given().cookie("connect.sid", userCookie).get("v1/user/" + userId);
        Assert.assertEquals(403, response.getStatusCode());
    }

    @Test
    public void testInvalidUserGetSalt(){
        String randomEmail = RandomStringUtils.randomAlphabetic(8) + "@co.pl";
        response = given().when().get("/v1/login/" + randomEmail);

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testSpeakerGetSpeakerById() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);
        String speakerCookie = createUserCookie(UserType.SPEAKER);

        String firstName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String firstEmail = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, firstName, firstEmail);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath firstJsonPath = response.jsonPath();
        String userId = firstJsonPath.get("_id");

        //

        response = given().cookie("connect.sid", speakerCookie).get("v1/user/" + userId);
        Assert.assertEquals(403, response.getStatusCode());
    }

    @Test
    public void testSpeakerGetSalt() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String email = RandomStringUtils.randomAlphabetic(8) + "@co.pl";
        String name = RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        //

        response = given().get("v1/login/" + email);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testAdminGetSpeakerById() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);
        String firstName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String firstEmail = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, firstName, firstEmail);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath firstJsonPath = response.jsonPath();
        String userId = firstJsonPath.get("_id");

        //

        response = given().cookie("connect.sid", adminCookie).get("v1/user/" + userId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String type = Integer.toString(secondJsonPath.getInt("type"));
        String name = secondJsonPath.get("name");

        Assert.assertEquals(UserType.SPEAKER.getId(), type);
        Assert.assertEquals(firstName, name);
    }

    @Test
    public void testSuperAdminGetSpeakerById() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);
        String firstName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String firstEmail = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, firstName, firstEmail);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath firstJsonPath = response.jsonPath();

        String userId = firstJsonPath.get("_id");

        System.out.println(userId);

        //

        response = given().cookie("connect.sid", superAdminCookie).get("v1/user/" + userId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String type = Integer.toString(secondJsonPath.getInt("type"));
        String name = secondJsonPath.get("name");

        Assert.assertEquals(UserType.SPEAKER.getId(), type);
        Assert.assertEquals(firstName, name);
    }

    //PATCH
    //todo każdy parametr

    @Test
    public void testPatchSpeaker() throws JSONException {
        //post speaker

        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());


        JsonPath jsonPath = response.jsonPath();
        String speakerId = jsonPath.get("_id");

        //login speaker

        String speakerCookie = given().header("email", user.getEmail()).header("password_hash", response.jsonPath().get("password_hash"))
                .when().get("v1/login")
                .then().extract().cookie("connect.sid");


        //patch speaker

        String newName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", speakerCookie).patch("v1/user/" + speakerId);
        response.prettyPrint();

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPatchAdmin() throws JSONException {
        //post admin

        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());


        JsonPath jsonPath = response.jsonPath();
        String adminId = jsonPath.get("_id");

        //login admin

        String adminCookie = given().header("email", user.getEmail()).header("password_hash", response.jsonPath().get("password_hash"))
                .when().get("v1/login")
                .then().extract().cookie("connect.sid");

        //patch admin

        String newName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", adminCookie).patch("v1/user/" + adminId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPatchSuperAdmin() throws JSONException {
        //post super-admin

        String initialCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.SUPER_ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", initialCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());


        JsonPath jsonPath = response.jsonPath();
        String superAdminId = jsonPath.get("_id");

        //login super-admin

        String superAdminCookie = given().header("email", user.getEmail()).header("password_hash", response.jsonPath().get("password_hash"))
                .when().get("v1/login")
                .then().extract().cookie("connect.sid");

        //patch super-admin

        String newName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminCookie).patch("v1/user/" + superAdminId);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testUserPatchSpeaker() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);
        String name1 = RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);
        String name2 = RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(5);
        String email = RandomStringUtils.randomAlphabetic(8) + "@co.pl";

        User user = new User(UserType.SPEAKER, name1, email);
        User user2 = new User(UserType.SPEAKER, name2, email);


        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        JsonPath jsonPath = response.jsonPath();

        //

        String userCookie = createUserCookie(UserType.USER);

        response = given().header("Content-Type", "application/json")
                .body(user2.toString())
                .cookie("connect.sid", userCookie).patch("v1/user/" + jsonPath.get("_id"));

        Assert.assertEquals(403, response.getStatusCode());
    }

    @Test
    public void testAdminPatchSpeaker() throws JSONException {
        String adminSessionCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminSessionCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath jsonPath = response.jsonPath();
        String speakerId = jsonPath.get("_id");

        //patch

        String newName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", adminSessionCookie).patch("v1/user/" + speakerId);

        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().cookie("connect.sid", adminSessionCookie).get("v1/user/" + speakerId);
        Assert.assertEquals(200, response.getStatusCode());

        JsonPath responseJson = response.jsonPath();
        String responseName = responseJson.get("name");

        Assert.assertEquals(newName, responseName);
    }

    @Test
    public void testSuperAdminPatchSpeaker() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath firstJsonPath = response.jsonPath();
        String speakerId = firstJsonPath.get("_id");
        Assert.assertEquals(UserType.SPEAKER.getId(), firstJsonPath.getString("type"));

        //patch

        String newName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminCookie).patch("v1/user/" + speakerId);

        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().cookie("connect.sid", superAdminCookie).get("v1/user/" + speakerId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String responseName = secondJsonPath.get("name");

        Assert.assertEquals(newName, responseName);
    }

    @Test
    public void testSuperAdminPatchAdmin() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());
        JsonPath firstJsonPath = response.jsonPath();
        String adminId = firstJsonPath.get("_id");
        Assert.assertEquals(UserType.ADMIN.getId(), firstJsonPath.getString("type"));

        //patch
        String newName = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        JSONObject object = new JSONObject();
        object.put("name", newName);

        response = given().header("Content-Type", "application/json")
                .body(object.toString())
                .cookie("connect.sid", superAdminCookie).patch("v1/user/" + adminId);

        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().cookie("connect.sid", superAdminCookie).get("v1/user/" + adminId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String responseName = secondJsonPath.get("name");

        Assert.assertEquals(newName, responseName);
    }

    //DELETE

    @Test
    public void testAdminDeleteSpeaker() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath firstJsonPath = response.jsonPath();
        String speakerId = firstJsonPath.get("_id");

        Assert.assertEquals(UserType.SPEAKER.getId(), firstJsonPath.getString("type"));

        //delete

        response = given().cookie("connect.sid", adminCookie).delete("v1/user/" + speakerId);
        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().cookie("connect.sid", adminCookie).get("v1/user/" + speakerId);
        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testSuperAdminDeleteAdmin() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath firstJsonPath = response.jsonPath();
        String adminId = firstJsonPath.get("_id");

        Assert.assertEquals(UserType.ADMIN.getId(), firstJsonPath.getString("type"));

        //delete

        response = given().cookie("connect.sid", superAdminCookie).delete("v1/user/" + adminId);
        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().cookie("connect.sid", superAdminCookie).get("v1/user/" + adminId);
        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testSuperAdminDeleteSpeaker() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.SPEAKER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).post("v1/user");

        Assert.assertEquals(201, response.getStatusCode());

        JsonPath firstJsonPath = response.jsonPath();
        String speakerId = firstJsonPath.get("_id");

        Assert.assertEquals(UserType.SPEAKER.getId(), firstJsonPath.getString("type"));

        //delete

        response = given().cookie("connect.sid", superAdminCookie).delete("v1/user/" + speakerId);
        Assert.assertEquals(200, response.getStatusCode());

        //get

        response = given().cookie("connect.sid", superAdminCookie).get("v1/user/" + speakerId);
        Assert.assertEquals(404, response.getStatusCode());
    }

    /*@Test
    public void test() throws JSONException {
        String adminCookie = createUserCookie(UserType.SUPER_ADMIN);
        System.out.println(adminCookie);
        User user = new User(UserType.SUPER_ADMIN);

        System.out.println(user.getObject().toString());

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        response.prettyPrint();
    }*/
}
