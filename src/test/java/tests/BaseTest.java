package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.given;

public class BaseTest {
    public RequestSpecification httprequest;
    public static Response response;
    public static JSONObject requestBody;

    @BeforeTest
    public void setUp() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8091)
                .setBasePath("/bear")
                .build();

        httprequest = given().spec(spec);
    }


}
