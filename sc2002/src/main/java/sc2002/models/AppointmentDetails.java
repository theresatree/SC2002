package sc2002.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import sc2002.enums.AppointmentStatus;
import sc2002.enums.Role;
import sc2002.repositories.UserDB;

/**
 * The AppointmentDetails class represents details of an appointment, including the patient,
 * doctor, appointment ID, date, time, and status.
 */
public class AppointmentDetails {
    
    private String patientID;
    private String doctorID;
    private int appointmentID;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private AppointmentStatus status;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs an AppointmentDetails object with specified attributes.
     *
     * @param patientID the ID of the patient
     * @param doctorID the ID of the doctor
     * @param appointmentID the unique ID of the appointment
     * @param date the date of the appointment
     * @param timeStart the start time of the appointment
     * @param timeEnd the end time of the appointment
     * @param status the status of the appointment
     */
    public AppointmentDetails(String patientID, String doctorID, int appointmentID, LocalDate date, LocalTime timeStart, LocalTime timeEnd, AppointmentStatus status) {
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    /**
     * Gets the appointment ID.
     *
     * @return the appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the patient ID.
     *
     * @return the patient ID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Gets the doctor ID.
     *
     * @return the doctor ID
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Gets the status of the appointment.
     *
     * @return the appointment status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Prints the details of the appointment in a formatted string.
     *
     * @return the formatted string of appointment details
     */
    public String printAppointmentDetails() {
        return ("Appointment ID: " + appointmentID + "\n" +
                "Patient ID: " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")" + "\n" +
                "Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")" + "\n" +
                "Date: " + date.format(dateFormat) + "\n" +
                "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                "Status: " + status + "\n");
    }
}