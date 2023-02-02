package beginner.account.upateUserData;

import beginner.login.LoginBuilder;
import beginner.login.LoginData;

import beginner.user.UserBuilder;
import beginner.user.UserPersonalData;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AccountUpdateUserDataTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void GivenUserNewDataWhenPutUserNewDataThenUserDataIsUpdatedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given().body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        Faker faker = new Faker();

        String newName = faker.name().firstName();
        String newSurname = faker.name().lastName();
        String newProfession = faker.job().position();


        given().contentType("multipart/form-data")
                .multiPart("name", newName)
                .multiPart("surname", newSurname)
                .multiPart("profession", newProfession)
                .cookies(cookies)
                .when().put("account/updateuserdata")
                .then().statusCode(302);

        given().cookies(cookies)
                .when().get("account/profile")
                .then().statusCode(200)
                .body("name", equalTo(newName),
                        "surname", equalTo(newSurname),
                        "profession", equalTo(newProfession));
    }

    @Test
    public void givenWrongUserDataTypeWhenPutUserDataThenUserIsNotUpdatedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, userPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        Faker faker = new Faker();
        String newName = faker.name().firstName() + "@123";
        String newSurname = faker.name().lastName() + "@123";
        String newProfession = faker.job().position() + "@123";

        given().contentType("multipart/form-data")
                .multiPart("name", newName)
                .multiPart("surname", newSurname)
                .multiPart("profession", newProfession)
                .cookies(cookies)
                .when().put("account/updateuserdata")
                .then().statusCode(302);

        given().cookies(cookies)
                .when().get("account/profile")
                .then().statusCode(200)
                .body("name", equalTo(newName),
                        "surname", equalTo(newSurname),
                        "profession", equalTo(newProfession));
    }
}
