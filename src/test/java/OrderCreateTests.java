import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class OrderCreateTests {
    private BurgersUser user;
    private Order order;
    private BurgersUserMethods userMethods;
    private OrderMethods orderMethods;
    private String accessToken;

    @Before
    public void setUp() {
        orderMethods = new OrderMethods();
    }

    @Test
    @DisplayName("Creating order without auth is successful")
    @Description("Send POST to api/orders, status 200 OK")
    public void createOrderWithoutAuthTest() {
        ValidatableResponse response = orderMethods.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[0]._id"));
        ingredients.add(response.extract().path("data[3]._id"));
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderMethods.createOrder(order);
        int statusCode = orderResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        boolean isCreate = response.extract().path("success");
        assertTrue("Order is not created", isCreate);

    }

    @Test
    @DisplayName("Creating order with auth")
    @Description("Send POST to api/auth/register and to api/order, status 200 OK")
    public void createOrderWithAuthTest() {
        userMethods = new BurgersUserMethods();
        user = new BurgersUser("testmail1@mail.ru", "5588", "testUser12");
        ValidatableResponse userResponse = userMethods.create(user);
        int status = userResponse.extract().statusCode();
        assertEquals(SC_OK, status);
        accessToken = userResponse.extract().path("accessToken");

        ValidatableResponse response = orderMethods.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[2]._id"));
        ingredients.add(response.extract().path("data[4]._id"));
        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderMethods.createAuthOrder(accessToken, order);
        int statusCode = orderResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        String order = orderResponse.extract().body().asString();
        assertThat(order, containsString("status"));
        userMethods.delete(accessToken);
    }

    @Test
    @DisplayName("Creating order without ingredients")
    @Description("Send POST to api/orders with empty body, status 400")
    public void createOrderWithoutIngredientsImpossibleTest() {
        List<String> ingredients = new ArrayList<>();
        order = new Order(ingredients);
        ValidatableResponse response = orderMethods.createOrder(order);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);
        String message = response.extract().path("message");
        assertEquals("Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Creating order with invalid ingredients")
    @Description("Send POST to api/orders with invalid hash, status 500")
    public void createOrderWithInvalidIngIsImpossibleTest() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("25896631");
        ingredients.add("1478963");
        order = new Order(ingredients);
        ValidatableResponse response = orderMethods.createOrder(order);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

}
