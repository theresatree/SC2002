package sc2002;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PatientAppointmentDB {
        private static final String FILE_NAME = "Appointment.xlsx"; //fixed file location for Patient_List.xlsx
        static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

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
}
