package sc2002.models;

import sc2002.repositories.PatientAppointmentDB;
import sc2002.repositories.PatientAppointmentOutcomeDB;
import sc2002.services.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Manages appointments for a patient, including scheduling, rescheduling, cancelling,
 * and viewing available slots and past outcomes.
 */
public class PatientAppointment implements Appointment {
    private String patientID;
    private List<AvailableDatesToChoose> availableDatesToChoose;
    private List<AppointmentOutcomeRecord> appointmentOutcomeRecord;
    private List<PatientScheduledAppointment> patientScheduledAppointments;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs a PatientAppointment object with a specified patient ID.
     *
     * @param patientID The ID of the patient.
     */
    public PatientAppointment(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Displays available appointment slots for the patient.
     */
    public void viewAvailableAppointmentSlots() {
        try {
            availableDatesToChoose = PatientAppointmentDB.getAvailableSlots();
            if (availableDatesToChoose.isEmpty()) {
                System.out.println("\n\n=========================================");
                System.out.println("       Available Appointment Slots");
                System.out.println("=========================================");
                System.out.println("        No available dates found!");
                System.out.println("=========================================\n\n");
            } else {
                System.out.println("\n\n=========================================");
                System.out.println("       Available Appointment Slots");
                System.out.println("=========================================");
                for (AvailableDatesToChoose dates : availableDatesToChoose) {
                    dates.printString();
                    System.out.println("=========================================");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedules an appointment by allowing the user to select a slot.
     *
     * @param scanner A Scanner object for user input.
     */
    public void scheduleAppointment(Scanner scanner) {
        int choice = -1;
        char proceed;
        viewAvailableAppointmentSlots();
        while (choice != 0) {
            System.out.println("Choose an appointment ID (or 0 to exit): ");
            choice = scanner.nextInt();
            if (choice == 0) {
                System.out.println("\nExiting booking process.\n\n");
                break;
            }

            AvailableDatesToChoose selectedSlot = null;
            for (AvailableDatesToChoose slot : availableDatesToChoose) {
                if (slot.getAppointmentID() == choice) {
                    selectedSlot = slot;
                    break;
                }
            }
            if (selectedSlot == null) {
                System.out.println("\nInvalid! Please select a valid appointment ID.");
            } else {
                System.out.println("\n\n=========================================");
                System.out.println("          You have selected:");
                System.out.println("      " + selectedSlot.getDate().format(dateFormat) + " - " +
                                   selectedSlot.getTimeStart().format(timeFormat) + " to " +
                                   selectedSlot.getTimeEnd().format(timeFormat));
                System.out.println("=========================================");
                System.out.println("         Schedule Appointment?");
                System.out.println("=========================================");
                System.out.println("Enter 'y' to confirm schedule: ");
                proceed = scanner.next().toUpperCase().charAt(0);

                if (proceed == 'Y') {
                    try {
                        PatientAppointmentDB.updateScheduleForPatients(selectedSlot.getAppointmentID(), this.patientID);
                        System.out.println("\nAppointment scheduled successfully!\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    System.out.println("\nBooking cancelled.");
                    viewAvailableAppointmentSlots();
                }
            }
        }
    }

    /**
     * Reschedules an existing appointment by selecting a new time slot.
     *
     * @param scanner A Scanner object for user input.
     */
    public void rescheduleAppointment(Scanner scanner) {
        int choice = -1;
        char proceed;

        while (choice != 0) {
            System.out.println("\n\n=========================================");
            System.out.println("   Choose an appointment to reschedule");
            System.out.println("=========================================");
            viewAppointmentStatus();
            System.out.println("Choose an appointment ID (or 0 to exit): ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input! Please enter a valid appointment ID.\n");
                scanner.nextLine();
                continue;
            }

            if (choice == 0) {
                System.out.println("\nExiting booking process.\n\n");
                break;
            }

            PatientScheduledAppointment selectedAppointment = null;
            for (PatientScheduledAppointment selected : patientScheduledAppointments) {
                if (selected.getAppointmentID() == choice) {
                    selectedAppointment = selected;
                    break;
                }
            }

            if (selectedAppointment == null) {
                System.out.println("\nInvalid! Please select a valid appointment ID.");
            } else {
                System.out.println("\n\n=========================================");
                System.out.println("          You have selected:");
                System.out.println("      " + selectedAppointment.getDate().format(dateFormat) + " - " +
                                   selectedAppointment.getTimeStart().format(timeFormat) + " to " +
                                   selectedAppointment.getTimeEnd().format(timeFormat));
                System.out.println("=========================================");
                System.out.println("         Reschedule Appointment?");
                System.out.println("=========================================");
                System.out.println("Enter 'y' to reschedule: ");
                proceed = scanner.next().toUpperCase().charAt(0);

                if (proceed == 'Y') {
                    try {
                        PatientAppointmentDB.reschedulePatientAppointment(selectedAppointment.getAppointmentID(), this.patientID);
                        scheduleAppointment(scanner);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    System.out.println("\nBooking cancelled.");
                }
            }
        }
    }

    /**
     * Cancels an existing appointment by selecting the appointment ID.
     *
     * @param scanner A Scanner object for user input.
     */
    public void cancelAppointment(Scanner scanner) {
        int choice = -1;
        char proceed;

        while (choice != 0) {
            System.out.println("\n\n=========================================");
            System.out.println("     Choose an appointment to cancel");
            System.out.println("=========================================");
            viewAppointmentStatus();
            System.out.println("Choose an appointment ID (or 0 to exit): ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input! Please enter a valid appointment ID.\n");
                scanner.nextLine();
                continue;
            }

            if (choice == 0) {
                System.out.println("\nExiting cancelling process.\n\n");
                break;
            }

            PatientScheduledAppointment selectedAppointment = null;
            for (PatientScheduledAppointment selected : patientScheduledAppointments) {
                if (selected.getAppointmentID() == choice) {
                    selectedAppointment = selected;
                    break;
                }
            }

            if (selectedAppointment == null) {
                System.out.println("\nInvalid! Please select a valid appointment ID.");
            } else {
                System.out.println("\n\n=========================================");
                System.out.println("          You have selected:");
                System.out.println("      " + selectedAppointment.getDate().format(dateFormat) + " - " +
                                   selectedAppointment.getTimeStart().format(timeFormat) + " to " +
                                   selectedAppointment.getTimeEnd().format(timeFormat));
                System.out.println("=========================================");
                System.out.println("         Cancel Appointment?");
                System.out.println("=========================================");
                System.out.println("Enter 'y' to cancel: ");
                proceed = scanner.next().toUpperCase().charAt(0);

                if (proceed == 'Y') {
                    try {
                        PatientAppointmentDB.reschedulePatientAppointment(selectedAppointment.getAppointmentID(), this.patientID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    System.out.println("\nCancellation cancelled.");
                }
            }
        }
    }

    /**
     * Views the status of all scheduled appointments for the patient.
     */
    @Override
    public void viewAppointmentStatus() {
        try {
            patientScheduledAppointments = PatientAppointmentDB.patientScheduledAppointments(this.patientID);
            if (!patientScheduledAppointments.isEmpty()) {
                for (PatientScheduledAppointment schedule : patientScheduledAppointments) {
                    schedule.printScheduledAppointment();
                    System.out.println("=========================================");
                }
            } else {
                System.out.println("     No Scheduled Appointment Found!");
                System.out.println("=========================================");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }
    }

    /**
     * Views the outcomes of past appointments for the patient.
     */
    public void viewPastAppointmentOutcome() {
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
        } catch (Exception e) {
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }
    }
}