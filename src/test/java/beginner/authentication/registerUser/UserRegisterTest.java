package beginner.authentication.registerUser;

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

public class UserRegisterTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.basePath = "authentication";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void givenUserPersonalDataWhenPostNewUserToDatabaseThenUserIsCreatedTest() {
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        UserPersonalData userPersonalData = new UserPersonalData();
        userPersonalData.setName("Jan");
        userPersonalData.setSurname("Kowalski");
        userPersonalData.setEmail(generatedString + "@example.com");
        userPersonalData.setPassword("Test123#@!");
        userPersonalData.setConfirmPassword("Test123#@!");
        userPersonalData.setPhoneNumber("123456789");
        userPersonalData.setProfession("Example Profession");

        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(201);
    }

    @Test
    public void givenExistingUserPersonalDataWhenPostNewUserToDatabaseThenUserIsNotCreated() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(201);

        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);
    }

    @Test
    public void givenEmptyUserPersonalDataWhenPostNewUserToDatabaseThenUserIsNotCreatedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setName("Jan");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setSurname("Kowalski");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setEmail("test@example.com");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setPassword("Test123#@!");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setConfirmPassword("Test123#@!");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setPhoneNumber("123456789");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setProfession("Example Profession");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);
    }

    @Test
    public void givenInvalidUserPersonalDataWhenPostNewUserToDatabaseThenUserIsNotCreated() {
        String randomEmail = RandomStringUtils.randomAlphabetic(10);
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        userPersonalData.setEmail(randomEmail);

        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);

        userPersonalData.setEmail(randomEmail + "@example.com");
        userPersonalData.setPhoneNumber("123456789012345678901234567890");

        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);


    }

    @Test
    public void givenPasswordAndMismatchingConfirmPasswordUserPersonalDataWhenPostNewUserToDatabaseThenUserIsNotCreatedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);
        userPersonalData.setConfirmPassword("MismatchingPassword");

        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);
    }

    @Test
    public void givenWrongEmailFormatWhenPostNewUserThenUserIsNotCreatedTest() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);
        userPersonalData.setEmail("test");
        given().body(userPersonalData)
                .when().post("register")
                .then().statusCode(400);
    }
}
