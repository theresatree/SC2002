package sc2002;

public class Medication {
    private String patientID;
    private int diagnosisCode;
    private String doctorID;
    private String medicationPrescribed;
    private String frequencyOfMedication;
    private String dateDispsensed;
    private String additionalNotes;

    Medication(String patientID, int diagnosisCode, String doctorID, String medicationPrescribed, String frequencyOfMedication, String dateDispsensed, String additionalNotes) {
        this.patientID = patientID;
        this.diagnosisCode = diagnosisCode;
        this.doctorID = doctorID;
        this.medicationPrescribed = medicationPrescribed;
        this.frequencyOfMedication = frequencyOfMedication;
        this.dateDispsensed = dateDispsensed;
        this.additionalNotes = additionalNotes;
    }

    public String printMedication(){
        return(
            "Diagnosis code: " + diagnosisCode + "\n" +
            "Date: " + dateDispsensed + "\n" +
            "By Doctor ID: " + doctorID + "\n" +
            "Medication: " + medicationPrescribed + "\n" +
            "Frequency: " + frequencyOfMedication + "\n" +
            "Additional notes: " + additionalNotes);
    }
}