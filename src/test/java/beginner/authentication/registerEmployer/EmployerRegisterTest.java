package beginner.authentication.registerEmployer;

import beginner.Employer.EmployerBuilder;
import beginner.Employer.EmployerPersonalData;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class EmployerRegisterTest {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.basePath = "authentication";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void givenCorrectEmployerDataWhenPostEmployerDataThenEmployerIsCreatedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given().body(employerPersonalData).when().post("registeremployer").then().statusCode(201);
    }

    @Test
    public void givenIncorrectEmployerDataTypeWhenPostEmployerDataThenEmployerIsNotCreatedTest() {
        String data = """
                {
                  "email": 123456789,
                  "password": "Test123#@!",
                  "confirmPassword": "Test123#@!",
                  "phoneNumber": 123456789,
                  "companyName": 123345567
                }""";
        given().body(data).when().post("registeremployer").then().statusCode(400);

    }

    @Test
    public void givenExistingEmployerCompanyNameAndEmailWhenPostEmployerDataThenEmployerIsNotCreatedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);

        given()
                .body(employerPersonalData)
                .when().post("registeremployer")
                .then().statusCode(201);

        given()
                .body(employerPersonalData)
                .when().post("registeremployer")
                .then().statusCode(400);
    }

    @Test
    public void givenWrongEmployerDataWhenPostEmployerDataThenEmployerIsNotCreatedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        EmployerBuilder employerBuilder = new EmployerBuilder();
        employerBuilder.build(employerPersonalData);
        employerPersonalData.setEmail("company"); //powinno być z końcówką @example.com dlatego wrong data

        given().body(employerPersonalData)
                .when().post("registeremployer")
                .then().statusCode(400);

    }

    @Test
    public void givenEmptyEmployerDataWhenPostEmployerDataThenEmployerIsNotCreatedTest() {
        EmployerPersonalData employerPersonalData = new EmployerPersonalData();
        given()
                .body(employerPersonalData)
                .when().post("registeremployer")
                .then().statusCode(400);
    }
}
