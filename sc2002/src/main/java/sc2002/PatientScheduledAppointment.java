package sc2002;

import java.time.LocalDate;
import java.time.LocalTime;

public class PatientScheduledAppointment {
    private int appointmentID;
    private String doctorID;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private AppointmentStatus status;

    PatientScheduledAppointment(int appointmentID, String doctorID, LocalDate date, LocalTime timeStart, LocalTime timeEnd, AppointmentStatus status){
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
                           "Date: " + date + "\n" +
                           "Time: " + timeStart + " to " + timeEnd + "\n" +
                           "Status: " + status);
    }

}
