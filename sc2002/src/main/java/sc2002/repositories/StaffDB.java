package sc2002.repositories;

import sc2002.models.Staff;
import sc2002.models.UserAccount;
import sc2002.enums.Role;
import sc2002.StaffFiltering.*;
import sc2002.StaffFiltering.StaffNoFilter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Represents the database for managing staff records, including adding, updating,
 * retrieving, and removing staff members.
 */
public class StaffDB {
    private static final String FILE_NAME = "Staff_List.xlsx";

    /**
     * Retrieves a list of staff members filtered by the specified criteria.
     * 
     * @param selectedFilter The filter criteria to apply to the staff records.
     * @return A list of staff members that match the filter criteria.
     * @throws IOException If there is an issue reading the file.
     */
    public static List<Staff> getStaff(StaffFilter selectedFilter) throws IOException {
        List<Staff> staffs = new ArrayList<>();
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream(FILE_NAME); 
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                
                String staffID = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                Role role = Role.valueOf(row.getCell(2).getStringCellValue().toUpperCase());
                String gender = row.getCell(3).getStringCellValue();
                int age = (int) row.getCell(4).getNumericCellValue();
                int phoneNumber = (int) row.getCell(5).getNumericCellValue();
                String email = row.getCell(6).getStringCellValue();
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
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream(FILE_NAME); 
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            String formattedRole = newStaff.getRole().toString().charAt(0)
                    + newStaff.getRole().toString().substring(1).toLowerCase();
            newRow.createCell(0).setCellValue(newStaff.getStaffID());
            newRow.createCell(1).setCellValue(newStaff.getName());
            newRow.createCell(2).setCellValue(formattedRole);
            newRow.createCell(3).setCellValue(newStaff.getGender());
            newRow.createCell(4).setCellValue(newStaff.getAge());
            newRow.createCell(5).setCellValue(newStaff.getPhoneNumber());
            newRow.createCell(6).setCellValue(newStaff.getEmail());

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        }

        // Add to User List with default password
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream("User.xlsx"); 
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            newRow.createCell(0).setCellValue(newStaff.getStaffID());
            newRow.createCell(1).setCellValue("password");

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/User.xlsx")) {
                workbook.write(fos);
            }
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
        StaffFilter noFilter = new StaffNoFilter();
        List<Staff> staffs = getStaff(noFilter);
        for (int i = 0; i < staffs.size(); i++) {
            if (staffs.get(i).getStaffID().equals(staffID)) {
                staffs.set(i, updatedStaff);
                newStaffFile(staffs);
                return;
            }
        }
    }

    /**
     * Removes a staff member from both the Staff and User databases.
     * 
     * @param staffID The ID of the staff member to remove.
     * @throws IOException If there is an issue updating the file.
     */
    public static void removeStaff(String staffID) throws IOException {
        StaffFilter noFilter = new StaffNoFilter();
        List<Staff> staffs = getStaff(noFilter);
        staffs.removeIf(staff -> staff.getStaffID().equals(staffID));
        newStaffFile(staffs);

        // Update User List by removing the staff member's account
        List<UserAccount> userAccs = new ArrayList<>();
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream("User.xlsx"); 
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                String hospitalID = row.getCell(0).getStringCellValue();
                String password = row.getCell(1).getStringCellValue();
                userAccs.add(new UserAccount(hospitalID, password));
            }
            userAccs.removeIf(userAcc -> userAcc.getHospitalID().equals(staffID));
            newUserAccFile(userAccs);
        }
    }

    /**
     * Saves the updated staff list back to the Staff file.
     * 
     * @param staffs The list of updated staff.
     * @throws IOException If there is an issue writing to the file.
     */
    private static void newStaffFile(List<Staff> staffs) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Staff");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Staff ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Role");
            headerRow.createCell(3).setCellValue("Gender");
            headerRow.createCell(4).setCellValue("Age");
            headerRow.createCell(5).setCellValue("Phone Number");
            headerRow.createCell(6).setCellValue("Email");

            int rowNum = 1;
            for (Staff staff : staffs) {
                Row row = sheet.createRow(rowNum++);
                String formattedRole = staff.getRole().toString().charAt(0)
                        + staff.getRole().toString().substring(1).toLowerCase();
                row.createCell(0).setCellValue(staff.getStaffID());
                row.createCell(1).setCellValue(staff.getName());
                row.createCell(2).setCellValue(formattedRole);
                row.createCell(3).setCellValue(staff.getGender());
                row.createCell(4).setCellValue(staff.getAge());
                row.createCell(5).setCellValue(staff.getPhoneNumber());
                row.createCell(6).setCellValue(staff.getEmail());
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Saves the updated user account list back to the User file.
     * 
     * @param userAccs The list of updated user accounts.
     * @throws IOException If there is an issue writing to the file.
     */
    private static void newUserAccFile(List<UserAccount> userAccs) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("User");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Hospital ID");
            headerRow.createCell(1).setCellValue("Password");

            int rowNum = 1;
            for (UserAccount userAcc : userAccs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(userAcc.getHospitalID());
                row.createCell(1).setCellValue(userAcc.getPassword());
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/User.xlsx")) {
                workbook.write(fos);
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
                throw e; // Re-throw to notify the caller
            }
        }
    }

    public static int findStaff(String staffID) {
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell staffIDCell = row.getCell(0);
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }
                if (staffIDCell.getStringCellValue().equals(staffID)) {
                    return 1;
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while finding staff: " + e.getMessage());
        }
        System.out.println("Staff not found. Please try again!\n");
        return 0; //not found
    }
}
