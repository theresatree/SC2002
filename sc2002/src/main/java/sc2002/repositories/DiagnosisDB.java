package sc2002.repositories;

import sc2002.models.Diagnosis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to diagnosis data stored in a CSV file.
 */
public class DiagnosisDB {
    private static final String FILE_NAME = "Patient_Diagnoses.csv";

    /**
     * Retrieves the diagnosis records for a specific patient from the CSV file.
     * 
     * @param hospitalID the hospital ID of the patient
     * @return List of Diagnosis objects
     * @throws IOException if an I/O error occurs
     */
    public static List<Diagnosis> getDiagnosis(String hospitalID) throws IOException {
        List<Diagnosis> diagnoses = new ArrayList<>();
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
                    String patientID = fields[0];
                    int diagnosisCode = Integer.parseInt(fields[1]);
                    String doctorID = fields[2];
                    String diagnosisDescription = fields[3];
                    String treatment = fields[4];
                    String notes = fields[5];

                    Diagnosis diagnosis = new Diagnosis(patientID, diagnosisCode, doctorID, diagnosisDescription, treatment, notes);
                    diagnoses.add(diagnosis);
                }
            }
        }
        return diagnoses;
    }

    /**
     * Adds a new diagnosis for a patient to the CSV file.
     * 
     * @param patientID the patient ID
     * @param doctorID the doctor ID
     * @param diagnosis the diagnosis description
     * @param treatment the treatment description
     * @param notes additional notes
     */
    public static void addDiagnosis(String patientID, String doctorID, String diagnosis, String treatment, String notes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            int newDiagnosisID = getNextDiagnosisID();
            bw.write(String.join(",", patientID, String.valueOf(newDiagnosisID), doctorID, diagnosis, treatment, notes));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing diagnosis in the CSV file based on diagnosis ID.
     * 
     * @param diagnosisID the diagnosis ID to update
     * @param diagnosis new diagnosis description
     * @param treatment new treatment description
     * @param notes new additional notes
     * @throws IOException if an I/O error occurs
     */
    public static void updateDiagnosis(int diagnosisID, String diagnosis, String treatment, String notes) throws IOException {
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
                int currentDiagnosisID = Integer.parseInt(fields[1]);

                if (currentDiagnosisID == diagnosisID) {
                    if (!diagnosis.isEmpty()) fields[3] = diagnosis;
                    if (!treatment.isEmpty()) fields[4] = treatment;
                    if (!notes.isEmpty()) fields[5] = notes;
                }

                bw.write(String.join(",", fields));
                bw.newLine();
            }
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException("Could not update file.");
        }
    }

    /**
     * Retrieves the next available diagnosis ID.
     * 
     * @return the next diagnosis ID as an integer
     */
    private static int getNextDiagnosisID() throws IOException {
        int maxID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                int currentID = Integer.parseInt(fields[1]);
                if (currentID > maxID) {
                    maxID = currentID;
                }
            }
        }
        return maxID + 1;
    }
}