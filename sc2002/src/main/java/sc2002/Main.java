package sc2002;
import java.io.IOException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
////////////////////////////////////// LOGIN ////////////////////////////////////// 
        boolean login = false;
        Scanner inputScanner = new Scanner(System.in);
        User user = null;
        boolean logout = false; // This is to facilitate logout
        int choice; // This is for tDhe dashboard later.
        String filePath = ""; // This is to access either Patient_List.xlsx or Staff_List.xlsx depending on the HospitalID.

        while (!login){ // Make sure the user is logged-in before continuing
            try {
                System.out.print("Enter Hospital ID: ");
                String hospitalID = inputScanner.nextLine().trim();

                System.out.print("Enter Password: ");
                String password = inputScanner.nextLine().trim();

                // Validate the login credentials
                login = UserDB.validateLogin(hospitalID, password);
                if (login) {
                    System.out.println("\n\nLogin successful!");
                    user = new User(hospitalID); // Create an instance of User when logged in.
                    filePath = user.getFilePath();
                    System.out.println("Welcome back " + user.getRole() + " " + UserDB.getNameByHospitalID(hospitalID, filePath));
                    login=true;
                } else {
                    System.out.println("Invalid Hospital ID or Password.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while loading user data: " + e.getMessage());
            }
        }

///////////////////////////////////// ROLE-SPECIFIC FUNCTIONS ////////////////////////////////////
        
    switch (user.getRole()) {
///////////////////////////////////// DOCTOR ////////////////////////////////////
            case DOCTOR:

                Doctor doctor = new Doctor(user.getHospitalID());

                while (!logout){
                    System.out.println("Redirecting to Doctor's dashboard...");
                    System.out.println("=========================================");
                    System.out.println("1. View Patient Medical Records");
                    System.out.println("2. Update Patient Medical Records");
                    System.out.println("3. View Personal Schedule");
                    System.out.println("4. Set Availability for Appointments");
                    System.out.println("5. Accept or Decline Appointment Requests");
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
                            break;  
                        case 4:
                            doctor.setAvailabilityDate(inputScanner);
                            waitForEnter(inputScanner);
                            break;
                        case 5:
                            break;     
                        case 6:
                            break;
                        case 7:
                            break;    
                        case 8: 
                            logout = user.logOut();
                            break;  
                        default:
                            System.out.println("Unexpected error occurred.");
                            break;  
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
                    System.out.println("4. Submit Replenishment Request");
                    System.out.println("5. Logout");
                    System.out.println("=========================================");
                    System.out.print("Select a choice: ");

                    choice = getValidChoice(inputScanner,5);

                    switch (choice){
                        case 1: 
                            break;
                        case 2:
                            break;
                        case 3:
                            pharmacist.viewMedicationInventory();
                            waitForEnter(inputScanner);
                            break;  
                        case 4:
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
                    System.out.println("1. View and Manage Hospital Staff");
                    System.out.println("2. View Appointment Details");
                    System.out.println("3. View and Manage Medication Inventory");
                    System.out.println("4. Approve Replenishment Requests");
                    System.out.println("5. Logout");
                    System.out.println("=========================================");
                    System.out.print("Select a choice: ");

                    choice = getValidChoice(inputScanner,5);

                    switch (choice){
                        case 1: 
                            admin.viewHospitalStaff();
                            waitForEnter(inputScanner);
                            break;
                        case 2:
                            break;
                        case 3:
                            break;  
                        case 4:
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
///////////////////////////////////// PATIENT ////////////////////////////////////
            case PATIENT:
                Patient patient = new Patient(user.getHospitalID());

                while (!logout){
                    System.out.println("\nRedirecting to Patient's dashboard...");
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


///////////////////////////////////// Function to check valid input for dashboard ////////////////////////////////////
    public static int getValidChoice(Scanner scanner, int maxChoice) {
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (choice >= 1 && choice <= maxChoice) {
                    return choice;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + maxChoice);
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

///////////////////////////////////// Function to wait after displaying information ////////////////////////////////////
    public static void waitForEnter(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
