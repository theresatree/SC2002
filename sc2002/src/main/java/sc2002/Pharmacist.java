package sc2002;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Pharmacist extends User{
    private String pharmacistID;
    private List<MedicationInventory> medicationInventory;
    private List<String> patientIDs;
    private List<AppointmentOutcomeRecord> appointmentOutcomeRecord;

    Pharmacist(String pharmacistID){
        super(pharmacistID);
        this.pharmacistID=pharmacistID;
    }

    public void viewPastAppointmentOutcome(Scanner scanner) {
    try {
        List<AppointmentOutcomeRecord> allOutcomes = PatientAppointmentOutcomeDB.getAllAppointmentOutcomes();
        Set<String> uniquePatientIDs = new HashSet<>(); // Use a Set to collect unique patient IDs

        for (AppointmentOutcomeRecord outcome : allOutcomes) {
            uniquePatientIDs.add(outcome.getPatientID());
        }

        if (uniquePatientIDs.isEmpty()) {
            System.out.println("No patients found in the appointment outcomes.");
            return;
        }

        List<String> patientIDList = new ArrayList<>(uniquePatientIDs);

        System.out.println("\n========================");
        System.out.println("    Choose a patient    ");
        System.out.println("========================");
        for (int i = 0; i < patientIDList.size(); i++) {
            System.out.println((i + 1) + ". " + UserDB.getNameByHospitalID(patientIDList.get(i), Role.PATIENT) + " (" + patientIDList.get(i) + ")");
        }
        System.out.println("========================");

        int choice;
        while (true) {
            System.out.print("Choose a Patient (or enter 0 to exit): ");
            choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Exiting viewing process.\n\n");
                return;
            }

            if (choice < 1 || choice > patientIDList.size()) {
                System.out.println("Invalid choice. Please select a valid Patient.\n");
            } else {
                scanner.nextLine(); 
                break;  
            }
        }

        String patientID = patientIDList.get(choice - 1);
        List<AppointmentOutcomeRecord> appointmentOutcomeRecord = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);

        if (appointmentOutcomeRecord.isEmpty()) {
            System.out.println("\n\n=========================================");
            System.out.println("No diagnosis found for patient");
        } else {
            System.out.println("\n\n=========================================");
            System.out.println("    Past Appointment Outcome Records     ");
            System.out.println("=========================================");
            for (AppointmentOutcomeRecord outcome : appointmentOutcomeRecord) {
                System.out.println(outcome.printAppointmentOutcome());
                System.out.println("=========================================");
            }
        }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }
    }

    public void updatePrescriptionStatus(Scanner scanner) {
        try {
            String patientID;
            while (true) {
                System.out.println("Enter patient ID:");
                patientID = scanner.nextLine().toUpperCase().trim();
    
                if (isValidPatientID(patientID)) {
                    break; 
                } else {
                    System.out.println("Invalid Patient ID. Please re-enter.");
                }
            }
    
            int appointmentID;
            while (true) {
                System.out.println("Enter appointment ID:");
                String input = scanner.nextLine().trim(); 
                
                if (input.isEmpty()) {
                    System.out.println("Invalid Appointment ID. Please re-enter.");
                    continue; 
                }
                try {
                    appointmentID = Integer.parseInt(input);
                    if (isValidAppointmentID(patientID, appointmentID)) {
                        break; 
                    } else {
                        System.out.println("Invalid Appointment ID for the given Patient ID. Please re-enter.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid appointment ID.");
                }
            }

            List<AppointmentOutcomeRecord> outcomes = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);
                
            boolean updated = false;
            for (AppointmentOutcomeRecord outcome : outcomes) {
                if (outcome.getAppointmentID() == appointmentID) {
                    if (outcome.getPrescriptionStatus() == PrescriptionStatus.DISPENSED) {
                        System.out.println("Medicine already dispensed to patient.");
                        return; 
                    }
                    if (outcome.getPrescriptionStatus() == PrescriptionStatus.PENDING) {
                        outcome.setPrescriptionStatus(PrescriptionStatus.DISPENSED);
                        updated = true;
                        System.out.println("Prescription status updated to: DISPENSED");
            
                        String medicine = outcome.getMedications().toUpperCase();
                        if (medicine != null && !medicine.isEmpty()) {
                            MedicationInventoryDB.updateStockLevel(medicine, -1);
                        } else {
                            System.out.println("No medications found in the appointment record.");
                        }
                        break; 
                    }
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
    
    private boolean isValidPatientID(String patientID) {
        try {
            List<AppointmentOutcomeRecord> outcomes = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);
            return outcomes != null && outcomes.size() > 0; 
        } catch (Exception e) {
            System.out.println("Error validating Patient ID: " + e.getMessage());
            return false;
        }
    }
    
    private boolean isValidAppointmentID(String patientID, int appointmentID) {
        try {
            List<AppointmentOutcomeRecord> outcomes = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);
            if (outcomes != null) {
                for (AppointmentOutcomeRecord outcome : outcomes) {
                    if (outcome.getAppointmentID() == appointmentID) {
                        return true; 
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error validating Appointment ID: " + e.getMessage());
        }
        return false; 
    }

    public void viewMedicationInventory(){
        try{
            this.medicationInventory = MedicationInventoryDB.getMedicationInventory();

            System.out.println("\n\n================================\n" +
                               "        Medical Inventory       ");

            StringBuilder medicationInventoryString = new StringBuilder(); 
            if (medicationInventory.isEmpty()) {
                System.out.println("No Medications found\n\n");

            }
            for (MedicationInventory medication : medicationInventory) {
                System.out.println("================================\n" +
                                    "Medicine: " + medication.getMedicine() + "\n" +
                                   "Initial Stock Level: " + medication.getStockLevel() + "\n" +
                                   "Low Stock Level Alert: " + medication.getLowStockLevelAlert());
            }
            System.out.println("================================");
        } catch (Exception e) {
            System.out.println("An error occurred while fetching medication details: " + e.getMessage());
        }
    }

    public void submitReplenishmentRequest(Scanner scanner) {
        try {
            List<MedicationInventory> inventoryList = MedicationInventoryDB.getMedicationInventory();
            Set<String> availableMedicines = new HashSet<>();
            for (MedicationInventory inventory : inventoryList) {
                availableMedicines.add(inventory.getMedicine().toUpperCase()); 
            }

            System.out.println("\n========================");
            System.out.println("    Choose a medicine    ");
            System.out.println("========================");
            for (int i = 0; i < inventoryList.size(); i++) {
                System.out.println((i + 1) + ". " + inventoryList.get(i).getMedicine());
            }

            int medicineIndex = -1;
    
            while (true) {
                System.out.println("Choose a medicine by entering the number (or enter 0 to exit): ");
                if (scanner.hasNextInt()) {
                    medicineIndex = scanner.nextInt();
                    scanner.nextLine(); 

                    if (medicineIndex == 0) {
                        System.out.println("Exiting replenishment request process.\n\n\n");
                        return;
                    }

                    if (medicineIndex < 1 || medicineIndex > inventoryList.size()) {
                        System.out.println("Invalid choice. Please enter a number corresponding to a listed medicine.\n");
                    } else {
                        break; 
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.\n");
                    scanner.nextLine(); 
                }
            }
    
            String medicine = inventoryList.get(medicineIndex - 1).getMedicine();

            System.out.println("Please enter the amount of stock you'd like to add for " + medicine + ": ");
            int amountToReplenish = scanner.nextInt();
    
            int newRequestID = ReplenishmentRequestDB.getLastRequestID() + 1;
            LocalDate dateOfRequest = LocalDate.now();
            ReplenishmentRequest newRequest = new ReplenishmentRequest(newRequestID, this.pharmacistID, dateOfRequest, medicine, amountToReplenish, RequestStatus.PENDING);
    
            ReplenishmentRequestDB.saveReplenishmentRequest(newRequest);
            System.out.println("Replenishment request submitted successfully for " + medicine + ".");
            
        } catch (IOException e) {
            System.out.println("An error occurred while submitting the replenishment request: " + e.getMessage());
        }
    }
    
}
