package javaclasses;

public class User{
    public enum Role {
        DOCTOR,
        PHARMACIST,
        PATIENT,
        ADMINISTRATOR;
    }
    
    private int hospitalID;
    private String password;
    private Role role;

    User(){
    }
}
