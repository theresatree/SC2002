package sc2002.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import sc2002.enums.RequestStatus;
import sc2002.enums.Role;
import sc2002.repositories.UserDB;

/**
 * Represents a request for replenishing medication inventory.
 */
public class ReplenishmentRequest {

    private int requestID;
    private String pharmacistID;
    private LocalDate dateOfRequest;
    private String medicine;
    private int amount;
    private RequestStatus status;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructs a ReplenishmentRequest with specified details.
     *
     * @param requestID the unique ID of the request
     * @param pharmacistID the ID of the pharmacist making the request
     * @param dateOfRequest the date of the request
     * @param medicine the medicine requested for replenishment
     * @param amount the quantity requested
     * @param status the status of the request
     */
    public ReplenishmentRequest(int requestID, String pharmacistID, LocalDate dateOfRequest, String medicine, int amount, RequestStatus status) {
        this.requestID = requestID;
        this.pharmacistID = pharmacistID;
        this.dateOfRequest = dateOfRequest;
        this.medicine = medicine;
        this.amount = amount;
        this.status = status;
    }

    /**
     * Gets the unique ID of the replenishment request.
     *
     * @return the request ID
     */
    public int getRequestID() {
        return requestID;
    }

    /**
     * Gets the ID of the pharmacist who made the request.
     *
     * @return the pharmacist ID
     */
    public String getPharmacistID() {
        return pharmacistID;
    }

    /**
     * Gets the date when the replenishment request was made.
     *
     * @return the date of the request
     */
    public LocalDate getDateOfRequest() {
        return dateOfRequest;
    }

    /**
     * Gets the name of the medicine requested for replenishment.
     *
     * @return the medicine name
     */
    public String getMedicine() {
        return medicine;
    }

    /**
     * Gets the amount of the medicine requested for replenishment.
     *
     * @return the quantity of the medicine
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets the status of the replenishment request.
     *
     * @return the request status
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * Prints the details of the replenishment request in a formatted string.
     *
     * @return a string containing the replenishment request details
     */
    public String printReplenishmentRequest() {
        return ("Request ID: " + requestID + "\n"
                + "Pharmacist ID: " + pharmacistID + " (" + UserDB.getNameByHospitalID(pharmacistID, Role.PHARMACIST) + ")" + "\n"
                + "Date: " + dateOfRequest.format(dateFormat) + "\n"
                + "Medicine: " + medicine + "\n"
                + "Amount: " + amount + "\n"
                + "Status: " + status + "\n");
    }
}