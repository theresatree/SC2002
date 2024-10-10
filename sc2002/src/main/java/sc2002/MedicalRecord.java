package sc2002;

import java.io.IOException;
import java.util.List;

public class MedicalRecord {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private ContactInformation contactInformation; 
    private String bloodType;
    private List<Diagnosis> diagnoses;
    // private List<Treatment> treatments; // Create a Treatment Class for this
    // private List<Medication> medications; // Create a Medication Class for this

    MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType) throws IOException {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInformation = PatientDB.getPatientContactDetails(patientID);
        this.diagnoses = DiagnosisDB.getDiagnosis(patientID);
    }

    public int getPhoneNumber(){
        return this.contactInformation.getPhoneNumber();
    }

    public String getEmailAddress(){
        return this.contactInformation.getEmailAddress();
    }

    // Method to print all diagnoses
    public String printDiagnoses() {
        StringBuilder diagnosisList = new StringBuilder(); 
        if (diagnoses.isEmpty()) {
            return("No diagnoses found for patient");

        }
        for (Diagnosis diagnosis : diagnoses) {
            diagnosisList.append(diagnosis.printDiagnosis()).append("\n\n"); // Append each diagnosis and a newline
        }
        return diagnosisList.toString();
    }


    public void viewMedicalRecord(){
        System.out.println( "\n" +
                "============================" + "\n" +
                "       Medical Record" + "\n" +
                "============================" + "\n" +
                "ID: " + patientID + "\n" +
                "Name: " + name + "\n" +
                "Date of birth: " + dateOfBirth + "\n" +
                "Gender: " + gender + "\n" +
                "Blood Type: " + bloodType + "\n" +
                "Phone Number: " + contactInformation.getPhoneNumber() + "\n" +
                "Email: " + contactInformation.getEmailAddress() + "\n" +
                "============================" + "\n" +
                "     List of Diagnoses"+ "\n" +
                "============================" + "\n" +
                printDiagnoses());
    }

    public void updatePersonalInfo(String email, int phoneNumber){
        PatientDB.updateContactInformation(this.patientID, email, phoneNumber);
    }
}
