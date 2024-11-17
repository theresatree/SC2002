package sc2002.enums;

/**
 * Enum representing the possible statuses of an appointment.
 * Each status indicates the current state of the appointment 
 * in the booking or completion process.
 */
public enum AppointmentStatus {
    /**
     * The appointment slot is available for booking.
     */
    AVAILABLE,

    /**
     * The appointment has been requested and is awaiting confirmation.
     */
    PENDING,

    /**
     * The appointment has been confirmed by both parties.
     */
    CONFIRMED,

    /**
     * The appointment has been declined or canceled.
     */
    DECLINED,

    /**
     * The appointment has been successfully completed.
     */
    COMPLETED;
}