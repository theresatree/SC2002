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

public class MedicationInventoryDB {
    private static final String FILE_NAME = "Medicine_List.xlsx"; //fixed file location for Patient_List.xlsx

    //////////////////////////////////////// Get Treatment Detail ////////////////////////////////////////
    public static List<MedicationInventory> getMedicationInventory() throws IOException {
        List<MedicationInventory> medicationInventory = new ArrayList<>(); // List to hold multiple diagnoses
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {

                    Cell medicineNameCell = row.getCell(0);
                    Cell initialStockCell = row.getCell(1);
                    Cell lowStockLevelAlertCell = row.getCell(2);

                    if (row.getRowNum() == 0) continue; // Skip header row
                    
                    if (medicineNameCell != null) {
                        Medicine medicineName = Medicine.valueOf(medicineNameCell.getStringCellValue().toUpperCase());
                        int initialStock = (int) initialStockCell.getNumericCellValue();
                        int lowStockLevelAlert = (int) lowStockLevelAlertCell.getNumericCellValue();
                        MedicationInventory medicationInventory1 = new MedicationInventory(medicineName, initialStock, lowStockLevelAlert);
                        medicationInventory.add(medicationInventory1);
                    }
                }
            }
        }
        return medicationInventory;
    }
}
