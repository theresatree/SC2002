package sc2002;

public class Diagnosis {
    private String patientID;
    private int diagnosisID;
    private String doctorID;
    private String descriptionOfDiagnosis;
    private String descriptionOfTreatmtent;
    private String additionalNotes;

    Diagnosis(String patientID, int diagnosisID, String doctorID, String descriptionOfDiagnosis, String descriptionOfTreatment, String additionalNotes) {
        this.patientID = patientID;
        this.diagnosisID = diagnosisID;
        this.doctorID = doctorID;
        this.descriptionOfDiagnosis = descriptionOfDiagnosis;
        this.descriptionOfTreatmtent = descriptionOfTreatment;
        this.additionalNotes = additionalNotes;
    }

    public int getDiagnosisCode(){
        return this.diagnosisID;
    }

    public String printDiagnosis(){
        return(
            "Diagnosis ID: " + diagnosisID + "\n" +
            "Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")" + "\n" +
            "Diagnosis: " + descriptionOfDiagnosis + "\n" +
            "Treatment: " + descriptionOfTreatmtent + "\n" +
            "Additional notes: " + additionalNotes);
    }
}