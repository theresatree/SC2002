package sc2002.repositories;

import sc2002.models.ContactInformation;
import sc2002.models.MedicalRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database operations related to patient information, including retrieval, updates, and creation of new patients.
 */
public class PatientDB {
    private static final String FILE_NAME = "Patient_List.csv"; // CSV file for patient information

    /**
     * Retrieves the basic details of a patient based on their hospital ID.
     *
     * @param hospitalID The hospital ID of the patient.
     * @return MedicalRecord object containing the patient's details, or null if not found.
     * @throws IOException If there is an issue accessing the file.
     */
    public static MedicalRecord getPatientDetails(String hospitalID) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                if (fields[0].equals(hospitalID)) {
                    String id = fields[0];
                    String name = fields[1];
                    String dateOfBirth = fields[2];
                    String gender = fields[3];
                    String bloodType = fields[4];
                    return new MedicalRecord(id, name, dateOfBirth, gender, bloodType);
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
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                if (fields[0].equals(hospitalID)) {
                    int phoneNumber = Integer.parseInt(fields[5]);
                    String emailAddress = fields[6];
                    return new ContactInformation(phoneNumber, emailAddress);
                }
            }
        }
        return null;
    }

    /**
     * Updates the contact information of a patient.
     *
     * @param hospitalID  The hospital ID of the patient.
     * @param email       The new email address of the patient.
     * @param phoneNumber The new phone number of the patient.
     * @throws IOException If there is an issue accessing the file.
     */
    public static void updateContactInformation(String hospitalID, String email, int phoneNumber) {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp_" + FILE_NAME);
    
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            boolean isHeader = true;
    
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    bw.write(line);
                    bw.newLine();
                    continue; // Write header as is
                }
    
                String[] fields = line.split(",");
                if (fields[0].equals(hospitalID)) {
                    fields[5] = String.valueOf(phoneNumber); // Update phone number
                    fields[6] = email; // Update email address
                }
    
                bw.write(String.join(",", fields));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("An error occurred while updating contact information: " + e.getMessage());
            return; // Exit the method after logging the error
        }
    
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.err.println("Failed to update contact information: Could not replace the original file.");
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
        List<String[]> patientData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                patientData.add(line.split(","));
            }
        }

        String nextPatientID = getNextPatientID(patientData);
        if (gender.equalsIgnoreCase("M")) gender = "Male";
        else if (gender.equalsIgnoreCase("F")) gender = "Female";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(String.join(",", nextPatientID, patientName, patientDOB, gender, bloodType, String.valueOf(phoneNumber), email));
            bw.newLine();
        }
        UserDB.createNewPatient(nextPatientID);

        return nextPatientID;
    }

    /**
     * Generates the next available patient ID by incrementing the maximum existing patient ID.
     *
     * @param patientData The current list of patient data.
     * @return The next available patient ID in the format "P" followed by a number.
     */
    private static String getNextPatientID(List<String[]> patientData) {
        int maxId = 0;
        
        // Skip header row (assuming the first row is a header)
        boolean isHeader = true;

        for (String[] fields : patientData) {
            // Skip header row
            if (isHeader) {
                isHeader = false;
                continue;
            }

            try {
                String idStr = fields[0];
                if (idStr.startsWith("P")) {
                    int idNumber = Integer.parseInt(idStr.substring(1));
                    maxId = Math.max(maxId, idNumber);
                }
            } catch (NumberFormatException e) {
                System.out.println("Skipping invalid ID format: " + fields[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Skipping invalid row: " + String.join(",", fields));
            }
        }
        
        return "P" + (maxId + 1);
    }
}