package sc2002.models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a time slot with start and end times, and availability status.
 */
public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;
    boolean isAvailable;

    /**
     * Constructs a TimeSlot object with specified start and end times.
     *
     * @param startTime the start time of the slot
     * @param endTime the end time of the slot
     */
    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }

    public LocalTime getStartTime() { return this.startTime; }

    public LocalTime getEndTime() { return this.endTime; }

    public void setAvailable(boolean y) {
        isAvailable = y;
    }

    public boolean viewAvailable() {
        return isAvailable;
    }

    /**
     * Returns a string representation of the time slot in a readable format.
     *
     * @return the formatted string of the time slot
     */
    public String getTimeSlotString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(startTime) + " to " + formatter.format(endTime) + (isAvailable ? "" : " [Booked]");
    }
}