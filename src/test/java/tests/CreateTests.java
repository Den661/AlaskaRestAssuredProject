package tests;

import com.google.gson.Gson;
import model.Bear;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.POST;
import static java.util.Map.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateTests extends BaseTest {

    private JSONObject requestBody;
    private Object Bear;

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {"BROWN", "ИСААК", 15D, 200},
                {"BLACK", "МАРИЯ", 11.7, 200},
                {"POLAR", "NIKOLA", 0.7, 200},
                {"GUMMY", "ALBERT", 15D, 200},
                {"BROWN", "Исаак", 1D, 200},
                {"NOTBEAR", "ИСААК", 1D, 500},
                {"POLAR", "NIKOLA", 101D, 400},
                {"POLAR", "NIKOLA", -1D, 400},
        };
    }


    @Test(dataProvider = "testData")
    public void CreateTest(String type, String name, Double age, int statusCode) {
        /*Создаем медведя и вытаскиваем его ID*/
        requestBody = new JSONObject(of("bear_type", type, "bear_age", age, "bear_name", name));
        httprequest.header("Content-Type", "application/JSON");
        httprequest.body(requestBody.toString());
        response = httprequest.request(POST);
        String bearId = response.getBody().asString();
        assertThat("Проверяем StatusCode", response.getStatusCode(), is(statusCode));

        /*Получаем данные по Id созданного медведя*/
        httprequest.header("Content-Type", "application/JSON");
        response = httprequest.request(GET, "/" + bearId);
        model.Bear responseBody = new Gson().fromJson(response.getBody().asString(), Bear.class);

        /*Проверяем что вернулось то, что отправили*/
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(responseBody.getAge(), is(age));
        assertThat(responseBody.getName(), is(name));
        assertThat(responseBody.getType(), is(type));
    }
}
