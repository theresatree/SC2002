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
                    Cell descriptionCell = row.getCell(3);
                    Cell dateCell = row.getCell(4);
                    Cell notesCell = row.getCell(5);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    if (idCell != null && idCell.getStringCellValue().equals(hospitalID)) {
                        String patientID = idCell.getStringCellValue();
                        int diagnosisCode = (int) diagnosisCodeCell.getNumericCellValue();
                        String doctorID = doctorIDCell.getStringCellValue();
                        String description = descriptionCell.getStringCellValue();
                        String date = dateCell.getStringCellValue();
                        String notes = notesCell.getStringCellValue();

                        Diagnosis diagnosis = new Diagnosis(patientID, diagnosisCode, doctorID, description, date, notes);
                        diagnoses.add(diagnosis);
                    }
                }
            }
        }
        return diagnoses;
    }
}
