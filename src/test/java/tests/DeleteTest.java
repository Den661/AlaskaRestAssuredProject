package tests;

import com.google.gson.Gson;
import model.Bear;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.http.Method.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DeleteTest extends BaseTest {

    private String id;
    private Bear bear;


    @BeforeMethod(description = "Создаем медведя для удаления")
    public void createBear() {
        bear = new Bear("BROWN", "DELETEMEIFYOUCAN", 1D);
        JSONObject requestBody = new JSONObject(new Gson().toJson(bear));
        httpRequest.body(requestBody.toString());
        response = httpRequest.request(POST);
        id = response.getBody().asString();
    }

    @Test
    public void deleteBear() {
        response = httpRequest.request(DELETE, "/" + id);
        assertThat("Проверяем статус код", response.getStatusCode(), is(200));

        // TODO: 16.02.2020 Пока проверяем на EMPTY потом переделать на 404
        response = httpRequest.request(GET, "/" + id);
        assertThat("Проверяем что медведь удален", response.getBody().asString(), is("EMPTY"));
    }

    @Test
    public void deleteAllBears() {
        createSomeBears();
        response = httpRequest.request(DELETE);
        response = httpRequest.request(GET);

        assertThat("Проверяем что вернется пустой массив", response.getBody().asString(), is("[]"));
    }

    @Test
    public void deleteBearWhichNotExist() {
        response = httpRequest.request(DELETE, "/" + id + 1);

        assertThat("Проверяем статус код", response.getStatusCode(), is(404));
    }

    private void createSomeBears() {
        JSONObject requestBody = new JSONObject(new Gson().toJson(bear));
        httpRequest.body(requestBody.toString());
        for (int i = 0; i < 3; i++) {
            response = httpRequest.request(POST);
            id = response.getBody().asString();
        }
    }
}
