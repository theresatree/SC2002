package sc2002.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import sc2002.enums.AppointmentStatus;

/**
 * The DoctorScheduledDates class represents scheduled dates for a doctor,
 * including date, time, and status.
 */
public class DoctorScheduledDates {
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String doctorID;
    private AppointmentStatus status;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs a DoctorScheduledDates object with specified details.
     *
     * @param doctorId the ID of the doctor
     * @param date the date of the schedule
     * @param timeStart the start time of the schedule
     * @param timeEnd the end time of the schedule
     * @param status the status of the appointment
     */
    public DoctorScheduledDates(String doctorId, LocalDate date, LocalTime timeStart, LocalTime timeEnd, AppointmentStatus status) {
        this.doctorID = doctorId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    /**
     * Prints the details of the scheduled date.
     */
    public void printSchedule() {
        System.out.println("Date: " + date.format(dateFormat) + "  Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                           "Status: " + status);
    }

    /**
     * Gets the scheduled date.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the start time of the schedule.
     *
     * @return the start time
     */
    public LocalTime getTimeStart() {
        return timeStart;
    }

    /**
     * Gets the end time of the schedule.
     *
     * @return the end time
     */
    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    /**
     * Gets the doctor's ID.
     *
     * @return the doctor's ID
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Gets the appointment status.
     *
     * @return the appointment status
     */
    public AppointmentStatus getStatus() {
        return status;
    }
}