package beginner.Employer.ApplicationManagement.get;

import beginner.Employer.EmployerBuilder;
import beginner.Employer.EmployerPersonalData;
import beginner.login.LoginBuilder;
import beginner.login.LoginData;
import beginner.offer.Offer;
import beginner.offer.OfferBuilder;
import beginner.offer.id.OfferIdBuilder;
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

public class EmployerApplicationManagementPublicUrlTest {

    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void givenEmployerPersonalDataWhenEmployerIsLoggedThenAppMgmntPublicUrlIsDisplayedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData)
                .when().post("authentication/registerEmployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, employerPersonalData);

        Response response = given().body(loginData)
                .when().post("authentication/login")
                .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        Offer offer = new Offer();
        OfferBuilder offerBuilder = new OfferBuilder();
        offerBuilder.build(offer);

        given().cookies(cookies).body(offer)
                .when().put("offers/createoffer")
                .then().statusCode(201);

        OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
        String publicUrl = offerIdBuilder.OfferBuilder(offer, employerPersonalData);

        given()
                .pathParam("publicUrl", publicUrl).cookies(cookies)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(200);

        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given().body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(201);

        LoginData loginUserData = new LoginData();
        loginBuilder.build(loginUserData, userPersonalData);

        Response response1 = given().body(loginUserData)
                .when().post("authentication/login")
                .then().statusCode(200).extract().response();
        Map<String, String> userCookies = response1.getCookies();

        given().pathParam("publicUrl", publicUrl).cookies(userCookies)
                .when().post("apply/{publicUrl}")
                .then().statusCode(200);

        given()
                .pathParam("publicUrl", publicUrl).cookies(cookies)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(200);
    }

    @Test
    public void givenNoEmployerDataWhenEmployerIsNotLoggedThenAppMgmntPublicUrlIsNotDisplayedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData)
                .when().post("authentication/registerEmployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, employerPersonalData);

        Response response = given().body(loginData)
                .when().post("authentication/login")
                .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        Offer offer = new Offer();
        OfferBuilder offerBuilder = new OfferBuilder();
        offerBuilder.build(offer);

        given().cookies(cookies).body(offer)
                .when().put("offers/createoffer")
                .then().statusCode(201);

        OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
        String publicUrl = offerIdBuilder.OfferBuilder(offer, employerPersonalData);

        given().cookies(cookies)
                .when().post("authentication/logout")
                .then().statusCode(200);

        given()
                .pathParam("publicUrl", publicUrl)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(404); //dlatego że będzie tu przekierowanie
    }

    @Test
    public void givenWrongPublicUrlWhenEmployerIsLoggedThenAppMgmntDisplaysError() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given()
                .body(employerPersonalData)
                .when().post("authentication/registerEmployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, employerPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        Offer offer = new Offer();
        OfferBuilder offerBuilder = new OfferBuilder();
        offerBuilder.build(offer);

        given()
                .cookies(cookies)
                .body(offer)
                .when().put("offers/createoffer")
                .then().statusCode(201);

        OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
        String publicUrl = offerIdBuilder.OfferBuilder(offer, employerPersonalData) + "wrongUrl!";

        given()
                .pathParam("publicUrl", publicUrl).cookies(cookies)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(404);
    }

    @Test
    public void givenPublicUrlWhenLoggedInAsNormalUserThenAppMgmntIsNotDisplayedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given()
                .body(employerPersonalData)
                .when().post("authentication/registerEmployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData, employerPersonalData);

        Response response =
                given()
                        .body(loginData)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        Offer offer = new Offer();
        OfferBuilder offerBuilder = new OfferBuilder();
        offerBuilder.build(offer);

        given()
                .cookies(cookies)
                .body(offer)
                .when().put("offers/createoffer")
                .then().statusCode(201);


        OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
        String publicUrl = offerIdBuilder.OfferBuilder(offer, employerPersonalData);

        given()
                .cookies(cookies)
                .when().post("authentication/logout")
                .then().statusCode(200);

        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given()
                .body(userPersonalData)
                .when().post("authentication/register")
                .then().statusCode(201);

        LoginData loginData1 = new LoginData();
        loginBuilder.build(loginData1, userPersonalData);

        Response response1 =
                given()
                        .body(loginData1)
                        .when().post("authentication/login")
                        .then().extract().response();

        Map<String, String> userCookies = response1.getCookies();

        given()
                .pathParam("publicUrl", publicUrl).cookies(userCookies)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(404);
    }

    @Test
    public void givenOtherEmployerPersonalDataWhenGetAppMgmntThenAppMgmntIsNotDisplayedTest() {
        EmployerPersonalData employerOnePersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerOnePersonalData);

        given()
                .body(employerOnePersonalData)
                .when().post("authentication/registerEmployer")
                .then().statusCode(201);

        LoginData loginDataEmployerOne = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginDataEmployerOne, employerOnePersonalData);

        Response responseEmployerOne =
                given()
                        .body(loginDataEmployerOne)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();


        Map<String, String> employerOneCookies = responseEmployerOne.getCookies();

        Offer offer = new Offer();
        OfferBuilder offerBuilder = new OfferBuilder();
        offerBuilder.build(offer);

        given()
                .cookies(employerOneCookies)
                .body(offer)
                .when().put("offers/createoffer")
                .then().statusCode(201);

        OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
        String publicUrl = offerIdBuilder.OfferBuilder(offer, employerOnePersonalData);


        EmployerPersonalData employerTwoPersonalData = new EmployerPersonalData();
        employerBuilder.build(employerTwoPersonalData);

        given()
                .body(employerTwoPersonalData)
                .when().post("authentication/registerEmployer")
                .then().statusCode(201);

        LoginData loginDataEmployerTwo = new LoginData();
        loginBuilder.build(loginDataEmployerTwo, employerTwoPersonalData);

        Response responseEmployerTwo =
                given()
                        .body(loginDataEmployerTwo)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> employerTwoCookies = responseEmployerTwo.getCookies();

        given()
                .pathParam("publicUrl", publicUrl)
                .cookies(employerTwoCookies)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(404);
    }
}
