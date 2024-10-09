package javaclasses;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class UserTextDB {
    public static final String SEPARATOR = "|";
    private static String filePath;

    public static void setFilePath(String path) {
        filePath = path;
    }

    // Read users from the file and return as an ArrayList
    public static ArrayList<User> readUsers() throws IOException {
		ArrayList stringArray = (ArrayList)read();
		ArrayList userList = new ArrayList() ;// to store User data

        for (int i=0; i< stringArray.size(); i++){
            // get individual 'fields' of the string separated by SEPARATOR
            String st = (String)stringArray.get(i);
            StringTokenizer item = new StringTokenizer(st, SEPARATOR);
            String hospitalID = item.nextToken().trim(); // first token
            String password = item.nextToken().trim(); // second token
            // create User object from file data
            User user = new User(hospitalID, password);
            // add to User list
            userList.add(user);
        }
        return userList;
    }

    // Method to validate login credentials
    public static User validateUser(ArrayList<User> users, String userId, String password, Scanner inputScanner) {
        for (User user : users) {
            if (user.getHospitalID().equals(userId) && user.getPassword().equals(password)) {
                if (user.getPassword().equals("password")){ // First login
                    System.out.println("First login detected, please change your passsword!");
                    changePassword(user, inputScanner);
                    return user; //login validated after we changd password
                }
                else{
                    return user; // Not first login
                }
            }
        }
        return null; // invalid credentials
    }


    //Method to change password
    public static void changePassword(User user, Scanner inputScanner) {
        System.out.print("New Password: ");
        String newPassword = inputScanner.nextLine().trim();
        user.setPassword(newPassword);
        updateUserFile(user);
    }

    // Update the user's password in the text file
    public static void updateUserFile(User updatedUser) {
        // Read all users from the file
        try {
            ArrayList<User> users = readUsers();

            // Rewrite the users to the file, replacing the old password with the new one
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                for (User user : users) {
                    if (user.getHospitalID().equals(updatedUser.getHospitalID())) {
                        // Update the password for this user
                        writer.println(user.getHospitalID() + SEPARATOR + updatedUser.getPassword());
                    } else {
                        // Write the existing user data
                        writer.println(user.getHospitalID() + SEPARATOR + user.getPassword());
                    }
                }
            }
            System.out.println("Password changed successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating the user file: " + e.getMessage());
        }
    }

    /** Read the contents of the given file. */
    public static List<String> read() throws IOException {
        List<String> data = new ArrayList<>();
        Scanner scanner = new Scanner(new FileInputStream(filePath));
        try {
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
        return data;
     }
}