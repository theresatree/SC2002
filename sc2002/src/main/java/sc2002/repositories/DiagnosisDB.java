package sc2002.repositories;

import sc2002.models.Diagnosis;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to diagnosis data stored in an Excel file.
 */
public class DiagnosisDB {
    private static final String FILE_NAME = "Patient_Diagnoses.xlsx";

    /**
     * Retrieves the diagnosis records for a specific patient from the Excel file.
     * 
     * @param hospitalID the hospital ID of the patient
     * @return List of Diagnosis objects
     * @throws IOException if an I/O error occurs
     */
    public static List<Diagnosis> getDiagnosis(String hospitalID) throws IOException {
        List<Diagnosis> diagnoses = new ArrayList<>();
        try (InputStream is = DiagnosisDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;  // Skip header row

                Cell idCell = row.getCell(0);
                if (idCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                    String patientID = idCell.getStringCellValue();
                    int diagnosisCode = (int) row.getCell(1).getNumericCellValue();
                    String doctorID = row.getCell(2).getStringCellValue();
                    String diagnosisDescription = row.getCell(3).getStringCellValue();
                    String treatment = row.getCell(4).getStringCellValue();
                    String notes = row.getCell(5).getStringCellValue();

                    Diagnosis diagnosis = new Diagnosis(patientID, diagnosisCode, doctorID, diagnosisDescription, treatment, notes);
                    diagnoses.add(diagnosis);
                }
            }
        }
        return diagnoses;
    }

    /**
     * Adds a new diagnosis for a patient to the Excel file.
     * 
     * @param patientID the patient ID
     * @param doctorID the doctor ID
     * @param diagnosis the diagnosis description
     * @param treatment the treatment description
     * @param notes additional notes
     */
    public static void addDiagnosis(String patientID, String doctorID, String diagnosis, String treatment, String notes) {
        try (InputStream is = DiagnosisDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowCount);

            int newDiagnosisID = getNextDiagnosisID(sheet);
            row.createCell(0).setCellValue(patientID);
            row.createCell(1).setCellValue(newDiagnosisID);
            row.createCell(2).setCellValue(doctorID);
            row.createCell(3).setCellValue(diagnosis);
            row.createCell(4).setCellValue(treatment);
            row.createCell(5).setCellValue(notes);

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing diagnosis in the Excel file based on diagnosis ID.
     * 
     * @param diagnosisID the diagnosis ID to update
     * @param diagnosis new diagnosis description
     * @param treatment new treatment description
     * @param notes new additional notes
     * @throws IOException if an I/O error occurs
     */
    public static void updateDiagnosis(int diagnosisID, String diagnosis, String treatment, String notes) throws IOException {
        try (InputStream is = DiagnosisDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell diagnosisIDCell = row.getCell(1);
                if (diagnosisIDCell != null && diagnosisIDCell.getCellType() == CellType.NUMERIC && (int) diagnosisIDCell.getNumericCellValue() == diagnosisID) {
                    if (!diagnosis.isEmpty()) row.getCell(3).setCellValue(diagnosis);
                    if (!treatment.isEmpty()) row.getCell(4).setCellValue(treatment);
                    if (!notes.isEmpty()) row.getCell(5).setCellValue(notes);
                    break;
                }
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Retrieves the next available diagnosis ID.
     * 
     * @param sheet the Excel sheet
     * @return the next diagnosis ID as an integer
     */
    private static int getNextDiagnosisID(Sheet sheet) {
        int maxID = 0;
        for (Row row : sheet) {
            Cell idCell = row.getCell(1);
            if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
                int currentID = (int) idCell.getNumericCellValue();
                if (currentID > maxID) {
                    maxID = currentID;
                }
            }
        }
        return maxID + 1;
    }
}