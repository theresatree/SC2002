package sc2002.repositories;

import sc2002.models.MedicationInventory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The MedicationInventoryDB class provides methods for managing medication inventory,
 * including retrieving, adding, removing, updating stock levels, and setting low stock alerts.
 */
public class MedicationInventoryDB {

    private static final String FILE_NAME = "Medicine_List.csv"; // CSV file for medicine inventory

    /**
     * Retrieves the list of all medications in the inventory.
     *
     * @return a list of MedicationInventory objects
     * @throws IOException if an error occurs while reading the file
     */
    public static List<MedicationInventory> getMedicationInventory() throws IOException {
        List<MedicationInventory> medicationInventory = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }
                String[] fields = line.split(",");
                String medicineName = fields[0].toUpperCase();
                int initialStock = Integer.parseInt(fields[1]);
                int lowStockLevelAlert = Integer.parseInt(fields[2]);
                medicationInventory.add(new MedicationInventory(medicineName, initialStock, lowStockLevelAlert));
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(String.join(",", 
                newMedication.getMedicine(),
                String.valueOf(newMedication.getStockLevel()),
                String.valueOf(newMedication.getLowStockLevelAlert())));
            bw.newLine();
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
        medicationInventory.removeIf(medicine -> medicine.getMedicine().equalsIgnoreCase(medicineName));

        writeInventoryToFile(medicationInventory);
    }

    /**
     * Updates the stock level of a specified medication.
     *
     * @param medicineName the name of the medication
     * @param restockAmount the amount to add to the current stock
     * @throws IOException if an error occurs while updating the file
     */
    public static void updateStockLevel(String medicineName, int restockAmount) throws IOException {
        List<MedicationInventory> medicationInventory = getMedicationInventory();

        for (MedicationInventory medication : medicationInventory) {
            if (medication.getMedicine().equalsIgnoreCase(medicineName)) {
                // Directly update the stock level
                int currentStock = medication.getStockLevel();
                medicationInventory.remove(medication);
                medicationInventory.add(new MedicationInventory(medication.getMedicine(), currentStock + restockAmount, medication.getLowStockLevelAlert()));
                break;
            }
        }

        writeInventoryToFile(medicationInventory);
    }

    /**
     * Updates the low stock alert level of a specified medication.
     *
     * @param medicineName the name of the medication
     * @param updatedLowLevelAlert the new low stock level alert
     * @throws IOException if an error occurs while updating the file
     */
    public static void updateLowAlert(String medicineName, int updatedLowLevelAlert) throws IOException {
        List<MedicationInventory> medicationInventory = getMedicationInventory();

        for (MedicationInventory medication : medicationInventory) {
            if (medication.getMedicine().equalsIgnoreCase(medicineName)) {
                // Directly update the low stock alert level
                int currentStock = medication.getStockLevel();
                medicationInventory.remove(medication);
                medicationInventory.add(new MedicationInventory(medication.getMedicine(), currentStock, updatedLowLevelAlert));
                break;
            }
        }

        writeInventoryToFile(medicationInventory);
    }

    /**
     * Finds if a specific medication exists in the inventory.
     *
     * @param medicine the name of the medicine to find
     * @return 1 if the medicine is found, 0 otherwise
     */
    public static int findMedicine(String medicine) {
        List<MedicationInventory> medicationInventory;
        try {
            medicationInventory = getMedicationInventory();
        } catch (IOException e) {
            System.err.println("An error occurred while retrieving medication inventory: " + e.getMessage());
            return 0; // Return 0 indicating that the medicine is not found due to an error
        }
    
        for (MedicationInventory medicationItem : medicationInventory) {
            if (medicationItem.getMedicine().equalsIgnoreCase(medicine)) {
                return 1;
            }
        }
    
        System.out.println("Medicine not found. Please try again!");
        return 0;
    }

    /**
     * Returns a list of medicines that have low stock levels.
     *
     * @return a comma-separated string of medicines with low stock or "NO" if none
     * @throws IOException if an error occurs while reading the file
     */
    public static String lowStockLevelAlert() throws IOException {
        List<String> lowStockMedicines = new ArrayList<>();
        List<MedicationInventory> medicationInventory = getMedicationInventory();

        for (MedicationInventory medication : medicationInventory) {
            if (medication.getStockLevel() <= medication.getLowStockLevelAlert()) {
                lowStockMedicines.add(medication.getMedicine());
            }
        }

        return lowStockMedicines.isEmpty() ? "NO" : String.join(", ", lowStockMedicines);
    }

    /**
     * Writes the updated inventory list back to the CSV file.
     *
     * @param medicationInventory the updated list of medications
     * @throws IOException if an error occurs while writing to the file
     */
    private static void writeInventoryToFile(List<MedicationInventory> medicationInventory) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write("Medicine Name,Initial Stock,Low Stock Level Alert");
            bw.newLine();

            for (MedicationInventory medication : medicationInventory) {
                bw.write(String.join(",",
                        medication.getMedicine(),
                        String.valueOf(medication.getStockLevel()),
                        String.valueOf(medication.getLowStockLevelAlert())));
                bw.newLine();
            }
        }
    }
}