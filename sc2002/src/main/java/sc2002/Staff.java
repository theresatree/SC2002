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
    public String printStaffList(){
        return(
            "Staff ID: " + staffID + "\n" +
            "Name: " + name + "\n" +
            "Role: " + role + "\n" +
            "Gender: " + gender + "\n" +
            "Age: " + age + "\n");
    }
}
