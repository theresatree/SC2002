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

    MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType) throws IOException {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        try{
            this.contactInformation = PatientDB.getPatientContactDetails(patientID);
            this.diagnoses = DiagnosisDB.getDiagnosis(patientID);         
        }catch(IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }

    public int getPhoneNumber(){
        return this.contactInformation.getPhoneNumber();
    }

    public String getEmailAddress(){
        return this.contactInformation.getEmailAddress();
    }

    public List<Diagnosis> getDiagnosis(){
        return this.diagnoses;
    }

    // Method to print all diagnoses
    public String printDiagnoses() {
        StringBuilder diagnosisList = new StringBuilder(); 
        if (diagnoses.isEmpty()) {
            return("No diagnoses found for patient\n\n");

        }
        for (Diagnosis diagnosis : diagnoses) {
            diagnosisList.append(diagnosis.printDiagnosis()).append("\n\n"); // Append each diagnosis and a newline
        }
        return diagnosisList.toString();
    }

    public void viewMedicalRecord(){
        System.out.println( "\n" +
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
                " Diagnoses and Treatments"+ "\n" +
                "============================" + "\n" +
                printDiagnoses());
    }

    public void updatePersonalInfo(String email, int phoneNumber){
        PatientDB.updateContactInformation(this.patientID, email, phoneNumber);
    }
}
