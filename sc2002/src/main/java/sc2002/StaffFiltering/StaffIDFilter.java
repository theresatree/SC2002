package sc2002.StaffFiltering;

import sc2002.*;

public class StaffIDFilter implements StaffFilter{
    
    private String staffID;

    public StaffIDFilter(String staffID) {
        this.staffID = staffID;
    }

    public boolean filter(Staff staff) {
        return staffID.equals(staff.getStaffID());
    }
}
