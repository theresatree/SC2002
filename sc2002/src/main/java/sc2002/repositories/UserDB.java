package sc2002.repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        Path filePath = Paths.get(PATH_FILE, USER_FILE);
        if (!Files.exists(filePath)) {
            System.out.println("Error: User file not found.");
            return false;
        }

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.trim().split(",");
                if (fields[0].equals(hospitalID) && fields[1].equals(password)) {
                    if (password.equals("password")) {
                        changePassword(hospitalID);
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
        String newPassword;

        System.out.print("\nFirst time login detected! ");
        while (true) {
            System.out.println("Please enter a new password: ");
            newPassword = scanner.nextLine().trim();

            if (!newPassword.equals("password") && !newPassword.isEmpty()) {
                try {
                    updatePasswordInFile(hospitalID, newPassword);
                } catch (IOException e) {
                    System.out.println("Error updating password: " + e.getMessage());
                }
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
     * @throws IOException if there is an error updating the file.
     */
    private static void updatePasswordInFile(String hospitalID, String newPassword) throws IOException {
        Path inputFilePath = Paths.get(PATH_FILE, USER_FILE);
        Path tempFilePath = Paths.get(PATH_FILE, "temp_" + USER_FILE);

        if (!Files.exists(inputFilePath)) {
            throw new IOException("User file not found.");
        }

        try (BufferedReader br = Files.newBufferedReader(inputFilePath);
             BufferedWriter bw = Files.newBufferedWriter(tempFilePath)) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    bw.write(line);
                    bw.newLine();
                    continue; // Write header row as is
                }

                String[] fields = line.trim().split(",");
                if (fields[0].equals(hospitalID)) {
                    fields[1] = newPassword; // Update password
                }

                bw.write(String.join(",", fields));
                bw.newLine();
            }
        }

        // Ensure atomic operation for renaming the temp file
        Files.deleteIfExists(inputFilePath); // Delete original file
        Files.move(tempFilePath, inputFilePath); // Rename temp file to original name
    }

    /**
     * Retrieves the name associated with a hospital ID from the appropriate file.
     *
     * @param hospitalID The user's hospital ID.
     * @param role       The role of the user.
     * @return The name associated with the ID, or null if not found.
     */
    public static String getNameByHospitalID(String hospitalID, Role role) {
        String fileName = role == Role.PATIENT ? PATIENT_FILE : STAFF_FILE;
        Path filePath = Paths.get(PATH_FILE, fileName);

        if (!Files.exists(filePath)) {
            System.out.println("Error: File not found - " + fileName);
            return null;
        }

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                String[] fields = line.trim().split(",");
                if (fields[0].equals(hospitalID)) {
                    return fields[1]; // Return the name
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
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

        Path filePath = Paths.get(PATH_FILE, USER_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            System.out.println("Adding new patient to file: " + patientID);
            bw.write(String.join(",", patientID, "password"));
            bw.newLine();
            System.out.println("Patient added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating a new patient: " + e.getMessage());
        }
    }
}
