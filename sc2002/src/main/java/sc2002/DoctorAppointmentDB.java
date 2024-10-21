package sc2002;

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


public class DoctorAppointmentDB {
        private static final String FILE_NAME = "Appointment.xlsx"; //fixed file location for Appointment.xlsx
        static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    //////////////////////////////////////// Get DoctorSchedule ////////////////////////////////////////
    public static List<DoctorScheduledDates> getScheduledDates(String hospitalID, LocalDate filterDate) throws IOException {
        List<DoctorScheduledDates> scheduledDates = new ArrayList<>(); // List to hold multiple dates
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell doctorIDCell = row.getCell(1);
                    Cell dateCell = row.getCell(3);
                    Cell timeStartCell = row.getCell(4);
                    Cell timeEndCell = row.getCell(5);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    // Check if the doctorID and if the date matches, add to the scheduled dates
                    if (doctorIDCell != null && doctorIDCell.getStringCellValue().equals(hospitalID)) {
                        String doctorID = doctorIDCell.getStringCellValue();
                        LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                        LocalTime timeStart = LocalTime.parse(timeStartCell.getStringCellValue(), timeFormat);
                        LocalTime timeEnd = LocalTime.parse(timeEndCell.getStringCellValue(), timeFormat);

                        // Check if the date matches the filter date before adding to scheduled dates
                        if (date.equals(filterDate)) {
                            DoctorScheduledDates scheduleDate = new DoctorScheduledDates(doctorID, date, timeStart, timeEnd);
                            scheduledDates.add(scheduleDate);
                        }
                    }
                }
            }
            return scheduledDates;
        }
    }

    /////////////////////////////////////// set new doctor schedule ///////////////////////////////////////////

    public static void setDoctorSchedule(String doctorID, LocalDate date, LocalTime startTime, LocalTime endTime) {
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            // Find the first empty row
            int rowCount = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowCount); // Create a new row at the end of the sheet

            int newAppointmentID = getNextAppointmentID(sheet);

            // Set the values in the appropriate columns
            row.createCell(0).setCellValue(""); // Set column 1 as empty
            row.createCell(1).setCellValue(doctorID); // Column 2 for Doctor ID
            row.createCell(2).setCellValue(newAppointmentID); // Column 2 for Doctor ID
            row.createCell(3).setCellValue(date.format(dateFormat)); // Column 3 for Date (String format)
            row.createCell(4).setCellValue(startTime.format(timeFormat)); // Column 4 for Time Start
            row.createCell(5).setCellValue(endTime.format(timeFormat)); // Column 5 for Time End
            row.createCell(6).setCellValue("Available"); 
            // Write the changes back to the Excel file

        try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
        }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }

    /////////////////////////////////////// getting Appointment ID///////////////////////////////////////////
    private static int getNextAppointmentID(Sheet sheet) {
        int maxID = 0;
    
        for (Row row : sheet) {
            Cell idCell = row.getCell(2); // Assuming Appointment ID is in the first column
            if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
                int currentID = (int) idCell.getNumericCellValue();
                if (currentID > maxID) {
                    maxID = currentID;
                }
            }
        }
        return maxID + 1; // Increment the max ID for the new appointment
    }

    /////////////////////////////////////// get List of Patients///////////////////////////////////////////
    public static List<String> getPatients(String doctorID) throws IOException {
        Set<String> patientIDs = new HashSet<>(); // Make a set to filter out duplicate IDs
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = DoctorAppointmentDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell patientIDCell = row.getCell(0);
                    Cell doctorIDCell = row.getCell(1);

                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    // Check if the doctorID, then extract the patientID.
                    if (doctorIDCell != null && doctorIDCell.getStringCellValue().equals(doctorID)) {
                        String patientID = patientIDCell != null ? patientIDCell.getStringCellValue() : "";

                        // Filter out empty strings and only add non-empty IDs to the set
                        if (!patientID.isEmpty()) {
                            patientIDs.add(patientID); // Set will automatically handle duplicates
                        }

                    }
                }
            }
        }
        return new ArrayList<>(patientIDs);
    }
}


