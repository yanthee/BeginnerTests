package beginner.company.put;

import beginner.Employer.EmployerBuilder;
import beginner.Employer.EmployerPersonalData;
import beginner.login.LoginBuilder;
import beginner.login.LoginData;
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

public class UpdateCompanyData {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";

        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }
    @Test
    public void givenNewCompanyDataWhenUpdateDataThenDataIsUpdatedTest(){
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData).when().post("authentication/registeremployer").then().statusCode(201);

        LoginData loginData = new LoginData();
        LoginBuilder loginBuilder = new LoginBuilder();
        loginBuilder.build(loginData,employerPersonalData);

       Response response = given()
                .body(loginData)
                .when().post("authentication/login")
                .then().statusCode(200).extract().response();

        Map<String, String> employerCookie = response.getCookies();

        Faker faker = new Faker();

        String newCompanyName = faker.company().name();

//        given().multiPart("CompanyName",newCompanyName)
//                .pathParam("companyPublicUrl")
//                .when().put()


    }

}
