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
    private List<Treatment> treatments;
    private List<Medication> medications;

    MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType) throws IOException {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInformation = PatientDB.getPatientContactDetails(patientID);
        this.diagnoses = DiagnosisDB.getDiagnosis(patientID);
        this.treatments = TreatmentDB.getTreatment(patientID);
        this.medications = MedicationDB.getMedication(patientID);
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

    // Method to print all Treatments
    public String printTreaments() {
        StringBuilder treatmentList = new StringBuilder(); 
        if (treatments.isEmpty()) {
            return("No Treatments found for patient");

        }
        for (Treatment treatment : treatments) {
            treatmentList.append(treatment.printTreatment()).append("\n\n"); // Append each treatment and a newline
        }
        return treatmentList.toString();
    }

    // Method to print all Medication
    public String printMedications() {
        StringBuilder MedicationList = new StringBuilder(); 
        if (medications.isEmpty()) {
            return("No Medications found for patient");

        }
        for (Medication medication : medications) {
            MedicationList.append(medication.printMedication()).append("\n\n"); // Append each medication and a newline
        }
        return MedicationList.toString();
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
                "     List of Diagnoses"+ "\n" +
                "============================" + "\n" +
                printDiagnoses() +
                "============================" + "\n" +
                "     List of Treatments"+ "\n" +
                "============================" + "\n" +
                printTreaments() +
                "============================" + "\n" +
                "     List of Medications"+ "\n" +
                "============================" + "\n" +
                printMedications());
    }

    public void updatePersonalInfo(String email, int phoneNumber){
        PatientDB.updateContactInformation(this.patientID, email, phoneNumber);
    }
}
