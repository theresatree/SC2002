package sc2002.models;

import sc2002.enums.AppointmentStatus;
import sc2002.repositories.DoctorAppointmentDB;

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

/**
 * Manages a doctor's appointment schedules, including booking, viewing, and updating slots.
 */
public class DoctorAppointment implements Appointment {
    Map<LocalDate, List<TimeSlot>> availableSlots;
    List<DoctorScheduledDates> scheduledDates;
    List<PatientScheduledAppointment> appointments;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    private String doctorID;

    public DoctorAppointment(String doctorID) {
        availableSlots = new HashMap<>();
        this.doctorID = doctorID;
    }

    /**
     * Generates a list of hourly timeslots from 9 AM to 6 PM for a specified date.
     * 
     * @param date The date for which to generate daily slots.
     * @return A list of available and booked timeslots for the specified date.
     */
    private List<TimeSlot> generateDailySlots(LocalDate date) {
        List<TimeSlot> dailySlots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);
        
        try {
            scheduledDates = DoctorAppointmentDB.getScheduledDates(doctorID, date);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        while (start.isBefore(end)) {
            LocalTime endTime = start.plusHours(1);
            TimeSlot timeSlot = new TimeSlot(start, endTime);

            for (DoctorScheduledDates scheduled : scheduledDates) {
                if (scheduled.getDate().equals(date) && 
                    scheduled.getTimeStart().equals(start) && 
                    scheduled.getTimeEnd().equals(endTime)) {
                    timeSlot.isAvailable = false;
                    break;
                }
            }
            dailySlots.add(timeSlot);
            start = start.plusHours(1);
        }
        return dailySlots;
    }

    /**
     * Displays available slots for a specific date and allows booking of slots.
     *
     * @param date    The date for which to show available slots.
     * @param scanner Scanner for capturing user input.
     */
    public void showAvailableSlotsForDate(LocalDate date, Scanner scanner) {
        List<TimeSlot> slots = generateDailySlots(date);
        availableSlots.put(date, slots);

        System.out.println("\n\nAvailable Slots for " + date.format(dateFormat) + ":");
        System.out.println("=============================================");
        for (int i = 0; i < slots.size(); i++) {
            System.out.println((i + 1) + ". " + slots.get(i).getTimeSlotString());
        }
        System.out.println("=============================================");
        bookAvailableSlotForDate(slots, date, scanner);
    }

    /**
     * Allows the doctor to book a specific available timeslot.
     *
     * @param slots   List of timeslots available for booking.
     * @param date    The date of the slot to be booked.
     * @param scanner Scanner for capturing user input.
     */
    private void bookAvailableSlotForDate(List<TimeSlot> slots, LocalDate date, Scanner scanner) {
        int choice;
        while (true) {
            System.out.print("Choose a slot to book (or enter 0 to exit): ");
            choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Exiting booking process.\n\n");
                break;
            }

            if (choice < 1 || choice > slots.size()) {
                System.out.println("Invalid choice. Please select a valid slot.\n");
                continue;
            }

            TimeSlot selectedSlot = slots.get(choice - 1);
            if (!selectedSlot.isAvailable) {
                System.out.println("This slot is already booked. Please choose another slot.\n");
            } else {
                try {
                    DoctorAppointmentDB.setDoctorSchedule(this.doctorID, date, selectedSlot.getStartTime(), selectedSlot.getEndTime());
                    selectedSlot.isAvailable = false;
                    System.out.println("Successfully booked the slot: " + selectedSlot.getStartTime().format(timeFormat) + " to " + selectedSlot.getEndTime().format(timeFormat) + " on " + date.format(dateFormat) + "\n\n");
                    break;
                } catch (Exception e) {
                    System.out.println("Error occurred while booking the slot. Please try again.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Generates a list of the next seven days starting from today.
     *
     * @return A list of LocalDate representing the next seven days.
     */
    private List<LocalDate> generateNextSevenDays() {
        List<LocalDate> nextSevenDays = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            nextSevenDays.add(today.plusDays(i));
        }
        return nextSevenDays;
    }

    /**
     * Displays a list of the next 7 days and allows selection of a date to view available slots.
     *
     * @param scanner Scanner for capturing user input.
     */
    public void showSelectableDates(Scanner scanner) {
        List<LocalDate> selectableDates = generateNextSevenDays();

        System.out.println("\n\nPlease select a date from the following options:");
        System.out.println("================================================");
        for (int i = 0; i < selectableDates.size(); i++) {
            System.out.println((i + 1) + ". " + selectableDates.get(i).format(dateFormat));
        }
        System.out.println("================================================");

        int choice;
        while (true) {
            System.out.print("Choose a date (or enter 0 to exit): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == 0) {
                    System.out.println("Exiting booking process.\n\n");
                    break;
                }
                if (choice >= 1 && choice <= selectableDates.size()) {
                    LocalDate selectedDate = selectableDates.get(choice - 1);
                    showAvailableSlotsForDate(selectedDate, scanner);
                    break;
                } else {
                    System.out.println("Invalid choice. Please select a valid date.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    /**
     * Displays the doctor's personal schedule of appointments, excluding completed appointments.
     */
    public void showPersonalSchedule() {
        try {
            scheduledDates = DoctorAppointmentDB.getAllPersonalSchedule(doctorID);
            if (scheduledDates.isEmpty()) {
                System.out.println("No personal schedule found!\n\n");
            } else {
                System.out.println("\n\n=========================================");
                System.out.println("       List of Personal Schedule");
                System.out.println("=========================================");
                for (DoctorScheduledDates dates : scheduledDates) {
                    if (dates.getStatus() != AppointmentStatus.COMPLETED) {
                        dates.printSchedule();
                        System.out.println("=========================================");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Views the status of all confirmed appointments for the doctor.
     */
    @Override
    public void viewAppointmentStatus() {
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
            } else {
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

    /**
     * Allows the doctor to accept or decline pending appointments.
     *
     * @param scanner Scanner for capturing user input.
     */
    public void acceptDeclineAppointment(Scanner scanner) {
        int choice = -1;
        try {
            while (choice != 0) {
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
                    scanner.nextLine();
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
                    scanner.nextLine();
                    continue;
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
                                updateAppointment(selectedAppointment.getAppointmentID(), this.doctorID, selectedAppointment.getPatientID(), true);
                                System.out.println("\nAppointment ID " + selectedAppointment.getAppointmentID() + " Accepted!");
                                proceed = 0;
                                break; 
                            case 2:
                                updateAppointment(selectedAppointment.getAppointmentID(), this.doctorID, selectedAppointment.getPatientID(), false);
                                System.out.println("\nAppointment ID " + selectedAppointment.getAppointmentID() + " Declined!");
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

    /**
     * Updates the status of an appointment by accepting or declining it.
     *
     * @param appointmentID The ID of the appointment to update.
     * @param doctorID      The ID of the doctor.
     * @param patientID     The ID of the patient.
     * @param accept        True if accepting, false if declining the appointment.
     */
    private void updateAppointment(int appointmentID, String doctorID, String patientID, boolean accept) {
        try {
            DoctorAppointmentDB.acceptDeclineAppointment(doctorID, appointmentID, patientID, accept);
            System.out.println("\nUpdating Appointments. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200);
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