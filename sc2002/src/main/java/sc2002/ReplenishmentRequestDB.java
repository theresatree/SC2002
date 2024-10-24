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

public class ReplenishmentRequestDB {

    private static final String FILE_NAME = "Replenishment_Requests.xlsx"; //fixed file location for Replenishment_Requests.xlsx
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //////////////////////////////////////// Get Replenishment Requests ////////////////////////////////////////
    public static List<ReplenishmentRequest> getReplenishmentRequest(RequestStatus requestStatus) throws IOException {
        List<ReplenishmentRequest> replenishmentRequests = new ArrayList<>();
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {

                    Cell requestIDCell = row.getCell(0);
                    Cell pharmacistIDCell = row.getCell(1);
                    Cell dateCell = row.getCell(2);
                    Cell medicineCell = row.getCell(3);
                    Cell amountCell = row.getCell(4);
                    Cell statusCell = row.getCell(5);

                    if (row.getRowNum() == 0) {
                        continue; // Skip header row
                    }
                    if (pharmacistIDCell != null && RequestStatus.valueOf(statusCell.getStringCellValue().toUpperCase()).equals(requestStatus)) {
                        int requestID = (int) requestIDCell.getNumericCellValue();
                        String pharmacistID = pharmacistIDCell.getStringCellValue().toUpperCase();
                        LocalDate date = LocalDate.parse(dateCell.getStringCellValue(), dateFormat);
                        String medicine = medicineCell.getStringCellValue();
                        int amount = (int) amountCell.getNumericCellValue();
                        RequestStatus status = RequestStatus.valueOf(statusCell.getStringCellValue().toUpperCase());
                        ReplenishmentRequest requests = new ReplenishmentRequest(requestID, pharmacistID, date, medicine, amount, status);
                        replenishmentRequests.add(requests);
                    }
                }
            }
        }
        return replenishmentRequests;
    }

    //////////////////////////////// Update status to approved /////////////////////////////////
    public static void updateStatus(int requestID, RequestStatus status) throws IOException {
        try (InputStream is = MedicationInventoryDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell requestIDCell = row.getCell(0);
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }
                if ((int) requestIDCell.getNumericCellValue() == requestID) {
                    Cell statusCell = row.getCell(5);
                    statusCell.setCellValue(status.name());
                    break;
                }
            }

            // Write the updated workbook back to the file
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while changing status: " + e.getMessage());
        }
    }

    public static int findPendingRequest(int requestID) {
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell requestIDCell = row.getCell(0);
                Cell statusCell = row.getCell(5);
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }
                if ((int) requestIDCell.getNumericCellValue() == requestID) {
                    if (RequestStatus.valueOf(statusCell.getStringCellValue().toUpperCase()).equals(RequestStatus.PENDING)) {
                        return 1;
                    } else {
                        System.out.println("Request is completed. Please try again!\n");
                        return 0;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while finding request: " + e.getMessage());
        }
        System.out.println("Request not found. Please try again!\n");
        return 0; //not found
    }
}
