import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrdersTest {
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
    @DisplayName("Get orders of auth user")
    @Description("Send GET to api/orders, status 200 OK")
    public void getOrdersWithAuthTest() {
        userMethods = new BurgersUserMethods();
        user = new BurgersUser("testmail1@mail.ru", "5588", "testUser12");
        ValidatableResponse userResponse = userMethods.create(user);
        accessToken = userResponse.extract().path("accessToken");

        ValidatableResponse response = orderMethods.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[4]._id"));
        ingredients.add(response.extract().path("data[6]._id"));
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderMethods.createOrder(order);
        int statusCode = orderResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);

        ValidatableResponse getResponse = orderMethods.getAuthOrders(accessToken);
        int getCode = getResponse.extract().statusCode();
        assertEquals(SC_OK, getCode);
        boolean isGetting = getResponse.extract().path("success");
        assertTrue("Orders are not available", isGetting);

        userMethods.delete(accessToken);

    }

    @Test
    @DisplayName("Get orders of user without auth")
    @Description("Send GET to api/orders, status 401")
    public void getOrdersWithoutAuthImpossibleTest() {
        ValidatableResponse response = orderMethods.getIngredients();
        List<String> ingredients = new ArrayList<>();
        ingredients.add(response.extract().path("data[4]._id"));
        ingredients.add(response.extract().path("data[6]._id"));
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderMethods.createOrder(order);
        int statusCode = orderResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);

        ValidatableResponse getResponse = orderMethods.getOrdersWithoutAuth();
        int getCode = getResponse.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, getCode);
        String message = getResponse.extract().path("message");
        assertEquals("You should be authorised", message);
    }
}
