package pl.jutupe;

import org.junit.Test;

import static io.restassured.RestAssured.*;

public class EventTests extends FunctionalTest {

    @Test
    public void testGetEvents(){
        when().get("v1/event")
                .then().log().body();
    }

    @Test
    public void testInvalidEvent(){
        when().get("v1/event/testid")
                .then().log().body().statusCode(404);
    }

    @Test
    public void testValidEvent(){
        when().get("v1/event/5b421e9667e43f05ccaca63d")
                .then().statusCode(200);
    }
}
