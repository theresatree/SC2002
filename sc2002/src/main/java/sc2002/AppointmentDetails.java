package sc2002;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    AppointmentDetails(String patientID, String doctorID, int appointmentID, LocalDate date, LocalTime timeStart, LocalTime timeEnd, AppointmentStatus status){
        this.patientID=patientID;
        this.appointmentID = appointmentID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String printAppointmentDetails(){
        return ("Appointment ID: " + appointmentID + "\n" +
                "Patient ID: " + patientID + " (" + UserDB.getNameByHospitalID(patientID, Role.PATIENT) + ")" + "\n" +
                "Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")" + "\n" +
                "Date: " + date.format(dateFormat) + "\n" +
                "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                "Status: " + status + "\n");
    }
}

    