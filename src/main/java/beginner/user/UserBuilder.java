package beginner.user;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class UserBuilder {
    public void build (UserPersonalData userPersonalData){
        Faker faker = new Faker();
        String userName = faker.name().firstName();
        String userLastName = faker.name().lastName();
        String userEmail = faker.internet().emailAddress();
//        String userPhoneNumber = faker.phoneNumber().cellPhone();
        String userPassword = RandomStringUtils.randomAlphanumeric(10);
        String userProfession = faker.job().position()+" "+faker.job().seniority();
        String randomString = RandomStringUtils.randomAlphabetic(5);


        userPersonalData.setName(userName);
        userPersonalData.setSurname(userLastName.replace("'"," "));
        userPersonalData.setEmail(randomString+userEmail);
        userPersonalData.setPassword(userPassword+"aA123#@!");
        userPersonalData.setConfirmPassword(userPassword+"aA123#@!");
        userPersonalData.setPhoneNumber("123456789");
        userPersonalData.setProfession(userProfession);
        userPersonalData.setOccupation(faker.job().keySkills());
    }
}
