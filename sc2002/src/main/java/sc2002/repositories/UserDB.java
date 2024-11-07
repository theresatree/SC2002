package sc2002.repositories;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sc2002.enums.Role;

/**
 * Represents the user database for handling user login and password management.
 */
public class UserDB {
    private static final String FILE_NAME = "User.xlsx"; // Fixed file location for User.xlsx

    /**
     * Validates the login when the user enters ID and password.
     * 
     * @param hospitalID The user's hospital ID.
     * @param password The user's password.
     * @return true if the login is valid; otherwise, false.
     * @throws IOException if there is an error accessing the file.
     */
    public static boolean validateLogin(String hospitalID, String password) throws IOException {
        try (InputStream is = UserDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                boolean isValid = false;

                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell passwordCell = row.getCell(1);
                    if (row.getRowNum() == 0) continue; // Skip header row

                    if (idCell != null && passwordCell != null) {
                        String id = idCell.getStringCellValue();
                        String pass = passwordCell.getStringCellValue();

                        if (id.equals(hospitalID) && pass.equals(password)) {
                            isValid = true;
                            if (pass.equals("password")) {
                                changePassword(id);
                            }
                            break;
                        }
                    }
                }
                return isValid;
            }
        }
    }

    /**
     * Prompts the user to change their password if it is set to "password".
     * 
     * @param hospitalID The user's hospital ID.
     */
    private static void changePassword(String hospitalID) {
        Scanner scanner = new Scanner(System.in);
        String newPassword = "";

        System.out.print("\nFirst time login detected! ");
        while (true) {
            System.out.println("Please enter a new password: ");
            newPassword = scanner.nextLine().trim();

            if (!newPassword.equals("password") && !newPassword.isEmpty()) {
                updatePasswordInFile(hospitalID, newPassword);
                System.out.println("Password changed successfully.");
                break;
            } else {
                System.out.println("New password cannot be 'password' or empty. Please try again.");
            }
        }
    }

    /**
     * Updates the password in User.xlsx after a change.
     * 
     * @param hospitalID The user's hospital ID.
     * @param newPassword The new password.
     */
    private static void updatePasswordInFile(String hospitalID, String newPassword) {
        try (InputStream is = UserDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell idCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row

                if (idCell.getStringCellValue().equals(hospitalID)) {
                    Cell passwordCell = row.getCell(1);
                    passwordCell.setCellValue(newPassword);
                    break;
                }
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the password: " + e.getMessage());
        }
    }

    /**
     * Retrieves the name associated with a hospital ID from the appropriate file.
     * 
     * @param hospitalID The user's hospital ID.
     * @param role The role of the user.
     * @return The name associated with the ID, or null if not found.
     */
    public static String getNameByHospitalID(String hospitalID, Role role) {
        String filePath = role == Role.PATIENT ? "Patient_List.xlsx" : "Staff_List.xlsx";

        try (InputStream is = UserDB.class.getClassLoader().getResourceAsStream(filePath);
             Workbook workbook = new XSSFWorkbook(is)) {
            if (is == null) {
                System.out.println("Error: Could not find file " + filePath);
                return null;
            }
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell idCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                if (idCell != null && nameCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                    return nameCell.getStringCellValue();
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read from file " + filePath);
            System.out.println("Error details: " + e.getMessage());
        }
        return null;
    }

    /**
     * Creates a new patient entry in User.xlsx with a default password.
     * 
     * @param patientID The new patient's ID.
     */
    public static void createNewPatient(String patientID) {
        try (InputStream is = UserDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowCount);

            row.createCell(0).setCellValue(patientID);
            row.createCell(1).setCellValue("password");

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}