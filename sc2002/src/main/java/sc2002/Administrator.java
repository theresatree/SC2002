package sc2002;

import java.io.IOException;
import java.util.List;

public class Administrator extends User {

    Administrator(String hospitalID) {
        super(hospitalID);
    }

    private List<Staff> staffs;

    public void viewAndManageHospitalStaff() {
        try {
            this.staffs = StaffDB.getStaff();

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

    public void viewAppointmentDetails() {

    }

    public void viewAndManageMedicationInventory() {

    }

    public void approveReplenishmentRequests() {

    }
}
