package beginner.Employer.ApplicationManagement.offers;

import beginner.Employer.EmployerBuilder;
import beginner.Employer.EmployerPersonalData;
import beginner.login.LoginBuilder;
import beginner.login.LoginData;
import beginner.offer.Offer;
import beginner.offer.OfferBuilder;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class EmployerOffersTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }
    @Test
    public void givenEmployerWhenEmployerIsLoggedInThenOffersAreDisplayedTest(){
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData)
                .when().post("authentication/registeremployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData,employerPersonalData);

        Response response = given().body(loginData)
                .when().post("authentication/login")
                .then().statusCode(200).extract().response();

        Map<String, String> employerCookies = response.getCookies();

        OfferBuilder offerBuilder = new OfferBuilder();
        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        Offer offer3 = new Offer();

        offerBuilder.build(offer1);
        offerBuilder.build(offer2);
        offerBuilder.build(offer3);

        given().body(offer1).cookies(employerCookies)
                .when().put("offers/createoffer")
                .then().statusCode(201);

        given().body(offer2).cookies(employerCookies)
                .when().put("offers/createoffer")
                .then().statusCode(201);

        given().body(offer3).cookies(employerCookies)
                .when().put("offers/createoffer")
                .then().statusCode(201);

        given().cookies(employerCookies)
                .when().get("employer/offers")
                .then().statusCode(200);

    }
}
