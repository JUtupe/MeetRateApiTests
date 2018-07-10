package pl.jutupe;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class FunctionalTest {
    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = "http://10.67.1.219/api/";
        RestAssured.port = 3000;
    }
}
