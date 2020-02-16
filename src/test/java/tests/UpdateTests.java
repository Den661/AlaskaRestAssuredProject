package tests;

import com.google.gson.Gson;
import model.Bear;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.http.Method.*;
import static java.util.Map.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UpdateTests extends BaseTest {

    private String id;

    @DataProvider
    public static Object[][] bearProvider() {
        return new Object[][]{
                {"BLACK", 1d, "UPDATEMEIFYOUCAN"},
                {"BROWN", 1d, "YOUDIDIT"},
                {"BROWN", 17d, "UPDATEMEIFYOUCAN"}
        };
    }

    /*Создаем медведя для теста*/
    @BeforeMethod
    public void createBear() {
        JSONObject requestBody = new JSONObject(of("bear_type", "BROWN", "bear_age", "1", "bear_name", "UPDATEMEIFYOUCAN"));
        httprequest.body(requestBody.toString());

        response = httprequest.request(POST);
        id = response.getBody().asString();
    }


    @Test(dataProvider = "bearProvider")
    public void updateTest(String type, double age, String name) {
        JSONObject requestBody = new JSONObject(of("bear_type", type, "bear_age", age, "bear_name", name));
        httprequest.body(requestBody.toString());

        response = httprequest.request(PUT, "/" + id);
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getBody().asString(), equalTo("OK"));

        response = httprequest.request(GET, "/" + id);
        Bear responseObject = new Gson().fromJson(response.getBody().asString(), Bear.class);

        assertThat("Проверяем имя", responseObject.getName(), is(name));
        assertThat("Проверяем возраст", responseObject.getAge(), is(age));
        assertThat("Проверяем тип", responseObject.getType(), is(type));

    }
}
