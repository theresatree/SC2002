package sc2002.models;

import sc2002.enums.Role;
import sc2002.repositories.UserDB;
import sc2002.repositories.PatientDB;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a user in the hospital system, with functionality for login, logout, 
 * and patient registration.
 */
public class User {
    private static final Set<String> VALID_BLOOD_TYPES = new HashSet<>(Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
    private String hospitalID;
    private Role role;

    /**
     * Constructs a User object with the specified hospital ID.
     *
     * @param hospitalID The unique hospital ID of the user.
     */
    public User(String hospitalID) {
        this.hospitalID = hospitalID;
        this.role = determineRole(hospitalID);
    }

    /**
     * Gets the hospital ID of the user.
     *
     * @return The hospital ID of the user.
     */
    public String getHospitalID() {
        return this.hospitalID;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role of the user.
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Logs the user out of the system.
     *
     * @return True indicating a successful logout.
     */
    public boolean logOut() {
        System.out.println("\nLogout successful!");
        return true;
    }

    /**
     * Determines the role of a user based on their hospital ID.
     *
     * @param hospitalID The hospital ID of the user.
     * @return The role associated with the hospital ID.
     */
    private Role determineRole(String hospitalID) {
        if (hospitalID.matches("[PDA]\\d{3}")) {
            if (hospitalID.startsWith("P")) {
                return Role.PHARMACIST;
            } else if (hospitalID.startsWith("D")) {
                return Role.DOCTOR;
            } else {
                return Role.ADMINISTRATOR;
            }
        } else {
            return Role.PATIENT;
        }
    }

    /**
     * Checks if the user input is "EXIT" to terminate a process.
     *
     * @param exit The user input string.
     * @return True if the input is "EXIT"; otherwise, false.
     */
    public static boolean checkForExit(String exit) {
        if (exit.equalsIgnoreCase("EXIT")) {
            System.out.println("\n\nExiting process!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Logs a user into the system by validating their credentials.
     *
     * @param scanner The Scanner object for user input.
     * @return The logged-in User object or null if login fails.
     */
    public User login(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n\n=========================================");
                System.out.println("       Please input login details");
                System.out.println("          Enter 'exit' to exit");
                System.out.println("=========================================");
                System.out.print("Enter Hospital ID: ");
                String enteredID = scanner.nextLine().trim().toUpperCase();

                if (checkForExit(enteredID)) {
                    return null;
                }

                System.out.print("Enter Password: ");
                String password = scanner.nextLine().trim();

                if (checkForExit(password)) {
                    return null;
                }

                boolean login = UserDB.validateLogin(enteredID, password);
                if (login) {
                    System.out.println("\nLogging in. Please wait...");
                    for (int i = 5; i > 0; i--) {
                        System.out.print("*");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("\nLogin process interrupted.");
                            return null;
                        }
                    }
                    this.hospitalID = enteredID;
                    this.role = determineRole(enteredID);
                    System.out.println("\n\n\nLogin successful!");
                    System.out.println("Welcome back, " + this.role + " " + UserDB.getNameByHospitalID(this.hospitalID, this.role));
                    return this;
                } else {
                    System.out.println("\nInvalid Hospital ID or Password. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while loading user data: " + e.getMessage());
            }
        }
    }

    /**
     * Registers a new patient in the hospital system.
     *
     * @param scanner The Scanner object for user input.
     */
    public static void register(Scanner scanner) {
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
            System.out.println("        Registering as a patient");
            System.out.println("          Enter 'exit' to exit");
            System.out.println("=========================================");
            System.out.print("Enter your full name: ");
            patientName = scanner.nextLine();
            if (checkForExit(patientName)) {
                return;
            }

            System.out.print("Enter year of birth (YYYY): ");
            patientYear = digitChecker(scanner, 4, 2024);
            if (checkForExit(patientYear)) {
                return;
            }
            System.out.print("Enter month of birth (MM): ");
            patientMonth = digitChecker(scanner, 2, 12);
            if (checkForExit(patientMonth)) {
                return;
            }
            System.out.print("Enter day of birth (DD): ");
            patientDay = digitChecker(scanner, 2, 31);
            if (checkForExit(patientDay)) {
                return;
            }

            patientDOB = patientYear + "-" + patientMonth + "-" + patientDay;

            while (true) {
                System.out.print("Enter your gender (M/F): ");
                patientGender = scanner.nextLine().trim().toUpperCase();
                if (checkForExit(patientGender)) {
                    return;
                }
                if (patientGender.equals("M") || patientGender.equals("F")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'M' for Male or 'F' for Female.");
                }
            }

            
            while (true) {
                System.out.print("Enter your blood type: ");
                patientBloodType = scanner.nextLine().toUpperCase();
                if (checkForExit(patientBloodType)) {
                    return;
                }
            
                // Check if the input is a valid blood type
                if (VALID_BLOOD_TYPES.contains(patientBloodType)) {
                    break; // Valid blood type entered
                } else {
                    System.out.println("Invalid blood type. Please enter a valid blood type (e.g., A+, O-, AB+, etc.): ");
                }
            }

            System.out.print("Enter Phone Number: ");
            String phone = digitChecker(scanner, 8, -1);
            if (checkForExit(phone)) {
                return;
            }
            patientPhone = Integer.parseInt(phone);

            System.out.print("Enter your email address: ");
            patientEmail = scanner.nextLine();
            if (checkForExit(patientEmail)) {
                return;
            }

            String patientID = PatientDB.createNewPatient(patientName, patientDOB, patientGender, patientBloodType, patientPhone, patientEmail);
            System.out.println("\nCreating new profile for " + patientName + ". Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nRegister process interrupted.");
                }
            }
            System.out.println("\n\n==========================================");
            System.out.println("     Patient successfully registered!");
            System.out.println("Please remember your Hospital ID to login!");
            System.out.println("==========================================");
            System.out.println("                ID: " + patientID);
            System.out.println("          Password: password");
            System.out.println("==========================================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates numeric input based on length and range requirements.
     *
     * @param scanner The Scanner object for user input.
     * @param digits The required number of digits.
     * @param max The maximum value allowed (-1 for no max).
     * @return The valid numeric input as a String.
     */
    public static String digitChecker(Scanner scanner, int digits, int max) {
        while (true) {
            //System.out.print("Enter a " + digits + "-digit number or type 'EXIT' to quit: ");
            String input = scanner.nextLine();
    
            // Check for 'EXIT' input
            if (input.equalsIgnoreCase("EXIT")) {
                return input;
            }
    
            // Check if the input is numeric and has the correct number of digits
            if (input.matches("\\d{" + digits + "}")) {
                int num = Integer.parseInt(input);
                // Validate against the max value if max is not -1
                if (max == -1 || num <= max) {
                    return input;
                }
            }
            
            // Prompt the user again if the input is invalid
            System.out.println("Invalid input. Please enter a valid " + digits + "-digit number.");
        }
    }
}