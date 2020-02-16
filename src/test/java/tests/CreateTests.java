package tests;

import com.google.gson.Gson;
import model.Bear;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.POST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CreateTests extends BaseTest {

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {new Bear("BROWN", "ИСААК", 15D), 200},
                {new Bear("BLACK", "МАРИЯ", 11.7), 200},
                {new Bear("POLAR", "NIKOLA", 0.7), 200},
                {new Bear("GUMMY", "ALBERT", 15D), 200},
                {new Bear("BROWN", "Исаак", 1D), 200},
                {new Bear("NOTBEAR", "ИСААК", 1D), 500},
                {new Bear("POLAR", "NIKOLA", 101D), 400},
                {new Bear("POLAR", "NIKOLA", -1D), 400},
        };
    }

    @Test(dataProvider = "testData")
    public void createTest(Bear bear, int statusCode) {
        /*Создаем медведя и вытаскиваем его ID*/
        JSONObject requestBody = new JSONObject(new Gson().toJson(bear));
        httpRequest.body(requestBody.toString());
        response = httpRequest.request(POST);
        String bearId = response.getBody().asString();
        assertThat("Проверяем StatusCode", response.getStatusCode(), is(statusCode));

        /*Получаем данные по Id созданного медведя*/
        response = httpRequest.request(GET, "/" + bearId);
        Bear responseBody = new Gson().fromJson(response.getBody().asString(), Bear.class);

        assertThat("Проверяем statuscode", response.getStatusCode(), equalTo(200));
        assertThat("Проверяем сохраненный возраст", responseBody.getAge(), is(bear.getAge()));
        assertThat("Проверяем сохраненное имя", responseBody.getName(), is(bear.getName()));
        assertThat("Проверяем сохраненный тип", responseBody.getType(), is(bear.getType()));
    }
}
