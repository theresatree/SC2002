package sc2002.models;

/**
 * The UserAccount class stores the hospital ID and password for a user's account.
 */
public class UserAccount {
    private String hospitalID;
    private String password;

    /**
     * Constructs a UserAccount with a specified hospital ID and password.
     *
     * @param hospitalID the unique identifier for the user's account
     * @param password the password associated with the account
     */
    public UserAccount(String hospitalID, String password) {
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