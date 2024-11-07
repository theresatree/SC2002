package sc2002.models;

import sc2002.enums.Role;

/**
 * Represents a staff member with various personal and role-based details.
 */
public class Staff {
    private String staffID;
    private String name;
    private Role role;
    private String gender;
    private int age;
    private int phoneNumber;
    private String email;

    /**
     * Constructs a new staff member.
     * 
     * @param staffID The unique ID of the staff member.
     * @param name The name of the staff member.
     * @param role The role of the staff member.
     * @param gender The gender of the staff member.
     * @param age The age of the staff member.
     * @param phoneNumber The phone number of the staff member.
     * @param email The email address of the staff member.
     */
    public Staff(String staffID, String name, Role role, String gender, int age, int phoneNumber, String email) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    /**
     * Gets the staff ID.
     * 
     * @return The staff ID.
     */
    public String getStaffID() {
        return staffID;
    }

    /**
     * Sets the staff ID.
     * 
     * @param staffID The new staff ID.
     */
    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    /**
     * Gets the name of the staff member.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the staff member.
     * 
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the role of the staff member.
     * 
     * @return The role.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of the staff member.
     * 
     * @param role The new role.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the gender of the staff member.
     * 
     * @return The gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the staff member.
     * 
     * @param gender The new gender.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the age of the staff member.
     * 
     * @return The age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the staff member.
     * 
     * @param age The new age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the phone number of the staff member.
     * 
     * @return The phone number.
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the staff member.
     * 
     * @param phoneNumber The new phone number.
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address of the staff member.
     * 
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the staff member.
     * 
     * @param email The new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a formatted string representation of the staff member's details.
     * 
     * @return A formatted string of the staff details.
     */
    public String printStaffList() {
        return (
            "Staff ID: " + staffID + "\n" +
            "Name: " + name + "\n" +
            "Role: " + role + "\n" +
            "Gender: " + gender + "\n" +
            "Age: " + age + "\n" +
            "Phone Number: " + phoneNumber + "\n" +
            "Email: " + email + "\n"
        );
    }
}