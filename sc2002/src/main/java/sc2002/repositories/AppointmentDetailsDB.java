package sc2002.repositories;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sc2002.enums.AppointmentStatus;
import sc2002.enums.PrescriptionStatus;
import sc2002.enums.Service;
import sc2002.models.AppointmentDetails;

/**
 * Provides access to appointment details and appointment outcome data stored in CSV files.
 */
public class AppointmentDetailsDB {

    private static final String FILE_NAME = "Appointment.csv";
    private static final String PATH_FILE = "resources/";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Retrieves all appointment details from the CSV file.
     *
     * @return A list of {@link AppointmentDetails} objects containing details of all appointments.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public static List<AppointmentDetails> getAppointmentDetails() throws IOException {
        List<AppointmentDetails> appointmentDetails = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE+FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");

                String patientID = fields[0];
                String doctorID = fields[1];
                int appointmentID = Integer.parseInt(fields[2]);
                LocalDate date = LocalDate.parse(fields[3], dateFormat);
                LocalTime timeStart = LocalTime.parse(fields[4], timeFormat);
                LocalTime timeEnd = LocalTime.parse(fields[5], timeFormat);
                AppointmentStatus status = AppointmentStatus.valueOf(fields[6].toUpperCase());

                AppointmentDetails appointmentDetail = new AppointmentDetails(patientID, doctorID, appointmentID, date, timeStart, timeEnd, status);
                appointmentDetails.add(appointmentDetail);
            }
        }

        return appointmentDetails;
    }

    /**
     * Prints the appointment outcome record for a specific hospital ID and appointment ID.
     *
     * @param hospitalID    The hospital ID of the patient.
     * @param appointmentID The ID of the appointment whose outcome is to be printed.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public static void printAppointmentOutcomeRecord(String hospitalID, int appointmentID) throws IOException {
        String fileName = "resources/Appointment_Outcomes.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");

                if (fields[0].equals(hospitalID) && Integer.parseInt(fields[2]) == appointmentID) {
                    Service services = Service.valueOf(fields[4].toUpperCase());
                    String medication = fields[5].toUpperCase();
                    PrescriptionStatus status = PrescriptionStatus.valueOf(fields[6].toUpperCase());
                    String notes = fields[7];

                    System.out.println("Services given: " + services +
                            "\nMedication given: " + medication +
                            "\nPrescription Status: " + status +
                            "\nNotes: " + notes);
                    return;
                }
            }
        }

        System.out.println("Appointment outcome record can't be found");
    }
}