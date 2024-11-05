package sc2002;

import java.io.IOException;
import java.util.Scanner;

public class User{

    private String hospitalID;
    private Role role;

    User(String hospitalID){
        this.hospitalID = hospitalID;
        this.role=determineRole(hospitalID);
    }

    public String getHospitalID(){
        return this.hospitalID;
    }

    public Role getRole() {
        return this.role; 
    }

    public boolean logOut(){
        System.out.println("\nLogout successful!");
        return true;
    }

/////////////////////////////////// Detremine Role based on hospitalID ///////////////////////////////////////////
    private Role determineRole(String hospitalID) {
        if (hospitalID.matches("[PDA]\\d{3}")) { // 3 digits for staff
            if (hospitalID.startsWith("P")) {
                return Role.PHARMACIST; 
            } else if (hospitalID.startsWith("D")) {
                return Role.DOCTOR;
            } else{
                return Role.ADMINISTRATOR;
            }
        } else {
            return Role.PATIENT; 
        }
    }
////////////////////////////////// Faciliate Exiting process///////////////////////////////////////////
    public static boolean checkForExit(String exit){
        if (exit.equalsIgnoreCase("EXIT")){
            System.out.println("\n\nExiting process!");
            return true;
        }
        else{
            return false;
        }
    }

////////////////////////////////// LOGIN ///////////////////////////////////////////
    public User login(Scanner scanner){
        while (true) {
            try {
                System.out.println("\n\n=========================================");
                System.out.println("       Please input login details");
                System.out.println("          Enter 'exit' to exit");
                System.out.println("=========================================");
                System.out.print("Enter Hospital ID: ");
                String enteredID = scanner.nextLine().trim().toUpperCase();

                if (checkForExit(enteredID)){
                    return null;
                }

                System.out.print("Enter Password: ");
                String password = scanner.nextLine().trim();

                if (checkForExit(password)){
                    return null;
                }

                // Validate the login credentials
                boolean login = UserDB.validateLogin(enteredID, password);
                if (login) {
                    System.out.println("\nLogging in. Please wait...");
                    for (int i = 5; i > 0; i--) {
                        System.out.print("*");
                        try {
                            Thread.sleep(200); // Sleep for 0.2 second
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
                    return this; //This returns the user that has succesfully logged in.
                }
                else{
                    System.out.println("\nInvalid Hospital ID or Password. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while loading user data: " + e.getMessage());
            }
        }
    }

    ///////////////////////////// REGISTER ////////////////////////////////////
    public static void register(Scanner scanner){
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
            patientName = scanner.nextLine();
            if (checkForExit(patientName)){
                return;
            }

            //// DOB ////
            System.out.print("Enter year of birth (YYYY): ");
            patientYear = Main.digitChecker(scanner, 4, 2024);
            if (checkForExit(patientYear)){
                return;
            }
            System.out.print("Enter month of birth (MM): ");
            patientMonth = Main.digitChecker(scanner,2 ,12);
            if (checkForExit(patientMonth)){
                return;
            }
            System.out.print("Enter day of birth (DD): ");
            patientDay = Main.digitChecker(scanner, 2,31);
            if (checkForExit(patientDay)){
                return;
            }

            patientDOB = patientYear +"-"+patientMonth+"-"+patientDay;

            //// Gender ////
            while (true) {
                System.out.print("Enter your gender (M/F): ");
                patientGender = scanner.nextLine().trim().toUpperCase(); 
                if (checkForExit(patientGender)){
                    return;
                }
                if (patientGender.equals("M") || patientGender.equals("F")) {
                    break; // Exit the loop if input is valid
                } else {
                    System.out.println("Invalid input. Please enter 'M' for Male or 'F' for Female.");
                }
            }

            //// Blood Type ////
            System.out.print("Enter your blood type: ");
            patientBloodType = scanner.nextLine().toUpperCase();
            if (checkForExit(patientBloodType)){
                return;
            }

            //// Phone number ////
            System.out.print("Enter Phone Number: ");
            String phone = Main.digitChecker(scanner, 8,-1);
            if (checkForExit(phone)){
                return;
            }
            patientPhone = Integer.parseInt(phone);

            //// Email ////
            System.out.print("Enter your email address: ");
            patientEmail = scanner.nextLine();
            if (checkForExit(patientEmail)){
                return;
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
            return;
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
