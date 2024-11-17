package sc2002.repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import sc2002.enums.Role;

/**
 * Represents the user database for handling user login and password management.
 */
public class UserDB {
    private static final String USER_FILE = "User.csv";
    private static final String PATIENT_FILE = "Patient_List.csv";
    private static final String STAFF_FILE = "Staff_List.csv";
    private static final String PATH_FILE = "resources/";


    /**
     * Validates the login when the user enters ID and password.
     *
     * @param hospitalID The user's hospital ID.
     * @param password   The user's password.
     * @return true if the login is valid; otherwise, false.
     * @throws IOException if there is an error accessing the file.
     */
    public static boolean validateLogin(String hospitalID, String password) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE+USER_FILE))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                String id = fields[0];
                String pass = fields[1];

                if (id.equals(hospitalID) && pass.equals(password)) {
                    if (pass.equals("password")) {
                        changePassword(id);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Prompts the user to change their password if it is set to "password".
     *
     * @param hospitalID The user's hospital ID.
     */
    private static void changePassword(String hospitalID) {
        Scanner scanner = new Scanner(System.in);
        String newPassword = "";

        System.out.print("\nFirst time login detected! ");
        while (true) {
            System.out.println("Please enter a new password: ");
            newPassword = scanner.nextLine().trim();

            if (!newPassword.equals("password") && !newPassword.isEmpty()) {
                updatePasswordInFile(hospitalID, newPassword);
                System.out.println("Password changed successfully.");
                break;
            } else {
                System.out.println("New password cannot be 'password' or empty. Please try again.");
            }
        }
    }

    /**
     * Updates the password in User.csv after a change.
     *
     * @param hospitalID  The user's hospital ID.
     * @param newPassword The new password.
     */
    private static void updatePasswordInFile(String hospitalID, String newPassword) {
        File inputFile = new File(PATH_FILE+USER_FILE);
        File tempFile = new File(PATH_FILE+"temp_" + USER_FILE);

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
                if (fields[0].equals(hospitalID)) {
                    fields[1] = newPassword; // Update password
                }

                bw.write(String.join(",", fields));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the password: " + e.getMessage());
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Error: Could not update the user file.");
        }
    }

    /**
     * Retrieves the name associated with a hospital ID from the appropriate file.
     *
     * @param hospitalID The user's hospital ID.
     * @param role       The role of the user.
     * @return The name associated with the ID, or null if not found.
     */
    public static String getNameByHospitalID(String hospitalID, Role role) {
        String filePath = role == Role.PATIENT ? PATH_FILE+PATIENT_FILE : PATH_FILE+STAFF_FILE;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                if (fields[0].equals(hospitalID)) {
                    return fields[1]; // Return the name
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read from file " + filePath);
            System.out.println("Error details: " + e.getMessage());
        }
        return null;
    }

    /**
     * Creates a new patient entry in User.csv with a default password.
     *
     * @param patientID The new patient's ID.
     */
    public static void createNewPatient(String patientID) {
        if (patientID == null || patientID.isEmpty()) {
            System.out.println("Invalid patient ID. Cannot create new patient.");
            return;
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_FILE+USER_FILE, true))) {
            System.out.println("Adding new patient to file: " + patientID);
            bw.write(String.join(",", patientID, "password"));
            bw.newLine();
            System.out.println("Patient added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating a new patient: " + e.getMessage());
        }
    }
}