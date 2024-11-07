package sc2002.models;

/**
 * The MedicationInventory class represents information about a specific medication's
 * stock level and low stock alert level.
 */
public class MedicationInventory {

    private String medicine;
    private int stockLevel;
    private int lowStockLevelAlert;

    /**
     * Constructs a MedicationInventory object with specified details.
     *
     * @param medicine the name of the medication
     * @param stockLevel the initial stock level
     * @param lowStockLevelAlert the threshold for low stock alerts
     */
    public MedicationInventory(String medicine, int stockLevel, int lowStockLevelAlert) {
        this.medicine = medicine;
        this.stockLevel = stockLevel;
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    /**
     * Gets the name of the medication.
     *
     * @return the name of the medication
     */
    public String getMedicine() {
        return this.medicine;
    }

    /**
     * Gets the current stock level of the medication.
     *
     * @return the stock level
     */
    public int getStockLevel() {
        return this.stockLevel;
    }

    /**
     * Gets the low stock level alert threshold.
     *
     * @return the low stock level alert
     */
    public int getLowStockLevelAlert() {
        return this.lowStockLevelAlert;
    }

    /**
     * Prints the medication inventory details in a formatted string.
     *
     * @return the formatted string of medication inventory details
     */
    public String printMedicationInventory() {
        return ("Medicine: " + medicine + "\n"
                + "Initial Stock Level: " + stockLevel + "\n"
                + "Low Stock Level Alert: " + lowStockLevelAlert + "\n");
    }
}