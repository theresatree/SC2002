package sc2002;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DiagnosisDB {
    private static final String FILE_NAME = "Patient_Diagnoses.xlsx"; //fixed file location for Patient_List.xlsx

    //////////////////////////////////////// Get Diagnosis Detail ////////////////////////////////////////
    public static List<Diagnosis> getDiagnosis(String hospitalID) throws IOException {
        List<Diagnosis> diagnoses = new ArrayList<>(); // List to hold multiple diagnoses
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = DiagnosisDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
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
                    Cell diagnosisCell = row.getCell(3);
                    Cell treatmentCell = row.getCell(4);
                    Cell notesCell = row.getCell(5);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    if (idCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                        String patientID = idCell.getStringCellValue();
                        int diagnosisCode = (int) diagnosisCodeCell.getNumericCellValue();
                        String doctorID = doctorIDCell.getStringCellValue();
                        String patientDiagnosis = diagnosisCell.getStringCellValue();
                        String treatment = treatmentCell.getStringCellValue();
                        String notes = notesCell.getStringCellValue();

                        Diagnosis diagnosis = new Diagnosis(patientID, diagnosisCode, doctorID, patientDiagnosis, treatment, notes);
                        diagnoses.add(diagnosis);
                    }
                }
            }
        }
        return diagnoses;
    }

     //////////////////////////////////////// FOR DOCTORS TO ADD DIAGNOSIS ///////////////////////////////////////
    public static void addDiagnosis(String patientID, String doctorID, String diagnosis, String treatment, String notes){
            try (InputStream is = DiagnosisDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            // Find the first empty row
            int rowCount = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowCount); // Create a new row at the end of the sheet

            int newDiagnosisID = getNextDiagosisID(sheet);

            // Set the values in the appropriate columns
            row.createCell(0).setCellValue(patientID);
            row.createCell(1).setCellValue(newDiagnosisID);
            row.createCell(2).setCellValue(doctorID); 
            row.createCell(3).setCellValue(diagnosis); 
            row.createCell(4).setCellValue(treatment); 
            row.createCell(5).setCellValue(notes);
            // Write the changes back to the Excel file

        try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
        }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }
    //////////////////////////////////////// FOR DOCTORS TO UPDATE DIAGNOSIS ///////////////////////////////////////
    public static void updateDiagnosis(int diagnosisID, String diagnosis, String treatment, String notes) throws IOException{
        try (InputStream is = DiagnosisDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Workbook workbook = new XSSFWorkbook(is)) {
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

        boolean found = false;

        for (Row row : sheet) {
            Cell diagnosisIDCell = row.getCell(1);
            if (diagnosisIDCell!=null && diagnosisIDCell.getCellType() == CellType.NUMERIC){
                int currentDiagnosisID = (int) diagnosisIDCell.getNumericCellValue();
                if (currentDiagnosisID == diagnosisID) {
                    // Update existing row
                    if (!diagnosis.isEmpty()) { // if not "", update
                        row.getCell(3).setCellValue(diagnosis);  // Update diagnosis description if not empty
                    }
                    if (!treatment.isEmpty()) { // if not "", update
                        row.getCell(4).setCellValue(treatment);  // Update treatment description if not empty
                    }
                    if (!notes.isEmpty()) { // if not "", update
                        row.getCell(5).setCellValue(notes);       // Update additional notes if not empty
                    }
                    found = true; // Mark as found
                    break; // Exit the loop after updating
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
        }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriateå
        }
    }

     //////////////////////////////////////// GET THE NEXT DIAGNOSIS ID ///////////////////////////////////////
        private static int getNextDiagosisID(Sheet sheet) {
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
        return maxID + 1; // Increment the max ID for the new DiagnosisID
    }
}
