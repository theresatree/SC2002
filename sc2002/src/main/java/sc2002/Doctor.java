package sc2002;

import java.util.Scanner;

public class Doctor extends User{
    private String doctorID;
    private DoctorAppointment docotorAppointment;

    public Doctor(String doctorID){
        super(doctorID);
        this.doctorID=doctorID;
        docotorAppointment = new DoctorAppointment(doctorID);
    }

    public void setAvailabilityDate(Scanner scanner){
        docotorAppointment.showSelectableDates(scanner);
    }
}
