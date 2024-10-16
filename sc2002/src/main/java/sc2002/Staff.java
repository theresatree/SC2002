package sc2002;

public class Staff {
    private String staffID;
    private String name;
    private String gender;
    private int age;
    private Role role;

    Staff(String staffID, String name, String gender, int age, Role role) {
        this.staffID = staffID;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.role = role;
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
    
    public String printStaffList(){
        return(
            "Staff ID: " + staffID + "\n" +
            "Name: " + name + "\n" +
            "Role: " + role + "\n" +
            "Gender: " + gender + "\n" +
            "Age: " + age + "\n");
    }
}
