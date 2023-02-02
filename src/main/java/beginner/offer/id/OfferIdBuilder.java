package beginner.offer.id;

import beginner.Employer.EmployerPersonalData;
import beginner.offer.Offer;

public class OfferIdBuilder {
    public String OfferBuilder(Offer offer, EmployerPersonalData employerPersonalData) {
        String jobTitle = offer.getTitle() + " " + employerPersonalData.getCompanyName();
        String[] arr = jobTitle.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(s);
            sb.append("-");
        }

        String publicUrl = String.valueOf(sb);
        return publicUrl.substring(0, publicUrl.length() - 1);
    }
}
