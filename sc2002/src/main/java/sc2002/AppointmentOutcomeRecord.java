package sc2002;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AppointmentOutcomeRecord {

    private String patientID;
    private String doctorID;
    private int appointmentID;
    private LocalDate dateOfAppointment;
    private Service service;
    private Medicine medications; 
    private PrescriptionStatus prescriptionStatus;
    private String consultationNotes;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    AppointmentOutcomeRecord(String patientID, String doctorID, int appointmentID, LocalDate dateOfAppointment, Service service, Medicine medications, PrescriptionStatus prescriptionStatus, String consultationNotes){
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentID = appointmentID;
        this.dateOfAppointment = dateOfAppointment;
        this.service = service;
        this.medications = medications;
        this.prescriptionStatus=prescriptionStatus;
        this.consultationNotes=consultationNotes;
    }

    public String printAppointmentOutcome(){
        return(
            "Appointment ID: " + appointmentID + "\n" +
            "By Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")" +"\n" +
            "Date and Time: " + dateOfAppointment.format(dateFormat) + "\n" +
            "Services given: " + service + "\n" +
            "Medication given: " + medications + "\n" +
            "Prescription Status: " + prescriptionStatus + "\n" +
            "Notes: " + consultationNotes);
    }

}
