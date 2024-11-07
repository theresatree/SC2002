package sc2002.models;

import sc2002.enums.AppointmentStatus;
import sc2002.enums.Role;
import sc2002.repositories.UserDB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a scheduled appointment between a patient and a doctor, including date, time, and status.
 */
public class PatientScheduledAppointment {
    private int appointmentID;
    private String patientID;
    private String doctorID;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private AppointmentStatus status;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs a PatientScheduledAppointment object with the specified details.
     *
     * @param patientID The ID of the patient.
     * @param appointmentID The ID of the appointment.
     * @param doctorID The ID of the doctor.
     * @param date The date of the appointment.
     * @param timeStart The start time of the appointment.
     * @param timeEnd The end time of the appointment.
     * @param status The status of the appointment.
     */
    public PatientScheduledAppointment(String patientID, int appointmentID, String doctorID, LocalDate date, LocalTime timeStart, LocalTime timeEnd, AppointmentStatus status) {
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    /**
     * Prints the scheduled appointment details for the patient, including doctor information.
     */
    public void printScheduledAppointment() {
        System.out.println("Appointment ID: " + appointmentID + "\n" +
                           "Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")" + "\n" +
                           "Date: " + date.format(dateFormat) + "\n" +
                           "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                           "Status: " + status);
    }

    /**
     * Prints the scheduled appointment details for the doctor, including patient information.
     */
    public void printDoctorScheduledAppointment() {
        System.out.println("Appointment ID: " + appointmentID + "\n" +
                            "Patient ID: " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")" + "\n" +
                            "Date: " + date.format(dateFormat) + "\n" +
                            "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                            "Status: " + status);
    }

    /**
     * Gets the appointment ID.
     *
     * @return The appointment ID.
     */
    public int getAppointmentID() {
        return this.appointmentID;
    }

    /**
     * Gets the date of the appointment.
     *
     * @return The date of the appointment.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Gets the start time of the appointment.
     *
     * @return The start time of the appointment.
     */
    public LocalTime getTimeStart() {
        return this.timeStart;
    }

    /**
     * Gets the end time of the appointment.
     *
     * @return The end time of the appointment.
     */
    public LocalTime getTimeEnd() {
        return this.timeEnd;
    }

    /**
     * Gets the status of the appointment.
     *
     * @return The status of the appointment.
     */
    public AppointmentStatus getStatus() {
        return this.status;
    }

    /**
     * Gets the patient ID associated with the appointment.
     *
     * @return The patient ID.
     */
    public String getPatientID() {
        return this.patientID;
    }

    /**
     * Sets the status of the appointment.
     *
     * @param status The new status of the appointment.
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}