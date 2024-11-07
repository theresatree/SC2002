package sc2002.models;

/**
 * The ContactInformation class represents a contact's phone number and email address.
 */
public class ContactInformation {
    private int phoneNumber;
    private String emailAddress;

    /**
     * Constructs a ContactInformation object with the specified phone number and email address.
     *
     * @param phoneNumber the phone number of the contact
     * @param emailAddress the email address of the contact
     */
    public ContactInformation(int phoneNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the email address.
     *
     * @param emailAddress the new email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}