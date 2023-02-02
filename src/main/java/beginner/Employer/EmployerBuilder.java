package beginner.Employer;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class EmployerBuilder {
    public void build(EmployerPersonalData employerPersonalData){
        Faker faker = new Faker();
        String companyPassword = RandomStringUtils.randomAlphanumeric(10);
        String companyName=faker.company().name();
        String correctCompanyName = companyName.replace(","," ");
        String companyEmail = faker.internet().emailAddress();
        String randomString = RandomStringUtils.randomAlphabetic(5);
//        String companyPhoneNumber= faker.phoneNumber().cellPhone();
        employerPersonalData.setCompanyName(randomString+correctCompanyName.replace("'"," "));
        employerPersonalData.setEmail(randomString+companyEmail);
        employerPersonalData.setPassword(companyPassword+"aA123#@!");
        employerPersonalData.setConfirmPassword(companyPassword+"aA123#@!");
        employerPersonalData.setPhoneNumber("123456789");
    }
}
