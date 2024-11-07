package sc2002.repositories;

import sc2002.enums.*;
import sc2002.models.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Provides methods for handling doctor appointments and schedules stored in an Excel file.
 */
public class DoctorAppointmentDB {
    private static final String FILE_NAME = "Appointment.xlsx";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Retrieves the scheduled dates for a specific doctor on a given date.
     *
     * @param hospitalID The ID of the doctor.
     * @param filterDate The date to filter schedules.
     * @return A list of DoctorScheduledDates on the specified date.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static List<DoctorScheduledDates> getScheduledDates(String hospitalID, LocalDate filterDate) throws IOException {
        List<DoctorScheduledDates> scheduledDates = new ArrayList<>();
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) throw new IOException("File not found in resources: " + FILE_NAME);

            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;
                    Cell doctorIDCell = row.getCell(1);
                    if (doctorIDCell != null && doctorIDCell.getStringCellValue().equals(hospitalID)) {
                        LocalDate date = LocalDate.parse(row.getCell(3).getStringCellValue(), dateFormat);
                        if (date.equals(filterDate)) {
                            LocalTime timeStart = LocalTime.parse(row.getCell(4).getStringCellValue(), timeFormat);
                            LocalTime timeEnd = LocalTime.parse(row.getCell(5).getStringCellValue(), timeFormat);
                            AppointmentStatus status = AppointmentStatus.valueOf(row.getCell(6).getStringCellValue().toUpperCase());
                            DoctorScheduledDates scheduleDate = new DoctorScheduledDates(hospitalID, date, timeStart, timeEnd, status);
                            scheduledDates.add(scheduleDate);
                        }
                    }
                }
            }
        }
        return scheduledDates;
    }

    /**
     * Sets a new schedule for a doctor.
     *
     * @param doctorID The ID of the doctor.
     * @param date The date of the schedule.
     * @param startTime The start time of the schedule.
     * @param endTime The end time of the schedule.
     */
    public static void setDoctorSchedule(String doctorID, LocalDate date, LocalTime startTime, LocalTime endTime) {
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
            row.createCell(0).setCellValue("");
            row.createCell(1).setCellValue(doctorID);
            row.createCell(2).setCellValue(getNextAppointmentID(sheet));
            row.createCell(3).setCellValue(date.format(dateFormat));
            row.createCell(4).setCellValue(startTime.format(timeFormat));
            row.createCell(5).setCellValue(endTime.format(timeFormat));
            row.createCell(6).setCellValue("Available");

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the next available appointment ID.
     *
     * @param sheet The sheet to search for the current maximum ID.
     * @return The next available appointment ID.
     */
    private static int getNextAppointmentID(Sheet sheet) {
        int maxID = 0;
        for (Row row : sheet) {
            Cell idCell = row.getCell(2);
            if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
                maxID = Math.max(maxID, (int) idCell.getNumericCellValue());
            }
        }
        return maxID + 1;
    }

    /**
     * Retrieves a list of patients assigned to a specific doctor.
     *
     * @param doctorID The ID of the doctor.
     * @return A list of patient IDs.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static List<String> getPatients(String doctorID) throws IOException {
        Set<String> patientIDs = new HashSet<>();
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell doctorIDCell = row.getCell(1);
                if (doctorIDCell != null && doctorIDCell.getStringCellValue().equals(doctorID)) {
                    Cell patientIDCell = row.getCell(0);
                    String patientID = patientIDCell != null ? patientIDCell.getStringCellValue() : "";
                    if (!patientID.isEmpty()) {
                        patientIDs.add(patientID);
                    }
                }
            }
        }
        return new ArrayList<>(patientIDs);
    }

    /**
     * Retrieves the full schedule of a doctor.
     *
     * @param hospitalID The ID of the doctor.
     * @return A list of DoctorScheduledDates.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static List<DoctorScheduledDates> getAllPersonalSchedule(String hospitalID) throws IOException {
        List<DoctorScheduledDates> scheduledDates = new ArrayList<>();
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell doctorIDCell = row.getCell(1);
                if (doctorIDCell != null && doctorIDCell.getStringCellValue().equals(hospitalID)) {
                    LocalDate date = LocalDate.parse(row.getCell(3).getStringCellValue(), dateFormat);
                    LocalTime timeStart = LocalTime.parse(row.getCell(4).getStringCellValue(), timeFormat);
                    LocalTime timeEnd = LocalTime.parse(row.getCell(5).getStringCellValue(), timeFormat);
                    AppointmentStatus status = AppointmentStatus.valueOf(row.getCell(6).getStringCellValue().toUpperCase());
                    DoctorScheduledDates scheduleDate = new DoctorScheduledDates(hospitalID, date, timeStart, timeEnd, status);
                    scheduledDates.add(scheduleDate);
                }
            }
        }
        return scheduledDates;
    }

    /**
     * Retrieves all confirmed appointments for a doctor.
     *
     * @param doctorID The ID of the doctor.
     * @return A list of PatientScheduledAppointments.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static List<PatientScheduledAppointment> doctorListOfAllAppointments(String doctorID) throws IOException {
        List<PatientScheduledAppointment> listOfSchedule = new ArrayList<>();
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (doctorID.equals(row.getCell(1).getStringCellValue())) {
                    PatientScheduledAppointment schedule = new PatientScheduledAppointment(
                            row.getCell(0).getStringCellValue(),
                            (int) row.getCell(2).getNumericCellValue(),
                            doctorID,
                            LocalDate.parse(row.getCell(3).getStringCellValue(), dateFormat),
                            LocalTime.parse(row.getCell(4).getStringCellValue(), timeFormat),
                            LocalTime.parse(row.getCell(5).getStringCellValue(), timeFormat),
                            AppointmentStatus.valueOf(row.getCell(6).getStringCellValue().toUpperCase())
                    );
                    listOfSchedule.add(schedule);
                }
            }
        }
        return listOfSchedule;
    }

    /**
     * Accepts or declines an appointment for a doctor.
     *
     * @param doctorID The ID of the doctor.
     * @param appointmentID The appointment ID.
     * @param patientID The ID of the patient.
     * @param status The acceptance status (true for accepted, false for declined).
     * @throws IOException If an I/O error occurs while updating the file.
     */
    public static void acceptDeclineAppointment(String doctorID, int appointmentID, String patientID, boolean status) throws IOException {
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
    
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
    
                Cell patientIDCell = row.getCell(0);
                Cell doctorIDCell = row.getCell(1);
                Cell appointmentIDCell = row.getCell(2);
                Cell statusCell = row.getCell(6);
    
                // Check if the appointment ID, doctor ID, and patient ID match
                if (appointmentIDCell != null && doctorIDCell != null && patientIDCell != null) {
                    int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                    String currentDoctorID = doctorIDCell.getStringCellValue();
                    String currentPatientID = patientIDCell.getStringCellValue();
    
                    if (currentAppointmentID == appointmentID && currentDoctorID.equals(doctorID) && currentPatientID.equals(patientID)) {
                        // Update the status
                        statusCell.setCellValue(status ? "Confirmed" : "Declined");
                        break;
                    }
                }
            }
    
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
            workbook.write(fos);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks an appointment as completed.
     *
     * @param doctorID The ID of the doctor.
     * @param appointmentID The appointment ID.
     * @param patientID The ID of the patient.
     * @throws IOException If an I/O error occurs while updating the file.
     */
    public static void completeAppointment(String doctorID, int appointmentID, String patientID) throws IOException {
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
    
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
    
                Cell patientIDCell = row.getCell(0);
                Cell doctorIDCell = row.getCell(1);
                Cell appointmentIDCell = row.getCell(2);
                Cell statusCell = row.getCell(6);
    
                // Check if the appointment ID, doctor ID, and patient ID match
                if (appointmentIDCell != null && doctorIDCell != null && patientIDCell != null) {
                    int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                    String currentDoctorID = doctorIDCell.getStringCellValue();
                    String currentPatientID = patientIDCell.getStringCellValue();
    
                    if (currentAppointmentID == appointmentID && currentDoctorID.equals(doctorID) && currentPatientID.equals(patientID)) {
                        // Update the status
                        statusCell.setCellValue("Completed");
                        break;
                    }
                }
            }
    
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
            workbook.write(fos);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the number of pending appointments for a specific doctor.
     *
     * @param doctorID The ID of the doctor.
     * @return A string showing the number of pending appointments, if any.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static String numberOfPending(String doctorID) throws IOException {
        int pendingAppointments = 0; // set it to 0.
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Workbook workbook = new XSSFWorkbook(is)) {
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

        for (Row row : sheet) {
            Cell doctorIDCell = row.getCell(1);
            Cell statusCell = row.getCell(6);
            if (doctorIDCell!=null && doctorIDCell.getStringCellValue().equals(doctorID) && statusCell.getStringCellValue().equals("Pending")){
                pendingAppointments=pendingAppointments+1;
            }
        }
        }

        if (pendingAppointments!=0){
           return("(" + pendingAppointments + " pending appointment)");
        }
        return "";
    }
}