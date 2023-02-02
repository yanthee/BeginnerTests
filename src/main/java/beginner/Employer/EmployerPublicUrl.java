package beginner.Employer;

public class EmployerPublicUrl {
    public String employerPublicUrlBuilder(EmployerPersonalData employerPersonalData){
        String employerName = employerPersonalData.getCompanyName();
        String array [] = employerName.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : array) {
            stringBuilder.append(s);
            stringBuilder.append("-");
        }
        String publicCompanyUrl = String.valueOf(stringBuilder);
        return publicCompanyUrl.substring(0,publicCompanyUrl.length()-1);
    }
}
