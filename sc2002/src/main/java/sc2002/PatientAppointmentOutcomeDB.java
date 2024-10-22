package sc2002;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PatientAppointmentOutcomeDB {
       private static final String FILE_NAME = "Appointment_Outcomes.xlsx"; //fixed file location for Patient_List.xlsx
       static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //////////////////////////////////////// Get Diagnosis Detail ////////////////////////////////////////
    public static List<AppointmentOutcomeRecord> getAppointmentOutcome(String hospitalID) throws IOException {
        List<AppointmentOutcomeRecord> appointmentOutcome = new ArrayList<>(); // List to hold multiple diagnoses
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = PatientAppointmentOutcomeDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
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
                    Cell servicesCell = row.getCell(4);
                    Cell medicationCell = row.getCell(5);
                    Cell prescriptionStatusCell = row.getCell(6);
                    Cell notesCell = row.getCell(7);



                    if (row.getRowNum() == 0) continue;                    // Skip header row
                    
                    if (patientIDCell != null && patientIDCell.getStringCellValue().equals(hospitalID)) {
                        String patientID = patientIDCell.getStringCellValue();
                        String doctorID = doctorIDCell.getStringCellValue();
                        int appointment = (int) appointmentIDCell.getNumericCellValue();
                        String date = dateCell.getStringCellValue();
                        Service services = Service.valueOf(servicesCell.getStringCellValue().toUpperCase());
                        Medicine medication = Medicine.valueOf(medicationCell.getStringCellValue().toUpperCase());
                        PrescriptionStatus status = PrescriptionStatus.valueOf(prescriptionStatusCell.getStringCellValue().toUpperCase());
                        String notes = notesCell.getStringCellValue();

                        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(patientID, doctorID, appointment, date, services, medication, status, notes);
                        appointmentOutcome.add(outcome);
                    }
                }
            }
        }
        return appointmentOutcome;
    }

    //////////////////////////////////////// Set PatientAppointmentOucome ////////////////////////////////////////
    public static void setAppointmentOutcome(String patientID, String doctorID, int appointmentID, LocalDate date, Service services, Medicine medication, String notes){
        try (InputStream is = PatientAppointmentOutcomeDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
        Workbook workbook = new XSSFWorkbook(is)) {
        Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

        // Find the first empty row
        int rowCount = sheet.getPhysicalNumberOfRows();
        Row row = sheet.createRow(rowCount); // Create a new row at the end of the sheet

        // Set the values in the appropriate columns
        row.createCell(0).setCellValue(patientID);
        row.createCell(1).setCellValue(doctorID);
        row.createCell(2).setCellValue(appointmentID); 
        row.createCell(3).setCellValue(date.format(dateFormat)); 
        row.createCell(4).setCellValue(services.name()); 
        row.createCell(5).setCellValue(medication.name());
        row.createCell(6).setCellValue("Pending");
        row.createCell(7).setCellValue(notes);
        // Write the changes back to the Excel file

    try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
            workbook.write(fos);
    }

    } catch (IOException e) {
        e.printStackTrace(); // Handle the exception as appropriate
    }
}

}
