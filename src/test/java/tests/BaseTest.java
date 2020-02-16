package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.given;

public class BaseTest {
    public RequestSpecification httpRequest;
    public static Response response;

    @BeforeTest
    public void setUp() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8091)
                .setBasePath("/bear")
                .setContentType("application/json")
                .build();

        httpRequest = given().spec(spec);
    }
}
