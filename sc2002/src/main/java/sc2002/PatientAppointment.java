package sc2002;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PatientAppointment implements Appointment{
    private String patientID;
    private List<AvailableDatesToChoose> availableDatesToChoose;
    private List<AppointmentOutcomeRecord> appointmentOutcomeRecord;
    private List<PatientScheduledAppointment> patientScheduledAppointments;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");


    PatientAppointment(String patientID){
        this.patientID=patientID;
    }

    public void viewAvailableAppointmentSlots(){
        try{
            availableDatesToChoose = PatientAppointmentDB.getAvailableSlots();

        if (availableDatesToChoose.isEmpty()) {
            System.out.println("\n\n=========================================");
            System.out.println("       Available Appointment Slots");
            System.out.println("=========================================");
            System.out.println("        No available dates found!");
            System.out.println("=========================================\n\n");
        }
        else{
            System.out.println("\n\n=========================================");
            System.out.println("       Available Appointment Slots");
            System.out.println("=========================================");
            for (AvailableDatesToChoose dates : availableDatesToChoose) {
                dates.printString();
                System.out.println("=========================================");
            }
        }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void scheduleAppointment(Scanner scanner){
        int choice=-1;
        char proceed;
        viewAvailableAppointmentSlots();
        while (choice!=0){
            System.out.println("Choose an appointment ID (or 0 to exit): ");
            choice=scanner.nextInt();
            if (choice == 0) {
                System.out.println("\nExiting booking process.\n\n");
                break;
            }

            // Search for the selected appointment ID in the availableDatesToChoose list
            AvailableDatesToChoose selectedSlot = null;
            for (AvailableDatesToChoose slot : availableDatesToChoose) {
                if (slot.getAppointmentID() == choice) {  // Check if the appointment ID matches
                    selectedSlot = slot;  // If found, set the selectedSlot
                    break;
                }
            }
            if (selectedSlot==null) {
                System.out.println("\nInvalid! Please select a valid appointment ID.");
            }
            else{
                System.out.println("\n\n=========================================");
                System.out.println("          You have selected:");
                System.out.println("      " +selectedSlot.getDate().format(dateFormat) + " - " + selectedSlot.getTimeStart().format(timeFormat) + " to " + selectedSlot.getTimeEnd().format(timeFormat));
                System.out.println("=========================================");
                System.out.println("         Schedule Appointment?");
                System.out.println("=========================================");
                System.out.println("Enter 'y' to confirm schdule: ");
                proceed = scanner.next().toUpperCase().charAt(0);

                // If the user confirms booking
                if (proceed == 'Y') {
                    try {
                        PatientAppointmentDB.updateScheduleForPatients(selectedSlot.getAppointmentID(), this.patientID);
                        System.out.println("\nAppointment scheduled successfully!\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break; // Exit loop after successful booking
                } else {
                    System.out.println("\nBooking cancelled.");
                    viewAvailableAppointmentSlots();
                }
            }
        }

    }

    public void rescheduleAppointment (Scanner scanner){
        int choice=-1;
        char proceed;

        while (choice!=0){
            System.out.println("\n\n=========================================");
            System.out.println("   Choose an appointment to reschedule");
            System.out.println("=========================================");
            viewAppointmentStatus();
            System.out.println("Choose an appointment ID (or 0 to exit): ");

            // This try-catch is to catch if user input something else other than numbers.
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input! Please enter a valid appointment ID.\n");
                scanner.nextLine(); // Clear the invalid input from the scanner
                continue; // Skip the rest of the loop and ask for input again
            }

            if (choice == 0) {
                System.out.println("\nExiting booking process.\n\n");
                break;
            }

            // Search for the selected appointment ID in the availableDatesToChoose list
            PatientScheduledAppointment selectedAppointment = null;
            for (PatientScheduledAppointment selected : patientScheduledAppointments) {
                if (selected.getAppointmentID() == choice) {  // Check if the appointment ID matches
                    selectedAppointment = selected;  // If found, set the selectedSlot
                    break;
                }
            }

            if (selectedAppointment==null) {
                System.out.println("\nInvalid! Please select a valid appointment ID.");
            }
            else{
                System.out.println("\n\n=========================================");
                System.out.println("          You have selected:");
                System.out.println("      " +selectedAppointment.getDate().format(dateFormat) + " - " + selectedAppointment.getTimeStart().format(timeFormat) + " to " + selectedAppointment.getTimeEnd().format(timeFormat));
                System.out.println("=========================================");
                System.out.println("         Reschedule Appointment?");
                System.out.println("=========================================");
                System.out.println("Enter 'y' to reschdule: ");
                proceed = scanner.next().toUpperCase().charAt(0);

                // If the user confirms booking
                if (proceed == 'Y') {
                    try {
                        PatientAppointmentDB.reschedulePatientAppointment(selectedAppointment.getAppointmentID(), this.patientID);
                        scheduleAppointment(scanner);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break; // Exit loop after successful booking
                } else {
                    System.out.println("\nBooking cancelled.");
                }
            }
        }
    }

    public void cancelAppointment(Scanner scanner){
        int choice=-1;
        char proceed;

        while (choice!=0){
            System.out.println("\n\n=========================================");
            System.out.println("     Choose an appointment to cancel");
            System.out.println("=========================================");
            viewAppointmentStatus();
            System.out.println("Choose an appointment ID (or 0 to exit): ");

            // This try-catch is to catch if user input something else other than numbers.
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input! Please enter a valid appointment ID.\n");
                scanner.nextLine(); // Clear the invalid input from the scanner
                continue; // Skip the rest of the loop and ask for input again
            }

            if (choice == 0) {
                System.out.println("\nExiting cancelling process.\n\n");
                break;
            }

            // Search for the selected appointment ID in the availableDatesToChoose list
            PatientScheduledAppointment selectedAppointment = null;
            for (PatientScheduledAppointment selected : patientScheduledAppointments) {
                if (selected.getAppointmentID() == choice) {  // Check if the appointment ID matches
                    selectedAppointment = selected;  // If found, set the selectedSlot
                    break;
                }
            }

            if (selectedAppointment==null) {
                System.out.println("\nInvalid! Please select a valid appointment ID.");
            }
            else{
                System.out.println("\n\n=========================================");
                System.out.println("          You have selected:");
                System.out.println("      " +selectedAppointment.getDate().format(dateFormat) + " - " + selectedAppointment.getTimeStart().format(timeFormat) + " to " + selectedAppointment.getTimeEnd().format(timeFormat));
                System.out.println("=========================================");
                System.out.println("         Cancel Appointment?");
                System.out.println("=========================================");
                System.out.println("Enter 'y' to cancel: ");
                proceed = scanner.next().toUpperCase().charAt(0);

                // If the user confirms booking
                if (proceed == 'Y') {
                    try {
                        PatientAppointmentDB.reschedulePatientAppointment(selectedAppointment.getAppointmentID(), this.patientID);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break; // Exit loop after successful booking
                } else {
                    System.out.println("\nCancellation cancelled.");
                }
            }
        }
    }

    @Override
    public void viewAppointmentStatus(){
        try {
            patientScheduledAppointments = PatientAppointmentDB.patientScheduledAppointments(this.patientID);
            if (!patientScheduledAppointments.isEmpty()){
                for (PatientScheduledAppointment schedule : patientScheduledAppointments) {
                    schedule.printScheduledAppointment();
                    System.out.println("=========================================");
                }
            }
            else{
                System.out.println("     No Scheduled Appointment Found!");
                System.out.println("=========================================");
            }   
        }
        catch (Exception e) {
            // Handle the exception
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }
    }

    public void viewPastAppointmentOutcome(){
        try {
            appointmentOutcomeRecord = PatientAppointmentOutcomeDB.getAppointmentOutcome(this.patientID);
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
            // Handle the exception
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }
    }

}
