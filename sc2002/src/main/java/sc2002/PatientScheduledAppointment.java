package sc2002;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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


    PatientScheduledAppointment(String patientID, int appointmentID, String doctorID, LocalDate date, LocalTime timeStart, LocalTime timeEnd, AppointmentStatus status){
        this.patientID=patientID;
        this.appointmentID = appointmentID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    public void printScheduledAppointment(){
        System.out.println("Appointment ID: " + appointmentID + "\n" +
                           "Doctor ID: " + doctorID + "\n" +
                           "Date: " + date.format(dateFormat) + "\n" +
                           "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                           "Status: " + status);
    }

    public void printDoctorScheduledAppointment(){
        System.out.println("Appointment ID: " + appointmentID + "\n" +
                            "Patient ID: " + patientID + "\n" +
                            "Date: " + date.format(dateFormat) + "\n" +
                            "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                            "Status: " + status);
    }

    public int getAppointmentID(){
        return this.appointmentID;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public LocalTime getTimeStart(){
        return this.timeStart;
    }

    public LocalTime getTimeEnd(){
        return this.timeEnd;
    }

    public AppointmentStatus getStatus(){
        return this.status;
    }

    public String getPatientID(){
        return this.patientID;
    }

}
