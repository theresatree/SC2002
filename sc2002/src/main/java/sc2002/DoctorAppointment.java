package sc2002;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DoctorAppointment implements Appointment {
    // Create a hashmap that makes each date to a List of timeslot
    Map<LocalDate, List<TimeSlot>> availableSlots;
    List<DoctorScheduledDates> scheduledDates;
    List<PatientScheduledAppointment> appointments;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Set the desired date format
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    private String doctorID;

    public DoctorAppointment(String doctorID) {
        availableSlots = new HashMap<>();
        this.doctorID=doctorID;
    }
//////////////////////////////This 5 functions are used together to generate and book dates////////////////
    // Function to generate from 9AM to 6PM daily
    private List<TimeSlot> generateDailySlots(LocalDate date) { // This date will help us map which day to print out.
        List<TimeSlot> dailySlots = new ArrayList<>();
        LocalTime start = LocalTime.of(9,0);
        LocalTime end = LocalTime.of(18,0);
        
        try {
            scheduledDates = DoctorAppointmentDB.getScheduledDates(doctorID, date); // Fetch scheduled dates from the database
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
            return new ArrayList<>(); // Return an empty list in case of an error
        }

        while (start.isBefore(end)){
            LocalTime endTime = start.plusHours(1); // End time is 1 hour after start time
            TimeSlot timeSlot = new TimeSlot(start, endTime); // Create a new TimeSlot to check first before adding

            // Check if the time slot matches any scheduled date's start and end time
            for (DoctorScheduledDates scheduled : scheduledDates) {
                if (scheduled.getDate().equals(date) && 
                    scheduled.getTimeStart().equals(start) && 
                    scheduled.getTimeEnd().equals(endTime)) {
                    timeSlot.isAvailable = false; // Mark as unavailable if it matches
                    break; // No need to check further
                }
            }

            dailySlots.add(timeSlot); // Add the timeSlot (whether available or not)
            start = start.plusHours(1); // Increment start time by 1 hour
        }
        return dailySlots;
    }

    // Function to use "generateDailySlots" to put it into viewable format"
    public void showAvailableSlotsForDate(LocalDate date, Scanner scanner) {
        List<TimeSlot> slots = generateDailySlots(date);
        availableSlots.put(date, slots); //Update the availabeSlots map

        System.out.println("\n\nAvailable Slots for " + date.format(dateFormat) + ":");
        System.out.println("=============================================");
        for (int i = 0; i < slots.size(); i++) {
            System.out.println((i + 1) + ". " + slots.get(i).getTimeSlotString());
        }
        System.out.println("=============================================");
        bookAvailableSlotForDate(slots, date, scanner); // Call the booking method
    }

    // Function to allow us to actually book the timeslot
    private void bookAvailableSlotForDate(List<TimeSlot> slots, LocalDate date, Scanner scanner) {
        int choice;

        while (true) {
            System.out.print("Choose a slot to book (or enter 0 to exit): ");
            choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Exiting booking process.\n\n");
                break; // Exit if the user chooses to exit
            }

            if (choice < 1 || choice > slots.size()) {
                System.out.println("Invalid choice. Please select a valid slot.\n");
                continue; // Prompt again if out of range
            }

            TimeSlot selectedSlot = slots.get(choice - 1);

            if (!selectedSlot.isAvailable) {
                System.out.println("This slot is already booked. Please choose another slot.\n");
            } else {
                try {
                    DoctorAppointmentDB.setDoctorSchedule(this.doctorID, date, selectedSlot.getStartTime(), selectedSlot.getEndTime());
                    selectedSlot.isAvailable = false; // Mark the slot as booked
                    System.out.println("Successfully booked the slot: " + selectedSlot.getStartTime().format(timeFormat) + " to " + selectedSlot.getEndTime().format(timeFormat) + " on " + date.format(dateFormat) + "\n\n");
                    break; // Exit after successful booking
                } catch (Exception e) {
                    System.out.println("Error occurred while booking the slot. Please try again.");
                    e.printStackTrace(); // Handle the exception as appropriate
                }
            }
        }
    }

    // Method to generate the next 7 days from the current date
    private List<LocalDate> generateNextSevenDays() {
        List<LocalDate> nextSevenDays = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            nextSevenDays.add(today.plusDays(i));
        }
        return nextSevenDays;
    }

    // Method to display the next 7 days for selection
    public void showSelectableDates(Scanner scanner) {
        List<LocalDate> selectableDates = generateNextSevenDays();

        System.out.println("\n\nPlease select a date from the following options:");
        System.out.println("================================================");
        for (int i = 0; i < selectableDates.size(); i++) {
            System.out.println((i + 1) + ". " + selectableDates.get(i).format(dateFormat));
        }
        System.out.println("================================================");

        //User input
        int choice;
        while (true) {
            System.out.print("Choose a date (or enter 0 to exit): ");
            
            if (scanner.hasNextInt()) { // Check if the input is an integer
                choice = scanner.nextInt();

                if (choice == 0) {
                    System.out.println("Exiting booking process.\n\n");
                    break; // Exit if the user chooses to exit
                }
                
                if (choice >= 1 && choice <= selectableDates.size()) {
                    LocalDate selectedDate = selectableDates.get(choice - 1); // Get the selected date
                    showAvailableSlotsForDate(selectedDate, scanner); // Show available slots for the chosen date
                    break; // Exit the loop if the choice is valid
                } else {
                    System.out.println("Invalid choice. Please select a valid date.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public void showPersonalSchedule(){
        try {
            scheduledDates = DoctorAppointmentDB.getAllPersonalSchedule(doctorID);

            if (scheduledDates.isEmpty()) {
                System.out.println("No personal schedule found!\n\n");
            }
            else{
                System.out.println("\n\n=========================================");
                System.out.println("       List of Personal Schedule");
                System.out.println("=========================================");
                for (DoctorScheduledDates dates : scheduledDates) {
                    if (dates.getStatus()!=AppointmentStatus.COMPLETED){ // Show only available and pending.
                        dates.printSchedule();
                        System.out.println("=========================================");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void viewAppointmentStatus(){
        try {
            appointments = DoctorAppointmentDB.doctorListOfAllAppointments(this.doctorID);
            List<PatientScheduledAppointment> confirmedAppointments = new ArrayList<>();

            for (PatientScheduledAppointment appointment : appointments) {
                if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
                    confirmedAppointments.add(appointment);
                }
            }
            if (confirmedAppointments.isEmpty()) {
                System.out.println("\n\n=========================================");
                System.out.println("         Scheduled Appointments");
                System.out.println("=========================================");
                System.out.println("        No scheduled dates found!");
                System.out.println("=========================================\n\n");
            }
            else{
                System.out.println("\n\n========================================");
                System.out.println("         Scheduled Appointments");
                System.out.println("========================================");
                for (PatientScheduledAppointment appointment : confirmedAppointments) {
                    appointment.printDoctorScheduledAppointment();
                    System.out.println("============================================");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
public void acceptDeclineAppointment(Scanner scanner) {
    int choice = -1;

    try {
        while (choice != 0) {
            // Refresh the list of appointments each time
            appointments = DoctorAppointmentDB.doctorListOfAllAppointments(this.doctorID);
            List<PatientScheduledAppointment> pendingAppointments = new ArrayList<>();

            for (PatientScheduledAppointment appointment : appointments) {
                if (appointment.getStatus() == AppointmentStatus.PENDING) {
                    pendingAppointments.add(appointment);
                }
            }
            
            if (pendingAppointments.isEmpty()) {
                System.out.println("\n\n=========================================");
                System.out.println("         Pending Appointments");
                System.out.println("=========================================");
                System.out.println("      No pending appointment found!");
                System.out.println("=========================================\n");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                scanner.nextLine(); // Clear the invalid input from the scanner
                break;
            } else {
                System.out.println("\n\n=========================================");
                System.out.println("         Pending Appointments");
                System.out.println("=========================================");
                for (PatientScheduledAppointment appointment : pendingAppointments) {
                    appointment.printDoctorScheduledAppointment();
                    System.out.println("=========================================");
                }
                System.out.println("Choose an appointment ID (or 0 to exit): ");
            }

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
            for (PatientScheduledAppointment selected : pendingAppointments) {
                if (selected.getAppointmentID() == choice) {
                    selectedAppointment = selected;
                    break;
                }
            }

            if (selectedAppointment == null) {
                System.out.println("\nInvalid! Please select a valid appointment ID.");
            } else {
                int proceed = -1;
                while (proceed != 0) {
                    System.out.println("\n\n=========================================");
                    System.out.println("          You have selected:");
                    System.out.println("      " + selectedAppointment.getDate().format(dateFormat) + " - " + 
                                       selectedAppointment.getTimeStart().format(timeFormat) + " to " + 
                                       selectedAppointment.getTimeEnd().format(timeFormat));
                    System.out.println("=========================================");
                    System.out.println("1. Accept");
                    System.out.println("2. Decline");
                    System.out.println("=========================================");
                    System.out.println("Choose an option (or enter 0 to exit): ");
                    proceed = scanner.nextInt();

                    switch (proceed) {
                        case 1:
                            updateAppointment(scanner, selectedAppointment.getAppointmentID(), this.doctorID, selectedAppointment.getPatientID(), true);
                            System.out.println("\nAppointment ID " +selectedAppointment.getAppointmentID() + " Accpeted!");
                            proceed = 0;
                            break; 
                        case 2:
                            updateAppointment(scanner, selectedAppointment.getAppointmentID(), this.doctorID, selectedAppointment.getPatientID(), false);
                            System.out.println("\nAppointment ID " +selectedAppointment.getAppointmentID() + " Declined!");
                            proceed = 0;
                            break;
                        case 0:
                            System.out.println("\nSelection cancelled.");
                            break;
                        default:
                            System.out.println("\nPlease choose a valid option!");
                            break;
                    }
                }
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while processing appointments: " + e.getMessage());
        e.printStackTrace();
    }
}

private void updateAppointment(Scanner scanner, int appointmentID, String doctorID, String patientID, boolean accept) {
    try {
        DoctorAppointmentDB.acceptDeclineAppointment(doctorID, appointmentID, patientID, accept);
        System.out.println("\nUpdating Appointments. Please wait...");
        for (int i = 5; i > 0; i--) {
            System.out.print("*");
            try {
                Thread.sleep(200); // Sleep for 0.2 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("\nUpdate process interrupted.");
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while updating the appointment: " + e.getMessage());
        e.printStackTrace();
    }
}
}
