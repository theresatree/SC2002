package sc2002;

public class Diagnosis {
    private String patientID;
    private int diagnosisCode;
    private String doctorID;
    private String descriptionOfDiagnosis;
    private String descriptionOfTreatmtent;
    private String dateOfDiagnosis;
    private String additionalNotes;

    Diagnosis(String patientID, int diagnosisCode, String doctorID, String descriptionOfDiagnosis, String descriptionOfTreatment, String dateOfDiagnosis, String additionalNotes) {
        this.patientID = patientID;
        this.diagnosisCode = diagnosisCode;
        this.doctorID = doctorID;
        this.descriptionOfDiagnosis = descriptionOfDiagnosis;
        this.descriptionOfTreatmtent = descriptionOfTreatment;
        this.dateOfDiagnosis = dateOfDiagnosis;
        this.additionalNotes = additionalNotes;
    }

    public String printDiagnosis(){
        return(
            "Diagnosis ID: " + diagnosisCode + "\n" +
            "Date: " + dateOfDiagnosis + "\n" +
            "By Doctor ID: " + doctorID + "\n" +
            "Diagnosis: " + descriptionOfDiagnosis + "\n" +
            "Treatment: " + descriptionOfTreatmtent + "\n" +
            "Additional notes: " + additionalNotes);
    }
}