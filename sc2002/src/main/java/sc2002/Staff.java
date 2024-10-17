package sc2002;

public class Staff {
    private String staffID;
    private String name;
    private Role role;
    private String gender;
    private int age;
    private int phoneNumber;
    private String email;

    Staff(String staffID, String name, Role role, String gender, int age, int phoneNumber, String email) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String printStaffList(){
        return(
            "Staff ID: " + staffID + "\n" +
            "Name: " + name + "\n" +
            "Role: " + role + "\n" +
            "Gender: " + gender + "\n" +
            "Age: " + age + "\n" +
            "Phone Number: " + phoneNumber + "\n" +
            "Email: " + email + "\n");
    }
}
