package sc2002;

public class Patient extends User{
    private String patientID;
    private MedicalRecord medicalRecord;

    Patient(String patientID){
        super(patientID);
        this.patientID=patientID;
    }

    public void viewMedicalRecord(){
        try{
            MedicalRecord medicalRecord = PatientDB.getPatientDetails(this.patientID);
            medicalRecord.viewMedicalRecord();
        } catch (Exception e) {
            // Handle the exception
            System.out.println("An error occurred while fetching patient details: " + e.getMessage());
        }
    }
}
