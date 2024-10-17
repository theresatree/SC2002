package sc2002;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PatientAppointment implements Appointment{
    private String patientID;
    private List<AvailableDatesToChoose> availableDatesToChoose;
    private List<AppointmentOutcomeRecord> appointmentOutcomeRecord;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");


    PatientAppointment(String patientID){
        this.patientID=patientID;
    }

    public void viewAvailableAppointmentSlots(){
        try{
            availableDatesToChoose = PatientAppointmentDB.getAvailableSlots();
        } catch (IOException e){
            e.printStackTrace();
        }

        if (availableDatesToChoose.isEmpty()) {
            System.out.println("No available dates found!\n\n");

        }
        System.out.println("\n\n=========================================");
        System.out.println("       Available Appointment Slots");
        System.out.println("=========================================");
        for (AvailableDatesToChoose dates : availableDatesToChoose) {
            dates.printString();
            System.out.println("=========================================");
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

    @Override
    public void viewAppointmentStatus(){}

    // public void rescheduleAppointment (appointment : Appointment, newDate : Date, newTimeSlot : TimeSlot){}

    @Override
    public void cancelAppointment(){};


    public void viewScheduledAppointments(){}


    @Override
    public void viewPastAppointmentOutcome(){
        try {
            appointmentOutcomeRecord = PatientAppointmentOutcomeDB.getAppointmentOutcome(this.patientID);
        }
        catch (Exception e) {
            // Handle the exception
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }

            StringBuilder appointmentOutcomeRecordList = new StringBuilder(); 
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



}
