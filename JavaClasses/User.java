package javaclasses;
public class User{
    public enum Role {
        DOCTOR,
        PHARMACIST,
        PATIENT,
        ADMINISTRATOR;
    }

    private String hospitalID;
    private String password;
    private Role role;

    User(String hospitalID, String password){
        this.hospitalID = hospitalID;
        this.password = password;
        this.role=determineRole(hospitalID);
    }

    public String getHospitalID(){
        return this.hospitalID;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String newPassword) { // This is for first login
        this.password = newPassword;
    }

    public Role getRole() {
        return this.role; 
    }

    public void logOut(){

    }

    public void login(String hospitalID, String password){
        this.hospitalID=hospitalID;
        this.password=password;
    }

    private Role determineRole(String hospitalID) {
        if (hospitalID.matches("[PDA]\\d{3}")) { // 3 digits for staff
            if (hospitalID.startsWith("P")) {
                return Role.PHARMACIST; 
            } else if (hospitalID.startsWith("D")) {
                return Role.DOCTOR;
            } else{
                return Role.ADMINISTRATOR;
            }
        } else {
            return Role.PATIENT; 
        }
    }
}
