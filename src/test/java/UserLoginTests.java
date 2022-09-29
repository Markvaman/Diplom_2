import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserLoginTests {
    private BurgersUser user;
    private BurgersUserMethods userMethods;
    private String accessToken;
    @Before
    public void setUp() {
        userMethods = new BurgersUserMethods();
        user = new BurgersUser("testUser54@gmail.com", "7777", "testUser54User");
        ValidatableResponse response = userMethods.create(user);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);

        accessToken = response.extract().path("accessToken");
    }

    @After
    public void deleteUser() {
        userMethods.delete(accessToken);
    }

    @Test
    @DisplayName("User login with credentials is successfully")
    @Description("Send post to /api/auth/login, status code 200 OK")
    public void loginWithCredentialsIsOkTest() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse response = userMethods.login(userCredentials);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
        boolean isLogin = response.extract().path("success");
        assertTrue("user is not login", isLogin);
    }

    @Test
    @DisplayName("User login without password is not successfully")
    @Description("Send post to /api/auth/login, status code 401")
    public void loginWithoutPasswordIsImpossibleTest() {
        UserCredentials userCredentials = new UserCredentials(user.getEmail());
        ValidatableResponse response = userMethods.login(userCredentials);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
        boolean isNotLogin = response.extract().path("success");
        assertFalse("User is login", isNotLogin);
    }

}
