package sc2002;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MedicationDB {
    private static final String FILE_NAME = "Patient_Medication.xlsx"; //fixed file location for Patient_List.xlsx

    //////////////////////////////////////// Get Medication Detail ////////////////////////////////////////
    public static List<Medication> getMedication(String hospitalID) throws IOException {
        List<Medication> medications = new ArrayList<>(); // List to hold multiple diagnoses
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = MedicationDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell diagnosisCodeCell = row.getCell(1);
                    Cell doctorIDCell = row.getCell(2);
                    Cell medicationCell = row.getCell(3);
                    Cell frequencyCell = row.getCell(4);
                    Cell dateCell = row.getCell(5);
                    Cell notesCell = row.getCell(6);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    if (idCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                        String patientID = idCell.getStringCellValue();
                        int diagnosisCode = (int) diagnosisCodeCell.getNumericCellValue();
                        String doctorID = doctorIDCell.getStringCellValue();
                        String medicationGiven = medicationCell.getStringCellValue();
                        String frequency = frequencyCell.getStringCellValue();
                        String date = dateCell.getStringCellValue();
                        String notes = notesCell.getStringCellValue();

                        Medication medication = new Medication(patientID, diagnosisCode, doctorID, medicationGiven, frequency, date, notes);
                        medications.add(medication);
                    }
                }
            }
        }
        return medications;
    }
}
