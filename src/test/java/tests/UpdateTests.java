package tests;

import com.google.gson.Gson;
import model.Bear;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.http.Method.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UpdateTests extends BaseTest {

    private String id;

    @DataProvider
    public static Object[][] bearProvider() {
        return new Object[][]{
                {new Bear("BLACK", "UPDATEMEIFYOUCAN", 1D)},
                {new Bear("BROWN", "YOUDIDIT", 1D)},
                {new Bear("BROWN", "UPDATEMEIFYOUCAN", 17d)}
        };
    }

    /*Создаем медведя для теста*/
    @BeforeMethod
    public void createBear() {
        Bear bear = new Bear("BROWN", "UPDATEMEIFYOUCAN", 1D);
        JSONObject requestBody = new JSONObject(new Gson().toJson(bear));
        httpRequest.body(requestBody.toString());
        response = httpRequest.request(POST);
        id = response.getBody().asString();
    }


    @Test(dataProvider = "bearProvider")
    public void updateTest(Bear bear) {
        JSONObject requestBody = new JSONObject(new Gson().toJson(bear));
        httpRequest.body(requestBody.toString());
        response = httpRequest.request(PUT, "/" + id);
        assertThat("Проверяем statuscode", response.getStatusCode(), equalTo(200));
        assertThat("Проверяем body", response.getBody().asString(), equalTo("OK"));

        response = httpRequest.request(GET, "/" + id);
        Bear responseObject = new Gson().fromJson(response.getBody().asString(), Bear.class);

        assertThat("Проверяем имя", responseObject.getName(), is(bear.getName()));
        assertThat("Проверяем возраст", responseObject.getAge(), is(bear.getAge()));
        assertThat("Проверяем тип", responseObject.getType(), is(bear.getType()));
    }
}
