package sc2002.controllers;

import java.util.Scanner;
import sc2002.models.MedicalRecord;
import sc2002.models.PatientAppointment;
import sc2002.repositories.PatientDB;
import sc2002.models.User;
import sc2002.main.*;

/**
 * The Patient class represents a patient user with methods to manage personal information,
 * view medical records, and schedule or manage appointments.
 */
public class Patient extends User {
    private String patientID;
    private MedicalRecord medicalRecord;
    private PatientAppointment patientAppointment;

    /**
     * Constructs a Patient object with the specified patient ID.
     *
     * @param patientID the unique identifier for the patient
     */
    public Patient(String patientID) {
        super(patientID);
        this.patientID = patientID;
        this.patientAppointment = new PatientAppointment(patientID);
    }

    /**
     * Fetches and displays the patient's medical record.
     */
    public void viewMedicalRecord() {
        try {
            this.medicalRecord = PatientDB.getPatientDetails(this.patientID);
            this.medicalRecord.viewMedicalRecord();
        } catch (Exception e) {
            System.out.println("An error occurred while fetching patient details: " + e.getMessage());
        }
    }

    /**
     * Updates the patient's personal information, including phone number and email address.
     *
     * @param scanner the Scanner object to receive user input
     */
    public void updatePersonalInfo(Scanner scanner) {
        try {
            this.medicalRecord = PatientDB.getPatientDetails(this.patientID);
        } catch (Exception e) {
            System.out.println("An error occurred while fetching patient details: " + e.getMessage());
        }

        String email = medicalRecord.getEmailAddress();
        int phoneNumber = medicalRecord.getPhoneNumber();
        int choice;
        int continueUpdate = 1;

        while (continueUpdate == 1) {
            System.out.println("\n\n===========================================");
            System.out.println("Which Information would you like to update?");
            System.out.println("===========================================");
            System.out.println("1. Phone Number");
            System.out.println("2. Email Address");
            System.out.println("3. Exit");
            System.out.println("===========================================");
            System.out.print("Select a choice: ");
            choice = Main.getValidChoice(scanner, 3);

            switch (choice) {
                case 1:
                    System.out.print("\nPlease Enter a new Phone Number: ");
                    while (true) {
                        String phoneString = scanner.nextLine();
                        if (phoneString.length() == 8 && phoneString.matches("\\d+")) {
                            phoneNumber = Integer.parseInt(phoneString);
                            System.out.println("Phone number changed to: " + phoneNumber);
                            break;
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
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
        medicalRecord.updatePersonalInfo(email, phoneNumber);
    }

    /**
     * Displays available appointment slots for scheduling.
     */
    public void viewAvailableAppointmentSlots() {
        patientAppointment.viewAvailableAppointmentSlots();
    }

    /**
     * Schedules a new appointment for the patient.
     *
     * @param scanner the Scanner object to receive user input
     */
    public void scheduleAppointment(Scanner scanner) {
        patientAppointment.scheduleAppointment(scanner);
    }

    /**
     * Reschedules an existing appointment for the patient.
     *
     * @param scanner the Scanner object to receive user input
     */
    public void rescheduleAppointment(Scanner scanner) {
        patientAppointment.rescheduleAppointment(scanner);
    }

    /**
     * Cancels an existing appointment for the patient.
     *
     * @param scanner the Scanner object to receive user input
     */
    public void cancelAppointment(Scanner scanner) {
        patientAppointment.cancelAppointment(scanner);
    }

    /**
     * Displays the list of scheduled appointments for the patient.
     */
    public void viewScheduledAppointments() {
        System.out.println("\n\n=========================================");
        System.out.println("     List of Scheduled Appointments");
        System.out.println("=========================================");
        patientAppointment.viewAppointmentStatus();
    }

    /**
     * Displays the outcome records of past appointments for the patient.
     */
    public void viewPastAppointmentOutcomeRecords() {
        patientAppointment.viewPastAppointmentOutcome();
    }
}