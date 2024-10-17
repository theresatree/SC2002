package sc2002;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class TimeSlot {
    LocalTime startTime;
    LocalTime endTime;
    boolean isAvailable;
    

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }

    public LocalTime getStartTime(){
        return this.startTime;
    }

    public LocalTime getEndTime(){
        return this.endTime;
    }

    public String getTimeSlotString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(startTime) + " to " + formatter.format(endTime) + (isAvailable ? "" : " [Booked]");
    }
}

