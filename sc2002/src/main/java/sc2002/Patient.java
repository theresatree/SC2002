package sc2002;

import java.util.Scanner;

public class Patient extends User{
    private String patientID;
    private MedicalRecord medicalRecord;
    private PatientAppointment patientAppointment;

    Patient(String patientID){
        super(patientID);
        this.patientID=patientID;
        this.patientAppointment = new PatientAppointment(patientID);
    }

    public void viewMedicalRecord(){
        try{
            this.medicalRecord = PatientDB.getPatientDetails(this.patientID);
            this.medicalRecord.viewMedicalRecord();
        } catch (Exception e) {
            // Handle the exception
            System.out.println("An error occurred while fetching patient details: " + e.getMessage());
        }
    }

    public void updatePersonalInfo(Scanner scanner){
        try{
            this.medicalRecord = PatientDB.getPatientDetails(this.patientID);
        } catch (Exception e) {
            // Handle the exception
            System.out.println("An error occurred while fetching patient details: " + e.getMessage());
        }

        String email = medicalRecord.getEmailAddress();
        int phoneNumber = medicalRecord.getPhoneNumber();
        int choice;
        int continueUpdate=1;
        
        while (continueUpdate==1){
            System.out.println("\n\n===========================================");
            System.out.println("Which Information would you like to update?");
            System.out.println("===========================================");
            System.out.println("1. Phone Number");
            System.out.println("2. Email Address");
            System.out.println("3. Exit");
            System.out.println("===========================================");
            System.out.print("Select a choice: ");
            choice = Main.getValidChoice(scanner,3);

            switch (choice){
                case 1:
                    System.out.print("\nPlease Enter a new Phone Number: ");
                    while (true){
                        String phoneString = scanner.nextLine();
                            if (phoneString.length() == 8 && phoneString.matches("\\d+")){ // Check for 8 digits and integer.
                                phoneNumber = Integer.parseInt(phoneString); // Convert to int
                                System.out.println("Phone number changed to: " + phoneNumber);
                                break; // Exit the loop if valid
                            } else {
                                System.out.println("Invalid input. Please enter a phone number with exactly 8 digits.");
                            }
                    }
                    break;
                case 2:
                    System.out.print("\nPlease Enter a new Email Address: ");
                    email = scanner.nextLine();
                    System.out.println("Email Address changed to: " + email);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("An error occurred while updating patient details:");
                    break;
            
            }
            System.out.println("\n===========================================");
            System.out.println("             Continue updating?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner,2);

        }
        medicalRecord.updatePersonalInfo(email, phoneNumber);
    }

    public void viewAvailableAppointmentSlots(){
        patientAppointment.viewAvailableAppointmentSlots();
    }

    public void scheduleAppointment(Scanner scanner){
        patientAppointment.scheduleAppointment(scanner);
    }

    public void rescheduleAppointment(Scanner scanner){
        patientAppointment.rescheduleAppointment(scanner);
    }

    public void cancelAppointment(Scanner scanner){
        System.out.println("\n\n=========================================");
        System.out.println("     Choose an appointment to cancel");
        System.out.println("=========================================");
        patientAppointment.cancelAppointment(scanner);
    }

    public void viewScheduledAppointments(){
        System.out.println("\n\n=========================================");
        System.out.println("     List of Scheduled Appointments");
        System.out.println("=========================================");
        patientAppointment.viewAppointmentStatus();
    }

    public void viewPastAppointmentOutcomeRecords(){
        patientAppointment.viewPastAppointmentOutcome();
     }
}