package pl.jutupe;

import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import pl.jutupe.object.User;
import static io.restassured.RestAssured.*;


public class UserTests extends FunctionalTest {
    @Test
    public void testPostSpeaker() throws JSONException {
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
    public void testPostAdmin() throws JSONException {
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
    public void testPostSuperAdmin() throws JSONException {
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
    public void testPostNormalUser() throws JSONException {
        String superAdminCookie = createUserCookie(UserType.SUPER_ADMIN);

        User user = new User(UserType.USER);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", superAdminCookie).when().post("v1/user");

        JsonPath jsonPath = response.jsonPath();



        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostSuperAdminByAdmin() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);
        User user = new User(UserType.SUPER_ADMIN);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).post("v1/user");

        Assert.assertEquals(401, response.getStatusCode());
    }

    @Test
    public void testPostUserWithDoubleType() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.INVALID_TYPE_1);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithStringType() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.INVALID_TYPE_2);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithTooBigType() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.INVALID_TYPE_3);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        response.prettyPrint();

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithDotEndedType() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        User user = new User(UserType.INVALID_TYPE_4);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithNameWithoutSpace() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(7) + "co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithSpaceAfterFirstName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(10) + " ";

        String email = RandomStringUtils.randomAlphabetic(7) + "co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithNameWithTwoSpaces() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(7) + " " + RandomStringUtils.randomAlphabetic(7) + " " + RandomStringUtils.randomAlphabetic(7);
        String email = RandomStringUtils.randomAlphabetic(7) + "co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithBadEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.randomAlphabetic(10);

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithLargeName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(10000) + " " + RandomStringUtils.randomAlphabetic(10000);
        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithLargeEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.randomAlphabetic(10000) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithNumberInName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomNumeric(8) + " " + RandomStringUtils.randomNumeric(8);
        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithNullName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";
        User user = new User(UserType.SPEAKER, null, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithEmptyName() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";
        User user = new User(UserType.SPEAKER, "", email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithNullEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        User user = new User(UserType.SPEAKER, name, null);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWithEmptyEmail() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);

        User user = new User(UserType.SPEAKER, name, "");

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWhenEmailIsInDatabase() throws JSONException {
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
    public void testPostUserWhenNameHasNotAlphanumericCharacters() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.random(8) + " " + RandomStringUtils.random(8);
        String email = RandomStringUtils.randomAlphabetic(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostUserWhenEmailHasNotAlphanumericCharacters() throws JSONException {
        String adminCookie = createUserCookie(UserType.ADMIN);

        String name = RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.random(10) + "@co.pl";

        User user = new User(UserType.SPEAKER, name, email);

        response = given().header("Content-Type", "application/json")
                .body(user.toString())
                .cookie("connect.sid", adminCookie).when().post("v1/user");

        Assert.assertEquals(400, response.getStatusCode());
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

        //

        response = given().cookie("connect.sid", superAdminCookie).get("v1/user/" + userId);

        Assert.assertEquals(200, response.getStatusCode());

        JsonPath secondJsonPath = response.jsonPath();
        String type = Integer.toString(secondJsonPath.getInt("type"));
        String name = secondJsonPath.get("name");

        Assert.assertEquals(UserType.SPEAKER.getId(), type);
        Assert.assertEquals(firstName, name);
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
        Assert.assertEquals(401, response.getStatusCode());
    }

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
        Assert.assertEquals(401, response.getStatusCode());
    }

    @Test
    public void testInvalidUserGetSalt(){
        String randomEmail = RandomStringUtils.randomAlphabetic(8) + "@co.pl";
        response = given().when().get("/v1/login/" + randomEmail);

        Assert.assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testUserGetSalt() throws JSONException {
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
    //todo testy patch user

}
