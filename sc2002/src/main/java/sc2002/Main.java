package sc2002;
import java.io.IOException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
////////////////////////////////////// LOGIN ////////////////////////////////////// 
        boolean login = false;
        Scanner inputScanner = new Scanner(System.in);
        User user = null;
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
        inputScanner.close();

////////////////////////////////////// ROLE SPECIFIC MENUS ////////////////////////////////////// 

//         switch (loggedInUser.getRole()) {
//             case DOCTOR:
//                 System.out.println("Redirecting to Doctor's dashboard...");
//                 // Code specific to doctor's functionality
//                 break;
//             case PHARMACIST:
//                 System.out.println("Redirecting to Pharmacist's dashboard...");
//                 // Code specific to pharmacist's functionality
//                 break;
//             case PATIENT:
//                 System.out.println("Redirecting to Patient's dashboard...");
//                 // Code specific to patient's functionality
//                 break;
//             case ADMINISTRATOR:
//                 System.out.println("Redirecting to Administrator's dashboard...");
//                 // Code specific to administrator's functionality
//                 break;
//             default:
//                 System.out.println("Unknown role.");
//                 break;
//         }
//     }
    }
}