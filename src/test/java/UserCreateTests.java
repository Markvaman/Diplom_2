import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class UserCreateTests {
    private BurgersUser user;
    private BurgersUserMethods userMethods;
    private String accessToken;

    @Before
    public void setUp() {
        userMethods = new BurgersUserMethods();
    }

    @Test
    @DisplayName("User account create successfully")
    @Description("Send post to /api/auth/register, status code 200 OK")
    public void userAccountCanBeCreatedTest() {
        user = new BurgersUser("twenty@gmail.com", "7777", "twentyUser");
        ValidatableResponse response = userMethods.create(user);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        boolean isCreated = response.extract().path("success");
        assertTrue("User is not created", isCreated);

        accessToken = response.extract().path("accessToken");
        userMethods.delete(accessToken);

    }

    @Test
    @DisplayName("Account with the same data is not created")
    @Description("Send post twice to /api/auth/register, status code 403")
    public void userAccountWithSameDataCannotBeCreatedTest() {
        user = new BurgersUser("twenty@gmail.com", "7777", "twentyUser");
        ValidatableResponse response = userMethods.create(user);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        boolean isCreated = response.extract().path("success");
        assertTrue("User is not created", isCreated);

        ValidatableResponse responseTwo = userMethods.create(user);
        int statusCodeForbidden = responseTwo.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCodeForbidden);
        String message = responseTwo.extract().path("message");
        assertEquals("User already exists", message);

        accessToken = response.extract().path("accessToken");
        userMethods.delete(accessToken);

    }
    @Test
    @DisplayName("Account without credentials is not created")
    @Description("Send post to /api/auth/register, status code 403")
    public void userAccountWithoutCredentialsIsNotCreatedTest() {
        user = new BurgersUser("12589@mail.ru", "36985");
        ValidatableResponse response = userMethods.create(user);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
        boolean isNotCreated = response.extract().path("success");
        assertFalse("User is created", isNotCreated);
    }

}
