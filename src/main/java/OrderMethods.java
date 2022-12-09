import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

public class OrderMethods extends RestClient {
    @Step("Get ingredients list")
    public ValidatableResponse getIngredients() {
        return
                 RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients/")
                .then();
    }

    @Step("Create an order")
    public ValidatableResponse createOrder(Order order) {
        return
                RestAssured.given()
                        .spec(getBaseSpec())
                        .body(order)
                        .when()
                        .post("/api/orders/")
                        .then();
    }

    @Step("Create an order with auth")
    public ValidatableResponse createAuthOrder(String accessToken, Order order) {
        return
                RestAssured.given()
                        .spec(getBaseSpec())
                        .header("Authorization", accessToken)
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders/")
                        .then();
    }

    @Step("Get auth users orders")
    public ValidatableResponse getAuthOrders(String accessToken) {
        return
                RestAssured.given()
                        .spec(getBaseSpec())
                        .header("Authorization", accessToken)
                        .when()
                        .get("/api/orders/")
                        .then();
    }

    @Step("Get order without auth")
    public ValidatableResponse getOrdersWithoutAuth() {
        return
                RestAssured.given()
                        .spec(getBaseSpec())
                        .when()
                        .get("/api/orders/")
                        .then();
    }


}
