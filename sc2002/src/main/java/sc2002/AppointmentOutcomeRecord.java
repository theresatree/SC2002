package sc2002;


public class AppointmentOutcomeRecord {

    private String patientID;
    private String doctorID;
    private int appointmentID;
    private String dateOfAppointment;
    private Service service;
    private Medicine medications; 
    private PrescriptionStatus prescriptionStatus;
    private String consultationNotes;

    AppointmentOutcomeRecord(String patientID, String doctorID, int appointmentID, String dateOfAppointment, Service service, Medicine medications, PrescriptionStatus prescriptionStatus, String consultationNotes){
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentID = appointmentID;
        this.dateOfAppointment = dateOfAppointment;
        this.service = service;
        this.medications = medications;
        this.prescriptionStatus=prescriptionStatus;
        this.consultationNotes=consultationNotes;
    }

    public String printAppointmentOutcome(){
        return(
            "Appointment ID: " + appointmentID + "\n" +
            "By Doctor ID: " + doctorID + "\n" +
            "Date and Time: " + dateOfAppointment + "\n" +
            "Services given: " + service + "\n" +
            "Medication given: " + medications + "\n" +
            "Prescription Status: " + prescriptionStatus + "\n" +
            "Additional Notes: " + consultationNotes);
    }

}
