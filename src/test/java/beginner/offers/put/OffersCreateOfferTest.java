package beginner.offers.put;

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

public class OffersCreateOfferTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
        //  RestAssured.filters(new CookieFilter()); <-Pozwala zachowac cookies ale nie do końca wiadomo w jaki sposób//
    }

    @Test
    public void givenOfferDataWhenPostOfferDataThenOfferIsCreatedTest(){
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData).when().post("authentication/registerEmployer").then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData,employerPersonalData);

        Response response  = given().body(loginData).when().post("authentication/login").then().statusCode(200).extract().response();
        Map<String, String> cookies = response.getCookies();

        Offer offer = new Offer();
        OfferBuilder offerBuilder =new OfferBuilder();
        offerBuilder.build(offer);

        given().cookies(cookies).body(offer).when().put("offers/createoffer").then().statusCode(201);
    }
}
