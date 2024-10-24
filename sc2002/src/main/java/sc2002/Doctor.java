package sc2002;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Doctor extends User{
    private String doctorID;
    private DoctorAppointment doctorAppointment;
    private MedicalRecord medicalRecord;
    private List<String> patientIDs;
    private List<MedicationInventory> medicationInventory;

    public Doctor(String doctorID){
        super(doctorID);
        this.doctorID=doctorID;
        doctorAppointment = new DoctorAppointment(doctorID);

        try {
            patientIDs = DoctorAppointmentDB.getPatients(doctorID);
        }catch(IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }

    }

/////////////////////////////////////////////////////////////////////////
    public void viewPatientMedicalRecord(Scanner scanner){
        // Get Patients under Doctor

        try{
            String patientID = choosePatientString(patientIDs, scanner); // Choose a patient
            if (patientID!=null){
                medicalRecord = PatientDB.getPatientDetails(patientID);
                medicalRecord.viewMedicalRecord();
            }
        }
        catch(IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }

    /// This function is just to get the patient that are under this doctor
    private String choosePatientString(List<String> patientIDs, Scanner scanner){
        int choice;
        System.out.println("\n\n==========================================");
        System.out.println("      Patients under " + UserDB.getNameByHospitalID(this.doctorID, Role.DOCTOR));
        System.out.println("==========================================");
        for (int i = 0; i < patientIDs.size(); i++) {
            System.out.println((i + 1) + ". " + patientIDs.get(i) + " (" + UserDB.getNameByHospitalID(patientIDs.get(i), Role.PATIENT) + ")");
        }
        System.out.println("==========================================");
        while (true) {
            System.out.print("Choose a Patient (or enter 0 to exit): ");
            choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Exiting viewing process.\n\n");
                return null; // Exit if the user chooses to exit
            }

            if (choice < 1 || choice > patientIDs.size()) {
                System.out.println("Invalid choice. Please select a valid Patient.\n");
            }
            else {
                scanner.nextLine();
                return patientIDs.get(choice - 1);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////
    
    public void viewPersonalSchedule(){
        doctorAppointment.showPersonalSchedule();
    }
    
    /////////////////////////////////////////////////////////////////////////
    public void updatePatientMedicalRecord(Scanner scanner){
        boolean exit=false;

        String patientID = choosePatientString(patientIDs, scanner);
        while (!exit && patientID!=null){
            System.out.println("\n\n====================================================");
            System.out.println("            Choose an option");
            System.out.println("====================================================");
            System.out.println("1. Add new diagnosis for " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
            System.out.println("2. Update existing diagnosis for " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
            System.out.println("====================================================");
            System.out.print("Choose an option (or enter 0 to exit): ");
            int choice = scanner.nextInt();
            switch (choice){
                case 0: 
                    System.out.println("Exiting viewing process.\n\n");
                    exit=true;
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

    private void createNewDiagnosis(String patientID, Scanner scanner){
        // Clear the newline character left over from a previous nextInt() or similar call
        if (scanner.hasNextLine()) {
            scanner.nextLine(); // Consume the leftover newline
        }
        System.out.println("Description of Diagnosis");
        String descriptionOfDiagnosis = scanner.nextLine();
        System.out.println("Description of Treatment given");
        String descriptionOfTreatmtent = scanner.nextLine();
        System.out.println("Additional Notes for " + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
        String additionalNotes = scanner.nextLine();

        DiagnosisDB.addDiagnosis(patientID, this.doctorID, descriptionOfDiagnosis, descriptionOfTreatmtent, additionalNotes);
    }

    private void updateDiagnosis(String patientID, Scanner scanner){
        try{
            medicalRecord = PatientDB.getPatientDetails(patientID);
            List<Diagnosis> diagnoses = medicalRecord.getDiagnosis(); //Get all existing diagnosis of the patient.

            if (diagnoses.isEmpty()) {
                System.out.println("\n\n====================================================");
                System.out.println("No diagnoses found for patient ID: " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
                System.out.println("====================================================");
                return;
            }

            int choice;
            while (true){
                System.out.println("\n\n==========================================");
                System.out.println("              Diagnosis ID");
                System.out.println("==========================================");
                for (Diagnosis diagnosis : diagnoses){
                    System.out.println("Diagnosis ID: " + diagnosis.getDiagnosisCode());
                }
                System.out.println("==========================================");
                System.out.print("Choose an option (or enter 0 to exit):");
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nInvalid input! Please enter a valid diagnosis ID.\n");
                    scanner.nextLine(); // Clear the invalid input from the scanner
                    continue; // Skip the rest of the loop and ask for input again
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
                    updateDiagnosisInDB(patientID, scanner, selectedCode.getDiagnosisCode()); // Update In DB
                }
            }
        }catch(IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }

    private void updateDiagnosisInDB(String patientID, Scanner scanner, int selectedDiagnosisID){
        int choice;
        String diagnosis="";
        String treatment="";
        String notes="";
        boolean exit=false;

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // Consume the leftover newline
        }

        while (!exit){
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

            scanner.nextLine(); // consume leftover newline

            switch (choice){
                case 0:
                    System.out.println("Exiting updating process.\n\n");
                    exit=true;
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
                default: System.out.println("Invalid choice. Please select a valid ID.\n");
                    break;
            }
        }
        try {
            DiagnosisDB.updateDiagnosis(selectedDiagnosisID, diagnosis, treatment, notes);
        } catch (IOException e){
            e.printStackTrace(); // Handle the exception as appropriate
        }

    }
    /////////////////////////////////////////////////////////////////////////
    public void setAvailabilityDate(Scanner scanner){
        doctorAppointment.showSelectableDates(scanner);
    }

    /////////////////////////////////////////////////////////////////////////
    public void acceptDeclineAppointment(Scanner scanner){
        doctorAppointment.acceptDeclineAppointment(scanner);
    }

    /////////////////////////////////////////////////////////////////////////
    public void viewAppointmentStatus(){
        doctorAppointment.viewAppointmentStatus();
    }



    /////////////////////////////////////////////////////////////////////////
    public void createAppointmentRecord(Scanner scanner){
        boolean exit=false;
        int choice=-1;
        int serviceChoice = -1;
        String medication="";
        Service service = null;
        String notes = "";
        String patientID = choosePatientString(patientIDs, scanner);

        try{
            while (!exit && patientID!=null){
                List<PatientScheduledAppointment> appointments = DoctorAppointmentDB.doctorListOfAllAppointments(this.doctorID);
                List<PatientScheduledAppointment> confirmedAppointments = new ArrayList<>();

                for (PatientScheduledAppointment appointment : appointments) {
                    if (appointment.getStatus() == AppointmentStatus.CONFIRMED && appointment.getPatientID().equals(patientID)) {
                        confirmedAppointments.add(appointment);
                    }
                }
                
                if (confirmedAppointments.isEmpty()){
                    System.out.println("\n\n==============================================");
                    System.out.println("Select Appointment ID with " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
                    System.out.println("==============================================");
                    System.out.println("\nThere is no available appointments to record!\n\n");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine(); // Clear the invalid input from the scanner
                    break;
                }
                System.out.println("\n\n==============================================");
                System.out.println("Select Appointment ID with " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")");
                System.out.println("==============================================");

                for (PatientScheduledAppointment appointment : confirmedAppointments){
                    System.out.println("ID: " + appointment.getAppointmentID());
                }
                System.out.println("==========================================");
                System.out.print("Choose an option (or enter 0 to exit): ");

                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\nInvalid input! Please enter a valid appointment ID.\n");
                    scanner.nextLine(); // Clear the invalid input from the scanner
                    continue; // Skip the rest of the loop and ask for input again
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

                    //SERVICES
                    while (serviceChoice < 1 || serviceChoice > Service.values().length) {
                        System.out.println("==========================================");
                        System.out.println("            Services provided");
                        System.out.println("==========================================");
                        // Start displaying services from index 1
                        for (int i = 1; i <= Service.values().length; i++) {
                            System.out.println(i + ": " + Service.values()[i - 1]); // Display index with the service name
                        }
                        System.out.println("==========================================");
                        System.out.print("Choose service provided: ");
                    
                        serviceChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                    
                        // Adjust the condition to check for the range starting from 1
                        if (serviceChoice >= 1 && serviceChoice <= Service.values().length) {
                            service = Service.values()[serviceChoice - 1]; // Access the enum by index
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
                    PatientAppointmentOutcomeDB.setAppointmentOutcome(patientID,doctorID,selectedAppointment.getAppointmentID(),selectedAppointment.getDate(),service,medication,notes);
                    DoctorAppointmentDB.completeAppointment(doctorID,selectedAppointment.getAppointmentID(),patientID);

                    System.out.println("\nUpdating Patient's Record. Please wait...");
                    for (int i = 5; i > 0; i--) {
                        System.out.print("*");
                        try {
                            Thread.sleep(200); // Sleep for 0.2 second
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("\nUpdate process interrupted.");
                        }
                    }
                    System.out.println("\nAppointment Outcome Recorded!");
                }
            }
            }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private String listOfMedicine(Scanner scanner){
        int medicineChoice=-1;
        try{
            while (true){
                medicationInventory = MedicationInventoryDB.getMedicationInventory();
                System.out.println("\n==========================================");
                System.out.println("            Medicine provided");
                System.out.println("==========================================");
                for (int i=0; i<medicationInventory.size(); i++){
                    System.out.println((i + 1) + ". " + medicationInventory.get(i).getMedicine());
                }
                System.out.println("==========================================");
                System.out.print("Choose medication prescribed: ");
                        
                medicineChoice = scanner.nextInt();

                if (medicineChoice < 1 || medicineChoice > medicationInventory.size()) {
                    System.out.println("Invalid choice. Please select a valid medicine.\n");
                }
                else {
                    scanner.nextLine();
                    return medicationInventory.get(medicineChoice-1).getMedicine();
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
