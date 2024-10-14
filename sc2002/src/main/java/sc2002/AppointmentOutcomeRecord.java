package sc2002;

import java.util.List;

import sc2002.AppointmentOutcomeRecord.PrescriptionStatus;

public class AppointmentOutcomeRecord {
    public enum Service{
        CONSULTATION,
        XRAY,
        BLOODTEST;
    }

    public enum PrescriptionStatus{
        PENDING,
        COMPLETED;
    }

    private String patientID;
    private String doctorID;
    private Date dateOfAppointment;
    private Service service;
    private List<Medicine> medications; 
    private PrescriptionStatus prescriptionStatus;
    private String consultationNotes;

    AppointmentOutcomeRecord(String patientID, String doctorID, Date dateOfAppointment, Service service, String consultationNotes){
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.dateOfAppointment = dateOfAppointment;
        this.service = service;
        this.consultationNotes=consultationNotes;
        this.prescriptionStatus=PrescriptionStatus.PENDING;
    }

}
