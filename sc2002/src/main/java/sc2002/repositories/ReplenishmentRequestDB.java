package sc2002.repositories;

import sc2002.models.ReplenishmentRequest;
import sc2002.enums.RequestStatus;

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

/**
 * This class handles operations related to replenishment requests, including retrieving, updating,
 * and saving request details from an Excel file.
 */
public class ReplenishmentRequestDB {

    private static final String FILE_NAME = "Replenishment_Requests.xlsx"; // Fixed file location for Replenishment Requests
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Retrieves all replenishment requests with the specified status.
     * @param requestStatus The status of requests to retrieve.
     * @return A list of replenishment requests matching the specified status.
     * @throws IOException If there is an issue reading the file.
     */
    public static List<ReplenishmentRequest> getReplenishmentRequest(RequestStatus requestStatus) throws IOException {
        List<ReplenishmentRequest> replenishmentRequests = new ArrayList<>();
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) throw new IOException("File not found in resources: " + FILE_NAME);

            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Skip header row
                    Cell statusCell = row.getCell(5);
                    if (RequestStatus.valueOf(statusCell.getStringCellValue().toUpperCase()).equals(requestStatus)) {
                        int requestID = (int) row.getCell(0).getNumericCellValue();
                        String pharmacistID = row.getCell(1).getStringCellValue().toUpperCase();
                        LocalDate date = LocalDate.parse(row.getCell(2).getStringCellValue(), dateFormat);
                        String medicine = row.getCell(3).getStringCellValue();
                        int amount = (int) row.getCell(4).getNumericCellValue();
                        RequestStatus status = RequestStatus.valueOf(statusCell.getStringCellValue().toUpperCase());
                        replenishmentRequests.add(new ReplenishmentRequest(requestID, pharmacistID, date, medicine, amount, status));
                    }
                }
            }
        }
        return replenishmentRequests;
    }

    /**
     * Updates the status of a specific replenishment request.
     * @param requestID The ID of the request to update.
     * @param status The new status for the request.
     * @throws IOException If there is an issue updating the file.
     */
    public static void updateStatus(int requestID, RequestStatus status) throws IOException {
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                if ((int) row.getCell(0).getNumericCellValue() == requestID) {
                    row.getCell(5).setCellValue(status.name());
                    break;
                }
            }
            try (FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
                workbook.write(fos);
            }
        }
    }

    /**
     * Finds a pending replenishment request by ID.
     * @param requestID The ID of the request to find.
     * @return 1 if the request is pending, 0 otherwise.
     */
    public static int findPendingRequest(int requestID) {
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                if ((int) row.getCell(0).getNumericCellValue() == requestID) {
                    if (RequestStatus.valueOf(row.getCell(5).getStringCellValue().toUpperCase()) == RequestStatus.PENDING) {
                        return 1;
                    }
                    return 0;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while finding request: " + e.getMessage());
        }
        return 0; // Request not found
    }

    /**
     * Gets the last request ID from the file to determine the next available ID.
     * @return The last request ID.
     * @throws IOException If there is an issue reading the file.
     */
    public static int getLastRequestID() throws IOException {
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowIndex = sheet.getLastRowNum();
            if (lastRowIndex > 0) {
                return (int) sheet.getRow(lastRowIndex).getCell(0).getNumericCellValue();
            }
        }
        return 0; // First request if no data exists
    }

    /**
     * Saves a new replenishment request to the file.
     * @param request The replenishment request to save.
     * @throws IOException If there is an issue writing to the file.
     */
    public static void saveReplenishmentRequest(ReplenishmentRequest request) throws IOException {
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is);
             FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            newRow.createCell(0).setCellValue(request.getRequestID());
            newRow.createCell(1).setCellValue(request.getPharmacistID());
            newRow.createCell(2).setCellValue(request.getDateOfRequest().format(dateFormat));
            newRow.createCell(3).setCellValue(request.getMedicine());
            newRow.createCell(4).setCellValue(request.getAmount());
            newRow.createCell(5).setCellValue(request.getStatus().name());
            workbook.write(fos);
        }
    }

    /**
     * Counts the number of pending replenishment requests.
     * @return The count of pending requests.
     */
    public static int numPending() {
        int count = 0;
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                if (RequestStatus.valueOf(row.getCell(5).getStringCellValue()) == RequestStatus.PENDING) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while finding request: " + e.getMessage());
        }
        return count;
    }
}