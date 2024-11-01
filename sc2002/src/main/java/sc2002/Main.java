package sc2002;
import java.io.IOException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
////////////////////////////////////// LOGIN ////////////////////////////////////// 
    while (true){
        boolean mainMenuExit = false;
        Scanner inputScanner = new Scanner(System.in);
        User user = null; // Set user to null
        boolean login = false; //facilitate login
        boolean register = false;//facilitate register
        boolean logout = false; // This is to facilitate logout
        int choice; // This is for all the selection choices.

        while (!mainMenuExit){
 //////////////////////////////////////////// MAIN MENU SECTION (LOGIN + REGISTER) /////////////////////////////////////////////////
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
                    while (!login){ // Make sure the user is logged-in before continuing
                        try {
                            System.out.println("\n\n=========================================");
                            System.out.println("       Please input login details");
                            System.out.println("          Enter 'exit' to exit");
                            System.out.println("=========================================");
                            System.out.print("Enter Hospital ID: ");
                            String hospitalID = inputScanner.nextLine().trim().toUpperCase();

                            if (hospitalID.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting login process!");
                                break;
                            }

                            System.out.print("Enter Password: ");
                            String password = inputScanner.nextLine().trim();

                            if (password.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting login process!");
                                break;
                            }

                            // Validate the login credentials
                            login = UserDB.validateLogin(hospitalID, password);
                            if (login) {
                                System.out.println("\nLogging in. Please wait...");
                                for (int i = 5; i > 0; i--) {
                                    System.out.print("*");
                                    try {
                                        Thread.sleep(200); // Sleep for 0.2 second
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        System.out.println("\nLogin process interrupted.");
                                    }
                                }
                                System.out.println("\n\nLogin successful!");
                                user = new User(hospitalID); // Create an instance of User when logged in.
                                System.out.println("Welcome back " + user.getRole() + " " + UserDB.getNameByHospitalID(hospitalID, user.getRole()));
                                login=true;
                                mainMenuExit=true;
                            } else {
                                System.out.println("\nInvalid Hospital ID or Password.");
                            }
                        } catch (IOException e) {
                            System.out.println("An error occurred while loading user data: " + e.getMessage());
                        }
                    }
                    break;
        //////////////////////////////////////////// REGISTER SECTION //////////////////////////////////////////////////
                case 2:
                    while (!register){
                        try {
                            String patientName;
                            String patientYear;
                            String patientMonth;
                            String patientDay;
                            String patientDOB;
                            String patientGender;
                            String patientBloodType;
                            int patientPhone;
                            String patientEmail;

                            System.out.println("\n\n=========================================");
                            System.out.println("        Registering as  a patient");
                            System.out.println("          Enter 'exit' to exit");
                            System.out.println("=========================================");
                            //// Name ////
                            System.out.print("Enter your full name: ");
                            patientName = inputScanner.nextLine();
                            if (patientName.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }

                            //// DOB ////
                            System.out.print("Enter year of birth (YYYY): ");
                            patientYear = digitChecker(inputScanner, 4, 2024);
                            if (patientYear.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }
                            System.out.print("Enter month of birth (MM): ");
                            patientMonth = digitChecker(inputScanner,2 ,12);
                            if (patientMonth.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }
                            System.out.print("Enter day of birth (DD): ");
                            patientDay = digitChecker(inputScanner, 2,31);
                            if (patientDay.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }

                            patientDOB = patientYear +"-"+patientMonth+"-"+patientDay;
 
                            //// Gender ////
                            while (true) {
                                System.out.print("Enter your gender (M/F): ");
                                patientGender = inputScanner.nextLine().trim().toUpperCase(); 
                                if (patientGender.equalsIgnoreCase("EXIT")){
                                    break;
                                }             
                                if (patientGender.equals("M") || patientGender.equals("F")) {
                                    break; // Exit the loop if input is valid
                                } else {
                                    System.out.println("Invalid input. Please enter 'M' for Male or 'F' for Female.");
                                }
                            }
                            if (patientGender.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }

                            //// Blood Type ////
                            System.out.print("Enter your blood type: ");
                            patientBloodType = inputScanner.nextLine().toUpperCase();
                            if (patientBloodType.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }

                            //// Phone number ////
                            System.out.print("Enter Phone Number: ");
                            String phone = digitChecker(inputScanner, 8,-1);
                            patientPhone = Integer.parseInt(phone);
                            if (phone.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }

                            //// Email ////
                            System.out.print("Enter your email address: ");
                            patientEmail = inputScanner.nextLine();
                            if (patientEmail.equalsIgnoreCase("EXIT")){
                                System.out.println("\n\nExiting registering process!");
                                break;
                            }


                            String patientID = PatientDB.createNewPatient(patientName, patientDOB, patientGender, patientBloodType, patientPhone, patientEmail);
                            System.out.println("\nCreating new profile for "+ patientName+ ". Please wait...");
                            for (int i = 5; i > 0; i--) {
                                System.out.print("*");
                                try {
                                    Thread.sleep(200); // Sleep for 0.2 second
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    System.out.println("\nRegister process interrupted.");
                                }
                            }
                            System.out.println("\n\n==========================================");
                            System.out.println("     Patient successfuly regisetered!");
                            System.out.println("Please remember your Hospital ID to login!");
                            System.out.println("==========================================");
                            System.out.println("                ID: " + patientID);
                            System.out.println("          Password: password");
                            System.out.println("==========================================");
                            waitForEnter(inputScanner);
                            register=true;
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    break;
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
                    System.out.println("4. Submit Replenishment Request");
                    System.out.println("5. Logout");
                    System.out.println("=========================================");
                    System.out.print("Select a choice: ");

                    choice = getValidChoice(inputScanner,5);

                    switch (choice){
                        case 1: 
                            System.out.println("=========================================");
                            System.out.println("Enter patient ID: ");
                            String patientID1 = inputScanner.nextLine().toUpperCase().trim();
                            pharmacist.viewPastAppointmentOutcome(patientID1);
                            waitForEnter(inputScanner);
                            break;
                        case 2:
                            System.out.println("=========================================");
                            System.out.println("Enter patient ID");
                            String patientID2 = inputScanner.nextLine().toUpperCase().trim();
                            System.out.println("Enter appointment ID");
                            int appointmentID = inputScanner.nextInt();
                            pharmacist.updatePrescriptionStatus(patientID2, appointmentID);
                            break;
                        case 3:
                            pharmacist.viewMedicationInventory();
                            waitForEnter(inputScanner);
                            break;  
                        case 4:
                            pharmacist.viewMedicationInventory();
                            waitForEnter(inputScanner);
                            System.out.println("=========================================");
                            System.out.println("Enter medicine name you would like to request replenishment for: ");
                            String medicine = inputScanner.nextLine().toUpperCase().trim();
                            pharmacist.submitReplenishmentRequest(medicine);
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
                    System.out.println("5. Approve Replenishment Requests (" + ReplenishmentRequestDB.numPending() + " new request)");
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
                            System.out.println("4. Exit");
                            System.out.println("=========================================");
                            System.out.print("Select a choice: ");
                            choice = getValidChoice(inputScanner,4);

                            switch (choice){
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

///////////////////////////////////// Function to wait after displaying information ////////////////////////////////////
    public static void waitForEnter(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }



///////////////////////////////////// Number of digits Checker ////////////////////////////////////
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
                    System.out.println("Invalid input. Please enter a number from 1 to " + range + ".");
                }
            } else {
                System.out.println("Invalid input. Please enter a number with exactly " + String.valueOf(maxDigit) + " digits.");
            }
        }
    }
}