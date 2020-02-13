import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.http.Method.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UpdateTests {

    private String id;

    @DataProvider
    public static Object[][] bearProvider()
    {
        return new Object[][] {
                {"bear_type","BLACK"},
                {"bear_name","YOUDIDIT"},
                {"bear_age", "55"}
        };
    }

    /*Создаем медведя для теста*/
    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "http://localhost/bear";
        RestAssured.port = 8091;
        RequestSpecification httprequest = RestAssured.given();

        JSONObject requestBody = new JSONObject();
        requestBody.put("bear_type", "BROWN");
        requestBody.put("bear_name", "UPDATEMEIFYOUCAN");
        requestBody.put("bear_age", "1");

        httprequest.body(requestBody.toString());

        Response response = httprequest.request(POST);
        id = response.getBody().asString();
    }


    @Test (dataProvider = "bearProvider")
    public void updateTest (String paramName, String paramValue){
        RequestSpecification httprequest = RestAssured.given();

        JSONObject requestBody = new JSONObject();
        requestBody.put(paramName, paramValue);
        httprequest.body(requestBody.toString());

        Response response = httprequest.request(PUT,"/"+id);
        assertThat(response.getStatusCode(),equalTo(200));
        assertThat(response.getBody().asString(), equalTo("OK"));

        response = httprequest.request(GET,"/"+id);
        JSONObject responseJson = new JSONObject(response.getBody().asString());

        assertThat(responseJson.get(paramName),equalTo(paramValue));

    }
}
