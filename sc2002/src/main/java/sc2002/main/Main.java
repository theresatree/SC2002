package sc2002.main;

import java.io.IOException;
import java.util.Scanner;

import sc2002.controllers.Administrator;
import sc2002.controllers.Doctor;
import sc2002.controllers.Patient;
import sc2002.controllers.Pharmacist;
import sc2002.models.User;
import sc2002.repositories.DoctorAppointmentDB;
import sc2002.repositories.MedicationInventoryDB;
import sc2002.repositories.ReplenishmentRequestDB;


/**
 * The main entry point for the hospital management system.
 * Handles user login, registration, and role-specific dashboards.
 */
public class Main {
    /**
     * Main method to initialize and manage the application's workflow.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) { 
    while (true){
        boolean mainMenuExit = false;
        Scanner inputScanner = new Scanner(System.in);
        User user = null; // Set user to null
        boolean logout = false; // This is to facilitate logout
        boolean register = false;
        int choice; // This is for all the selection choices.

        while (!mainMenuExit){
            System.out.println("=========================================");
            System.out.println("                Main Menu");
            System.out.println("=========================================");
            System.out.println("1. Login");
            System.out.println("2. Register as Patient");
            System.out.println("=========================================");
            choice = getValidChoice(inputScanner,2);

            switch (choice){
        //////////////////////////////////////////// LOGIN SECTION /////////////////////////////////////////////////
                case 1:
                    user = new User(""); // Placeholder User object to invoke login
                    user = user.login(inputScanner);
        
                    if (user != null) { // Successful login
                        mainMenuExit = true; // Optional: Exit main menu loop if necessary
                    }
                    break;
        //////////////////////////////////////////// REGISTER SECTION //////////////////////////////////////////////////
                case 2:
                    user.register(inputScanner);
                    break;
         //////////////////////////////////////////// OUT OF RANGE SECTION //////////////////////////////////////////////////
                default:
                System.out.println("Unexpected error occurred.");
                    break;
                }
                
            }

///////////////////////////////////// ROLE-SPECIFIC FUNCTIONS ////////////////////////////////////
        
    switch (user.getRole()) {
///////////////////////////////////// DOCTOR ////////////////////////////////////
            case DOCTOR:

                Doctor doctor = new Doctor(user.getHospitalID());

                while (!logout){
                    try {
                    System.out.println("Redirecting to Doctor's dashboard...");
                    System.out.println("=========================================");
                    System.out.println("1. View Patient Medical Records");
                    System.out.println("2. Update Patient Medical Records");
                    System.out.println("3. View Personal Schedule");
                    System.out.println("4. Set Availability for Appointments");
                    System.out.println("5. Accept or Decline Appointment Requests " + DoctorAppointmentDB.numberOfPending(user.getHospitalID()));
                    System.out.println("6. View Upcoming Appointments");
                    System.out.println("7. Record Appointment Outcome");
                    System.out.println("8. Logout");
                    System.out.println("=========================================");
                    System.out.print("Select a choice: ");

                    choice = getValidChoice(inputScanner,8);

                    switch (choice){
                        case 1: 
                            doctor.viewPatientMedicalRecord(inputScanner);
                            waitForEnter(inputScanner);
                            break;
                        case 2:
                            doctor.updatePatientMedicalRecord(inputScanner);
                            break;
                        case 3:
                            doctor.viewPersonalSchedule();
                            waitForEnter(inputScanner);
                            break;  
                        case 4:
                            doctor.setAvailabilityDate(inputScanner);
                            waitForEnter(inputScanner);
                            break;
                        case 5:
                            doctor.acceptDeclineAppointment(inputScanner);
                            break;     
                        case 6:
                            doctor.viewAppointmentStatus();
                            waitForEnter(inputScanner);
                            break;
                        case 7:
                            doctor.createAppointmentRecord(inputScanner);
                            break;    
                        case 8: 
                            logout = user.logOut();
                            break;  
                        default:
                            System.out.println("Unexpected error occurred.");
                            break;  
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                }

            break;
///////////////////////////////////// PHARMACIST ////////////////////////////////////
            case PHARMACIST:

                Pharmacist pharmacist = new Pharmacist(user.getHospitalID());
                while (!logout){
                    System.out.println("Redirecting to Pharmacist's dashboard...");
                    System.out.println("=========================================");
                    System.out.println("1. View Appointment Outcome Record");
                    System.out.println("2. Update Prescription Status");
                    System.out.println("3. View Medication Inventory");
                    try {
                        System.out.println("4. Submit Replenishment Request (" + MedicationInventoryDB.lowStockLevelAlert() + " LOW STOCK)");
                    } catch (IOException e) {
                        System.out.println("An error occurred while checking for low stock levels: " + e.getMessage());
                    }
                    
                    System.out.println("5. Logout");
                    System.out.println("=========================================");
                    System.out.print("Select a choice: ");

                    choice = getValidChoice(inputScanner,5);

                    switch (choice){
                        case 1: 
                            pharmacist.viewPastAppointmentOutcome(inputScanner);
                            waitForEnter(inputScanner);
                            break;
                        case 2:
                            pharmacist.updatePrescriptionStatus(inputScanner);
                            break;
                        case 3:
                            pharmacist.viewMedicationInventory();
                            waitForEnter(inputScanner);
                            break;  
                        case 4:
                            pharmacist.viewMedicationInventory();
                            pharmacist.submitReplenishmentRequest(inputScanner);
                            waitForEnter(inputScanner);
                            break;
                        case 5:
                            logout = user.logOut();
                            break;  
                        default:
                            System.out.println("Unexpected error occurred.");
                            break;
                        }
                }

            break;
///////////////////////////////////// ADMININSTRATOR ////////////////////////////////////
            case ADMINISTRATOR:
                Administrator admin = new Administrator(user.getHospitalID());
                while (!logout){
                    System.out.println("Redirecting to Administrator's dashboard...");
                    System.out.println("=========================================");
                    System.out.println("1. View Hospital Staff");
                    System.out.println("2. Manage Hospital Staff");
                    System.out.println("3. View Appointment Details");
                    System.out.println("4. View and Manage Medication Inventory");
                    System.out.println("5. Approve Replenishment Requests (" + ReplenishmentRequestDB.numPending() + " NEW REQUEST)");
                    System.out.println("6. Logout");
                    System.out.println("=========================================");
                    System.out.print("Select a choice: ");

                    choice = getValidChoice(inputScanner,6);

                    switch (choice){
                        case 1: 
                            admin.viewHospitalStaff();
                            waitForEnter(inputScanner);
                            break;
                        case 2:
                            System.out.println("\n\n=========================================");
                            System.out.println("          Manage Hospital Staff");
                            System.out.println("=========================================");
                            System.out.println("1. Add Hospital Staff");
                            System.out.println("2. Update Hospital Staff");
                            System.out.println("3. Remove Hospital Staff");
                            System.out.println("=========================================");
                            System.out.print("Select a choice (or 0 to exit): ");

                            while (true) {
                                if (inputScanner.hasNextInt()) {
                                    choice = inputScanner.nextInt();
                                    inputScanner.nextLine(); // Consume the newline
                                    if (choice >= 0 && choice <= 3) {
                                        break;
                                    } else {
                                        System.out.print("Invalid option. Please enter a number between 0 and 3: ");
                                    }
                                } else {
                                    System.out.print("Invalid input. Please enter a valid integer: ");
                                    inputScanner.nextLine(); // Clear the invalid input
                                }
                            }

                            switch (choice){
                                case 0:
                                    System.out.println("\n\nExiting process");
                                    break;
                                case 1:
                                    admin.addHospitalStaff();
                                    break;
                                case 2:
                                    admin.updateHospitalStaff();
                                    break;
                                case 3:
                                    admin.removeHospitalStaff();
                                    break;
                                case 4:
                                    break;
                                default:
                                    System.out.println("Unexpected error occurred.");
                                    break;
                            }
                            System.out.println("\n\n");
                            break;
                        case 3:
                            admin.viewAppointmentDetails();
                            waitForEnter(inputScanner);
                            break;  
                        case 4:
                            admin.viewAndManangeMedicationInventory();
                            break;  
                        case 5:
                            admin.approveReplenishmentRequests();
                            break;
                        case 6:
                            logout = user.logOut();
                            break;  
                        default:
                            System.out.println("Unexpected error occurred.");
                            break;
                        }
                }

            break;
///////////////////////////////////// PATIENT ////////////////////////////////////
            case PATIENT:
                Patient patient = new Patient(user.getHospitalID());

                while (!logout){
                    System.out.println("Redirecting to Patient's dashboard...");
                    System.out.println("=========================================");
                    System.out.println("1. View Medical Record");
                    System.out.println("2. Update Personal Information");
                    System.out.println("3. View Available Appointment Slots");
                    System.out.println("4. Schedule an Appointment");
                    System.out.println("5. Reschedule an Appointment");
                    System.out.println("6. Cancel an Appointment");
                    System.out.println("7. View Scheduled Appointments");
                    System.out.println("8. View Past Appointment Outcome Records");
                    System.out.println("9. Logout");
                    System.out.println("=========================================");
                    System.out.print("Select a choice: ");

                    choice = getValidChoice(inputScanner,9);

                    switch (choice){
                        case 1: 
                            patient.viewMedicalRecord();
                            waitForEnter(inputScanner);
                            break;
                        case 2:
                            patient.updatePersonalInfo(inputScanner);
                            break;
                        case 3:
                            patient.viewAvailableAppointmentSlots();
                            waitForEnter(inputScanner);
                            break;  
                        case 4:
                            patient.scheduleAppointment(inputScanner);
                            break;
                        case 5:
                            patient.rescheduleAppointment(inputScanner);
                            break;     
                        case 6:
                            patient.cancelAppointment(inputScanner);
                            break;
                        case 7:
                            patient.viewScheduledAppointments();
                            waitForEnter(inputScanner);
                            break;    
                        case 8:
                            patient.viewPastAppointmentOutcomeRecords();
                            waitForEnter(inputScanner);
                            break;
                        case 9: 
                            logout = user.logOut();
                            break;  
                        default:
                            System.out.println("Unexpected error occurred.");
                            break;
                        }
                }

            break;
///////////////////////////////////// UNKNOWN ROLE ////////////////////////////////////
            default:
                System.out.println("Unknown role.");
                break;
        }
    }
}


///////////////////////////////////// Function to check valid input for dashboard ////////////////////////////////////
    public static int getValidChoice(Scanner scanner, int maxChoice) {
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (choice >= 1 && choice <= maxChoice) {
                    return choice;
                } else {
                    System.out.print("Invalid input. Please enter a number between 1 and " + maxChoice + ": ");
                }
            } else {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    /**
     * Waits for the user to press Enter to continue.
     *
     * @param scanner Scanner object for user input.
     */
    public static void waitForEnter(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }



    /**
     * Validates a numeric input string for digit length and range.
     *
     * @param scanner  Scanner object for user input.
     * @param maxDigit Expected number of digits in the input.
     * @param range    Maximum allowed range for the input.
     * @return A valid numeric input string.
     */
    public static String digitChecker(Scanner scanner, int maxDigit, int range){
        while (true){
            String number = scanner.nextLine();
            if (number.equalsIgnoreCase("EXIT")){
                return number;
            }
            if (number.length() == maxDigit && number.matches("\\d+")){ // Check for 8 digits and integer.
                int rangeChecker = Integer.parseInt(number);
            
                if (range == -1 || (rangeChecker >= 1 && rangeChecker <= range)) {  // check the range
                    return number;
                } else {
                    System.out.print("Invalid input. Please enter a number from 1 to " + range + ": ");
                }
            } else {
                System.out.print("Invalid input. Please enter a number with exactly " + String.valueOf(maxDigit) + " digits: ");
            }
        }
    }
}