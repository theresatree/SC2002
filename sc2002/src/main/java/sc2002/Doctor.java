package sc2002;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Doctor extends User{
    private String doctorID;
    private DoctorAppointment docotorAppointment;
    private MedicalRecord medicalRecord;
    private List<String> patientIDs;

    public Doctor(String doctorID){
        super(doctorID);
        this.doctorID=doctorID;
        docotorAppointment = new DoctorAppointment(doctorID);

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
        System.out.println("          Patients under " + this.doctorID);
        System.out.println("==========================================");
        for (int i = 0; i < patientIDs.size(); i++) {
            System.out.println((i + 1) + ". " + patientIDs.get(i));
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
                return patientIDs.get(choice - 1);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////
    
    public void viewPersonalSchedule(){
        docotorAppointment.showPersonalSchedule();
    }
    
    /////////////////////////////////////////////////////////////////////////
    public void updatePatientMedicalRecord(Scanner scanner){
        boolean exit=false;

        String patientID = choosePatientString(patientIDs, scanner);
        while (!exit && patientID!=null){
            System.out.println("\n\n==========================================");
            System.out.println("            Choose an option");
            System.out.println("==========================================");
            System.out.println("1. Add new diagnosis for " + patientID);
            System.out.println("2. Update existing diagnosis for " + patientID);
            System.out.println("==========================================");
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
        System.out.println("Additional Notes for " + patientID);
        String additionalNotes = scanner.nextLine();

        DiagnosisDB.addDiagnosis(patientID, this.doctorID, descriptionOfDiagnosis, descriptionOfTreatmtent, additionalNotes);
    }

    private void updateDiagnosis(String patientID, Scanner scanner){
        try{
            medicalRecord = PatientDB.getPatientDetails(patientID);
            List<Diagnosis> diagnoses = medicalRecord.getDiagnosis(); //Get all existing diagnosis of the patient.

            if (diagnoses.isEmpty()) {
                System.out.println("\n\n==========================================");
                System.out.println("No diagnoses found for patient ID: " + patientID);
                System.out.println("==========================================");
                return;
            }

            int choice;
            while (true){
                for (int i = 0; i < diagnoses.size(); i++) {
                    Diagnosis diagnosis = diagnoses.get(i);
                    System.out.println((i + 1) + ". Diagnosis ID: " + diagnosis.getDiagnosisCode());
                }
                System.out.println("\n\n==========================================");
                System.out.println("            Choose an option");
                System.out.println("==========================================");
                System.out.print("Choose an option (or enter 0 to exit):");
                choice = scanner.nextInt();

                if (choice == 0) {
                    System.out.println("Exiting viewing process.\n\n");
                    break;
                }
    
                if (choice < 1 || choice > diagnoses.size()) {
                    System.out.println("Invalid choice. Please select a valid ID.\n");
                }
                else {
                    int selectedDiagnosisID = diagnoses.get(choice - 1).getDiagnosisCode(); // Actual Diagnosis code
                    updateDiagnosisInDB(patientID, scanner, selectedDiagnosisID); // Update In DB

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
            System.out.println("            Diagnosis code: " + selectedDiagnosisID);
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
        docotorAppointment.showSelectableDates(scanner);
    }

    /////////////////////////////////////////////////////////////////////////
    public void acceptDeclineAppointment(Scanner scanner){
        docotorAppointment.acceptDeclineAppointment(scanner);
    }


    /////////////////////////////////////////////////////////////////////////
    public void viewAppointmentStatus(){
        docotorAppointment.viewAppointmentStatus();
    }

}
