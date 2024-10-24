package sc2002;

import java.io.FileOutputStream;
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

    //////////////////////////////////////// Get Medication Detail ////////////////////////////////////////
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

                    if (row.getRowNum() == 0) {
                        continue; // Skip header row
                    }
                    if (medicineNameCell != null) {
                        String medicineName = medicineNameCell.getStringCellValue().toUpperCase();
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
    //////////////////////////////////////// Add Medication ////////////////////////////////////////
    public static void addMedication(MedicationInventory newMedication) throws IOException {
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            newRow.createCell(0).setCellValue(newMedication.getMedicine());
            newRow.createCell(1).setCellValue(newMedication.getStockLevel());
            newRow.createCell(2).setCellValue(newMedication.getLowStockLevelAlert());

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while adding the new medication: " + e.getMessage());
        }
    }

    //////////////////////////////// Remove Medication /////////////////////////////////
    public static void removeMedication(String medicineName) throws IOException {
        List<MedicationInventory> medicationInventory = getMedicationInventory();
        medicationInventory.removeIf(medicine -> medicine.getMedicine().equals(medicineName));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Medication");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Medicine Name");
            headerRow.createCell(1).setCellValue("Initial Stock");
            headerRow.createCell(2).setCellValue("Low Stock Level Alert");

            // Write existing medication to the new sheet
            int rowNum = 1;
            for (MedicationInventory medication : medicationInventory) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(medication.getMedicine());
                row.createCell(1).setCellValue(medication.getStockLevel());
                row.createCell(2).setCellValue(medication.getLowStockLevelAlert());
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
                throw e; // Re-throw to notify the caller
            }
        }
    }

    //////////////////////////////// Update Stock Level /////////////////////////////////
    public static void updateStockLevel(String medicineName, int restockAmount) throws IOException {
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell medicineCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row

                if (medicineCell.getStringCellValue().equals(medicineName)) {
                    Cell stockLevelCell = row.getCell(1);
                    int currentStock = (int) stockLevelCell.getNumericCellValue();
                    stockLevelCell.setCellValue(currentStock + restockAmount);
                    break;
                }
            }

            // Write the updated workbook back to the file
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the stock level: " + e.getMessage());
        }
    }

    //////////////////////////////// Change Low Stock Level Alert /////////////////////////////////
    public static void updateLowAlert(String medicineName, int updatedLowLevelAlert) throws IOException {
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell medicineCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row

                if (medicineCell.getStringCellValue().equals(medicineName)) {
                    Cell lowLevelAlert = row.getCell(2);
                    lowLevelAlert.setCellValue(updatedLowLevelAlert);
                    break;
                }
            }

            // Write the updated workbook back to the file
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the low stock level alert: " + e.getMessage());
        }
    }

    public static int findMedicine(String medicine){
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell medicineCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row

                if (medicineCell.getStringCellValue().equals(medicine)) {
                    return 1;
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while finding medicine: " + e.getMessage());
        }
        System.out.println("Medicine not found. Please try again!\n");
        return 0; //not found
    }
}
