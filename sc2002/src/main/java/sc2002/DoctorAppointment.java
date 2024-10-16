package sc2002;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DoctorAppointment implements Appointment {
    // Create a hashmap that makes each date to a List of timeslot
    Map<LocalDate, List<TimeSlot>> availableSlots;
    List<DoctorScheduledDates> scheduledDates;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Set the desired date format

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
            scheduledDates = DoctorScheduledDatesDB.getScheduledDates(doctorID, date); // Fetch scheduled dates from the database
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

        System.out.println("\n\nAvailable Slots for " + date.format(formatter) + ":");
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
                    DoctorScheduledDatesDB.setDoctorSchedule(this.doctorID, date, selectedSlot.getStartTime(), selectedSlot.getEndTime());
                    selectedSlot.isAvailable = false; // Mark the slot as booked
                    System.out.println("Successfully booked the slot: " + selectedSlot.getStartTime() + " to " + selectedSlot.getEndTime() + " on " + date.format(formatter) + "\n\n");
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
            System.out.println((i + 1) + ". " + selectableDates.get(i).format(formatter));
        }
        System.out.println("================================================");

        //User input
        int choice;
        while (true) {
            System.out.print("Enter a number corresponsing to the date: ");
            
            if (scanner.hasNextInt()) { // Check if the input is an integer
                choice = scanner.nextInt();
                
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
    @Override
    public void viewAppointmentStatus(){}

    @Override
    public void cancelAppointment(){};

    @Override
    public void viewPastAppointmentOutcome(){}

}
