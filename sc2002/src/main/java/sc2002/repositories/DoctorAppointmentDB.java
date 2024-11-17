package sc2002.repositories;

import sc2002.enums.*;
import sc2002.models.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Provides methods for managing doctor appointments and schedules stored in a CSV file.
 */
public class DoctorAppointmentDB {
    private static final String FILE_NAME = "Appointment.csv";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Retrieves scheduled dates for a specific doctor filtered by a given date.
     *
     * @param hospitalID The hospital ID of the doctor.
     * @param filterDate The date to filter appointments by.
     * @return A list of scheduled dates matching the criteria.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<DoctorScheduledDates> getScheduledDates(String hospitalID, LocalDate filterDate) throws IOException {
        List<DoctorScheduledDates> scheduledDates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                if (fields[1].equals(hospitalID) && LocalDate.parse(fields[3], dateFormat).equals(filterDate)) {
                    LocalTime timeStart = LocalTime.parse(fields[4], timeFormat);
                    LocalTime timeEnd = LocalTime.parse(fields[5], timeFormat);
                    AppointmentStatus status = AppointmentStatus.valueOf(fields[6].toUpperCase());
                    DoctorScheduledDates scheduleDate = new DoctorScheduledDates(hospitalID, filterDate, timeStart, timeEnd, status);
                    scheduledDates.add(scheduleDate);
                }
            }
        }
        return scheduledDates;
    }

    /**
     * Adds a new schedule for a doctor.
     *
     * @param doctorID  The ID of the doctor.
     * @param date      The date of the schedule.
     * @param startTime The start time of the schedule.
     * @param endTime   The end time of the schedule.
     */
    public static void setDoctorSchedule(String doctorID, LocalDate date, LocalTime startTime, LocalTime endTime) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            int newAppointmentID = getNextAppointmentID();
            bw.write(String.join(",", "", doctorID, String.valueOf(newAppointmentID), date.format(dateFormat), startTime.format(timeFormat), endTime.format(timeFormat), "Available"));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the next available appointment ID.
     *
     * @return The next appointment ID.
     * @throws IOException If an error occurs while reading the file.
     */
    private static int getNextAppointmentID() throws IOException {
        int maxID = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                maxID = Math.max(maxID, Integer.parseInt(fields[2]));
            }
        }
        return maxID + 1;
    }

    /**
     * Retrieves a list of patients associated with a doctor.
     *
     * @param doctorID The doctor's ID.
     * @return A list of unique patient IDs.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<String> getPatients(String doctorID) throws IOException {
        Set<String> patientIDs = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                if (fields[1].equals(doctorID)) {
                    patientIDs.add(fields[0]);
                }
            }
        }
        return new ArrayList<>(patientIDs);
    }

    /**
     * Retrieves the full personal schedule for a doctor.
     *
     * @param hospitalID The hospital ID of the doctor.
     * @return A list of all scheduled dates for the doctor.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<DoctorScheduledDates> getAllPersonalSchedule(String hospitalID) throws IOException {
        List<DoctorScheduledDates> scheduledDates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                if (fields[1].equals(hospitalID)) {
                    LocalDate date = LocalDate.parse(fields[3], dateFormat);
                    LocalTime timeStart = LocalTime.parse(fields[4], timeFormat);
                    LocalTime timeEnd = LocalTime.parse(fields[5], timeFormat);
                    AppointmentStatus status = AppointmentStatus.valueOf(fields[6].toUpperCase());
                    DoctorScheduledDates scheduleDate = new DoctorScheduledDates(hospitalID, date, timeStart, timeEnd, status);
                    scheduledDates.add(scheduleDate);
                }
            }
        }
        return scheduledDates;
    }

    /**
     * Retrieves a list of all scheduled appointments for a doctor.
     *
     * @param doctorID The doctor's ID.
     * @return A list of scheduled appointments.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<PatientScheduledAppointment> doctorListOfAllAppointments(String doctorID) throws IOException {
        List<PatientScheduledAppointment> listOfSchedule = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                if (fields[1].equals(doctorID)) {
                    PatientScheduledAppointment schedule = new PatientScheduledAppointment(
                            fields[0], Integer.parseInt(fields[2]), doctorID,
                            LocalDate.parse(fields[3], dateFormat),
                            LocalTime.parse(fields[4], timeFormat),
                            LocalTime.parse(fields[5], timeFormat),
                            AppointmentStatus.valueOf(fields[6].toUpperCase())
                    );
                    listOfSchedule.add(schedule);
                }
            }
        }
        return listOfSchedule;
    }

    /**
     * Updates the status of an appointment to "Confirmed" or "Declined".
     *
     * @param doctorID      The ID of the doctor.
     * @param appointmentID The ID of the appointment.
     * @param patientID     The ID of the patient.
     * @param status        True to confirm, false to decline.
     * @throws IOException If an error occurs while updating the file.
     */
    public static void acceptDeclineAppointment(String doctorID, int appointmentID, String patientID, boolean status) throws IOException {
        updateAppointmentStatus(doctorID, appointmentID, patientID, status ? "Confirmed" : "Declined");
    }

    /**
     * Updates the status of an appointment to "Completed".
     *
     * @param doctorID      The ID of the doctor.
     * @param appointmentID The ID of the appointment.
     * @param patientID     The ID of the patient.
     * @throws IOException If an error occurs while updating the file.
     */
    public static void completeAppointment(String doctorID, int appointmentID, String patientID) throws IOException {
        updateAppointmentStatus(doctorID, appointmentID, patientID, "Completed");
    }

    /**
     * Updates the status of a specific appointment.
     *
     * @param doctorID      The ID of the doctor.
     * @param appointmentID The ID of the appointment.
     * @param patientID     The ID of the patient.
     * @param newStatus     The new status to set for the appointment.
     * @throws IOException If an error occurs while updating the file.
     */
    private static void updateAppointmentStatus(String doctorID, int appointmentID, String patientID, String newStatus) throws IOException {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp_" + FILE_NAME);

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    bw.write(line);
                    bw.newLine();
                    continue;
                }
                String[] fields = line.split(",");
                if (fields[1].equals(doctorID) && Integer.parseInt(fields[2]) == appointmentID && fields[0].equals(patientID)) {
                    fields[6] = newStatus;
                }
                bw.write(String.join(",", fields));
                bw.newLine();
            }
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException("Could not update file.");
        }
    }

    /**
     * Counts the number of pending appointments for a doctor.
     *
     * @param doctorID The ID of the doctor.
     * @return A string indicating the number of pending appointments.
     * @throws IOException If an error occurs while reading the file.
     */
    public static String numberOfPending(String doctorID) throws IOException {
        int pendingAppointments = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                if (fields[1].equals(doctorID) && fields[6].equalsIgnoreCase("Pending")) {
                    pendingAppointments++;
                }
            }
        }
        return pendingAppointments > 0 ? "(" + pendingAppointments + " pending appointment)" : "";
    }
}