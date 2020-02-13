import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.POST;
import static java.util.Map.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateTests {
    private String type;
    private String name;
    private Double age;
    private int statusCode;
    private RequestSpecification httprequest;
    private String bearId;
    private JSONObject requestBody;
    private Object Bear;

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {"BROWN", "ИСААК", 15D, 200},
                {"BLACK", "МАРИЯ", 100D, 200},
                {"POLAR", "NIKOLA", 0D, 200},
                {"GUMMY", "Reeny", 10D, 200},
                {"BROWN", "Исаак", 1D, 200},
                {"Brown", "ИСААК", 1D, 500},
                {"POLAR", "NIKOLA", 101D, 200},
                {"POLAR", "NIKOLA", -1D, 500},
        };
    }

    @Factory(dataProvider = "testData")
    public CreateTests(String type, String name, Double age, int statusCode) {
        this.type = type;
        this.name = name;
        this.age = age;
        this.statusCode = statusCode;
    }

    @BeforeMethod(description = "Создаем JSON для создания медведя")
    public void setUp1() {
        RestAssured.baseURI = "http://localhost/bear";
        RestAssured.port = 8091;
        httprequest = RestAssured.given();

        requestBody = new JSONObject(of("bear_type", type, "bear_age", age, "bear_name", name));
    }

    @BeforeMethod(description = "Отправляем запрос на создание и вытаскиваем полученный ID", dependsOnMethods = "setUp1")
    public void setUp2() {
        httprequest.header("Content-Type", "application/JSON");
        httprequest.body(requestBody.toString());
        Response response = httprequest.request(POST);
        bearId = response.getBody().asString();
    }

    @Test(description = "")
    public void CreateTest() {
        /*Получаем данные по Id созданного медведя*/
        httprequest.header("Content-Type", "application/JSON");
        Response response = httprequest.request(GET, "/" + bearId);
        Bear responseBody = new Gson().fromJson(response.getBody().asString(), Bear.class);

        /*Проверяем что вернулось то, что отправили*/
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(responseBody.getAge(), is(age));
        assertThat(responseBody.getName(), is(name));
        assertThat(responseBody.getType(), is(type));

       /* assertThat(responseBody,not(emptyOrNullString()));
        assertThat(responseBody,not(equalTo("null")));
        JSONObject responseBodyJson = new JSONObject(responseBody);
        assertThat(responseBodyJson.get("bear_type"), equalTo(type));
        assertThat(responseBodyJson.get("bear_age"), equalTo(age));
        assertThat(responseBodyJson.get("bear_name"), equalTo(name));*/
    }
}
