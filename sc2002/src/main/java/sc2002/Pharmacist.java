package sc2002;

import java.util.List;

public class Pharmacist extends User{
    private String pharmacistID;
    private List<MedicationInventory> medicationInventory;

    Pharmacist(String pharmacistID){
        super(pharmacistID);
        this.pharmacistID=pharmacistID;
    }

    public void viewMedicationInventory(){
        try{
            this.medicationInventory = MedicationInventoryDB.getMedicationInventory();

            StringBuilder medicationInventoryString = new StringBuilder(); 
            if (medicationInventory.isEmpty()) {
                System.out.println("No Medications found\n\n");

            }
            for (MedicationInventory medication : medicationInventory) {
                System.out.println("\n================================\n" +
                                    "Medicine: " + medication.getMedicine() + "\n" +
                                   "Initial Stock Level: " + medication.getStockLevel() + "\n" +
                                   "Low Stock Level Alert: " + medication.getLowStockLevelAlert() + "\n");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching medication details: " + e.getMessage());
        }
    }

}
