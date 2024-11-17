package sc2002.repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sc2002.enums.RequestStatus;
import sc2002.models.ReplenishmentRequest;

/**
 * This class handles operations related to replenishment requests, including retrieving, updating,
 * and saving request details in a CSV file.
 */
public class ReplenishmentRequestDB {

    private static final String FILE_NAME = "resources/Replenishment_Requests.csv"; // CSV file for Replenishment Requests
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Retrieves all replenishment requests with the specified status.
     *
     * @param requestStatus The status of requests to retrieve.
     * @return A list of replenishment requests matching the specified status.
     * @throws IOException If there is an issue reading the file.
     */
    public static List<ReplenishmentRequest> getReplenishmentRequest(RequestStatus requestStatus) throws IOException {
        List<ReplenishmentRequest> replenishmentRequests = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                RequestStatus status = RequestStatus.valueOf(fields[5].toUpperCase());
                if (status.equals(requestStatus)) {
                    int requestID = Integer.parseInt(fields[0]);
                    String pharmacistID = fields[1];
                    LocalDate date = LocalDate.parse(fields[2], dateFormat);
                    String medicine = fields[3];
                    int amount = Integer.parseInt(fields[4]);

                    replenishmentRequests.add(new ReplenishmentRequest(requestID, pharmacistID, date, medicine, amount, status));
                }
            }
        }
        return replenishmentRequests;
    }

    /**
     * Updates the status of a specific replenishment request.
     *
     * @param requestID The ID of the request to update.
     * @param status    The new status for the request.
     * @throws IOException If there is an issue updating the file.
     */
    public static void updateStatus(int requestID, RequestStatus status) throws IOException {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("resources/Temp_Replenishment_Requests.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    bw.write(line);
                    bw.newLine();
                    continue; // Write header row as is
                }

                String[] fields = line.split(",");
                int currentRequestID = Integer.parseInt(fields[0]);
                if (currentRequestID == requestID) {
                    fields[5] = status.name(); // Update status
                }

                bw.write(String.join(",", fields));
                bw.newLine();
            }
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException("Failed to update request status.");
        }
    }

    /**
     * Finds a pending replenishment request by ID.
     *
     * @param requestID The ID of the request to find.
     * @return 1 if the request is pending, 0 otherwise.
     */
    public static int findPendingRequest(int requestID) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                int currentRequestID = Integer.parseInt(fields[0]);
                RequestStatus status = RequestStatus.valueOf(fields[5].toUpperCase());

                if (currentRequestID == requestID && status == RequestStatus.PENDING) {
                    return 1;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while finding request: " + e.getMessage());
        }
        System.out.println("Invalid request ID! Request status is NOT pending!");
        return 0; // Request not found
    }

    /**
     * Gets the last request ID from the file to determine the next available ID.
     *
     * @return The last request ID.
     * @throws IOException If there is an issue reading the file.
     */
    public static int getLastRequestID() throws IOException {
        int lastRequestID = 0;
        boolean isHeader = true; // Add this flag
    
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip header row
                    continue;
                }
    
                String[] fields = line.split(",");
                lastRequestID = Integer.parseInt(fields[0]); // Update with the latest request ID
            }
        }
        return lastRequestID; // Return the last request ID
    }

    /**
     * Saves a new replenishment request to the file.
     *
     * @param request The replenishment request to save.
     * @throws IOException If there is an issue writing to the file.
     */
    public static void saveReplenishmentRequest(ReplenishmentRequest request) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(String.join(",",
                String.valueOf(request.getRequestID()),
                request.getPharmacistID(),
                request.getDateOfRequest().format(dateFormat),
                request.getMedicine(),
                String.valueOf(request.getAmount()),
                request.getStatus().name()));
            bw.newLine();
        }
    }

    /**
     * Counts the number of pending replenishment requests.
     *
     * @return The count of pending requests.
     */
    public static int numPending() {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                if (RequestStatus.valueOf(fields[5].toUpperCase()) == RequestStatus.PENDING) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while counting pending requests: " + e.getMessage());
        }
        return count;
    }
}