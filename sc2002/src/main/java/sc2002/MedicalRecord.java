package sc2002;

public class MedicalRecord {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    // private ContactInformation contactInformation; // Create a ContactInformation Class for this
    private String bloodType;
    // private List<Diagnosis> diagnoses; // Create a Diagnosis Class for this
    // private List<Treatment> treatments; // Create a Treatment Class for this
    // private List<Medication> medications; // Create a Medication Class for this

    MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String bloodType) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
    }


    public void viewMedicalRecord(){
        System.out.println(
                "ID: " + patientID + "\n" +
                "Name: " + name + "\n" +
                "Date of birth: " + dateOfBirth + "\n" +
                "Gender: " + gender + "\n" +
                "Blood Type: " + bloodType);
    }
}
