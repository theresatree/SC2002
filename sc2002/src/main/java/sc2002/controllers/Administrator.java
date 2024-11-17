package sc2002.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import sc2002.StaffFiltering.StaffAgeFilter;
import sc2002.StaffFiltering.StaffFilter;
import sc2002.StaffFiltering.StaffGenderFilter;
import sc2002.StaffFiltering.StaffIDFilter;
import sc2002.StaffFiltering.StaffNoFilter;
import sc2002.StaffFiltering.StaffRoleFilter;
import sc2002.enums.AppointmentStatus;
import sc2002.enums.RequestStatus;
import sc2002.enums.Role;
import sc2002.main.Main;
import sc2002.models.AppointmentDetails;
import sc2002.models.MedicationInventory;
import sc2002.models.ReplenishmentRequest;
import sc2002.models.Staff;
import sc2002.models.User;
import sc2002.repositories.AppointmentDetailsDB;
import sc2002.repositories.MedicationInventoryDB;
import sc2002.repositories.ReplenishmentRequestDB;
import sc2002.repositories.StaffDB;

/**
 * The Administrator class represents an administrator user with abilities to manage
 * hospital staff, medication inventory, and replenishment requests. It extends the User class
 * and contains various administrative methods for managing hospital resources.
 */
public class Administrator extends User {

    private List<Staff> staffs;
    private StaffFilter selectedFilter = null;
    private List<MedicationInventory> medicationInventory;
    private List<AppointmentDetails> appointmentDetails;
    private List<ReplenishmentRequest> replenishmentRequests;
    Scanner scanner = new Scanner(System.in);

    /**
     * Constructs an Administrator object with the specified hospital ID.
     *
     * @param hospitalID the unique identifier for the administrator
     */
    public Administrator(String hospitalID) {
        super(hospitalID);
    }

    /**
     * Displays and filters the hospital staff list based on user-selected criteria.
     */
    public void viewHospitalStaff() {

        int choice;

        System.out.println("\n============================");
        System.out.println("       Filter Options");
        System.out.println("============================");
        System.out.println("1. No Filter");
        System.out.println("----------------------------");
        System.out.println("Filter by Role:");
        System.out.println("2. ADMINISTRATOR");
        System.out.println("3. DOCTOR");
        System.out.println("4. PHARMACIST");
        System.out.println("----------------------------");
        System.out.println("Filter by Gender:");
        System.out.println("5. Male");
        System.out.println("6. Female");
        System.out.println("----------------------------");
        System.out.println("7. Filter by Age");
        System.out.println("============================");
        System.out.print("Select a choice (or 0 to exit): ");
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (choice >= 0 && choice <= 7) {
                    break;
                } else {
                    System.out.print("Invalid option. Please enter a number between 0 and 7: ");
                }
            } else {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        switch (choice) {
            case 0:
                System.out.println("\n\nExiting process!");
                return;
            case 1:
                selectedFilter = new StaffNoFilter();
                break;
            case 2:
                selectedFilter = new StaffRoleFilter(Role.ADMINISTRATOR);
                break;
            case 3:
                selectedFilter = new StaffRoleFilter(Role.DOCTOR);
                break;
            case 4:
                selectedFilter = new StaffRoleFilter(Role.PHARMACIST);
                break;
            case 5:
                selectedFilter = new StaffGenderFilter("Male");
                break;
            case 6:
                selectedFilter = new StaffGenderFilter("Female");
                break;
            case 7:
                System.out.println("\n============================");

                int minAge = -1;
                int maxAge = -1;

                while (true) {
                    try {
                        System.out.print("Enter age range (min): ");
                        minAge = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter age range (max): ");
                        maxAge = Integer.parseInt(scanner.nextLine());

                        if (maxAge >= minAge) {
                            break; // Valid input, exit the loop
                        } else {
                            System.out.println("Invalid input. Max age must be greater than or equal to min age. Please try again!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter valid integers!");
                    }
                }

                selectedFilter = new StaffAgeFilter(minAge, maxAge);
                break;
            default:
                System.out.println("\nInvalid option. Please try again!\n");
                break;
        }

        try {
            this.staffs = StaffDB.getStaff(selectedFilter);

            if (staffs == null || staffs.isEmpty()) {
                System.out.println("\nNo staff found!\n\n");
                return;
            }

            System.out.println("\n============================");
            System.out.println("        Staff List");
            System.out.println("============================");

            for (Staff staff : staffs) {
                System.out.print(staff.printStaffList());
                System.out.println("============================");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while fetching staff details: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

    }

    /**
     * Allows the administrator to add a new hospital staff member by collecting necessary
     * information from the user and adding the staff to the database.
     */
    public void addHospitalStaff() {
int roleChoice = 0, genderChoice = 0, idNumber = 0, continueUpdate = 1, newAge = 0;
        Role newRole = null;
        String newGender = "", newStaffIDPrefix, age, gender, role;

        while (continueUpdate == 1) {

            System.out.println("\n============================");
            System.out.println("      Adding New Staff");
            System.out.println("    Enter 'exit' to exit");
            System.out.println("============================");

            //// Name ////
            System.out.print("Enter your full name: ");
            String newName = scanner.nextLine();
            if (newName.equalsIgnoreCase("EXIT")) {
                System.out.println("\n\nExiting process!");
                return;
            }

            //// Roles ////
            System.out.println("Roles: ");
            System.out.println("1. Administrator");
            System.out.println("2. Doctor");
            System.out.println("3. Pharmacist");
            System.out.print("Select the Role: ");

            while (true) {
                role = scanner.nextLine().trim();

                if (role.equalsIgnoreCase("EXIT")) {
                    System.out.println("\nExiting process!");
                    return;
                }

                if (role.matches("\\d+")) {
                    roleChoice = Integer.parseInt(role);

                    if (roleChoice >= 1 && roleChoice <= 3) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 3: ");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid integer: ");
                }
            }

            switch (roleChoice) {
                case 1:
                    newRole = Role.ADMINISTRATOR;
                    break;
                case 2:
                    newRole = Role.DOCTOR;
                    break;
                case 3:
                    newRole = Role.PHARMACIST;
                    break;
                default:
                    break;
            }

            //// Gender ////
            System.out.println("Gender: ");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.print("Select the Gender: ");

            while (true) {
                gender = scanner.nextLine().trim();

                if (gender.equalsIgnoreCase("EXIT")) {
                    System.out.println("\nExiting process!");
                    return;
                }

                if (gender.matches("\\d+")) {
                    genderChoice = Integer.parseInt(gender);

                    // Check if the role choice is valid
                    if (genderChoice >= 1 && genderChoice <= 2) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 2: ");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid integer: ");
                }
            }

            switch (genderChoice) {
                case 1:
                    newGender = "Male";
                    break;
                case 2:
                    newGender = "Female";
                    break;
                default:
                    break;
            }

            //// Age ////
            while (true) {
                System.out.print("Enter Age: ");
                age = scanner.nextLine();

                if (age.equalsIgnoreCase("EXIT")) {
                    System.out.println("\nExiting process!");
                    return;
                }

                if (age.matches("\\d+")) { // Ensure the input is only digits
                    newAge = Integer.parseInt(age);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid integer!");
                }
            }

            //// Phone number ////
            System.out.print("Enter Phone Number: ");
            String phone = Main.digitChecker(scanner, 8, -1);
            if (phone.equalsIgnoreCase("EXIT")) {
                System.out.println("\n\nExiting process!");
                return;
            }
            int newPhoneNumber = Integer.parseInt(phone);

            //// Email ////
            System.out.print("Enter Email: ");
            String newEmail = scanner.nextLine();
            if (newEmail.equalsIgnoreCase("EXIT")) {
                System.out.println("\n\nExiting process!");
                return;
            }

            //// Generate new ID based on current list ////
            try {
                selectedFilter = new StaffNoFilter();
                this.staffs = StaffDB.getStaff(selectedFilter);

            } catch (IOException e) {
                System.out.println("An error occurred while fetching staff details: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            for (Staff staff : staffs) {
                if (staff.getRole().equals(newRole)) {
                    String numericString = staff.getStaffID().substring(1);
                    idNumber = Integer.parseInt(numericString);
                }
            }

            int newIdNumber = idNumber + 1;

            switch (newRole) {
                case ADMINISTRATOR:
                    newStaffIDPrefix = "A";
                    break;
                case DOCTOR:
                    newStaffIDPrefix = "D";
                    break;
                default:
                    newStaffIDPrefix = "P";
                    break;
            }

            String newStaffID = String.format("%s%03d", newStaffIDPrefix, newIdNumber);

            Staff newStaff = new Staff(newStaffID, newName, newRole, newGender, newAge, newPhoneNumber, newEmail);

            try {
                StaffDB.addStaff(newStaff);

            } catch (IOException e) {
                System.out.println("An error occurred while fetching staff details: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\nAdding Staff's Details. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200); // Sleep for 0.2 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nUpdate process interrupted.");
                }
            }
            System.out.println("\nSuccessfully Added Staff!");

            System.out.println("\n===========================================");
            System.out.println("             Continue adding?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);

        }
    }

    /**
     * Allows the administrator to update an existing hospital staff member's details.
     */
    public void updateHospitalStaff() {
        int choice, continueUpdate = 1;
        String staffID, age;
        Staff selectedStaff = null;

        System.out.println("\n============================");
        System.out.println("       Updating Staff");
        System.out.println("    Enter 'exit' to exit");
        System.out.println("============================");

        do {
            System.out.print("Enter StaffID to update: ");
            staffID = scanner.nextLine().toUpperCase();
            if (staffID.equalsIgnoreCase("EXIT")) {
                System.out.println("\nExiting process!");
                return;
            }
        } while (StaffDB.findStaff(staffID) == 0);

        while (continueUpdate == 1) {
            selectedFilter = new StaffIDFilter(staffID);
            try {
                List<Staff> staff = StaffDB.getStaff(selectedFilter);
                selectedStaff = staff.get(0);
            } catch (IOException e) {
                System.out.println("An error occurred while getting staff: " + e.getMessage());
            }

            System.out.println("============================");
            System.out.print(selectedStaff.printStaffList());
            System.out.println("============================");
            System.out.println("1. Name");
            System.out.println("2. Age");
            System.out.println("3. Phone Number");
            System.out.println("4. Email");
            System.out.print("Select what to update (or 0 to exit): ");

            while (true) {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline
                    if (choice >= 0 && choice <= 4) {
                        break;
                    } else {
                        System.out.print("Invalid option. Please enter a number between 0 and 4: ");
                    }
                } else {
                    System.out.print("Invalid input. Please enter a valid integer: ");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            switch (choice) {
                case 0: //exit
                    System.out.println("\n\nExiting process!");
                    return;
                case 1: //update name
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    if (newName.equalsIgnoreCase("EXIT")) {
                        System.out.println("\n\nExiting process!");
                        return;
                    }
                    selectedStaff.setName(newName);
                    break;
                case 2: //update age
                    while (true) {
                        System.out.print("Enter new age: ");
                        age = scanner.nextLine();

                        if (age.equalsIgnoreCase("EXIT")) {
                            System.out.println("\n\nExiting process!");
                            return;
                        }

                        if (age.matches("\\d+")) { // Ensure the input is only digits
                            int newAge = Integer.parseInt(age);
                            selectedStaff.setAge(newAge);
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter a valid integer!");
                        }
                    }
                    break;
                case 3: //update phone number
                    System.out.print("Enter new phone number: ");
                    String phone = Main.digitChecker(scanner, 8, -1);
                    if (phone.equalsIgnoreCase("EXIT")) {
                        System.out.println("\n\nExiting process!");
                        return;
                    }
                    int newPhoneNumber = Integer.parseInt(phone);
                    selectedStaff.setPhoneNumber(newPhoneNumber);
                    break;
                case 4: //update email
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    if (newEmail.equalsIgnoreCase("EXIT")) {
                        System.out.println("\n\nExiting process!");
                        return;
                    }
                    selectedStaff.setEmail(newEmail);
                    break;
                default:
                    break;
            }

            try {
                StaffDB.updateStaff(staffID, selectedStaff);
            } catch (IOException e) {
                System.out.println("An error occurred while updating staff details: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\nUpdating Staff's Details. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200); // Sleep for 0.2 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nUpdate process interrupted.");
                }
            }
            System.out.println("\nSuccessfully Updated Staff!");

            System.out.println("\n===========================================");
            System.out.println("       Continue updating same staff?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    /**
     * Allows the administrator to remove a hospital staff member from the system.
     */
    public void removeHospitalStaff() {
        String staffID;
        int continueUpdate = 1;

        while (continueUpdate == 1) {
            System.out.println("\n============================");
            System.out.println("       Removing Staff");
            System.out.println("    Enter 'exit' to exit");
            System.out.println("============================");

            do {
                System.out.print("Enter StaffID to remove: ");
                staffID = scanner.nextLine().toUpperCase();
                if (staffID.equalsIgnoreCase("EXIT")) {
                    System.out.println("\n\nExiting process!");
                    return;
                }
            } while (StaffDB.findStaff(staffID) == 0);

            try {
                StaffDB.removeStaff(staffID);
            } catch (IOException e) {
                System.out.println("An error occurred while removing staff: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\nRemoving Staff's Details. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200); // Sleep for 0.2 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nRemove process interrupted.");
                }
            }
            System.out.println("\nSuccessfully Removed Staff!");

            System.out.println("\n===========================================");
            System.out.println("             Continue removing?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    /**
     * Displays details of all hospital appointments, including completed appointments with
     * outcome records.
     */
    public void viewAppointmentDetails() {
        try {
            this.appointmentDetails = AppointmentDetailsDB.getAppointmentDetails();

            if (appointmentDetails.isEmpty()) {
                System.out.println("No appointment found\n\n");

            }
            System.out.println("\n===========================================");
            System.out.println("        List of Appointment Details");
            System.out.println("===========================================");

            for (AppointmentDetails appointmentDetail : appointmentDetails) {
                System.out.print(appointmentDetail.printAppointmentDetails());

                if (appointmentDetail.getStatus() == AppointmentStatus.COMPLETED) {
                    AppointmentDetailsDB.printAppointmentOutcomeRecord(appointmentDetail.getPatientID(), appointmentDetail.getAppointmentID());
                }

                System.out.println("===========================================");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching appointment details: " + e.getMessage());
        }
    }

    /**
     * Displays the medication inventory and allows the administrator to manage the inventory
     * (e.g., adding, removing, updating stock levels and alerts).
     */
    public void viewAndManangeMedicationInventory() {
        int choice;
        try {
            this.medicationInventory = MedicationInventoryDB.getMedicationInventory();

            if (medicationInventory.isEmpty()) {
                System.out.println("No Medications found\n\n");

            }
            System.out.println("\n============================");
            System.out.println("    Medication Inventory");
            System.out.println("============================");

            for (MedicationInventory medication : medicationInventory) {
                System.out.print(medication.printMedicationInventory());
                System.out.println("============================");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching medication details: " + e.getMessage());
        }

        System.out.println("===========================================");
        System.out.println("        Manage Medication Inventory");
        System.out.println("===========================================");
        System.out.println("1. Add Medication");
        System.out.println("2. Remove Medication");
        System.out.println("3. Update Stock Level");
        System.out.println("4. Update Low Stock Level Alert");
        System.out.println("===========================================");
        System.out.print("Select a choice (or 0 to exit): ");

        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (choice >= 0 && choice <= 4) {
                    break;
                } else {
                    System.out.print("Invalid option. Please enter a number between 0 and 4: ");
                }
            } else {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        switch (choice) {
            case 0:
                System.out.println("\nExiting process!");
                return;
            case 1:
                addMedication();
                break;
            case 2:
                removeMedication();
                break;
            case 3:
                updateStockLevel();
                break;
            case 4:
                updateLowStockLevelAlert();
                break;
            case 5:
                break;
            default:
                System.out.println("Unexpected error occurred.");
                break;
        }
    }

    /**
     * Allows the administrator to add new medication to the inventory by collecting information
     * about the medication and adding it to the database.
     */
    public void addMedication() {
        int continueUpdate = 1, newStock, newLowAlert;

        while (continueUpdate == 1) {
            System.out.println("\n===========================================");
            System.out.println("           Adding New Medication");
            System.out.println("           Enter 'exit' to exit");
            System.out.println("===========================================");

            System.out.print("Enter Medicine Name: ");
            String newMedicine = scanner.nextLine().toUpperCase();
            if (newMedicine.equalsIgnoreCase("EXIT")){
                System.out.println("\n\nExiting process!");
                return;
            }

            while (true) {
                System.out.print("Enter Initial Stock: ");
                String stock = scanner.nextLine();

                if (stock.equalsIgnoreCase("EXIT")) {
                    System.out.println("\nExiting process!");
                    return;
                }

                if (stock.matches("\\d+")) { // Ensure the input is only digits
                    newStock = Integer.parseInt(stock);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid integer!");
                }
            }

            while (true) {
                System.out.print("Enter Low Stock Level Alert: ");
                String lowAlert = scanner.nextLine();

                if (lowAlert.equalsIgnoreCase("EXIT")) {
                    System.out.println("\nExiting process!");
                    return;
                }

                if (lowAlert.matches("\\d+")) { // Ensure the input is only digits
                    newLowAlert = Integer.parseInt(lowAlert);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid integer!");
                }
            }

            MedicationInventory newMedication = new MedicationInventory(newMedicine, newStock, newLowAlert);

            try {
                MedicationInventoryDB.addMedication(newMedication);

            } catch (IOException e) {
                System.out.println("An error occurred while fetching medication inventory: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\nAdding New Medication. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200); // Sleep for 0.2 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nUpdate process interrupted.");
                }
            }
            System.out.println("\nSuccessfully Added Medication!");

            System.out.println("\n===========================================");
            System.out.println("             Continue adding?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    /**
     * Allows the administrator to remove a medication from the inventory.
     */
    public void removeMedication() {
        int continueUpdate = 1;
        String medicineName;

        while (continueUpdate == 1) {
            System.out.println("\n===========================================");
            System.out.println("            Removing Medication");
            System.out.println("           Enter 'exit' to exit");
            System.out.println("===========================================");

            do {
                System.out.print("Enter Medicine Name: ");
                medicineName = scanner.nextLine().toUpperCase();
                if (medicineName.equalsIgnoreCase("EXIT")){
                    System.out.println("\n\nExiting process!");
                    return;
                }
            } while (MedicationInventoryDB.findMedicine(medicineName) == 0);

            try {
                MedicationInventoryDB.removeMedication(medicineName);

            } catch (IOException e) {
                System.out.println("An error occurred while removing medication: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\nRemoving Medication. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200); // Sleep for 0.2 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nRemove process interrupted.");
                }
            }
            System.out.println("\nSuccessfully Removed Medication!");

            System.out.println("\n===========================================");
            System.out.println("             Continue removing?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    /**
     * Allows the administrator to update the stock level of an existing medication.
     */
    public void updateStockLevel() {
        int continueUpdate = 1, restockAmount;
        String medicineName;

        while (continueUpdate == 1) {
            System.out.println("\n===========================================");
            System.out.println("           Updating Stock Level");
            System.out.println("           Enter 'exit' to exit");
            System.out.println("===========================================");

            do {
                System.out.print("Enter Medicine Name: ");
                medicineName = scanner.nextLine().toUpperCase();
                if (medicineName.equalsIgnoreCase("EXIT")){
                    System.out.println("\n\nExiting process!");
                    return;
                }
            } while (MedicationInventoryDB.findMedicine(medicineName) == 0);

            while (true) {
                System.out.print("Enter Restock Amount: ");
                String stock = scanner.nextLine();

                if (stock.equalsIgnoreCase("EXIT")) {
                    System.out.println("\nExiting process!");
                    return;
                }

                if (stock.matches("\\d+")) { // Ensure the input is only digits
                    restockAmount = Integer.parseInt(stock);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid integer!");
                }
            }

            try {
                MedicationInventoryDB.updateStockLevel(medicineName, restockAmount);

            } catch (IOException e) {
                System.out.println("An error occurred while removing medication: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\nUpdating Stock Level. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200); // Sleep for 0.2 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nUpdate process interrupted.");
                }
            }
            System.out.println("\nSuccessfully Updated Stock Level!");

            System.out.println("\n===========================================");
            System.out.println("             Continue updating?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    /**
     * Allows the administrator to update the low stock level alert for an existing medication.
     */
    public void updateLowStockLevelAlert() {
        int continueUpdate = 1, updatedLowLevelAlert;
        String medicineName;

        while (continueUpdate == 1) {
            System.out.println("\n===========================================");
            System.out.println("      Updating Low Stock Level Alert");
            System.out.println("           Enter 'exit' to exit");
            System.out.println("===========================================");

            do {
                System.out.print("Enter Medicine Name: ");
                medicineName = scanner.nextLine().toUpperCase();
                if (medicineName.equalsIgnoreCase("EXIT")){
                    System.out.println("\n\nExiting process!");
                    return;
                }
            } while (MedicationInventoryDB.findMedicine(medicineName) == 0);

            while (true) {
                System.out.print("Enter New Low Stock Level Alert: ");
                String lowAlert = scanner.nextLine();

                if (lowAlert.equalsIgnoreCase("EXIT")) {
                    System.out.println("\nExiting process!");
                    return;
                }

                if (lowAlert.matches("\\d+")) { // Ensure the input is only digits
                    updatedLowLevelAlert = Integer.parseInt(lowAlert);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid integer!");
                }
            }

            try {
                MedicationInventoryDB.updateLowAlert(medicineName, updatedLowLevelAlert);

            } catch (IOException e) {
                System.out.println("An error occurred while updating low stock level alert: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\nUpdating Low Stock Level Alert. Please wait...");
            for (int i = 5; i > 0; i--) {
                System.out.print("*");
                try {
                    Thread.sleep(200); // Sleep for 0.2 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("\nUpdate process interrupted.");
                }
            }
            System.out.println("\nSuccessfully Updated Low Stock Level Alert!");

            System.out.println("\n===========================================");
            System.out.println("             Continue updating?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    /**
     * Displays and manages replenishment requests, allowing the administrator to view and
     * approve or reject requests.
     */
    public void approveReplenishmentRequests() {
        int continueUpdate = 1, choice;
        RequestStatus requestStatus = null, approval = null;

        System.out.println("\n===========================================");
        System.out.println("        View Replenishment Requests");
        System.out.println("===========================================");
        System.out.println("1. Approved");
        System.out.println("2. Pending");
        System.out.println("3. Rejected");
        System.out.println("===========================================");
        System.out.print("Select status to view (or 0 to exit): ");

        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (choice >= 0 && choice <= 3) {
                    break;
                } else {
                    System.out.print("Invalid option. Please enter a number between 0 and 3: ");
                }
            } else {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.nextLine(); // Clear the invalid input
            }
        }


        switch (choice) {
            case 0:
                System.out.println("\nExiting process!");
                return;
            case 1:
                requestStatus = RequestStatus.APPROVED;
                break;
            case 2:
                requestStatus = RequestStatus.PENDING;
                break;
            case 3:
                requestStatus = RequestStatus.REJECTED;
                break;
            case 4:
                return;
            default:
                System.out.println("Unexpected error occurred.");
                break;
        }

        while (continueUpdate == 1) {
            try {
                this.replenishmentRequests = ReplenishmentRequestDB.getReplenishmentRequest(requestStatus);

                System.out.println("\n===========================================");
                System.out.println("      " + requestStatus + " Replenishment Requests");
                System.out.println("===========================================");

                for (ReplenishmentRequest replenishmentRequest : replenishmentRequests) {
                    System.out.print(replenishmentRequest.printReplenishmentRequest());
                    System.out.println("===========================================");
                }

            } catch (IOException e) {
                System.out.println("An error occurred while getting replenishment requests: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            if (requestStatus == RequestStatus.PENDING) {

                while(true){
                    System.out.print("Enter the Request ID to approve or reject (or 0 to exit): ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline
                        if (choice == 0) {
                            System.out.println("\nExiting process!");
                            return;
                        }
                        else if (ReplenishmentRequestDB.findPendingRequest(choice) == 1){
                            break;
                        }
                    } else {
                        System.out.print("Invalid input. Please enter a valid integer: ");
                        scanner.nextLine(); // Clear the invalid input
                    }
                } 
                
                ReplenishmentRequest selectedRequest = null;
                for (ReplenishmentRequest selected : replenishmentRequests) {
                    if (selected.getRequestID() == choice) {
                        selectedRequest = selected;
                        break;
                    }
                }

                System.out.println("\n===========================================");
                System.out.println("         Approve selected request?");
                System.out.println("===========================================");
                System.out.print(selectedRequest.printReplenishmentRequest());
                System.out.println("===========================================");
                System.out.println("1. Approve");
                System.out.println("2. Reject");
                System.out.println("===========================================");
                System.out.print("Select a choice (or 0 to exit): ");

                while (true) {
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline
                        if (choice >= 0 && choice <= 2) {
                            break;
                        } else {
                            System.out.print("Invalid option. Please enter a number between 0 and 2: ");
                        }
                    } else {
                        System.out.print("Invalid input. Please enter a valid integer: ");
                        scanner.nextLine(); // Clear the invalid input
                    }
                }

                switch (choice) {
                    case 0:
                        System.out.println("\n\nExiting process!");
                        return;
                    case 1:
                        approval = RequestStatus.APPROVED;
                        break;
                    case 2:
                        approval = RequestStatus.REJECTED;
                        break;
                    default:
                        System.out.println("Unexpected error occurred.");
                        break;
                }

                try {
                    ReplenishmentRequestDB.updateStatus(selectedRequest.getRequestID(), approval);

                } catch (IOException e) {
                    System.out.println("An error occurred while getting replenishment requests: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }

                if (choice == 1) {
                    
                    try {
                        MedicationInventoryDB.updateStockLevel(selectedRequest.getMedicine(), selectedRequest.getAmount());
    
                    } catch (IOException e) {
                        System.out.println("An error occurred while getting replenishment requests: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }

                    System.out.println("\nUpdating Status and Stock Level. Please wait...");
                    for (int i = 5; i > 0; i--) {
                        System.out.print("*");
                        try {
                            Thread.sleep(200); // Sleep for 0.2 second
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("\nUpdate process interrupted.");
                        }
                    }
                    System.out.println("\nSuccessfully Updated Status and Stock Level!");
                } else {
                    System.out.println("\nSuccessfully Rejected Request!");
                }

                System.out.println("\n===========================================");
                System.out.println("            Continue approving?");
                System.out.println("===========================================");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.println("===========================================");
                continueUpdate = Main.getValidChoice(scanner, 2);

            } else {
                continueUpdate = 0;
                Main.waitForEnter(scanner);
            }

        }
    }
}