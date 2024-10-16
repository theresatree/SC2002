package sc2002;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sc2002.StaffFiltering.StaffFilter;

/**
 * Represents the staff database
 */
public class StaffDB {

    /**
     * fixed file location for Staff_List.xlsx
     */
    private static final String FILE_NAME = "Staff_List.xlsx";

    /**
     *
     * @return @throws IOException
     */
    public static List<Staff> getStaff(StaffFilter selectedFilter) throws IOException {
        List<Staff> staffs = new ArrayList<>();
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell staffIDCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);
                    Cell roleCell = row.getCell(2);
                    Cell genderCell = row.getCell(3);
                    Cell ageCell = row.getCell(4);

                    if (row.getRowNum() == 0) {
                        continue; // Skip header row
                    }
                    if (staffIDCell != null) {
                        String staffID = staffIDCell.getStringCellValue();
                        String name = nameCell.getStringCellValue();
                        Role role = Role.valueOf(roleCell.getStringCellValue().toUpperCase());
                        String gender = genderCell.getStringCellValue();
                        int age = (int) ageCell.getNumericCellValue();
                        Staff staff = new Staff(staffID, name, gender, age, role);

                        if (selectedFilter == null || selectedFilter.filter(staff)) {
                            staffs.add(staff);
                        }
                    }
                }
            }
        }
        return staffs;
    }

    public static void addStaff(Staff newStaff) throws IOException {
        // Load the existing workbook
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            try (Workbook workbook = new XSSFWorkbook(is)) {

                // Get the existing sheet (assuming it's the first sheet)
                Sheet sheet = workbook.getSheetAt(0);
                // Add the new staff member
                Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
                String formattedRole = newStaff.getRole().toString().charAt(0)
                        + newStaff.getRole().toString().substring(1).toLowerCase();
                newRow.createCell(0).setCellValue(newStaff.getStaffID());
                newRow.createCell(1).setCellValue(newStaff.getName());
                newRow.createCell(2).setCellValue(formattedRole);
                newRow.createCell(3).setCellValue(newStaff.getGender());
                newRow.createCell(4).setCellValue(newStaff.getAge());

                String absolutePath = new File("src/main/resources/" + FILE_NAME).getAbsolutePath();
                try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
                    workbook.write(fos);
                    System.out.println("Successfully added new staff details to: " + absolutePath);
                } catch (IOException e) {
                    System.err.println("Error writing to the file: " + e.getMessage());
                    throw e; // Re-throw to notify the caller
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while processing the Excel file: " + e.getMessage());
            throw e; // Re-throw to notify the caller
        }

        // Update the User.xlsx and put the password as the default "password"
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream("User.xlsx"); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            // Add the new staff member
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            newRow.createCell(0).setCellValue(newStaff.getStaffID());
            newRow.createCell(1).setCellValue("password");

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/User.xlsx")) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    // Update an existing staff member
    public static void updateStaff(String staffID, Staff updatedStaff) throws IOException {
        List<Staff> staffs = getStaff(null);
        for (int i = 0; i < staffs.size(); i++) {
            if (staffs.get(i).getStaffID().equals(staffID)) {
                staffs.set(i, updatedStaff); // Update the staff member

                // Save back to the file
                if (newFile(staffs) == 1) {
                    System.out.println("Successfully updated the staff member");
                }
                return;
            }
        }
    }

    // // Remove a staff member
    // public static void removeStaff(String staffID) throws IOException {
    // List<Staff> staffs = getStaff(null);
    // staffs.removeIf(staff -> staff.getStaffID().equals(staffID)); // Remove the
    // staff member
    // // Save back to the file
    // newFile(staffs);
    // }
    // Save all staff members back to the file
    private static int newFile(List<Staff> staffs) throws IOException {
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

            // Write existing staff to the new sheet
            int rowNum = 1;
            for (Staff staff : staffs) {
                Row row = sheet.createRow(staffs.indexOf(staff) + 1);
                String formattedRole = staff.getRole().toString().charAt(0)
                        + staff.getRole().toString().substring(1).toLowerCase();
                row.createCell(0).setCellValue(staff.getStaffID());
                row.createCell(1).setCellValue(staff.getName());
                row.createCell(2).setCellValue(formattedRole);
                row.createCell(3).setCellValue(staff.getGender());
                row.createCell(4).setCellValue(staff.getAge());
            }

            String absolutePath = new File("src/main/resources/" + FILE_NAME).getAbsolutePath();
            try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
                workbook.write(fos);
                return 1;
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
                throw e; // Re-throw to notify the caller
            }
        }
    }
}
