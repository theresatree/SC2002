package sc2002;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class UserDB {
    private static final String FILE_NAME = "User.xlsx"; //fixed file location for User.xlsx

/////////////////////////////////////// Vadlidate Login when user enters ID + Password ///////////////////////////////////////////
    public static boolean validateLogin(String hospitalID, String password) throws IOException {
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = UserDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
                boolean isValid = false;

                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell passwordCell = row.getCell(1);
                    
                    if (row.getRowNum() == 0) continue;                    // Skip header row

                    if (idCell != null && passwordCell != null) {
                        String id = idCell.getStringCellValue();
                        String pass = passwordCell.getStringCellValue();

                        if (id.equals(hospitalID) && pass.equals(password)) {
                            isValid = true; // Login is valid
                            if (pass.equals("password")) {
                                changePassword(id); // Prompt to change password
                            }
                            break;
                        }
                    }
                }
                return isValid; // Return the validation result
            }
        }
    }

/////////////////////////////////////// Function for changing password if password == "password" ///////////////////////////////////////////
    private static void changePassword(String hospitalID) {
        Scanner scanner = new Scanner(System.in);
        String newPassword = "";

        System.out.print("\nFirst time login Detected! ");
        while (true) {
            System.out.println("Please enter a new password: ");
            newPassword = scanner.nextLine().trim();

            if (!newPassword.equals("password") && !newPassword.isEmpty()) {
                // Update the password in the database (Excel file)
                updatePasswordInFile(hospitalID, newPassword);
                System.out.println("Password changed successfully.");
                break;
            } else {
                System.out.println("New password cannot be 'password' or empty. Please try again.");
            }
        }
    }

/////////////////////////////////////// Update the "User.xlsx" after changing password ///////////////////////////////////////////

    private static void updatePasswordInFile(String hospitalID, String newPassword) {
        try (InputStream is = UserDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell idCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row

                if (idCell.getStringCellValue().equals(hospitalID)) {
                    // Update the password cell
                    Cell passwordCell = row.getCell(1);
                    passwordCell.setCellValue(newPassword);
                    break;
                }
            }

            // Write the updated workbook back to the file
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the password: " + e.getMessage());
        }
    }

   /////////////////////////////////// Get name by hospital ID ///////////////////////////////////////////
   public static String getNameByHospitalID(String hospitalID, Role role){
    String filePath = "";
    if (role == Role.PATIENT) {
        filePath = "Patient_List.xlsx";
    } else {
        filePath = "Staff_List.xlsx";  // for DOCTOR, PHARMACIST, ADMINISTRATOR
    }
        try {
            // Get the file
            InputStream is = UserDB.class.getClassLoader().getResourceAsStream(filePath);
            if (is == null) {
                System.out.println("Error: Could not find file " + filePath);
                return null;
            }
            
            // Open the workbook
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            
            // Look for matching ID
            for (Row row : sheet) {
                // Skip header row
                if (row.getRowNum() == 0) continue;
                
                // Get ID and name cells
                Cell idCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                
                // Check if cells exist and ID matches
                if (idCell != null && nameCell != null) {
                    String id = idCell.getStringCellValue();
                    if (id.equals(hospitalID)) {
                        String name = nameCell.getStringCellValue();
                        
                        // Clean up
                        workbook.close();
                        is.close();
                        
                        return name;
                    }
                }
            }
            
            // Clean up if no match found
            workbook.close();
            is.close();
            
        } catch (Exception e) {
            System.out.println("Error: Could not read from file " + filePath);
            System.out.println("Error details: " + e.getMessage());
        }
        
        return null;
    }

}
