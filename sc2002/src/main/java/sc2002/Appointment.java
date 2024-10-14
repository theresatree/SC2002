package sc2002;

interface Appointment {
    public enum AppointmentStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED;
    }
    
    public void viewPastAppointmentOutcome();
    public void viewAppointmentStatus();
    public void cancelAppointment();

}
