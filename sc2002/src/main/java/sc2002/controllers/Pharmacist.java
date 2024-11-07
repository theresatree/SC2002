package sc2002.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import sc2002.models.MedicationInventory;
import sc2002.services.AppointmentOutcomeRecord;
import sc2002.repositories.PatientAppointmentOutcomeDB;
import sc2002.repositories.UserDB;
import sc2002.repositories.MedicationInventoryDB;
import sc2002.repositories.ReplenishmentRequestDB;
import sc2002.enums.Role;
import sc2002.enums.PrescriptionStatus;
import sc2002.enums.RequestStatus;
import sc2002.models.User;
import sc2002.models.ReplenishmentRequest;

public class Pharmacist extends User {
    private String pharmacistID;
    private List<MedicationInventory> medicationInventory;
    private List<AppointmentOutcomeRecord> appointmentOutcomeRecord;

    /**
     * Constructs a Pharmacist object with the specified pharmacist ID.
     *
     * @param pharmacistID the unique identifier for the pharmacist
     */
    public Pharmacist(String pharmacistID) {
        super(pharmacistID);
        this.pharmacistID = pharmacistID;
    }

    /**
     * Fetches and displays the past appointment outcomes for a specific patient.
     *
     * @param scanner the Scanner object to receive user input
     */
    public void viewPastAppointmentOutcome(Scanner scanner) {
        try {
            String patientID = isValidPatientID(scanner);
            if (patientID.equals("Exit")) {
                return;
            }
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

    /**
     * Updates the prescription status for a specific patient's appointment.
     *
     * @param scanner the Scanner object to receive user input
     */
    public void updatePrescriptionStatus(Scanner scanner) {
        try {
            String patientID = isValidPatientID(scanner);
            if (patientID.equals("Exit")) {
                return;
            }
            int appointmentID = isValidAppointmentID(patientID, scanner);
            if (appointmentID == -1) {
                return;
            }
            List<AppointmentOutcomeRecord> outcomes = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);

            boolean updated = false;
            for (AppointmentOutcomeRecord outcome : outcomes) {
                if (outcome.getAppointmentID() == appointmentID) {
                    if (outcome.getPrescriptionStatus() == PrescriptionStatus.DISPENSED) {
                        System.out.println("\n\nMedicine already dispensed to patient.\n\n");
                        Thread.sleep(500);
                        return;
                    }
                    if (outcome.getPrescriptionStatus() == PrescriptionStatus.PENDING) {
                        outcome.setPrescriptionStatus(PrescriptionStatus.DISPENSED);
                        updated = true;
                        System.out.println("\nUpdating Prescription Status. Please wait");
                        for (int i = 5; i > 0; i--) {
                            System.out.print("*");
                            try {
                                Thread.sleep(200); // Sleep for 0.2 second
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.out.println("\nUpdating process interrupted.");
                            }
                        }
                        System.out.println("\nPrescription status updated to: DISPENSED\n\n");

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

    /**
     * Validates the patient ID input by the user.
     *
     * @param scanner the Scanner object to receive user input
     * @return the valid patient ID or "Exit" if the user chooses to exit
     */
    private String isValidPatientID(Scanner scanner) {
        try {
            List<AppointmentOutcomeRecord> allOutcomes = PatientAppointmentOutcomeDB.getAllAppointmentOutcomes();
            Set<String> uniquePatientIDs = new HashSet<>(); // Use a Set to collect unique patient IDs

            for (AppointmentOutcomeRecord outcome : allOutcomes) {
                uniquePatientIDs.add(outcome.getPatientID());
            }

            if (uniquePatientIDs.isEmpty()) {
                System.out.println("No patients found in the appointment outcomes.");
                return "Exit";
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
                    return "Exit";
                }

                if (choice < 1 || choice > patientIDList.size()) {
                    System.out.println("Invalid choice. Please select a valid Patient.\n");
                } else {
                    scanner.nextLine();
                    break;
                }
            }
            return patientIDList.get(choice - 1);
        } catch (IOException e) {
            e.printStackTrace();
            return "Exit";
        }
    }

    /**
     * Validates the appointment ID for a given patient.
     *
     * @param patientID the ID of the patient
     * @param scanner   the Scanner object to receive user input
     * @return the valid appointment ID or -1 if the user chooses to exit
     */
    private int isValidAppointmentID(String patientID, Scanner scanner) {
        int choice = -1;
        try {
            appointmentOutcomeRecord = PatientAppointmentOutcomeDB.getAppointmentOutcome(patientID);
            System.out.println("\n\n=========================================");
            System.out.println("  Appointment Outcome Records for " + patientID);
            System.out.println("=========================================");
            for (AppointmentOutcomeRecord outcome : appointmentOutcomeRecord) {
                System.out.println("Appointment ID " + outcome.getAppointmentID());
                System.out.println("Prescription Status " + outcome.getPrescriptionStatus());
                System.out.println("=========================================");
            }
            while (choice != 0) {
                System.out.print("Choose an appointment ID (or 0 to exit): ");
                choice = scanner.nextInt();
                if (choice == 0) {
                    System.out.println("\nExiting selection process.\n\n");
                    break;
                }

                AppointmentOutcomeRecord selectedID = null;
                for (AppointmentOutcomeRecord ID : appointmentOutcomeRecord) {
                    if (ID.getAppointmentID() == choice) {
                        selectedID = ID;
                        break;
                    }
                }
                if (selectedID == null) {
                    System.out.println("\nInvalid! Please select a valid appointment ID.");
                } else {
                    return selectedID.getAppointmentID();
                }
            }
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Displays the current medication inventory.
     */
    public void viewMedicationInventory() {
        try {
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

    /**
     * Submits a replenishment request for a specific medication.
     *
     * @param scanner the Scanner object to receive user input
     */
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