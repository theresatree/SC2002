package sc2002.models;

import sc2002.repositories.UserDB;
import sc2002.enums.Role;

/**
 * The Diagnosis class represents a patient's diagnosis, including details on the doctor, treatment,
 * and additional notes.
 */
public class Diagnosis {
    private String patientID;
    private int diagnosisID;
    private String doctorID;
    private String descriptionOfDiagnosis;
    private String descriptionOfTreatmtent;
    private String additionalNotes;

    /**
     * Constructs a Diagnosis object with specified details.
     *
     * @param patientID the ID of the patient
     * @param diagnosisID the unique ID of the diagnosis
     * @param doctorID the ID of the doctor who made the diagnosis
     * @param descriptionOfDiagnosis the description of the diagnosis
     * @param descriptionOfTreatment the description of the treatment
     * @param additionalNotes any additional notes
     */
    public Diagnosis(String patientID, int diagnosisID, String doctorID, String descriptionOfDiagnosis, String descriptionOfTreatment, String additionalNotes) {
        this.patientID = patientID;
        this.diagnosisID = diagnosisID;
        this.doctorID = doctorID;
        this.descriptionOfDiagnosis = descriptionOfDiagnosis;
        this.descriptionOfTreatmtent = descriptionOfTreatment;
        this.additionalNotes = additionalNotes;
    }

    /**
     * Gets the diagnosis ID.
     *
     * @return the diagnosis ID
     */
    public int getDiagnosisCode() {
        return this.diagnosisID;
    }

    /**
     * Prints the details of the diagnosis.
     *
     * @return the formatted string of diagnosis details
     */
    public String printDiagnosis() {
        return (
            "Diagnosis ID: " + diagnosisID + "\n" +
            "Doctor ID: " + doctorID + " (" + UserDB.getNameByHospitalID(doctorID, Role.DOCTOR) + ")" + "\n" +
            "Diagnosis: " + descriptionOfDiagnosis + "\n" +
            "Treatment: " + descriptionOfTreatmtent + "\n" +
            "Additional notes: " + additionalNotes
        );
    }
}