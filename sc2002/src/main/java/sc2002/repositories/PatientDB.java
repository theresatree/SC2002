package sc2002.repositories;

import sc2002.models.ContactInformation;
import sc2002.models.MedicalRecord;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Provides database operations related to patient information, including retrieval, updates, and creation of new patients.
 */
public class PatientDB {
    private static final String FILE_NAME = "Patient_List.xlsx"; // Fixed file location for Patient_List.xlsx

    /**
     * Retrieves the basic details of a patient based on their hospital ID.
     *
     * @param hospitalID The hospital ID of the patient.
     * @return MedicalRecord object containing the patient's details, or null if not found.
     * @throws IOException If there is an issue accessing the file.
     */
    public static MedicalRecord getPatientDetails(String hospitalID) throws IOException {
        try (InputStream is = PatientDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);
                    Cell dateOfBirthCell = row.getCell(2);
                    Cell genderCell = row.getCell(3);
                    Cell bloodTypeCell = row.getCell(4);
                    if (row.getRowNum() == 0) continue; // Skip header row
                    if (idCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                        String id = idCell.getStringCellValue();
                        String name = nameCell.getStringCellValue();
                        String dateOfBirth = dateOfBirthCell.getStringCellValue();
                        String gender = genderCell.getStringCellValue();
                        String bloodType = bloodTypeCell.getStringCellValue();
                        return new MedicalRecord(id, name, dateOfBirth, gender, bloodType);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the contact information of a patient based on their hospital ID.
     *
     * @param hospitalID The hospital ID of the patient.
     * @return ContactInformation object containing the patient's contact details, or null if not found.
     * @throws IOException If there is an issue accessing the file.
     */
    public static ContactInformation getPatientContactDetails(String hospitalID) throws IOException {
        try (InputStream is = PatientDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell phoneNumberCell = row.getCell(5);
                    Cell emailAddressCell = row.getCell(6);
                    if (row.getRowNum() == 0) continue; // Skip header row
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

    /**
     * Updates the contact information of a patient.
     *
     * @param hospitalID The hospital ID of the patient.
     * @param email      The new email address of the patient.
     * @param phoneNumber The new phone number of the patient.
     */
    public static void updateContactInformation(String hospitalID, String email, int phoneNumber) {
        try (InputStream is = PatientDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell idCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row
                if (idCell.getStringCellValue().equals(hospitalID)) {
                    Cell phoneNumberCell = row.getCell(5);
                    phoneNumberCell.setCellValue(phoneNumber);
                    Cell emailCell = row.getCell(6);
                    emailCell.setCellValue(email);
                    break;
                }
            }
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating contact information: " + e.getMessage());
        }
    }

    /**
     * Creates a new patient entry in the database.
     *
     * @param patientName The name of the patient.
     * @param patientDOB  The date of birth of the patient in "YYYY-MM-DD" format.
     * @param gender      The gender of the patient ("M" or "F").
     * @param bloodType   The blood type of the patient.
     * @param phoneNumber The phone number of the patient.
     * @param email       The email address of the patient.
     * @return The newly assigned patient ID.
     * @throws IOException If there is an issue accessing the file.
     */
    public static String createNewPatient(String patientName, String patientDOB, String gender, String bloodType, int phoneNumber, String email) throws IOException {
        String nextPatientID = "";
        try (InputStream is = PatientDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowCount);
            nextPatientID = getNextPatientID(sheet);
            if (gender.equalsIgnoreCase("M")) gender = "Male";
            else if (gender.equalsIgnoreCase("F")) gender = "Female";
            row.createCell(0).setCellValue(nextPatientID);
            row.createCell(1).setCellValue(patientName);
            row.createCell(2).setCellValue(patientDOB);
            row.createCell(3).setCellValue(gender);
            row.createCell(4).setCellValue(bloodType);
            row.createCell(5).setCellValue(phoneNumber);
            row.createCell(6).setCellValue(email);
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
                UserDB.createNewPatient(nextPatientID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextPatientID;
    }

    /**
     * Generates the next available patient ID by incrementing the maximum existing patient ID.
     *
     * @param sheet The Excel sheet containing patient information.
     * @return The next available patient ID in the format "P" followed by a number.
     */
    private static String getNextPatientID(Sheet sheet) {
        int maxId = 0;
        for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getCell(0) != null) {
                String idStr = row.getCell(0).getStringCellValue();
                if (idStr.startsWith("P")) {
                    int idNumber = Integer.parseInt(idStr.substring(1));
                    maxId = Math.max(maxId, idNumber);
                }
            }
        }
        return "P" + (maxId + 1);
    }
}