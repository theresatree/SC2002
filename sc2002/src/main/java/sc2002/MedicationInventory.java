package sc2002;

public class MedicationInventory {

    private String medicine;
    private int stockLevel;
    private int lowStockLevelAlert;

    MedicationInventory(String medicine, int stockLevel, int lowStockLevelAlert) {
        this.medicine = medicine;
        this.stockLevel = stockLevel;
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    public String getMedicine() {
        return this.medicine;
    }

    public int getStockLevel() {
        return this.stockLevel;
    }

    public int getLowStockLevelAlert() {
        return this.lowStockLevelAlert;
    }

    public String printMedicationInventory() {
        return ("Medicine: " + medicine + "\n"
                + "Initial Stock Level: " + stockLevel + "\n"
                + "Low Stock Level Alert: " + lowStockLevelAlert + "\n");
    }

    
}
