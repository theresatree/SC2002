package sc2002;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PatientAppointmentDB {
        private static final String FILE_NAME = "Appointment.xlsx"; //fixed file location for Patient_List.xlsx
        static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

     //////////////////////////////////////// Get AvailableSlots ////////////////////////////////////////
    public static List<AvailableDatesToChoose> getAvailableSlots() throws IOException {
        List<AvailableDatesToChoose> availableSlots = new ArrayList<>(); // List to hold multiple dates
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell patientIDCell = row.getCell(0);
                    Cell doctorIDCell = row.getCell(1);
                    Cell appointmentIDCell = row.getCell(2);
                    Cell dateCell = row.getCell(3);
                    Cell timeStartCell = row.getCell(4);
                    Cell timeEndCell = row.getCell(5);
                    Cell statusCell = row.getCell(6);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    // Check if the doctorID and if the date matches, add to the scheduled dates
                    if (statusCell != null && 
                        statusCell.getStringCellValue().equalsIgnoreCase("Available") &&
                        patientIDCell.getStringCellValue().isEmpty()){

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

    //////////////////////////////////////// FETCH PATIENT'S SCHEDULED APPOINTMENTS  ///////////////////////////////////////
    public static List<PatientScheduledAppointment> patientScheduledAppointments(String patientID) throws IOException{
        List<PatientScheduledAppointment> listOfSchedule = new ArrayList<>(); // List to hold multiple dates
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Workbook workbook = new XSSFWorkbook(is)) {
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

        for (Row row : sheet) {
            Cell patientIDCell = row.getCell(0);
            Cell doctorIDCell = row.getCell(1);
            Cell appointmentIDCell = row.getCell(2);
            Cell dateCell = row.getCell(3);
            Cell timeStartCell = row.getCell(4);
            Cell timeEndCell = row.getCell(5);
            Cell statusCell = row.getCell(6);
            if (patientIDCell!=null && patientIDCell.getStringCellValue().equals(patientID)){
                if (statusCell.getStringCellValue().equals("Confirmed") || statusCell.getStringCellValue().equals("Pending") || statusCell.getStringCellValue().equals("Declined")){
                    int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                    String doctorID = doctorIDCell.getStringCellValue();
                    LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                    LocalTime timeStart = LocalTime.parse(timeStartCell.getStringCellValue(), timeFormat);
                    LocalTime timeEnd = LocalTime.parse(timeEndCell.getStringCellValue(), timeFormat);
                    AppointmentStatus status = AppointmentStatus.valueOf(statusCell.getStringCellValue().toUpperCase()); 

                    PatientScheduledAppointment schedule = new PatientScheduledAppointment(patientID, currentAppointmentID, doctorID, date, timeStart, timeEnd, status);
                    listOfSchedule.add(schedule);
                }
            }
        }
        return listOfSchedule;
        }
    }

    //////////////////////////////////////// RESCHEDULE PATIENT APPOINTMENT  ///////////////////////////////////////
    public static void reschedulePatientAppointment(int appointmentID, String patientID) throws IOException{
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Workbook workbook = new XSSFWorkbook(is)) {
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet


        for (Row row : sheet) {
            Cell patientIDCell = row.getCell(0);
            Cell appointmentIDCell = row.getCell(2);
            if (appointmentIDCell!=null && appointmentIDCell.getCellType() == CellType.NUMERIC){
                int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                if (currentAppointmentID == appointmentID && patientIDCell.getStringCellValue().equals(patientID)) {
                    // Update existing row
                    row.getCell(0).setCellValue("");
                    row.getCell(6).setCellValue("Available");
                    break; // Exit the loop after updating
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
        }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriateå
        }
    }

    //////////////////////////////////////// FOR PATIENTS TO SCHEDULE  ///////////////////////////////////////
    public static void updateScheduleForPatients(int appointmentID, String patientID) throws IOException{
        try (InputStream is = PatientAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Workbook workbook = new XSSFWorkbook(is)) {
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet


        for (Row row : sheet) {
            Cell appointmentIDCell = row.getCell(2);
            if (appointmentIDCell!=null && appointmentIDCell.getCellType() == CellType.NUMERIC){
                int currentAppointmentID = (int) appointmentIDCell.getNumericCellValue();
                if (currentAppointmentID == appointmentID) {
                    // Update existing row
                    row.getCell(0).setCellValue(patientID);
                    row.getCell(6).setCellValue("Pending");
                    break; // Exit the loop after updating
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
        }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriateå
        }
    }
}

