package beginner.offer;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class OfferBuilder {
    public void build(Offer offer) {
        Faker faker = new Faker();
        String requirementsOne = faker.witcher().character();
        String requirementsTwo = faker.witcher().character();
        String requirementsThree = faker.witcher().character();
        String dutiesRequirementsOne = faker.witcher().monster();
        String dutiesRequirementsTwo = faker.witcher().monster();
        String dutiesRequirementsThree = faker.witcher().monster();
        String languageOne = faker.programmingLanguage().name();
        String languageTwo = faker.programmingLanguage().name();
        String street = faker.address().streetName();
        String postalCode = faker.address().zipCode();
        String companyBenefitsOne = faker.beer().name();
        String companyBenefitsTwo = faker.beer().name();
        String companyBenefitsThree = faker.beer().name();
        String offerTitle = faker.company().profession() + " "+ "job offer";
        String jobProfession = faker.company().profession();
        String companyDescription = faker.lorem().characters(50);
        String randomString = RandomStringUtils.randomAlphabetic(5);
        OfferDetails offerDetails = new OfferDetails();
        Random random = new Random();
        int companySize = random.nextInt(200);
        int salaryFrom = random.nextInt(3500);
        int salaryTo = random.nextInt(24000);
        String companyCity = faker.address().city();
        String[] benefitsArray = {companyBenefitsOne, companyBenefitsTwo, companyBenefitsThree};
        String[] requirementsArray = {requirementsOne, requirementsTwo, requirementsThree};
        String[] dutiesArray = {dutiesRequirementsOne, dutiesRequirementsTwo, dutiesRequirementsThree};
        String[] languages = {languageOne, languageTwo};
        offerDetails.setDuties(dutiesArray);
        offerDetails.setLanguages(languages);
        offerDetails.setRequirements(requirementsArray);
        offerDetails.setPostalCode(postalCode);
        offerDetails.setStreet(street);
        offerDetails.setDescription(companyDescription);
        offerDetails.setCompanySize(String.valueOf(companySize));
        offerDetails.setBenefits(benefitsArray);

        offer.setTitle(offerTitle);
        offer.setSalaryFrom(salaryFrom);
        offer.setSalaryTo(salaryTo);
        offer.setPremium(true);
        offer.setCity(companyCity);
        offer.setProfession(jobProfession);
        offer.setJobType(faker.lorem().characters(10));
        offer.setOfferDetails(offerDetails);

    }
}
