package beginner.fillDatabase;

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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class FillDatabase {
    @BeforeTest
    public void setupConfig() {
        RestAssured.baseURI = "https://localhost:3000";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter());
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void givenUserPersonalDataWhenPostNewUserToDatabaseThenUserIsCreatedTest() {
        for (int i = 0; i < 2; i++) {
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
            Offer offer2 = new Offer();
            OfferBuilder offerBuilder2 = new OfferBuilder();
            offerBuilder2.build(offer2);
            given().cookies(cookies).body(offer2).when().put("offers/createoffer").then().statusCode(201);
            Offer offer3 = new Offer();
            OfferBuilder offerBuilder3 = new OfferBuilder();
            offerBuilder3.build(offer3);
            given().cookies(cookies).body(offer3).when().put("offers/createoffer").then().statusCode(201);

            OfferIdBuilder offerIdBuilder = new OfferIdBuilder();
            String offerUrl1 = offerIdBuilder.OfferBuilder(offer1,employerPersonalData);
            String offerUrl2 = offerIdBuilder.OfferBuilder(offer2,employerPersonalData);



            UserPersonalData userPersonalData1 = new UserPersonalData();
            UserBuilder userBuilder1 = new UserBuilder();
            userBuilder1.build(userPersonalData1);

            given().body(userPersonalData1)
                    .when().post("authentication/register")
                    .then().statusCode(201);

            LoginBuilder loginBuilder1 = new LoginBuilder();
            LoginData loginUserData1 = new LoginData();
            loginBuilder1.build(loginUserData1, userPersonalData1);

            Response response1 = given().body(loginUserData1)
                    .when().post("authentication/login")
                    .then().statusCode(200).extract().response();
            Map<String, String> userCookies1 = response1.getCookies();


            given().pathParam("publicUrl", offerUrl1).cookies(userCookies1)
                    .when().post("apply/{publicUrl}")
                    .then().statusCode(200);

            given().pathParam("publicUrl", offerUrl2).cookies(userCookies1)
                    .when().post("apply/{publicUrl}")
                    .then().statusCode(200);
//-----------------------------
//            given().cookies(userCookies1).when().get("account/profile").then().statusCode(200);

//            given().cookies(userCookies1).when().post("authentication/logout").then().statusCode(200);
//
//            Response response2 = given().body(loginUserData1)
//                    .when().post("authentication/login")
//                    .then().statusCode(200).extract().response();
//            Map<String, String> userCookiesRenewed= response2.getCookies();
//
//            given().pathParam("publicUrl", publicUrl2).cookies(userCookiesRenewed)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);


//            given().pathParam("publicUrl", publicUrl2).cookies(userCookies1)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);
//            given().pathParam("publicUrl", publicUrl3).cookies(userCookies1)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);


//            UserPersonalData userPersonalData2 = new UserPersonalData();
//            UserBuilder userBuilder2 = new UserBuilder();
//            userBuilder2.build(userPersonalData2);
//
//            given().body(userPersonalData2)
//                    .when().post("authentication/register")
//                    .then().statusCode(201);
//
//            LoginBuilder loginBuilder2 = new LoginBuilder();
//            LoginData loginUserData2 = new LoginData();
//            loginBuilder2.build(loginUserData2, userPersonalData2);
//
//            Response response2 = given().body(loginUserData2)
//                    .when().post("authentication/login")
//                    .then().statusCode(200).extract().response();
//            Map<String, String> userCookies2 = response2.getCookies();
//
//
//            given().pathParam("publicUrl", publicUrl1).cookies(userCookies2)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);
//            given().pathParam("publicUrl", publicUrl2).cookies(userCookies2)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);
//            given().pathParam("publicUrl", publicUrl3).cookies(userCookies2)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);
//
//            UserPersonalData userPersonalData3 = new UserPersonalData();
//            UserBuilder userBuilder3 = new UserBuilder();
//            userBuilder3.build(userPersonalData1);
//
//            given().body(userPersonalData3)
//                    .when().post("authentication/register")
//                    .then().statusCode(201);
//
//            LoginBuilder loginBuilder3 = new LoginBuilder();
//            LoginData loginUserData3 = new LoginData();
//            loginBuilder3.build(loginUserData3, userPersonalData3);
//
//            Response response3 = given().body(loginUserData3)
//                    .when().post("authentication/login")
//                    .then().statusCode(200).extract().response();
//            Map<String, String> userCookies3 = response3.getCookies();
//
//            given().pathParam("publicUrl", publicUrl1).cookies(userCookies3)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);
//            given().pathParam("publicUrl", publicUrl2).cookies(userCookies3)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);
//            given().pathParam("publicUrl", publicUrl3).cookies(userCookies3)
//                    .when().post("apply/{publicUrl}")
//                    .then().statusCode(200);
        }
    }
}