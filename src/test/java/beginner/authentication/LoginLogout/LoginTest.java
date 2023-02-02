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
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoginTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.basePath = "authentication";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    //Testy dla Employera
    @Test
    public void givenValidEmployerDataWhenPostThenEmployerIsLoggedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given()
                .body(employerPersonalData)
                .when().post("registerEmployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, employerPersonalData);

        given()
                .body(loginData)
                .when().post("login")
                .then().statusCode(200);
    }

    @Test
    public void givenInvalidEmployerDataWhenPostThenEmployerIsNotLoggedInTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given()
                .body(employerPersonalData)
                .when().post("registerEmployer")
                .then().statusCode(201);
        String randomString = RandomStringUtils.randomAlphabetic(10);

        employerPersonalData.setPassword(randomString);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, employerPersonalData);

        given()
                .body(loginData)
                .when().post("login")
                .then().statusCode(400);
    }

    @Test
    public void givenEmptyUserDataWhenPostUserDataThenUserIsNotLoggedInTest() {
        UserPersonalData userPersonalData = new UserPersonalData();

        given()
                .body(userPersonalData)
                .when().post("login")
                .then().statusCode(400);
    }

    //Testy dla Usera

    @Test
    public void givenValidUserDataWhenPostThenUserIsLoggedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("register")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        given()
                .body(loginData)
                .when().post("login")
                .then().statusCode(200);
    }

    @Test
    public void givenInvalidUserDataWhenPostThenUserIsNotLoggedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("register")
                .then().statusCode(201);

        String randomString = RandomStringUtils.randomAlphabetic(10);
        userPersonalData.setPassword(randomString);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        given()
                .body(loginData)
                .when().post("login")
                .then().statusCode(400);
    }

}
