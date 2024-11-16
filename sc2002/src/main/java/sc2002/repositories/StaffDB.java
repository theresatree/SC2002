package sc2002.repositories;

import sc2002.models.Staff;
import sc2002.models.UserAccount;
import sc2002.enums.Role;
import sc2002.StaffFiltering.StaffFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the database for managing staff records, including adding, updating,
 * retrieving, and removing staff members.
 */
public class StaffDB {
    private static final String STAFF_FILE = "Staff_List.csv";
    private static final String USER_FILE = "User.csv";

    /**
     * Retrieves a list of staff members filtered by the specified criteria.
     * 
     * @param selectedFilter The filter criteria to apply to the staff records.
     * @return A list of staff members that match the filter criteria.
     * @throws IOException If there is an issue reading the file.
     */
    public static List<Staff> getStaff(StaffFilter selectedFilter) throws IOException {
        List<Staff> staffs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(STAFF_FILE))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                String staffID = fields[0];
                String name = fields[1];
                Role role = Role.valueOf(fields[2].toUpperCase());
                String gender = fields[3];
                int age = Integer.parseInt(fields[4]);
                int phoneNumber = Integer.parseInt(fields[5]);
                String email = fields[6];

                Staff staff = new Staff(staffID, name, role, gender, age, phoneNumber, email);

                if (selectedFilter.filter(staff)) {
                    staffs.add(staff);
                }
            }
        }

        return staffs;
    }

    /**
     * Adds a new staff member to the Staff and User databases.
     * 
     * @param newStaff The staff member to add.
     * @throws IOException If there is an issue writing to the file.
     */
    public static void addStaff(Staff newStaff) throws IOException {
        // Add to Staff List
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE, true))) {
            String formattedRole = newStaff.getRole().toString().charAt(0)
                    + newStaff.getRole().toString().substring(1).toLowerCase();

            String newStaffEntry = String.join(",", newStaff.getStaffID(), newStaff.getName(), formattedRole,
                    newStaff.getGender(), String.valueOf(newStaff.getAge()), String.valueOf(newStaff.getPhoneNumber()),
                    newStaff.getEmail());

            bw.write(newStaffEntry);
            bw.newLine();
        }

        // Add to User List with default password
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            bw.write(String.join(",", newStaff.getStaffID(), "password"));
            bw.newLine();
        }
    }

    /**
     * Updates an existing staff member's information in the database.
     * 
     * @param staffID The ID of the staff member to update.
     * @param updatedStaff The updated staff details.
     * @throws IOException If there is an issue updating the file.
     */
    public static void updateStaff(String staffID, Staff updatedStaff) throws IOException {
        List<Staff> staffs = getStaff(staff -> true); // Get all staff

        for (int i = 0; i < staffs.size(); i++) {
            if (staffs.get(i).getStaffID().equals(staffID)) {
                staffs.set(i, updatedStaff);
                break;
            }
        }

        writeStaffListToFile(staffs);
    }

    /**
     * Removes a staff member from both the Staff and User databases.
     * 
     * @param staffID The ID of the staff member to remove.
     * @throws IOException If there is an issue updating the file.
     */
    public static void removeStaff(String staffID) throws IOException {
        List<Staff> staffs = getStaff(staff -> true); // Get all staff
        staffs.removeIf(staff -> staff.getStaffID().equals(staffID));
        writeStaffListToFile(staffs);

        // Update User List by removing the staff member's account
        List<UserAccount> userAccounts = getUserAccounts();
        userAccounts.removeIf(user -> user.getHospitalID().equals(staffID));
        writeUserAccountsToFile(userAccounts);
    }

    /**
     * Finds if a staff member exists in the database.
     * 
     * @param staffID The ID of the staff member.
     * @return 1 if the staff member exists; otherwise, 0.
     */
    public static int findStaff(String staffID) {
        try {
            List<Staff> staffs = getStaff(staff -> true); // Get all staff
            for (Staff staff : staffs) {
                if (staff.getStaffID().equals(staffID)) {
                    return 1;
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while finding staff: " + e.getMessage());
        }
        return 0; // Staff not found
    }

    /**
     * Helper method to write the updated staff list back to the file.
     * 
     * @param staffs The updated staff list.
     * @throws IOException If there is an issue writing to the file.
     */
    private static void writeStaffListToFile(List<Staff> staffs) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE))) {
            bw.write("StaffID,Name,Role,Gender,Age,PhoneNumber,Email");
            bw.newLine();

            for (Staff staff : staffs) {
                String formattedRole = staff.getRole().toString().charAt(0)
                        + staff.getRole().toString().substring(1).toLowerCase();

                String staffEntry = String.join(",", staff.getStaffID(), staff.getName(), formattedRole,
                        staff.getGender(), String.valueOf(staff.getAge()), String.valueOf(staff.getPhoneNumber()),
                        staff.getEmail());

                bw.write(staffEntry);
                bw.newLine();
            }
        }
    }

    /**
     * Retrieves all user accounts from the User file.
     * 
     * @return A list of UserAccount objects.
     * @throws IOException If there is an issue reading the file.
     */
    private static List<UserAccount> getUserAccounts() throws IOException {
        List<UserAccount> userAccounts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                userAccounts.add(new UserAccount(fields[0], fields[1]));
            }
        }

        return userAccounts;
    }

    /**
     * Helper method to write the updated user accounts back to the file.
     * 
     * @param userAccounts The updated user account list.
     * @throws IOException If there is an issue writing to the file.
     */
    private static void writeUserAccountsToFile(List<UserAccount> userAccounts) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            bw.write("HospitalID,Password");
            bw.newLine();

            for (UserAccount user : userAccounts) {
                bw.write(String.join(",", user.getHospitalID(), user.getPassword()));
                bw.newLine();
            }
        }
    }
}