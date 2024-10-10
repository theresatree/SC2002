package sc2002;


public class ContactInformation{
    private int phoneNumber;
    private String emailAddress;

    ContactInformation(int phoneNumber, String emailAddress){
        this.phoneNumber= phoneNumber;
        this.emailAddress = emailAddress;
    }   

    public int getPhoneNumber(){
        return this.phoneNumber;
    }

    public String getEmailAddress(){
        return this.emailAddress;
    }

    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }
}