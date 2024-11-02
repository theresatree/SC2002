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

    public static int getLastRequestID() throws IOException {
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                int lastRowIndex = sheet.getLastRowNum();

                if (lastRowIndex > 0) {     //header is not counted
                    Row lastRow = sheet.getRow(lastRowIndex);
                    Cell requestIDCell = lastRow.getCell(0);
                    return (int)requestIDCell.getNumericCellValue();
                }

            }
        } catch (IOException e) {
            System.out.println("An error occurred while finding request: " + e.getMessage());
        }        
        return 0; //first request
    }

    public static void saveReplenishmentRequest(ReplenishmentRequest request) throws IOException {
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is); FileOutputStream fos = new FileOutputStream("src/main/resources/" + FILE_NAME)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRow + 1);

            newRow.createCell(0).setCellValue(request.getRequestID());
            newRow.createCell(1).setCellValue(request.getPharmacistID());
            newRow.createCell(2).setCellValue(request.getDateOfRequest().format(dateFormat));
            newRow.createCell(3).setCellValue(request.getMedicine());
            newRow.createCell(4).setCellValue(request.getAmount());
            newRow.createCell(5).setCellValue(request.getStatus().name());

            workbook.write(fos);

        } catch (IOException e) {
            System.out.println("An error occurred while saving the replenishment request: " + e.getMessage());
            throw e;
        }
    }

    public static int numPending() {
        int count=0;
        try (InputStream is = ReplenishmentRequestDB.class.getClassLoader().getResourceAsStream(FILE_NAME); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            for (Row row : sheet) {
                Cell statusCell = row.getCell(5);
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }
                if (RequestStatus.valueOf(statusCell.getStringCellValue()) == RequestStatus.PENDING) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while finding request: " + e.getMessage());
        }
        return count;
    }
}
