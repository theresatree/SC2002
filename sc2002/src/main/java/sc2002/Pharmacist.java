package sc2002;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
                if (outcome.getPrescriptionStatus() == PrescriptionStatus.DISPENSED){
                    System.out.println("Medicine already dispensed to patient.");
                    return;
                }
                if (outcome.getAppointmentID() == appointmentID && outcome.getPrescriptionStatus() == PrescriptionStatus.PENDING) {
                    outcome.setPrescriptionStatus(PrescriptionStatus.DISPENSED);
                    updated = true;
                    System.out.println("Prescription status updated to: DISPENSED");
    
                    String medicine = outcome.getMedications();
                    if (medicine != null && !medicine.isEmpty()) {
                        reduceStockLevel(medicine);
                    } else {
                        System.out.println("No medications found in the appointment record.");
                    }
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
    
    private void reduceStockLevel(String medicationName) {
        try {
            List<MedicationInventory> inventoryList = MedicationInventoryDB.getMedicationInventory();
    
            for (MedicationInventory inventory : inventoryList) {
                if (inventory.getMedicine().equalsIgnoreCase(medicationName)) {
                    int currentStock = inventory.getStockLevel();
                    if (currentStock > 0) {
                        MedicationInventoryDB.updateStockLevel(medicationName, -1); 
                        System.out.println("Stock level for " + medicationName + " reduced by 1.");
                    } else {
                        System.out.println("Stock level for " + medicationName + " is already zero.");
                    }
                    return;
                }
            }
            System.out.println("Medicine " + medicationName + " not found in inventory.");
    
        } catch (IOException e) {
            System.out.println("An error occurred while reducing medicine stock: " + e.getMessage());
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

    public void submitReplenishmentRequest(String medicine) {
    try {

        List<MedicationInventory> inventoryList = MedicationInventoryDB.getMedicationInventory();
        MedicationInventory selectedMedicine = null;

        for (MedicationInventory inventory : inventoryList) {
            if (inventory.getMedicine().equalsIgnoreCase(medicine)) {
                selectedMedicine = inventory;
                break;
            }
        }

        if (selectedMedicine == null) {
            System.out.println("Medicine " + medicine + " not found in inventory.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the amount of stock you'd like to add for " + medicine + ": ");
        int amountToReplenish = scanner.nextInt();

        List<ReplenishmentRequest> replenishmentRequests = ReplenishmentRequestDB.getReplenishmentRequest(RequestStatus.PENDING);
        int newRequestID = replenishmentRequests.size() + 1; 
        LocalDate dateOfRequest = LocalDate.now();
        ReplenishmentRequest newRequest = new ReplenishmentRequest(newRequestID, this.pharmacistID, dateOfRequest, medicine, amountToReplenish, RequestStatus.PENDING);

        // Write the request to the Excel sheet
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream("Replenishment_Requests.xlsx");
             Workbook workbook = new XSSFWorkbook(is);
             FileOutputStream fos = new FileOutputStream("src/main/resources/Replenishment_Requests.xlsx")) {

            Sheet sheet = workbook.getSheetAt(0); 
            int lastRow = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRow + 1); 

            newRow.createCell(0).setCellValue(newRequestID);  
            newRow.createCell(1).setCellValue(this.pharmacistID);  
            newRow.createCell(2).setCellValue(dateOfRequest.format(ReplenishmentRequestDB.dateFormat));  
            newRow.createCell(3).setCellValue(medicine);  
            newRow.createCell(4).setCellValue(amountToReplenish);  
            newRow.createCell(5).setCellValue(RequestStatus.PENDING.name());  

            workbook.write(fos);

            System.out.println("Replenishment request for " + medicine + " has been submitted successfully.");
        }

    } catch (IOException e) {
        System.out.println("An error occurred while submitting the replenishment request: " + e.getMessage());
    }
}

    
}
