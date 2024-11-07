package sc2002.repositories;

import sc2002.services.AvailableDatesToChoose;
import sc2002.models.PatientScheduledAppointment;
import sc2002.enums.AppointmentStatus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The PatientAppointmentDB class handles appointment scheduling and rescheduling
 * for patients, including fetching available slots and updating the database.
 */
public class PatientAppointmentDB {

    private static final String FILE_NAME = "Appointment.xlsx";
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
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Skip header row

                    Cell patientIDCell = row.getCell(0);
                    Cell doctorIDCell = row.getCell(1);
                    Cell appointmentIDCell = row.getCell(2);
                    Cell dateCell = row.getCell(3);
                    Cell timeStartCell = row.getCell(4);
                    Cell timeEndCell = row.getCell(5);
                    Cell statusCell = row.getCell(6);

                    if (statusCell != null &&
                        statusCell.getStringCellValue().equalsIgnoreCase("Available") &&
                        patientIDCell.getStringCellValue().isEmpty()) {

                        String doctorID = doctorIDCell.getStringCellValue();
                        int appointmentID = (int) appointmentIDCell.getNumericCellValue();
                        LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                        LocalTime timeStart = LocalTime.parse(timeStartCell.getStringCellValue(), timeFormat);
                        LocalTime timeEnd = LocalTime.parse(timeEndCell.getStringCellValue(), timeFormat);

                        AvailableDatesToChoose availableDate = new AvailableDatesToChoose(doctorID, appointmentID, date, timeStart, timeEnd);
                        availableSlots.add(availableDate);
                    }
                }
            }
            return availableSlots;
        }
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
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell patientIDCell = row.getCell(0);
                Cell doctorIDCell = row.getCell(1);
                Cell appointmentIDCell = row.getCell(2);
                Cell dateCell = row.getCell(3);
                Cell timeStartCell = row.getCell(4);
                Cell timeEndCell = row.getCell(5);
                Cell statusCell = row.getCell(6);

                if (patientIDCell != null && patientIDCell.getStringCellValue().equals(patientID)) {
                    String status = statusCell.getStringCellValue();
                    if (status.equals("Confirmed") || status.equals("Pending") || status.equals("Declined")) {
                        int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                        String doctorID = doctorIDCell.getStringCellValue();
                        LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                        LocalTime timeStart = LocalTime.parse(timeStartCell.getStringCellValue(), timeFormat);
                        LocalTime timeEnd = LocalTime.parse(timeEndCell.getStringCellValue(), timeFormat);
                        AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());

                        PatientScheduledAppointment schedule = new PatientScheduledAppointment(patientID, currentAppointmentID, doctorID, date, timeStart, timeEnd, appointmentStatus);
                        listOfSchedule.add(schedule);
                    }
                }
            }
            return listOfSchedule;
        }
    }

    /**
     * Reschedules a patient's appointment by updating the status to "Available".
     *
     * @param appointmentID the appointment ID to reschedule
     * @param patientID the patient ID associated with the appointment
     * @throws IOException if an error occurs while updating the file
     */
    public static void reschedulePatientAppointment(int appointmentID, String patientID) throws IOException {
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell patientIDCell = row.getCell(0);
                Cell appointmentIDCell = row.getCell(2);

                if (appointmentIDCell != null && appointmentIDCell.getCellType() == CellType.NUMERIC) {
                    int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                    if (currentAppointmentID == appointmentID && patientIDCell.getStringCellValue().equals(patientID)) {
                        row.getCell(0).setCellValue("");
                        row.getCell(6).setCellValue("Available");
                        break;
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Updates an appointment status for a patient to "Pending".
     *
     * @param appointmentID the appointment ID to schedule
     * @param patientID the patient ID associated with the appointment
     * @throws IOException if an error occurs while updating the file
     */
    public static void updateScheduleForPatients(int appointmentID, String patientID) throws IOException {
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell appointmentIDCell = row.getCell(2);

                if (appointmentIDCell != null && appointmentIDCell.getCellType() == CellType.NUMERIC) {
                    int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                    if (currentAppointmentID == appointmentID) {
                        row.getCell(0).setCellValue(patientID);
                        row.getCell(6).setCellValue("Pending");
                        break;
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        }
    }
}