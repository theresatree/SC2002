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
    private List<MedicationInventory> medicationInventory;
    private List<AppointmentDetails> appointmentDetails;
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
        Staff selectedStaff=null;

        System.out.println("\n============================");
        System.out.println("       Updating Staff");
        System.out.println("============================");

        do {
            System.out.print("Enter StaffID to update: ");
            staffID = scanner.nextLine();
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
            System.out.println("       Continue updating same staff?");
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

        while (continueUpdate == 1) {
            System.out.println("\n============================");
            System.out.println("       Removing Staff");
            System.out.println("============================");

            do {
                System.out.print("Enter StaffID to remove: ");
                staffID = scanner.nextLine();
            } while (StaffDB.findStaff(staffID) == 0);

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
        try {
            this.appointmentDetails = AppointmentDetailsDB.getAppointmentDetails();

            if (appointmentDetails.isEmpty()) {
                System.out.println("No appointment found\n\n");

            }
            System.out.println("\n=========================================");
            System.out.println("     List of Appointment Details");
            System.out.println("=========================================");

            for (AppointmentDetails appointmentDetail : appointmentDetails) {
                System.out.print(appointmentDetail.printAppointmentDetails());

                if (appointmentDetail.getStatus() == AppointmentStatus.COMPLETED) {
                    AppointmentDetailsDB.printAppointmentOutcomeRecord(appointmentDetail.getPatientID(), appointmentDetail.getAppointmentID());
                }

                System.out.println("=========================================");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching appointment details: " + e.getMessage());
        }
    }

    public void viewAndManangeMedicationInventory() {
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

        System.out.println("=========================================");
        System.out.println("      Manage Medication Inventory");
        System.out.println("=========================================");
        System.out.println("1. Add Medication");
        System.out.println("2. Remove Medication");
        System.out.println("3. Update Stock Level");
        System.out.println("4. Change Low Stock Level Alert");
        System.out.println("5. Exit");
        System.out.println("=========================================");
        System.out.print("Select a choice: ");
        int choice = Main.getValidChoice(scanner, 5);

        switch (choice) {
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
                changeLowStockLevelAlert();
                break;
            case 5:
                break;
            default:
                System.out.println("Unexpected error occurred.");
                break;
        }
    }

    public void addMedication() {
        int continueUpdate = 1;

        while (continueUpdate == 1) {
            System.out.println("\n=========================================");
            System.out.println("        Adding New Medication");
            System.out.println("=========================================");

            System.out.print("Enter Medicine Name: ");
            String newMedicine = scanner.nextLine().toUpperCase();

            System.out.print("Enter Initial Stock: ");
            int newStock = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Low Stock Level Alert: ");
            int newLowAlert = scanner.nextInt();
            scanner.nextLine();

            MedicationInventory newMedication = new MedicationInventory(newMedicine, newStock, newLowAlert);

            try {
                MedicationInventoryDB.addMedication(newMedication);

            } catch (IOException e) {
                System.out.println("An error occurred while fetching medication inventory: " + e.getMessage());
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

    public void removeMedication() {
        int continueUpdate = 1;
        String medicineName;

        while (continueUpdate == 1) {
            System.out.println("\n=========================================");
            System.out.println("        Removing Medication");
            System.out.println("=========================================");

            do {
                System.out.print("Enter Medicine Name: ");
                medicineName = scanner.nextLine().toUpperCase();
            } while (MedicationInventoryDB.findMedicine(medicineName) == 0);

            try {
                MedicationInventoryDB.removeMedication(medicineName);

            } catch (IOException e) {
                System.out.println("An error occurred while removing medication: " + e.getMessage());
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

    public void updateStockLevel() {
        int continueUpdate = 1;
        String medicineName;

        while (continueUpdate == 1) {
            System.out.println("\n=========================================");
            System.out.println("        Updating Stock Level");
            System.out.println("=========================================");

            do {
                System.out.print("Enter Medicine Name: ");
                medicineName = scanner.nextLine().toUpperCase();
            } while (MedicationInventoryDB.findMedicine(medicineName) == 0);

            System.out.print("Enter Updated Stock Level: ");
            int updatedStockLevel = scanner.nextInt();

            try {
                MedicationInventoryDB.updateStockLevel(medicineName, updatedStockLevel);

            } catch (IOException e) {
                System.out.println("An error occurred while removing medication: " + e.getMessage());
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

    public void changeLowStockLevelAlert() {
        int continueUpdate = 1;
        String medicineName;

        while (continueUpdate == 1) {
            System.out.println("\n=========================================");
            System.out.println("     Changing Low Stock Level Alert");
            System.out.println("=========================================");

            do {
                System.out.print("Enter Medicine Name: ");
                medicineName = scanner.nextLine().toUpperCase();
            } while (MedicationInventoryDB.findMedicine(medicineName) == 0);

            System.out.print("Enter New Low Stock Level Alert: ");
            int updatedLowLevelAlert = scanner.nextInt();

            try {
                MedicationInventoryDB.changeLowAlert(medicineName, updatedLowLevelAlert);

            } catch (IOException e) {
                System.out.println("An error occurred while changing low stock level alert: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }

            System.out.println("\n===========================================");
            System.out.println("             Continue changing?");
            System.out.println("===========================================");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("===========================================");
            continueUpdate = Main.getValidChoice(scanner, 2);
        }
    }

    public void approveReplenishmentRequests() {

    }
}
