import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(DataProviderRunner.class)

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
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost/bear";
        RestAssured.port = 8091;
        RequestSpecification httprequest = RestAssured.given();

        JSONObject requestBody = new JSONObject();
        requestBody.put("bear_type", "BROWN");
        requestBody.put("bear_name", "UPDATEMEIFYOUCAN");
        requestBody.put("bear_age", "1");

        httprequest.body(requestBody.toString());

        Response response = httprequest.request(Method.POST);
        id = response.getBody().asString();
    }


    @Test
    @UseDataProvider("bearProvider")
    public void updateTest (String paramName, String paramValue){
        RequestSpecification httprequest = RestAssured.given();

        JSONObject requestBody = new JSONObject();
        requestBody.put(paramName, paramValue);
        httprequest.body(requestBody.toString());

        Response response = httprequest.request(Method.PUT,"/"+id);
        assertThat(response.getStatusCode(),equalTo(200));
        assertThat(response.getBody().asString(), equalTo("OK"));

        Response getResponse = httprequest.request(Method.GET,"/"+id);
        JSONObject getResponseJson = new JSONObject(getResponse.getBody().asString());

        assertThat(getResponseJson.get(paramName),equalTo(paramValue));

    }
}
