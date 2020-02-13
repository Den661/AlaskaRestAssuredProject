import io.restassured.RestAssured;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.http.Method.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateTests {

    @DataProvider (name = "bearProvider")
    public static Object[][] dataProviderMethod()
    {
        return new Object[][] {
                {"BROWN","ИСААК",15.5,200},
                {"BLACK","МАРИЯ",100,200},
                {"POLAR","NIKOLA",0,200},
                {"GUMMY","Reeny",10,200},
                {"BROWN","Исаак",1,200},
                {"Brown","ИСААК",1,500},
                {"POLAR","NIKOLA",101,200},
                {"POLAR","NIKOLA",-1,500},
        };
    }

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "http://localhost/bear";
        RestAssured.port=8091;
    }

    @Test (dataProvider = "bearProvider")
    public void CreateTest (String type, String name, double age, int expectedResponse)
    {
        RequestSpecification httprequest = RestAssured.given();

        /*Создаем JSON для создания медведя*/
        JSONObject requestBody = new JSONObject();
        requestBody.put("bear_type", type);
        requestBody.put("bear_age", age);
        requestBody.put("bear_name",name);

        /*Отправляем запрос на создание и вытаскиваем полученный ID*/
        httprequest.header("Content-Type","application/JSON");
        httprequest.body(requestBody.toString());
        Response response = httprequest.request(POST);
        assertThat(response.getStatusCode(),equalTo(expectedResponse));
        String bearId = response.getBody().asString();

        /*Получаем данные по Id созданного медведя*/
        Response getResponse = httprequest.request(GET,"/"+bearId);
        String responseBody = getResponse.getBody().asString();

        /*Проверяем что вернулось то, что отправили*/
        assertThat(getResponse.getStatusCode(),equalTo(200));
        assertThat(responseBody,not(emptyOrNullString()));
        assertThat(responseBody,not(equalTo("null")));
        JSONObject responseBodyJson = new JSONObject(responseBody);
        assertThat(responseBodyJson.get("bear_type"),equalTo(type));
        assertThat(responseBodyJson.get("bear_age"),equalTo(age));
        assertThat(responseBodyJson.get("bear_name"),equalTo(name));
    }
}
