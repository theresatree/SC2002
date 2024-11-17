package sc2002.repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sc2002.enums.PrescriptionStatus;
import sc2002.enums.Service;
import sc2002.services.AppointmentOutcomeRecord;

/**
 * The PatientAppointmentOutcomeDB class handles retrieving, setting, and updating
 * patient appointment outcomes in the database.
 */
public class PatientAppointmentOutcomeDB {

    private static final String FILE_NAME = "Appointment_Outcomes.csv";
    private static final String PATH_FILE = "resources/";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Retrieves the appointment outcome for a specific patient.
     *
     * @param hospitalID the ID of the patient
     * @return a list of appointment outcome records for the patient
     * @throws IOException if an error occurs while reading the file
     */
    public static List<AppointmentOutcomeRecord> getAppointmentOutcome(String hospitalID) throws IOException {
        List<AppointmentOutcomeRecord> appointmentOutcome = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE+FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                if (fields[0].equals(hospitalID)) {
                    String doctorID = fields[1];
                    int appointmentID = Integer.parseInt(fields[2]);
                    LocalDate date = LocalDate.parse(fields[3], dateFormat);
                    Service services = Service.valueOf(fields[4].toUpperCase());
                    String medication = fields[5];
                    PrescriptionStatus status = PrescriptionStatus.valueOf(fields[6].toUpperCase());
                    String notes = fields[7];

                    AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(hospitalID, doctorID, appointmentID, date, services, medication, status, notes);
                    appointmentOutcome.add(outcome);
                }
            }
        }
        return appointmentOutcome;
    }

    /**
     * Sets a new appointment outcome in the database.
     *
     * @param patientID the patient ID
     * @param doctorID the doctor ID
     * @param appointmentID the appointment ID
     * @param date the date of the appointment
     * @param services the services provided
     * @param medication the medication prescribed
     * @param notes additional notes on the appointment outcome
     */
    public static void setAppointmentOutcome(String patientID, String doctorID, int appointmentID, LocalDate date, Service services, String medication, String notes) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_FILE+FILE_NAME, true))) {
            bw.write(String.join(",",
                patientID,
                doctorID,
                String.valueOf(appointmentID),
                date.format(dateFormat),
                services.name(),
                medication,
                "Pending",
                notes));
            bw.newLine();
        }
    }

    /**
     * Updates appointment outcomes in the database.
     *
     * @param updatedStatus a list of updated appointment outcome records
     * @throws IOException if an error occurs while updating the file
     */
    public static void updateAppointmentOutcome(List<AppointmentOutcomeRecord> updatedStatus) throws IOException {
        File inputFile = new File(PATH_FILE+FILE_NAME);
        File tempFile = new File(PATH_FILE+"temp_" + FILE_NAME);

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    bw.write(line);
                    bw.newLine();
                    continue; // Write header row as is
                }

                String[] fields = line.split(",");
                String patientID = fields[0];
                int appointmentID = Integer.parseInt(fields[2]);

                for (AppointmentOutcomeRecord updatedRecord : updatedStatus) {
                    if (updatedRecord.getPatientID().equals(patientID) && updatedRecord.getAppointmentID() == appointmentID) {
                        fields[6] = updatedRecord.getPrescriptionStatus().name(); // Update prescription status
                    }
                }

                bw.write(String.join(",", fields));
                bw.newLine();
            }
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException("Could not update the appointment outcomes.");
        }
    }

    /**
     * Retrieves all appointment outcomes from the database.
     *
     * @return a list of all appointment outcome records
     * @throws IOException if an error occurs while reading the file
     */
    public static List<AppointmentOutcomeRecord> getAllAppointmentOutcomes() throws IOException {
        List<AppointmentOutcomeRecord> appointmentOutcome = new ArrayList<>();

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
                Service services = Service.valueOf(fields[4].toUpperCase());
                String medication = fields[5];
                PrescriptionStatus status = PrescriptionStatus.valueOf(fields[6].toUpperCase());
                String notes = fields[7];

                AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(patientID, doctorID, appointmentID, date, services, medication, status, notes);
                appointmentOutcome.add(outcome);
            }
        }
        return appointmentOutcome;
    }
}