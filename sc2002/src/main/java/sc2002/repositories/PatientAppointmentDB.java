package sc2002.repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sc2002.enums.AppointmentStatus;
import sc2002.models.PatientScheduledAppointment;
import sc2002.services.AvailableDatesToChoose;

/**
 * The PatientAppointmentDB class handles appointment scheduling and rescheduling
 * for patients, including fetching available slots and updating the database.
 */
public class PatientAppointmentDB {

    private static final String FILE_NAME = "Appointment.csv";
    private static final String PATH_FILE = "resources/";

    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Retrieves the list of available appointment slots.
     *
     * @return a list of available dates to choose from
     * @throws IOException if an error occurs while reading the file
     */
    public static List<AvailableDatesToChoose> getAvailableSlots() throws IOException {
        List<AvailableDatesToChoose> availableSlots = new ArrayList<>();

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
                String status = fields[6];

                if (status.equalsIgnoreCase("Available") && patientID.isEmpty()) {
                    AvailableDatesToChoose availableDate = new AvailableDatesToChoose(doctorID, appointmentID, date, timeStart, timeEnd);
                    availableSlots.add(availableDate);
                }
            }
        }
        return availableSlots;
    }

    /**
     * Fetches the list of scheduled appointments for a specific patient.
     *
     * @param patientID the ID of the patient
     * @return a list of scheduled appointments for the patient
     * @throws IOException if an error occurs while reading the file
     */
    public static List<PatientScheduledAppointment> patientScheduledAppointments(String patientID) throws IOException {
        List<PatientScheduledAppointment> listOfSchedule = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE+FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                String currentPatientID = fields[0];
                String doctorID = fields[1];
                int appointmentID = Integer.parseInt(fields[2]);
                LocalDate date = LocalDate.parse(fields[3], dateFormat);
                LocalTime timeStart = LocalTime.parse(fields[4], timeFormat);
                LocalTime timeEnd = LocalTime.parse(fields[5], timeFormat);
                String status = fields[6];

                if (currentPatientID.equals(patientID) &&
                    (status.equalsIgnoreCase("Confirmed") ||
                     status.equalsIgnoreCase("Pending") ||
                     status.equalsIgnoreCase("Declined"))) {

                    AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
                    PatientScheduledAppointment schedule = new PatientScheduledAppointment(patientID, appointmentID, doctorID, date, timeStart, timeEnd, appointmentStatus);
                    listOfSchedule.add(schedule);
                }
            }
        }
        return listOfSchedule;
    }

    /**
     * Reschedules a patient's appointment by updating the status to "Available".
     *
     * @param appointmentID the appointment ID to reschedule
     * @param patientID the patient ID associated with the appointment
     * @throws IOException if an error occurs while updating the file
     */
    public static void reschedulePatientAppointment(int appointmentID, String patientID) throws IOException {
        updateAppointment(appointmentID, patientID, "", "Available");
    }

    /**
     * Updates an appointment status for a patient to "Pending".
     *
     * @param appointmentID the appointment ID to schedule
     * @param patientID the patient ID associated with the appointment
     * @throws IOException if an error occurs while updating the file
     */
    public static void updateScheduleForPatients(int appointmentID, String patientID) throws IOException {
        updateAppointment(appointmentID, "", patientID, "Pending");
    }

    /**
     * Updates an appointment record in the CSV file.
     *
     * @param appointmentID the appointment ID
     * @param oldPatientID the current patient ID (use "" to skip validation)
     * @param newPatientID the new patient ID to set
     * @param newStatus the new status to set
     * @throws IOException if an error occurs while updating the file
     */
    private static void updateAppointment(int appointmentID, String oldPatientID, String newPatientID, String newStatus) throws IOException {
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
                    continue; // Write header as is
                }

                String[] fields = line.split(",");
                int currentAppointmentID = Integer.parseInt(fields[2]);

                if (currentAppointmentID == appointmentID &&
                    (oldPatientID.isEmpty() || fields[0].equals(oldPatientID))) {

                    fields[0] = newPatientID; // Update patient ID
                    fields[6] = newStatus;   // Update status
                }

                bw.write(String.join(",", fields));
                bw.newLine();
            }
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException("Failed to update the appointment.");
        }
    }
}