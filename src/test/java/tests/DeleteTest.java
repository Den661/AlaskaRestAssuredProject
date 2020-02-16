package tests;

import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.http.Method.*;
import static java.util.Map.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DeleteTest extends BaseTest {

    private String id;


    @BeforeClass(description = "Создаем медведя для удаления")
    public void createBear() {
        JSONObject requestBody = new JSONObject(of("bear_type", "BROWN", "bear_age", "1", "bear_name", "DELETEMEIFYOUCAN"));
        httprequest.body(requestBody.toString());
        response = httprequest.request(POST);
        id = response.getBody().asString();
    }

    @Test()
    public void deleteBear() {
        response = httprequest.request(DELETE, "/" + id);
        assertThat("Проверяем статус код", response.getStatusCode(), is(200));

        // TODO: 16.02.2020 Пока проверяем на EMPTY потом переделать на 404
        response = httprequest.request(GET, "/" + id);
        assertThat("Проверяем что медведь удален", response.getBody().asString(), is("EMPTY"));
    }

    @BeforeTest(description = "Создаем медведей для удаления")
    public void createSomeBears() {
        requestBody = new JSONObject(of("bear_type", "BROWN", "bear_age", "1", "bear_name", "DELETEMEIFYOUCAN"));
        httprequest.body(requestBody.toString());
        for (int i = 0; i < 3; i++) {
            response = httprequest.request(POST);
            id = response.getBody().asString();
        }
    }

    @Test
    public void deleteAllBears() {
        response = httprequest.request(DELETE);
        response = httprequest.request(GET);
        assertThat(response.getBody().asString(), is("[]"));
    }

    @Test
    public void deleteBearWhichNotExist() {
        response = httprequest.request(DELETE, "/" + id + 1);
        assertThat("Проверяем статус код", response.getStatusCode(), is(404));
    }


}
