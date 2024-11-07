package sc2002.repositories;

import sc2002.models.AppointmentDetails;
import sc2002.models.Diagnosis;
import sc2002.enums.AppointmentStatus;
import sc2002.enums.Service;
import sc2002.enums.PrescriptionStatus;
import sc2002.services.AppointmentOutcomeRecord;

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

/**
 * Provides access to appointment details data stored in an Excel file.
 */
public class AppointmentDetailsDB {

    private static final String FILE_NAME = "Appointment.xlsx";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Retrieves all appointment details from the Excel file.
     * 
     * @return List of AppointmentDetails objects
     * @throws IOException if an I/O error occurs
     */
    public static List<AppointmentDetails> getAppointmentDetails() throws IOException {
        List<AppointmentDetails> appointmentDetails = new ArrayList<>();
        try (InputStream is = AppointmentDetailsDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;  // Skip header row

                String patientID = row.getCell(0).getStringCellValue();
                String doctorID = row.getCell(1).getStringCellValue();
                int appointmentID = (int) row.getCell(2).getNumericCellValue();
                LocalDate date = LocalDate.parse(row.getCell(3).getStringCellValue(), dateFormat);
                LocalTime timeStart = LocalTime.parse(row.getCell(4).getStringCellValue(), timeFormat);
                LocalTime timeEnd = LocalTime.parse(row.getCell(5).getStringCellValue(), timeFormat);
                AppointmentStatus status = AppointmentStatus.valueOf(row.getCell(6).getStringCellValue().toUpperCase());

                AppointmentDetails appointmentDetail = new AppointmentDetails(patientID, doctorID, appointmentID, date, timeStart, timeEnd, status);
                appointmentDetails.add(appointmentDetail);
            }
        }
        return appointmentDetails;
    }

    /**
     * Prints the appointment outcome record for a specific hospital ID and appointment ID.
     * 
     * @param hospitalID the hospital ID of the patient
     * @param appointmentID the appointment ID
     * @throws IOException if an I/O error occurs
     */
    public static void printAppointmentOutcomeRecord(String hospitalID, int appointmentID) throws IOException {
        try (InputStream is = AppointmentOutcomeRecord.class.getClassLoader().getResourceAsStream("Appointment_Outcomes.xlsx"); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;  // Skip header row

                if (row.getCell(0).getStringCellValue().equals(hospitalID) && (int) row.getCell(2).getNumericCellValue() == appointmentID) {
                    Service services = Service.valueOf(row.getCell(4).getStringCellValue().toUpperCase());
                    String medication = row.getCell(5).getStringCellValue().toUpperCase();
                    PrescriptionStatus status = PrescriptionStatus.valueOf(row.getCell(6).getStringCellValue().toUpperCase());
                    String notes = row.getCell(7).getStringCellValue();

                    System.out.println("Services given: " + services + "\nMedication given: " + medication + "\nPrescription Status: " + status + "\nNotes: " + notes);
                    return;
                }
            }
        }
        System.out.println("Appointment outcome record can't be found");
    }
}