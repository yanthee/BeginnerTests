package beginner.offers.get;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class OffersTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
        //  RestAssured.filters(new CookieFilter()); <-Pozwala zachowac cookies ale nie do końca wiadomo w jaki sposób//
    }
    @Test
    public void givenWhenGetOffersThenOffersAreDisplayedTest(){

        given()
                .when().get("offers")
                .then().statusCode(200);
    }
    @Test
    public void givenPageNumberWhenGetOffersThenOffersPageIsDisplayed(){

        given()
                .queryParam("page",1)
                .when().get("offers")
                .then().statusCode(200);
    }
    @Test
    public void givenWrongTypePageNumberWhenGetOffersThenOffersPageIsNotDisplayed(){

        given()
                .queryParam("page","one")
                .when().get("offers")
                .then().statusCode(400);
    }

}
