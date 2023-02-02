package beginner.account.userPublicUrl;

import beginner.login.LoginBuilder;
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

public class AccountUserIdTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void givenExistingUserIdWhenUserIsLoggedInGetUserIdProfileThenUserIdProfileIsDisplayedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(200);

        UserPersonalData userPersonalData1 = new UserPersonalData();
        userBuilder.build(userPersonalData1);

        given()
                .body(userPersonalData1)
                .when().post("authentication/register")
                .then().statusCode(200);

        String userPublicUrl = userPersonalData1.getName() + "." + userPersonalData1.getSurname(); //przypisanie id drugiego uzytkownika do zmiennej

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response(); //logowanie na konto pierwszego usera

        Map<String, String> cookies = response.getCookies();

        given()
                .cookies(cookies)
                .pathParam("userPublicUrl", userPublicUrl)
                .when().get("account/user/{userPublicUrl}")
                .then().statusCode(200);
    }

    @Test
    public void givenUserIdWhenUserIsNotLoggedInThenUserIsNotFound() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(201);

        String userPublicUrl = userPersonalData.getName() + "." + userPersonalData.getSurname();

        given().pathParam("userPublicUrl", userPublicUrl)
                .when().get("account/user/{userPublicUrl}")
                .then().statusCode(200);
    }

    @Test
    public void givenNonExistingUserIdWhenUserIsLoggedInThenUserIsNotFoundTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(200);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies;
        cookies = response.getCookies();

        String userId = "Nonexi.Stinguser";

        given()
                .cookies(cookies)
                .pathParam("userPublicUrl", userId)
                .when().get("account/user/{userPublicUrl}")
                .then().statusCode(404);
    }

    @Test
    public void GivenWrongDataTypeUserIdWhenUserIsLoggedInThenUserIsNotFound() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(200);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        int userId = 123123123;

        given()
                .cookies(cookies)
                .pathParam("userPublicUrl", userId)
                .when().get("account/user/{userPublicUrl}")
                .then().statusCode(404);

    }
}
