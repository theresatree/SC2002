package sc2002.StaffFiltering;

import sc2002.models.Staff;

/**
 * Filters staff members based on staff ID.
 */
public class StaffIDFilter implements StaffFilter {
    private String staffID;

    /**
     * Constructs a StaffIDFilter with the specified staff ID.
     * 
     * @param staffID The staff ID to filter by.
     */
    public StaffIDFilter(String staffID) {
        this.staffID = staffID;
    }

    /**
     * Filters the staff member based on staff ID.
     * 
     * @param staff The staff member to be filtered.
     * @return True if the staff member's ID matches the specified ID, false otherwise.
     */
    @Override
    public boolean filter(Staff staff) {
        return staffID.equals(staff.getStaffID());
    }
}