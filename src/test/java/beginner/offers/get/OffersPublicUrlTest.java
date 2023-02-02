package beginner.offers.get;

import beginner.Employer.EmployerBuilder;
import beginner.Employer.EmployerPersonalData;
import beginner.login.LoginData;
import beginner.offer.Offer;
import beginner.offer.OfferBuilder;
import beginner.offer.id.OfferIdBuilder;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OffersPublicUrlTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
        //  RestAssured.filters(new CookieFilter()); <-Pozwala zachowac cookies ale nie do końca wiadomo w jaki sposób//
    }
    @Test
    public void givenOfferDetailsWhenPutOfferThenOfferIsCreatedTest(){
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData)
                .when().post("authentication/registeremployer")
                .then().statusCode(201);

        LoginData loginData = new LoginData();
        loginData.setEmail(employerPersonalData.getEmail());
        loginData.setPassword(employerPersonalData.getPassword());

        Response response = given().body(loginData).when().post("authentication/login").then().statusCode(200).extract().response();

        Map<String, String> cookies = response.getCookies();

        Offer offer1 = new Offer();
        OfferBuilder offerBuilder1 = new OfferBuilder();
        offerBuilder1.build(offer1);
        given().cookies(cookies).body(offer1).when().put("offers/createoffer").then().statusCode(201);
        OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
        String offerUrl1 = offerIdBuilder.OfferBuilder(offer1,employerPersonalData);

        given()
                .cookies(cookies)
                .pathParam("publicUrl",offerUrl1)
                .when().get("offers/{publicUrl}")
                .then().statusCode(200);
    }
}
