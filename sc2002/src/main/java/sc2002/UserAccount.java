package sc2002;

public class UserAccount {
    private String hospitalID;
    private String password;

    UserAccount(String hospitalID, String password){
        this.hospitalID = hospitalID;
        this.password = password;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public String getPassword() {
        return password;
    }
}
