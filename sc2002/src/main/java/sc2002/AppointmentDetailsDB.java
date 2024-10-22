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

public class AppointmentDetailsDB {

    private static final String FILE_NAME = "Appointment.xlsx"; //fixed file location for Appointment.xlsx
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    public static List<AppointmentDetails> getAppointmentDetails() throws IOException {
        List<AppointmentDetails> appointmentDetails = new ArrayList<>();
        try (InputStream is = AppointmentDetails.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell patientIDCell = row.getCell(0);
                Cell doctorIDCell = row.getCell(1);
                Cell appointmentIDCell = row.getCell(2);
                Cell dateCell = row.getCell(3);
                Cell timeStartCell = row.getCell(4);
                Cell timeEndCell = row.getCell(5);
                Cell statusCell = row.getCell(6);

                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                String patientID = patientIDCell.getStringCellValue();
                String doctorID = doctorIDCell.getStringCellValue();
                int appointmentID = (int) appointmentIDCell.getNumericCellValue();
                LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                LocalTime timeStart = LocalTime.parse(timeStartCell.getStringCellValue(), timeFormat);
                LocalTime timeEnd = LocalTime.parse(timeEndCell.getStringCellValue(), timeFormat);
                AppointmentStatus status = AppointmentStatus.valueOf(statusCell.getStringCellValue().toUpperCase());

                AppointmentDetails appointmentDetail = new AppointmentDetails(patientID, doctorID, appointmentID, date, timeStart, timeEnd, status);
                appointmentDetails.add(appointmentDetail);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while getting the staff info: " + e.getMessage());
        }
        return appointmentDetails;
    }

    public static void printAppointmentOutcomeRecord(String hospitalID, int appointmentID) throws IOException {
        try (InputStream is = AppointmentOutcomeRecord.class.getClassLoader().getResourceAsStream("Appointment_Outcomes.xlsx"); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell patientIDCell = row.getCell(0);
                Cell appointmentIDCell = row.getCell(2);
                Cell servicesCell = row.getCell(4);
                Cell medicationCell = row.getCell(5);
                Cell prescriptionStatusCell = row.getCell(6);
                Cell notesCell = row.getCell(7);

                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                if (patientIDCell != null && patientIDCell.getStringCellValue().equals(hospitalID) && appointmentIDCell.getNumericCellValue() == appointmentID) {
                    Service services = Service.valueOf(servicesCell.getStringCellValue().toUpperCase());
                    String medication = medicationCell.getStringCellValue().toUpperCase();
                    PrescriptionStatus status = PrescriptionStatus.valueOf(prescriptionStatusCell.getStringCellValue().toUpperCase());
                    String notes = notesCell.getStringCellValue();

                    System.out.println("Services given: " + services + "\n"
                            + "Medication given: " + medication + "\n"
                            + "Prescription Status: " + status + "\n"
                            + "Notes: " + notes);

                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while getting the staff info: " + e.getMessage());
        }
        System.out.println("Appointment outcome record can't be found");
    }
}
