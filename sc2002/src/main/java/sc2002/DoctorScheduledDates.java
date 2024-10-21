package sc2002;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DoctorScheduledDates {
    LocalDate date;
    LocalTime timeStart;
    LocalTime timeEnd;
    String doctorID;
    AppointmentStatus status;
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    public DoctorScheduledDates(String doctorId, LocalDate date, LocalTime timeStart, LocalTime timeEnd, AppointmentStatus status) {
        this.doctorID = doctorId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    public void printSchedule(){
        System.out.println("Date: " + date.format(dateFormat) + "  Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                           "Status: " + status);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTimeStart(){
        return timeStart;
    }

    public LocalTime getTimeEnd(){
        return timeEnd;
    }

    public String getDoctorID(){
        return doctorID;
    }
    
    public AppointmentStatus getStatus(){
        return status;
    }
    
}
