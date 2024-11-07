package sc2002.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import sc2002.enums.Role;
import sc2002.repositories.UserDB;

/**
 * Represents the available dates and times for appointments with a specific doctor.
 */
public class AvailableDatesToChoose {
    
    private String doctorID;
    private int appointmentID;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs an AvailableDatesToChoose instance with the specified doctor and appointment details.
     * 
     * @param doctorID The ID of the doctor.
     * @param appointmentID The ID of the appointment.
     * @param date The date of the appointment.
     * @param timeStart The start time of the appointment.
     * @param timeEnd The end time of the appointment.
     */
    public AvailableDatesToChoose(String doctorID, int appointmentID, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.doctorID = doctorID;
        this.appointmentID = appointmentID;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    /**
     * Prints the appointment details, including date, time, and doctor information.
     */
    public void printString() {
        System.out.println("Appointment ID: " + appointmentID + "\n" +
                            "Date: " + date.format(dateFormat) + "\n" +
                            "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                            "By Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")");
    }

    /**
     * Gets the date of the appointment.
     * 
     * @return The appointment date.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Gets the start time of the appointment.
     * 
     * @return The appointment start time.
     */
    public LocalTime getTimeStart() {
        return this.timeStart;
    }

    /**
     * Gets the end time of the appointment.
     * 
     * @return The appointment end time.
     */
    public LocalTime getTimeEnd() {
        return this.timeEnd;
    }

    /**
     * Gets the appointment ID.
     * 
     * @return The appointment ID.
     */
    public int getAppointmentID() {
        return this.appointmentID;
    }
}