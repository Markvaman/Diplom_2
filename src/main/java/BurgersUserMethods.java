import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class BurgersUserMethods extends RestClient {
    @Step("Create new user")
    public ValidatableResponse create(BurgersUser user) {
        return
                given()
                        .spec(getBaseSpec())
                        .body(user)
                        .when()
                        .post("/api/auth/register/")
                        .then();
    }
    @Step("Login with credentials")
        public ValidatableResponse login(UserCredentials userCredentials) {
            return
                    given()
                            .spec(getBaseSpec())
                            .body(userCredentials)
                            .when()
                            .post("/api/auth/login")
                            .then();
        }

    @Step("Delete user")
    public ValidatableResponse delete(String accessToken) {
        return
                given()
                        .spec(getBaseSpec())
                        .header("Authorization", accessToken)
                        .when()
                        .delete("/api/auth/user/")
                        .then();
    }
    }


