package sc2002;

import java.util.List;

public class Pharmacist extends User{
    private String pharmacistID;
    private List<MedicationInventory> medicationInventory;
    private List<String> patientIDs;
    private List<AppointmentOutcomeRecord> appointmentOutcomeRecord;

    Pharmacist(String pharmacistID){
        super(pharmacistID);
        this.pharmacistID=pharmacistID;
    }

    public void viewPastAppointmentOutcome(String patientID){
        try {
            appointmentOutcomeRecord = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);
            if (appointmentOutcomeRecord.isEmpty()) {
                System.out.println("\n\n=========================================");
                System.out.println("No diagnosis found for patient");

            }
            System.out.println("\n\n=========================================");
            System.out.println("    Past Appointment Outcome Records");
            System.out.println("=========================================");
            for (AppointmentOutcomeRecord outcome : appointmentOutcomeRecord) {
                System.out.println(outcome.printAppointmentOutcome());
                System.out.println("=========================================");
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }
    }

    public void updatePrescriptionStatus(String patientID, int appointmentID) {
        try {
            List<AppointmentOutcomeRecord> outcomes = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);
            
            boolean updated = false;
            for (AppointmentOutcomeRecord outcome : outcomes) {
                if (outcome.getAppointmentID() == appointmentID && outcome.getPrescriptionStatus() == PrescriptionStatus.PENDING) {
                    outcome.setPrescriptionStatus(PrescriptionStatus.DISPENSED);
                    updated = true;
                    System.out.println("Prescription status updated to: DISPENSED");
                    break;
                }
            }
    
            if (updated) {
                PatientAppointmentOutcomeDB.updateAppointmentOutcome(outcomes);
            } else {
                System.out.println("No matching appointment found for update.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the prescription status: " + e.getMessage());
        }
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

    public void submitReplenishmentRequest(String medicine, int stockLevel){
        
    }
    
}
