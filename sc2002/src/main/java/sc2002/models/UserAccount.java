package sc2002.models;

/**
 * Represents a user account in the hospital system, containing the hospital ID and associated password.
 */
public class UserAccount {
    private String hospitalID;
    private String password;

    /**
     * Constructs a UserAccount with the specified hospital ID and password.
     *
     * @param hospitalID The unique identifier for the user's account.
     * @param password The password associated with the user's account.
     */
    public UserAccount(String hospitalID, String password) {
        this.hospitalID = hospitalID;
        this.password = password;
    }

    /**
     * Retrieves the hospital ID associated with the user account.
     *
     * @return The hospital ID of the user account.
     */
    public String getHospitalID() {
        return hospitalID;
    }

    /**
     * Retrieves the password associated with the user account.
     *
     * @return The password of the user account.
     */
    public String getPassword() {
        return password;
    }
}