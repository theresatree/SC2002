package sc2002;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorScheduledDates {
    LocalDate date;
    LocalTime timeStart;
    LocalTime timeEnd;
    String doctorID;

    public DoctorScheduledDates(String doctorId, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.doctorID = doctorId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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

    public String doctorID(){
        return doctorID;
    }
    
    
}
