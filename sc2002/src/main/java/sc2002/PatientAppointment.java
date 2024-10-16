package sc2002;
import java.io.IOException;
import java.util.List;

public class PatientAppointment implements Appointment{
    private String patientID;
    private List<AvailableDatesToChoose> availableDatesToChoose;
    private List<AppointmentOutcomeRecord> appointmentOutcomeRecord;

    PatientAppointment(String patientID){
        this.patientID=patientID;
    }

    public void viewAvailableAppointmentSlots(){
        try{
            availableDatesToChoose = PatientAppointmentDB.getAvailableSlots();
        } catch (IOException e){
            e.printStackTrace();
        }

        if (availableDatesToChoose.isEmpty()) {
            System.out.println("No available dates found!\n\n");

        }
        System.out.println("\n\n=========================================");
        System.out.println("       Available Appointment Slots");
        System.out.println("=========================================");
        for (AvailableDatesToChoose dates : availableDatesToChoose) {
            dates.printString();
            System.out.println("=========================================");
        }
    }

    public void scheduleAppointment(){}

    @Override
    public void viewAppointmentStatus(){}

    // public void rescheduleAppointment (appointment : Appointment, newDate : Date, newTimeSlot : TimeSlot){}

    @Override
    public void cancelAppointment(){};


    public void viewScheduledAppointments(){}


    @Override
    public void viewPastAppointmentOutcome(){
        try {
            appointmentOutcomeRecord = PatientAppointmentOutcomeDB.getAppointmentOutcome(this.patientID);
        }
        catch (Exception e) {
            // Handle the exception
            System.out.println("An error occurred while fetching Appointment Details: " + e.getMessage());
        }

            StringBuilder appointmentOutcomeRecordList = new StringBuilder(); 
            if (appointmentOutcomeRecord.isEmpty()) {
                System.out.println("\n\n=========================================");
                System.out.println("No diagnosis found for patient");

            }
            System.out.println("\n\n=========================================");
            System.out.println("    Past Appointment Outcome Records");
            System.out.println("=========================================");
            for (AppointmentOutcomeRecord outcome : appointmentOutcomeRecord) {
                System.out.println(outcome.printAppointmentOutcome());
                System.out.println("=========================================");
            }
    }



}
