package sc2002.models;

import sc2002.enums.Role;
import sc2002.repositories.UserDB;
import sc2002.repositories.PatientDB;

import java.io.IOException;
import java.util.Scanner;

public class User {
    private String hospitalID;
    private Role role;

    public User(String hospitalID) {
        this.hospitalID = hospitalID;
        this.role = determineRole(hospitalID);
    }

    public String getHospitalID() {
        return this.hospitalID;
    }

    public Role getRole() {
        return this.role;
    }

    public boolean logOut() {
        System.out.println("\nLogout successful!");
        return true;
    }

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

    public static boolean checkForExit(String exit) {
        if (exit.equalsIgnoreCase("EXIT")) {
            System.out.println("\n\nExiting process!");
            return true;
        } else {
            return false;
        }
    }

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
            System.out.println("        Registering as  a patient");
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

            System.out.print("Enter your blood type: ");
            patientBloodType = scanner.nextLine().toUpperCase();
            if (checkForExit(patientBloodType)) {
                return;
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
     * Checks if a numeric input meets specified requirements.
     *
     * @param scanner The scanner for input.
     * @param digits Expected number of digits.
     * @param max Max value for the input.
     * @return A valid digit string.
     */
    public static String digitChecker(Scanner scanner, int digits, int max) {
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("\\d{" + digits + "}")) {
                int num = Integer.parseInt(input);
                if (max == -1 || num <= max) {
                    return input;
                }
            }
            System.out.println("Please enter a valid " + digits + "-digit number.");
        }
    }
}