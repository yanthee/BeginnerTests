package beginner.login;

import beginner.Employer.EmployerPersonalData;
import beginner.user.UserPersonalData;

public class LoginBuilder {
    public void build(LoginData loginData, UserPersonalData userPersonalData){
        loginData.setEmail(userPersonalData.getEmail());
        loginData.setPassword(userPersonalData.getPassword());
    }
    public void build(LoginData loginData, EmployerPersonalData employerPersonalData){
        loginData.setEmail(employerPersonalData.getEmail());
        loginData.setPassword(employerPersonalData.getPassword());
    }
}
