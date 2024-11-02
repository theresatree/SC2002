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

                     // Debug information

                    if (patientIDCell != null && patientIDCell.getStringCellValue().equals(hospitalID)) {
                        String patientID = patientIDCell.getStringCellValue();
                        String doctorID = doctorIDCell.getStringCellValue();
                        int appointment = (int) appointmentIDCell.getNumericCellValue();
                        LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                        Service services = Service.valueOf(servicesCell.getStringCellValue().toUpperCase());
                        String medication = medicationCell.getStringCellValue();
                        PrescriptionStatus status = PrescriptionStatus.valueOf(prescriptionStatusCell.getStringCellValue().toUpperCase());
                        String notes = notesCell.getStringCellValue();

                        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(patientID, doctorID, appointment, date, services, medication, status, notes);
                        appointmentOutcome.add(outcome);
                    }
                }
            } catch(Exception e){
                System.out.println("Error at specific field conversion:");
                e.printStackTrace();
            }
        }
        return appointmentOutcome;
    }

    //////////////////////////////////////// Set PatientAppointmentOucome ////////////////////////////////////////
    public static void setAppointmentOutcome(String patientID, String doctorID, int appointmentID, LocalDate date, Service services, String medication, String notes){
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
        row.createCell(5).setCellValue(medication);
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

    public static void updateAppointmentOutcome(List<AppointmentOutcomeRecord> updatedStatus) throws IOException {
        try (InputStream is = PatientAppointmentOutcomeDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell patientIDCell = row.getCell(0);
                if (row.getRowNum() == 0) continue; // Skip header row

                String patientID = patientIDCell.getStringCellValue();
                int appointmentID = (int) row.getCell(2).getNumericCellValue();

                // Find the matching row in the Excel sheet and update the prescription status
                for (AppointmentOutcomeRecord updatedRecord : updatedStatus) {
                    if (updatedRecord.getPatientID().equals(patientID) && updatedRecord.getAppointmentID() == appointmentID) {
                        // Update the prescription status cell
                        Cell prescriptionStatusCell = row.getCell(6);
                        prescriptionStatusCell.setCellValue(updatedRecord.getPrescriptionStatus().name());
                    }
                }
            }

            // Write changes back to the file
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<AppointmentOutcomeRecord> getAllAppointmentOutcomes() throws IOException {
        List<AppointmentOutcomeRecord> appointmentOutcome = new ArrayList<>();
        try (InputStream is = PatientAppointmentOutcomeDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }
    
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); 
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; 
                    
                    Cell patientIDCell = row.getCell(0);
                    Cell doctorIDCell = row.getCell(1);
                    Cell appointmentIDCell = row.getCell(2);
                    Cell dateCell = row.getCell(3);
                    Cell servicesCell = row.getCell(4);
                    Cell medicationCell = row.getCell(5);
                    Cell prescriptionStatusCell = row.getCell(6);
                    Cell notesCell = row.getCell(7);
                    
                    if (patientIDCell != null) {
                        String patientID = patientIDCell.getStringCellValue();
                        String doctorID = doctorIDCell != null ? doctorIDCell.getStringCellValue() : "";
                        int appointmentID = (int) appointmentIDCell.getNumericCellValue();
                        LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                        Service services = Service.valueOf(servicesCell.getStringCellValue().toUpperCase());
                        String medication = medicationCell != null ? medicationCell.getStringCellValue() : "";
                        PrescriptionStatus status = PrescriptionStatus.valueOf(prescriptionStatusCell.getStringCellValue().toUpperCase());
                        String notes = notesCell != null ? notesCell.getStringCellValue() : "";
    
                        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(patientID, doctorID, appointmentID, date, services, medication, status, notes);
                        appointmentOutcome.add(outcome);
                    }
                }
            }
        }
        return appointmentOutcome;
    }    

}
