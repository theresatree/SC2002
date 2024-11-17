package sc2002.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import sc2002.enums.PrescriptionStatus;
import sc2002.enums.Role;
import sc2002.enums.Service;
import sc2002.repositories.UserDB;

/**
 * Represents the record of an appointment outcome, including details of the patient, doctor, 
 * services provided, and medications.
 */
public class AppointmentOutcomeRecord {

    private String patientID;
    private String doctorID;
    private int appointmentID;
    private LocalDate dateOfAppointment;
    private Service service;
    private String medications; 
    private PrescriptionStatus prescriptionStatus;
    private String consultationNotes;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructs an AppointmentOutcomeRecord with the specified details.
     * 
     * @param patientID          The patient's ID.
     * @param doctorID           The doctor's ID.
     * @param appointmentID      The appointment ID.
     * @param dateOfAppointment  The date of the appointment.
     * @param service            The service provided during the appointment.
     * @param medications        The medications prescribed.
     * @param prescriptionStatus The status of the prescription.
     * @param consultationNotes  Notes from the consultation.
     */
    public AppointmentOutcomeRecord(String patientID, String doctorID, int appointmentID, LocalDate dateOfAppointment, 
                                    Service service, String medications, PrescriptionStatus prescriptionStatus, String consultationNotes) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentID = appointmentID;
        this.dateOfAppointment = dateOfAppointment;
        this.service = service;
        this.medications = medications;
        this.prescriptionStatus = prescriptionStatus;
        this.consultationNotes = consultationNotes;
    }

    /**
     * Retrieves the appointment ID.
     * 
     * @return The appointment ID.
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Retrieves the patient ID.
     * 
     * @return The patient ID.
     */
    public String getPatientID() {
        return patientID;
    }
    
    /**
     * Retrieves the doctor ID.
     * 
     * @return The doctor ID.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Retrieves the date of the appointment.
     * 
     * @return The date of the appointment.
     */
    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    /**
     * Retrieves the service provided during the appointment.
     * 
     * @return The service provided.
     */
    public Service getService() {
        return service;
    }

    /**
     * Retrieves the medications prescribed during the appointment.
     * 
     * @return The prescribed medications.
     */
    public String getMedications() {
        return medications;
    }
    
    /**
     * Retrieves the status of the prescription.
     * 
     * @return The prescription status.
     */
    public PrescriptionStatus getPrescriptionStatus() {
        return prescriptionStatus;
    }
    
    /**
     * Retrieves the consultation notes.
     * 
     * @return The consultation notes.
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Prints the outcome of the appointment, including details about the doctor, services, 
     * medications, and notes.
     * 
     * @return A string representation of the appointment outcome.
     */
    public String printAppointmentOutcome() {
        return (
            "Appointment ID: " + appointmentID + "\n" +
            "By Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")" + "\n" +
            "Date and Time: " + dateOfAppointment.format(dateFormat) + "\n" +
            "Services given: " + service + "\n" +
            "Medication given: " + medications + "\n" +
            "Prescription Status: " + prescriptionStatus + "\n" +
            "Notes: " + consultationNotes
        );
    }

    /**
     * Updates the prescription status with a new status.
     * 
     * @param newStatus The new prescription status.
     */
    public void setPrescriptionStatus(PrescriptionStatus newStatus) {
        this.prescriptionStatus = newStatus;
    }
}