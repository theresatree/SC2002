package sc2002.repositories;

import sc2002.models.MedicationInventory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The MedicationInventoryDB class provides methods for managing medication inventory,
 * including retrieving, adding, removing, updating stock levels, and setting low stock alerts.
 */
public class MedicationInventoryDB {

    private static final String FILE_NAME = "Medicine_List.xlsx"; // Fixed file location for Medicine_List.xlsx

    /**
     * Retrieves the list of all medications in the inventory.
     *
     * @return a list of MedicationInventory objects
     * @throws IOException if an error occurs while reading the file
     */
    public static List<MedicationInventory> getMedicationInventory() throws IOException {
        List<MedicationInventory> medicationInventory = new ArrayList<>();
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Skip header row

                    Cell medicineNameCell = row.getCell(0);
                    Cell initialStockCell = row.getCell(1);
                    Cell lowStockLevelAlertCell = row.getCell(2);

                    if (medicineNameCell != null) {
                        String medicineName = medicineNameCell.getStringCellValue().toUpperCase();
                        int initialStock = (int) initialStockCell.getNumericCellValue();
                        int lowStockLevelAlert = (int) lowStockLevelAlertCell.getNumericCellValue();
                        medicationInventory.add(new MedicationInventory(medicineName, initialStock, lowStockLevelAlert));
                    }
                }
            }
        }
        return medicationInventory;
    }

    /**
     * Adds a new medication to the inventory.
     *
     * @param newMedication the MedicationInventory object to add
     * @throws IOException if an error occurs while updating the file
     */
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

    /**
     * Removes a medication from the inventory based on the medicine name.
     *
     * @param medicineName the name of the medicine to remove
     * @throws IOException if an error occurs while updating the file
     */
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

    /**
     * Updates the stock level of a specified medication.
     *
     * @param medicineName the name of the medication
     * @param restockAmount the amount to add to the current stock
     * @throws IOException if an error occurs while updating the file
     */
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

    /**
     * Updates the low stock alert level of a specified medication.
     *
     * @param medicineName the name of the medication
     * @param updatedLowLevelAlert the new low stock level alert
     * @throws IOException if an error occurs while updating the file
     */
    public static void updateLowAlert(String medicineName, int updatedLowLevelAlert) throws IOException {
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell medicineCell = row.getCell(0);
                if (medicineCell.getStringCellValue().equals(medicineName)) {
                    Cell lowLevelAlert = row.getCell(2);
                    lowLevelAlert.setCellValue(updatedLowLevelAlert);
                    break;
                }
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Finds if a specific medication exists in the inventory.
     *
     * @param medicine the name of the medicine to find
     * @return 1 if the medicine is found, 0 otherwise
     */
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

    /**
     * Returns a list of medicines that have low stock levels.
     *
     * @return a comma-separated string of medicines with low stock or "NO" if none
     * @throws IOException if an error occurs while reading the file
     */
    public static String lowStockLevelAlert() throws IOException {
        List<String> lowStockMedicines = new ArrayList<>();
        
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); 
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; 
                
                Cell medicineNameCell = row.getCell(0);
                Cell initialStockCell = row.getCell(1);
                Cell lowStockLevelCell = row.getCell(2);
                
                if (medicineNameCell != null && initialStockCell != null && lowStockLevelCell != null) {
                    String medicineName = medicineNameCell.getStringCellValue();
                    int initialStock = (int) initialStockCell.getNumericCellValue();
                    int lowStockLevelAlert = (int) lowStockLevelCell.getNumericCellValue();
                    
                    if (initialStock <= lowStockLevelAlert) {
                        lowStockMedicines.add(medicineName);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while checking for low stock levels: " + e.getMessage());
            throw e;
        }
        return lowStockMedicines.isEmpty() ? "NO" : String.join(", ", lowStockMedicines);
    }
}