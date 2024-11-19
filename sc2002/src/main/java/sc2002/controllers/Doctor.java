package sc2002.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import sc2002.enums.AppointmentStatus;
import sc2002.enums.Role;
import sc2002.enums.Service;
import sc2002.models.Diagnosis;
import sc2002.models.DoctorAppointment;
import sc2002.models.MedicalRecord;
import sc2002.models.MedicationInventory;
import sc2002.models.PatientScheduledAppointment;
import sc2002.models.User;
import sc2002.repositories.DiagnosisDB;
import sc2002.repositories.DoctorAppointmentDB;
import sc2002.repositories.MedicationInventoryDB;
import sc2002.repositories.PatientAppointmentOutcomeDB;
import sc2002.repositories.PatientDB;
import sc2002.repositories.UserDB;

/**
 * Represents a doctor in the system who can manage appointments, view medical records,
 * update diagnoses, and prescribe medications for patients.
 */
public class Doctor extends User {
    private String doctorID;
    private DoctorAppointment doctorAppointment;
    private MedicalRecord medicalRecord;
    private List<String> patientIDs;
    private List<MedicationInventory> medicationInventory;

    /**
     * Constructs a Doctor instance with the specified doctor ID and loads the patient's information.
     * 
     * @param doctorID The ID of the doctor.
     */
    public Doctor(String doctorID) {
        super(doctorID);
        this.doctorID = doctorID;
        doctorAppointment = new DoctorAppointment(doctorID);

        try {
            patientIDs = DoctorAppointmentDB.getPatients(doctorID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays and allows selection of a patient's medical record to view.
     * 
     * @param scanner Scanner instance for user input.
     */
    public void viewPatientMedicalRecord(Scanner scanner) {
        try {
            String patientID = choosePatientString(patientIDs, scanner);
            if (patientID != null) {
                medicalRecord = PatientDB.getPatientDetails(patientID);
                medicalRecord.viewMedicalRecord();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists patients under the doctor and allows selection.
     * 
     * @param patientIDs List of patient IDs under the doctor.
     * @param scanner Scanner instance for user input.
     * @return The selected patient ID.
     */
    private String choosePatientString(List<String> patientIDs, Scanner scanner) {
        System.out.println("\n\n==========================================");
        System.out.println("      Patients under " + UserDB.getNameByHospitalID(this.doctorID, Role.DOCTOR));
        System.out.println("==========================================");
        for (int i = 1; i < patientIDs.size(); i++) {
            System.out.println((i) + ". " + patientIDs.get(i) + " (" + UserDB.getNameByHospitalID(patientIDs.get(i), Role.PATIENT) + ")");
        }
        System.out.println("==========================================");
        while (true) {
            System.out.print("Choose a Patient (or enter 0 to exit): ");
            if (scanner.hasNextInt()) { // Check if the input is an integer
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
    
                if (choice == 0) {
                    System.out.println("Exiting viewing process.\n\n");
                    return null;
                }
    
                if (choice < 1 || choice >= patientIDs.size()) {
                    System.out.println("Invalid choice. Please select a valid Patient.\n");
                } else {
                    return patientIDs.get(choice);
                }
            } else {
                System.out.println("Invalid input. Please enter a number.\n");
                scanner.nextLine(); // Consume the invalid input to avoid an infinite loop
            }
        }
    }

    /**
     * Views the doctor's personal appointment schedule.
     */
    public void viewPersonalSchedule() {
        doctorAppointment.showPersonalSchedule();
    }

    /**
     * Updates a patient's medical record with new or modified diagnoses.
     * 
     * @param scanner Scanner instance for user input.
     */
    public void updatePatientMedicalRecord(Scanner scanner) {
        boolean exit = false;
        String patientID = choosePatientString(patientIDs, scanner);

        while (!exit && patientID != null) {
            System.out.println("\n\n====================================================");
            System.out.println("            Choose an option");
            System.out.println("====================================================");
            System.out.println("1. Add new diagnosis for " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
            System.out.println("2. Update existing diagnosis for " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
            System.out.println("====================================================");
            System.out.print("Choose an option (or enter 0 to exit): ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    System.out.println("Exiting viewing process.\n\n");
                    exit = true;
                    break;
                case 1:
                    createNewDiagnosis(patientID, scanner);
                    break;
                case 2:
                    updateDiagnosis(patientID, scanner);
                    break;
                default:
                    System.out.println("Please enter a valid option!");
                    break;
            }
        }
    }

    /**
     * Creates a new diagnosis entry for the specified patient.
     * 
     * @param patientID The ID of the patient.
     * @param scanner Scanner instance for user input.
     */
    private void createNewDiagnosis(String patientID, Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        System.out.println("Description of Diagnosis");
        String descriptionOfDiagnosis = scanner.nextLine();
        System.out.println("Description of Treatment given");
        String descriptionOfTreatment = scanner.nextLine();
        System.out.println("Additional Notes for " + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
        String additionalNotes = scanner.nextLine();

        DiagnosisDB.addDiagnosis(patientID, this.doctorID, descriptionOfDiagnosis, descriptionOfTreatment, additionalNotes);
    }

    /**
     * Updates an existing diagnosis entry for the specified patient.
     * 
     * @param patientID The ID of the patient.
     * @param scanner Scanner instance for user input.
     */
    private void updateDiagnosis(String patientID, Scanner scanner) {
        try {
            medicalRecord = PatientDB.getPatientDetails(patientID);
            List<Diagnosis> diagnoses = medicalRecord.getDiagnosis();

            if (diagnoses.isEmpty()) {
                System.out.println("\n\n====================================================");
                System.out.println("No diagnoses found for patient ID: " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
                System.out.println("====================================================");
                return;
            }

            int choice;
            while (true) {
                System.out.println("\n\n==========================================");
                System.out.println("              Diagnosis ID");
                System.out.println("==========================================");
                for (Diagnosis diagnosis : diagnoses) {
                    System.out.println("Diagnosis ID: " + diagnosis.getDiagnosisCode());
                }
                System.out.println("==========================================");
                System.out.print("Choose an option (or enter 0 to exit):");
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nInvalid input! Please enter a valid diagnosis ID.\n");
                    scanner.nextLine();
                    continue;
                }

                if (choice == 0) {
                    System.out.println("\nExiting process.\n\n");
                    break;
                }

                Diagnosis selectedCode = null;
                for (Diagnosis selected : diagnoses) {
                    if (selected.getDiagnosisCode() == choice) {
                        selectedCode = selected;
                        break;
                    }
                }

                if (selectedCode == null) {
                    System.out.println("\nInvalid! Please select a valid diagnosis ID.");
                } else {
                    updateDiagnosisInDB(patientID, scanner, selectedCode.getDiagnosisCode());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates diagnosis details in the database.
     * 
     * @param patientID The ID of the patient.
     * @param scanner Scanner instance for user input.
     * @param selectedDiagnosisID The diagnosis ID to be updated.
     */
    private void updateDiagnosisInDB(String patientID, Scanner scanner, int selectedDiagnosisID) {
        int choice;
        String diagnosis = "";
        String treatment = "";
        String notes = "";
        boolean exit = false;

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (!exit) {
            System.out.println("\n\n==========================================");
            System.out.println("           Diagnosis code: " + selectedDiagnosisID);
            System.out.println("        Choose a data to update");
            System.out.println("==========================================");
            System.out.println("1. Diagnosis");
            System.out.println("2. Treatment");
            System.out.println("3. Notes");
            System.out.println("==========================================");
            System.out.print("Choose an option (or enter 0 to exit): ");
            choice = scanner.nextInt();

            scanner.nextLine();

            switch (choice) {
                case 0:
                    System.out.println("Exiting updating process.\n\n");
                    exit = true;
                    break;
                case 1:
                    System.out.print("\nEnter Diagnosis: ");
                    diagnosis = scanner.nextLine();
                    System.out.println("\nDiagnosis updated!\n");
                    break;
                case 2:
                    System.out.print("\nEnter Treatment: ");
                    treatment = scanner.nextLine();
                    System.out.println("\nTreatment updated!\n");
                    break;
                case 3:
                    System.out.print("\nEnter Notes: ");
                    notes = scanner.nextLine();
                    System.out.println("\nNotes updated!\n");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid ID.\n");
                    break;
            }
        }
        try {
            DiagnosisDB.updateDiagnosis(selectedDiagnosisID, diagnosis, treatment, notes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows the doctor to set their availability dates for appointments.
     * 
     * @param scanner Scanner instance for user input.
     */
    public void setAvailabilityDate(Scanner scanner) {
        doctorAppointment.showSelectableDates(scanner);
    }

    /**
     * Manages the acceptance or decline of patient appointments.
     * 
     * @param scanner Scanner instance for user input.
     */
    public void acceptDeclineAppointment(Scanner scanner) {
        doctorAppointment.acceptDeclineAppointment(scanner);
    }

    /**
     * Views the status of all appointments for the doctor.
     */
    public void viewAppointmentStatus() {
        doctorAppointment.viewAppointmentStatus();
    }

    /**
     * Creates an appointment record for a patient after a consultation.
     * 
     * @param scanner Scanner instance for user input.
     */
    public void createAppointmentRecord(Scanner scanner) {
        boolean exit = false;
        int choice = -1;
        int serviceChoice = -1;
        String medication = "";
        Service service = null;
        String notes = "";
        String patientID = choosePatientString(patientIDs, scanner);

        try {
            while (!exit && patientID != null) {
                List<PatientScheduledAppointment> appointments = DoctorAppointmentDB.doctorListOfAllAppointments(this.doctorID);
                List<PatientScheduledAppointment> confirmedAppointments = new ArrayList<>();

                for (PatientScheduledAppointment appointment : appointments) {
                    if (appointment.getStatus() == AppointmentStatus.CONFIRMED && appointment.getPatientID().equals(patientID)) {
                        confirmedAppointments.add(appointment);
                    }
                }

                if (confirmedAppointments.isEmpty()) {
                    System.out.println("\n\n==========================================");
                    System.out.println("Select Appointment ID with " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
                    System.out.println("==========================================");
                    System.out.println("\nThere is no available appointments to record!\n\n");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                }
                System.out.println("\n\n==========================================");
                System.out.println("Select Appointment ID with " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
                System.out.println("==========================================");

                for (PatientScheduledAppointment appointment : confirmedAppointments) {
                    System.out.println("ID: " + appointment.getAppointmentID());
                }
                System.out.println("==========================================");
                System.out.print("Choose an option (or enter 0 to exit): ");

                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nInvalid input! Please enter a valid appointment ID.\n");
                    scanner.nextLine();
                    continue;
                }

                if (choice == 0) {
                    System.out.println("\nExiting process.\n\n");
                    break;
                }

                PatientScheduledAppointment selectedAppointment = null;
                for (PatientScheduledAppointment selected : confirmedAppointments) {
                    if (selected.getAppointmentID() == choice) {
                        selectedAppointment = selected;
                        break;
                    }
                }

                if (selectedAppointment == null) {
                    System.out.println("\nInvalid! Please select a valid appointment ID.");
                } else {
                    System.out.println("\n\n==========================================");
                    System.out.println("  Appointment ID " + selectedAppointment.getAppointmentID() + " with " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
                    System.out.println("   Date: " + selectedAppointment.getDate() + " - " + selectedAppointment.getTimeStart() + " to " + selectedAppointment.getTimeEnd());
                    System.out.println("==========================================\n");

                    // SERVICES
                    while (serviceChoice < 1 || serviceChoice > Service.values().length) {
                        System.out.println("==========================================");
                        System.out.println("            Services provided");
                        System.out.println("==========================================");
                        for (int i = 1; i <= Service.values().length; i++) {
                            System.out.println(i + ": " + Service.values()[i - 1]);
                        }
                        System.out.println("==========================================");
                        System.out.print("Choose service provided: ");

                        serviceChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (serviceChoice >= 1 && serviceChoice <= Service.values().length) {
                            service = Service.values()[serviceChoice - 1];
                            System.out.println("You selected: " + service);
                        } else {
                            System.out.println("Invalid choice! Please enter a number between 1 and " + Service.values().length);
                        }
                    }

                    // MEDICINE
                    medication = listOfMedicine(scanner);
                    System.out.print("You selected: " + medication);

                    System.out.print("\n\nConsultation notes for patient: ");
                    notes = scanner.nextLine();
                    PatientAppointmentOutcomeDB.setAppointmentOutcome(patientID, doctorID, selectedAppointment.getAppointmentID(), selectedAppointment.getDate(), service, medication, notes);
                    DoctorAppointmentDB.completeAppointment(doctorID, selectedAppointment.getAppointmentID(), patientID);

                    System.out.println("\nUpdating Patient's Record. Please wait...");
                    for (int i = 5; i > 0; i--) {
                        System.out.print("*");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("\nUpdate process interrupted.");
                        }
                    }
                    System.out.println("\nAppointment Outcome Recorded!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides a list of available medications for selection.
     * 
     * @param scanner Scanner instance for user input.
     * @return The name of the selected medication.
     */
    private String listOfMedicine(Scanner scanner) {
        int medicineChoice = -1;
        try {
            while (true) {
                medicationInventory = MedicationInventoryDB.getMedicationInventory();
                System.out.println("\n==========================================");
                System.out.println("            Medicine provided");
                System.out.println("==========================================");
                for (int i = 0; i < medicationInventory.size(); i++) {
                    System.out.println((i + 1) + ". " + medicationInventory.get(i).getMedicine());
                }
                System.out.println("==========================================");
                System.out.print("Choose medication prescribed: ");

                medicineChoice = scanner.nextInt();

                if (medicineChoice < 1 || medicineChoice > medicationInventory.size()) {
                    System.out.println("Invalid choice. Please select a valid medicine.\n");
                } else {
                    scanner.nextLine();
                    return medicationInventory.get(medicineChoice - 1).getMedicine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}