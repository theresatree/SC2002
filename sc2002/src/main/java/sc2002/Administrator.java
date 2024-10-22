package sc2002;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import sc2002.StaffFiltering.StaffAgeFilter;
import sc2002.StaffFiltering.StaffFilter;
import sc2002.StaffFiltering.StaffGenderFilter;
import sc2002.StaffFiltering.StaffIDFilter;
import sc2002.StaffFiltering.StaffNoFilter;
import sc2002.StaffFiltering.StaffRoleFilter;

public class Administrator extends User {

    Administrator(String hospitalID) {
        super(hospitalID);
    }

    private List<Staff> staffs;
    private StaffFilter selectedFilter = null;
    Scanner scanner = new Scanner(System.in);

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
        System.out.print("Select a choice: ");
        choice = Main.getValidChoice(scanner, 7);

        switch (choice) {
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
                System.out.print("Enter age range(min): ");
                int minAge = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter age range(max): ");
                int maxAge = scanner.nextInt();
                scanner.nextLine();
                selectedFilter = new StaffAgeFilter(minAge, maxAge);
                break;
            default:
                System.out.println("\nInvalid option. Please try again!\n");
                break;
        }

        try {
            this.staffs = StaffDB.getStaff(selectedFilter);

            if (staffs == null || staffs.isEmpty()) {
                System.out.println("\nNo staff found\n\n");
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

    public void addHospitalStaff() {
        int roleChoice, genderChoice, idNumber = 0, continueUpdate = 1;
        Role newRole = null;
        String newGender = "", newStaffIDPrefix;

        while (continueUpdate == 1) {

            System.out.println("\n============================");
            System.out.println("      Adding New Staff");
            System.out.println("============================");

            System.out.print("Enter Name: ");
            String newName = scanner.nextLine();

            System.out.println("Roles: ");
            System.out.println("1. Administrator");
            System.out.println("2. Doctor");
            System.out.println("3. Pharmacist");
            System.out.print("Select the Role: ");
            roleChoice = Main.getValidChoice(scanner, 3);

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
                    System.out.println("\nInvalid option. Please try again!\n");
                    break;
            }

            System.out.println("Gender: ");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.print("Select the Gender: ");
            genderChoice = Main.getValidChoice(scanner, 2);

            switch (genderChoice) {
                case 1:
                    newGender = "Male";
                    break;
                case 2:
                    newGender = "Female";
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again!\n");
                    break;
            }

            System.out.print("Enter Age: ");
            int newAge = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Phone Number: ");
            int newPhoneNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Email: ");
            String newEmail = scanner.nextLine();

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
            System.out.println("\n===========================================");
            System.out.println("             Continue adding?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);

        }

    }

    public void updateHospitalStaff() {
        int choice, continueUpdate = 1;
        String staffID;

        System.out.println("\n============================");
        System.out.println("       Updating Staff");
        System.out.println("============================");

        do {
            System.out.print("Enter StaffID to update: ");
            staffID = scanner.nextLine();
            selectedFilter = new StaffIDFilter(staffID);
            try {
                this.staffs = StaffDB.getStaff(selectedFilter);
            } catch (IOException e) {
                System.out.println("An error occurred while fetching staff details: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
            if (staffs.isEmpty()) {
                System.out.println("\nStaff not found. Please try again!\n");
            }
        } while (staffs.isEmpty());

        while (continueUpdate==1) {
            Staff selectedStaff = staffs.get(0);

            System.out.println("============================");
            System.out.print(selectedStaff.printStaffList());
            System.out.println("============================");
            System.out.println("1. Name");
            System.out.println("2. Age");
            System.out.println("3. Phone Number");
            System.out.println("4. Email");
            System.out.print("Select what to update: ");
            choice = Main.getValidChoice(scanner, 4);

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    selectedStaff.setName(newName);
                    break;
                case 2:
                    System.out.print("Enter new age: ");
                    int newAge = scanner.nextInt();
                    scanner.nextLine();
                    selectedStaff.setAge(newAge);
                    break;
                case 3:
                    System.out.print("Enter new phone number: ");
                    int newPhoneNumber = scanner.nextInt();
                    scanner.nextLine();
                    selectedStaff.setPhoneNumber(newPhoneNumber);
                    break;
                case 4:
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    selectedStaff.setEmail(newEmail);
                    break;
                default:
                    System.out.println("Invalid option. Please try again!");
                    break;
            }

            try {
                StaffDB.updateStaff(staffID, selectedStaff);
            } catch (IOException e) {
                System.out.println("An error occurred while updating staff details: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
            System.out.println("\n===========================================");
            System.out.println("             Continue updating?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    public void removeHospitalStaff() {
        String staffID;
        int continueUpdate = 1;

        while (continueUpdate==1) {
            System.out.println("\n============================");
            System.out.println("       Removing Staff");
            System.out.println("============================");

            do {
                System.out.print("Enter StaffID to remove: ");
                staffID = scanner.nextLine();
                selectedFilter = new StaffIDFilter(staffID);
                try {
                    this.staffs = StaffDB.getStaff(selectedFilter);
                } catch (IOException e) {
                    System.out.println("An error occurred while fetching staff details: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
                if (staffs.isEmpty()) {
                    System.out.println("\nStaff not found. Please try again!\n");
                }
            } while (staffs.isEmpty());

            try {
                StaffDB.removeStaff(staffID);
            } catch (IOException e) {
                System.out.println("An error occurred while removing staff: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
            System.out.println("\n===========================================");
            System.out.println("             Continue removing?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    public void viewAppointmentDetails() {

    }

    public void viewAndManageMedicationInventory() {

    }

    public void approveReplenishmentRequests() {

    }
}
