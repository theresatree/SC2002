package sc2002.repositories;

import sc2002.enums.*;
import sc2002.models.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Provides methods for handling doctor appointments and schedules stored in a CSV file.
 */
public class DoctorAppointmentDB {
    private static final String FILE_NAME = "Appointment.csv";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

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

    public static void setDoctorSchedule(String doctorID, LocalDate date, LocalTime startTime, LocalTime endTime) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            int newAppointmentID = getNextAppointmentID();
            bw.write(String.join(",", "", doctorID, String.valueOf(newAppointmentID), date.format(dateFormat), startTime.format(timeFormat), endTime.format(timeFormat), "Available"));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static void acceptDeclineAppointment(String doctorID, int appointmentID, String patientID, boolean status) throws IOException {
        updateAppointmentStatus(doctorID, appointmentID, patientID, status ? "Confirmed" : "Declined");
    }

    public static void completeAppointment(String doctorID, int appointmentID, String patientID) throws IOException {
        updateAppointmentStatus(doctorID, appointmentID, patientID, "Completed");
    }

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