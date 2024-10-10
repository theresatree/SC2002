package sc2002;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class PatientDB {
    private static final String FILE_NAME = "Patient_List.xlsx"; //fixed file location for Patient_List.xlsx

//////////////////////////////////////// Get Details of the first 5 rows ////////////////////////////////////////
    public static MedicalRecord getPatientDetails(String hospitalID) throws IOException {
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = PatientDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);
                    Cell dateOfBirthCell = row.getCell(2);
                    Cell genderCell = row.getCell(3);
                    Cell bloodTypeCell = row.getCell(4);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    if (idCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                        String id = idCell.getStringCellValue();
                        String name = nameCell.getStringCellValue();
                        String dateOfBirth = dateOfBirthCell.getStringCellValue();
                        String gender = genderCell.getStringCellValue();
                        String bloodType = bloodTypeCell.getStringCellValue();

                        return new MedicalRecord(id,name,dateOfBirth,gender,bloodType);
                    }
                }
            }
        }
        return null;
    }

    //////////////////////////////////////// Get Contact Information of Patient////////////////////////////////////////
    public static ContactInformation getPatientContactDetails(String hospitalID) throws IOException {
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = PatientDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell phoneNumberCell = row.getCell(5);
                    Cell emailAddressCell = row.getCell(6);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    if (idCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                        int phoneNumber = (int) phoneNumberCell.getNumericCellValue();
                        String emailAddress = emailAddressCell.getStringCellValue();

                        return new ContactInformation(phoneNumber, emailAddress);
                    }
                }
            }
        }
        return null;
    }

    //////////////////////////////////////// Get Contact Information of Patient////////////////////////////////////////
    public static void updateContactInformation(String hospitalID, String email, int phoneNumber) {
        try (InputStream is = PatientDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell idCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row

                if (idCell.getStringCellValue().equals(hospitalID)) {
                    // Update the password cell
                    Cell phoneNumberCell = row.getCell(5);
                    phoneNumberCell.setCellValue(phoneNumber);
                    Cell emailCell = row.getCell(6);
                    emailCell.setCellValue(email);
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
}