package beginner.Employer.ApplicationManagement.post;

import beginner.ApplicationManagement.ApplicationManagement;
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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

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
    public void givenApplyUserWhenUserAppliedThenUserStatusIsChanged() {
        UserPersonalData userPersonalData = new UserPersonalData();
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.build(userPersonalData);

        given().body(userPersonalData).when().post("authentication/register").then().statusCode(201);

        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given()
                .body(employerPersonalData)
                .when().post("authentication/registeremployer")
                .then().statusCode(201);

        LoginBuilder loginBuilder = new LoginBuilder();

        LoginData loginDataEmployer = new LoginData();
        loginBuilder.build(loginDataEmployer, employerPersonalData);

        LoginData loginDataUser = new LoginData();
        loginBuilder.build(loginDataUser, userPersonalData);

        Response responseEmployer =
                given()
                        .body(loginDataEmployer)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookiesEmployer = responseEmployer.getCookies();

        Offer offer = new Offer();
        OfferBuilder offerBuilder = new OfferBuilder();
        offerBuilder.build(offer);

        OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
        String publicUrl = offerIdBuilder.OfferBuilder(offer, employerPersonalData);

        given()
                .cookies(cookiesEmployer)
                .body(offer)
                .when().put("offers/createoffer").then().statusCode(201);


        Response responseUser =
                given()
                        .body(loginDataUser)
                        .when().post("authentication/login")
                        .then().statusCode(200).extract().response();

        Map<String, String> cookiesUser = responseUser.getCookies();

        given()
                .cookies(cookiesUser)
                .pathParam("publicUrl", publicUrl)
                .when().post("apply/{publicUrl}")
                .then().statusCode(200);

        JsonPath jsonPath = given()
                .pathParam("publicUrl", publicUrl).cookies(cookiesEmployer)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(200).extract().jsonPath();

        String publicUrlFromResponse = jsonPath.getString("publicUrl[0]");

        ApplicationManagement applicationManagement = new ApplicationManagement();
        applicationManagement.setStatus(1);
        applicationManagement.setEmployeePublicUrl(publicUrlFromResponse);

        given()
                .cookies(cookiesEmployer)
                .body(applicationManagement)
                .pathParam("publicUrl", publicUrl)
                .when().post("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(200);
        given()
                .pathParam("publicUrl", publicUrl).cookies(cookiesEmployer)
                .when().get("employer/applicationmanagement/{publicUrl}")
                .then().statusCode(200)
                .assertThat().body("isAccepted[0]", equalTo(true));
    }
}
