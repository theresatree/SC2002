package sc2002;

import sc2002.StaffFiltering.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Administrator extends User {

    Administrator(String hospitalID) {
        super(hospitalID);
    }

    private List<Staff> staffs;

    public void viewHospitalStaff() {

        Scanner scanner = new Scanner(System.in);
        StaffFilter selectedFilter = null;

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
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
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
                System.out.print("Enter age range(max): ");
                int maxAge = scanner.nextInt();
                selectedFilter = new StaffAgeFilter(minAge, maxAge);
                break;
            default:
                System.out.println("\nInvalid option");
                return;
        }

        try {
            this.staffs = StaffDB.getStaff(selectedFilter);

            if (staffs == null || staffs.isEmpty()) {
                System.out.println("No staff found\n\n");
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

    public void manageHospitalStaff(){

    }

    public void viewAppointmentDetails() {

    }

    public void viewAndManageMedicationInventory() {

    }

    public void approveReplenishmentRequests() {

    }
}
