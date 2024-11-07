package sc2002.repositories;

import sc2002.services.AppointmentOutcomeRecord;
import sc2002.enums.PrescriptionStatus;
import sc2002.enums.Service;
import sc2002.models.ContactInformation;
import sc2002.models.MedicalRecord;
import sc2002.enums.Role;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The PatientAppointmentOutcomeDB class handles retrieving, setting, and updating
 * patient appointment outcomes in the database.
 */
public class PatientAppointmentOutcomeDB {

    private static final String FILE_NAME = "Appointment_Outcomes.xlsx";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Retrieves the appointment outcome for a specific patient.
     *
     * @param hospitalID the ID of the patient
     * @return a list of appointment outcome records for the patient
     * @throws IOException if an error occurs while reading the file
     */
    public static List<AppointmentOutcomeRecord> getAppointmentOutcome(String hospitalID) throws IOException {
        List<AppointmentOutcomeRecord> appointmentOutcome = new ArrayList<>();
        try (InputStream is = PatientAppointmentOutcomeDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) throw new IOException("File not found in resources: " + FILE_NAME);

            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;

                    Cell patientIDCell = row.getCell(0);
                    if (patientIDCell != null && patientIDCell.getStringCellValue().equals(hospitalID)) {
                        String doctorID = row.getCell(1).getStringCellValue();
                        int appointment = (int) row.getCell(2).getNumericCellValue();
                        LocalDate date = LocalDate.parse(row.getCell(3).getStringCellValue(), dateFormat);
                        Service services = Service.valueOf(row.getCell(4).getStringCellValue().toUpperCase());
                        String medication = row.getCell(5).getStringCellValue();
                        PrescriptionStatus status = PrescriptionStatus.valueOf(row.getCell(6).getStringCellValue().toUpperCase());
                        String notes = row.getCell(7).getStringCellValue();

                        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(hospitalID, doctorID, appointment, date, services, medication, status, notes);
                        appointmentOutcome.add(outcome);
                    }
                }
            }
        }
        return appointmentOutcome;
    }

    /**
     * Sets a new appointment outcome in the database.
     *
     * @param patientID the patient ID
     * @param doctorID the doctor ID
     * @param appointmentID the appointment ID
     * @param date the date of the appointment
     * @param services the services provided
     * @param medication the medication prescribed
     * @param notes additional notes on the appointment outcome
     */
    public static void setAppointmentOutcome(String patientID, String doctorID, int appointmentID, LocalDate date, Service services, String medication, String notes) {
        try (InputStream is = PatientAppointmentOutcomeDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(rowCount);

            row.createCell(0).setCellValue(patientID);
            row.createCell(1).setCellValue(doctorID);
            row.createCell(2).setCellValue(appointmentID);
            row.createCell(3).setCellValue(date.format(dateFormat));
            row.createCell(4).setCellValue(services.name());
            row.createCell(5).setCellValue(medication);
            row.createCell(6).setCellValue("Pending");
            row.createCell(7).setCellValue(notes);

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
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