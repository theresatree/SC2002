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

    /**
     * Retrieves the start time of the time slot.
     *
     * @return the start time of the slot
     */
    public LocalTime getStartTime() {
        return this.startTime;
    }

    /**
     * Retrieves the end time of the time slot.
     *
     * @return the end time of the slot
     */
    public LocalTime getEndTime() {
        return this.endTime;
    }

    /**
     * Sets the availability status of the time slot.
     *
     * @param y the availability status to set; true for available, false for unavailable
     */
    public void setAvailable(boolean y) {
        isAvailable = y;
    }

    /**
     * Checks if the time slot is available.
     *
     * @return true if the slot is available; false otherwise
     */
    public boolean viewAvailable() {
        return isAvailable;
    }

    /**
     * Retrieves a formatted string representation of the time slot.
     *
     * @return the formatted string showing the time range and availability status
     */
    public String getTimeSlotString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(startTime) + " to " + formatter.format(endTime) + (isAvailable ? "" : " [Booked]");
    }
}