package sc2002.models;

import java.io.IOException;
import java.util.List;
import sc2002.repositories.PatientDB;
import sc2002.repositories.DiagnosisDB;

/**
 * The MedicalRecord class represents a patient's medical record, including personal
 * details, contact information, and diagnoses.
 */
public class MedicalRecord {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private ContactInformation contactInformation;
    private String bloodType;
    private List<Diagnosis> diagnoses;

    /**
     * Constructs a MedicalRecord object with specified details.
     *
     * @param patientID the ID of the patient
     * @param name the name of the patient
     * @param dateOfBirth the patient's date of birth
     * @param gender the patient's gender
     * @param bloodType the patient's blood type
     * @throws IOException if there is an issue retrieving contact or diagnosis information
     */
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType) throws IOException {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        try {
            this.contactInformation = PatientDB.getPatientContactDetails(patientID);
            this.diagnoses = DiagnosisDB.getDiagnosis(patientID);         
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the patient's phone number.
     *
     * @return the phone number
     */
    public int getPhoneNumber() {
        return this.contactInformation.getPhoneNumber();
    }

    /**
     * Gets the patient's email address.
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return this.contactInformation.getEmailAddress();
    }

    /**
     * Gets the patient's list of diagnoses.
     *
     * @return the list of diagnoses
     */
    public List<Diagnosis> getDiagnosis() {
        return this.diagnoses;
    }

    /**
     * Prints all diagnoses associated with the patient.
     *
     * @return a formatted string of diagnoses
     */
    public String printDiagnoses() {
        StringBuilder diagnosisList = new StringBuilder();
        if (diagnoses.isEmpty()) {
            return("No diagnoses found for patient\n\n");
        }
        for (Diagnosis diagnosis : diagnoses) {
            diagnosisList.append(diagnosis.printDiagnosis()).append("\n\n");
        }
        return diagnosisList.toString();
    }

    /**
     * Displays the complete medical record, including personal details and diagnoses.
     */
    public void viewMedicalRecord() {
        System.out.println(
                "\n" +
                "============================" + "\n" +
                "       Patient Details" + "\n" +
                "============================" + "\n" +
                "ID: " + patientID + "\n" +
                "Name: " + name + "\n" +
                "Date of birth: " + dateOfBirth + "\n" +
                "Gender: " + gender + "\n" +
                "Blood Type: " + bloodType + "\n" +
                "Phone Number: " + contactInformation.getPhoneNumber() + "\n" +
                "Email: " + contactInformation.getEmailAddress() + "\n\n" +
                "============================" + "\n" +
                " Diagnoses and Treatments" + "\n" +
                "============================" + "\n" +
                printDiagnoses());
    }

    /**
     * Updates the patient's personal contact information.
     *
     * @param email the new email address
     * @param phoneNumber the new phone number
     */
    public void updatePersonalInfo(String email, int phoneNumber) {
        PatientDB.updateContactInformation(this.patientID, email, phoneNumber);
    }
}