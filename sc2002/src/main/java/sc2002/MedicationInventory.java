package sc2002;

public class MedicationInventory {
    private Medicine medicine;
    private int stockLevel;
    private int lowStockLevelAlert;

    MedicationInventory(Medicine medicine, int stockLevel, int lowStockLevelAlert){
        this.medicine = medicine;
        this.stockLevel = stockLevel;
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    public Medicine getMedicine(){
        return this.medicine;
    }

    public int getStockLevel(){
        return this.stockLevel;
    }

    public int getLowStockLevelAlert(){
        return this.lowStockLevelAlert; 
    }

    public void printMedicationInventory(){

    }
}
