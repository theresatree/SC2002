package sc2002;

public class Treatment {
    private String patientID;
    private int diagnosisCode;
    private String doctorID;
    private String descriptionOfTreatment;
    private String dateOfTreatment;
    private String additionalNotes;

    Treatment(String patientID, int diagnosisCode, String doctorID, String descriptionOfTreatment, String dateOfTreatment, String additionalNotes) {
        this.patientID = patientID;
        this.diagnosisCode = diagnosisCode;
        this.doctorID = doctorID;
        this.descriptionOfTreatment = descriptionOfTreatment;
        this.dateOfTreatment = dateOfTreatment;
        this.additionalNotes = additionalNotes;
    }

    public String printTreatment(){
        return(
            "Diagnosis code: " + diagnosisCode + "\n" +
            "Date: " + dateOfTreatment + "\n" +
            "By Doctor ID: " + doctorID + "\n" +
            "Treatment: " + descriptionOfTreatment + "\n" +
            "Additional notes: " + additionalNotes);
    }
}
