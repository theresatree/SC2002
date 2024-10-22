package sc2002;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AvailableDatesToChoose {
    private String doctorID;
    private int appointmentID;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");


    AvailableDatesToChoose(String doctorID, int appointmentID, LocalDate date, LocalTime timeStart, LocalTime timeEnd){
        this.doctorID=doctorID;
        this.appointmentID=appointmentID;
        this.date=date;
        this.timeStart=timeStart;
        this.timeEnd=timeEnd;
    }

    public void printString(){
        System.out.println("Appointment ID: " + appointmentID + "\n" +
                            "Date: " + date.format(dateFormat) + "\n" +
                            "Time: " + timeStart.format(timeFormat) + " to " + timeEnd.format(timeFormat) + "\n" +
                            "By Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")");
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

    public int getAppointmentID(){
        return this.appointmentID;
    }
}
