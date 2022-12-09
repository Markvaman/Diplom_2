import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateUserDataTests {
    private BurgersUser user;
    private BurgersUserMethods userMethods;
    private String accessToken;

    @Before
    public void setUp() {
        userMethods = new BurgersUserMethods();
        user = new BurgersUser("testuser8@gmail.com", "7777", "testUser8");
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
    @DisplayName("Update user data with authorization is successfully")
    @Description("Send patch to /api/auth/user, status code 200 OK")
    public void userDataUpdateWithAuthorizationOkTest() {
        user = new BurgersUser("updateuser10@mail.ru", "updateUser10");
        ValidatableResponse response = userMethods.update(accessToken, user);
        int updateCode = response.extract().statusCode();
        assertEquals(SC_OK, updateCode);
        boolean isUpdate = response.extract().path("success");
        assertTrue("User is not update", isUpdate);
        String updateUser = response.extract().body().asString();
        assertThat(updateUser, containsString("updateuser10@mail.ru"));

    }

    @Test
    @DisplayName("Update user data without authorization is impossible")
    @Description("Send patch to /api/auth/user, status code 401")
    public void userDataCannotUpdateWithoutAuthorizationTest() {
        user = new BurgersUser("updateuser9@mail.ru", "updateUser9");
        ValidatableResponse response = userMethods.updateWithoutAuth(user);
        int updateCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, updateCode);
        String message = response.extract().path("message");
        assertEquals("You should be authorised", message);
    }

}
