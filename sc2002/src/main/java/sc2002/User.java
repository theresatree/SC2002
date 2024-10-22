package sc2002;

public class User{

    private String hospitalID;
    private Role role;

    User(String hospitalID){
        this.hospitalID = hospitalID;
        this.role=determineRole(hospitalID);
    }

    public String getHospitalID(){
        return this.hospitalID;
    }

    public Role getRole() {
        return this.role; 
    }

    public boolean logOut(){
        System.out.println("\nLogout successful!");
        return true;
    }

/////////////////////////////////// Detremine Role based on hospitalID ///////////////////////////////////////////
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
