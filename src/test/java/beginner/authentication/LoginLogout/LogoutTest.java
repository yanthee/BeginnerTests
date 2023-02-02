package beginner.authentication.LoginLogout;

import beginner.Employer.EmployerBuilder;
import beginner.Employer.EmployerPersonalData;
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


public class LogoutTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.basePath = "authentication";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
        //  RestAssured.filters(new CookieFilter()); <-Pozwala zachowac cookies ale nie do końca wiadomo w jaki sposób//
    }

    @Test
    public void givenNoBodyWhenPostThenUserIsLoggedOutTest() {
        given()
                .when().post("logout")
                .then().statusCode(400);
    }

    @Test
    public void givenEmployerIsLoggedWhenPostLogOutThenEmployerIsLoggedOutTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData)
                .when().post("registerEmployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, employerPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        given()
                .cookies(cookies)
                .when().post("logout")
                .then().statusCode(200);
    }

    @Test
    public void givenUserIsLoggedWhenPostLogOutThenUserIsLoggedOutTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        given()
                .cookies(cookies)
                .when().post("logout")
                .then().statusCode(200);

    }
}
