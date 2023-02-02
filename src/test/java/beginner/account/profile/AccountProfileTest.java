package beginner.account.profile;

import beginner.login.LoginData;
import beginner.user.UserBuilder;
import beginner.user.UserPersonalData;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AccountProfileTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void givenUserLoggedInWhenGetProfileThenShowProfileOfLoggedUserTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(201);

        LoginData loginData2 = new LoginData();
        loginData2.setEmail(userPersonalData.getEmail());
        loginData2.setPassword(userPersonalData.getPassword());

        Response response =
                given()
                        .body(loginData2)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        given()
                .cookies(cookies)
                .when().get("account/profile")
                .then().statusCode(200);

    }

    @Test
    public void givenUserIsNotLoggedWhenGetProfileThenUserProfileIsNotDisplayedTest() {
        given()
                .when().get("account/profile")
                .then().statusCode(404);
    }
}
