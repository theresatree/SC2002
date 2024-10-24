package sc2002;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReplenishmentRequest {

    private int requestID;
    private String pharmacistID;
    private LocalDate dateOfRequest;
    private String medicine;
    private int amount;
    private RequestStatus status;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReplenishmentRequest(int requestID, String pharmacistID, LocalDate dateOfRequest, String medicine, int amount, RequestStatus status) {
        this.requestID = requestID;
        this.pharmacistID = pharmacistID;
        this.dateOfRequest = dateOfRequest;
        this.medicine = medicine;
        this.amount = amount;
        this.status = status;
    }

    public int getRequestID() {
        return requestID;
    }

    public String getPharmacistID() {
        return pharmacistID;
    }

    public LocalDate getDateOfRequest() {
        return dateOfRequest;
    }

    public String getMedicine() {
        return medicine;
    }

    public int getAmount() {
        return amount;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String printReplenishmentRequest() {
        return ("Request ID: " + requestID + "\n"
                + "Pharmacist ID: " + pharmacistID + " (" + UserDB.getNameByHospitalID(pharmacistID, Role.PHARMACIST) + ")" + "\n"
                + "Date: " + dateOfRequest.format(dateFormat) + "\n"
                + "Medicine: " + medicine + "\n"
                + "Amount: " + amount + "\n"
                + "Status: " + status + "\n");
    }
}
