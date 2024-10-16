package sc2002;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailableDatesToChoose {
    private String doctorID;
    private int appointmentID;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    AvailableDatesToChoose(String doctorID, int appointmentID, LocalDate date, LocalTime timeStart, LocalTime timeEnd){
        this.doctorID=doctorID;
        this.appointmentID=appointmentID;
        this.date=date;
        this.timeStart=timeStart;
        this.timeEnd=timeEnd;
    }

    public void printString(){
        System.out.println("Appointment ID: " + appointmentID + "\n" +
                            "Date: " + date + "\n" +
                            "Time: " + timeStart + " to " + timeEnd + "\n" +
                            "By Doctor ID: " + doctorID);
    }
}
