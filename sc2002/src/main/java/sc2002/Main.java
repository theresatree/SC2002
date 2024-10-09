package sc2002;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import static sc2002.UserTextDB.*;

class Main {
    public static void main(String[] args) {

////////////////////////////////////// LOGIN ////////////////////////////////////// 
        String currentDir = System.getProperty("user.dir");
        String filename = Paths.get(currentDir, "Database", "User.txt").toString(); // To get to User.txxt in database
        UserTextDB.setFilePath(filename);
        boolean login = false;
        User loggedInUser = null;
        Scanner inputScanner = new Scanner(System.in);
        while (!login){ // Make sure the user is logged-in before continuing
            try {
                ArrayList<User> userDB = readUsers();

                System.out.print("Enter Hospital ID: ");
                String userId = inputScanner.nextLine().trim();

                System.out.print("Enter Password: ");
                String password = inputScanner.nextLine().trim();

                // Validate the login credentials
                loggedInUser = UserTextDB.validateUser(userDB, userId, password, inputScanner);
                if (loggedInUser != null) {
                    System.out.println("\n\nLogin successful!");
                    System.out.println("Welcome back " + loggedInUser.getRole());
                    login=true;
                } else {
                    System.out.println("Invalid Hospital ID or Password.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while loading user data: " + e.getMessage());
            }
        }
        inputScanner.close();

////////////////////////////////////// ROLE SPECIFIC MENUS ////////////////////////////////////// 

        switch (loggedInUser.getRole()) {
            case DOCTOR:
                System.out.println("Redirecting to Doctor's dashboard...");
                // Code specific to doctor's functionality
                break;
            case PHARMACIST:
                System.out.println("Redirecting to Pharmacist's dashboard...");
                // Code specific to pharmacist's functionality
                break;
            case PATIENT:
                System.out.println("Redirecting to Patient's dashboard...");
                // Code specific to patient's functionality
                break;
            case ADMINISTRATOR:
                System.out.println("Redirecting to Administrator's dashboard...");
                // Code specific to administrator's functionality
                break;
            default:
                System.out.println("Unknown role.");
                break;
        }
    }
}