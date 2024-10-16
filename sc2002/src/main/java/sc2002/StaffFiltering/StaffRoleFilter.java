package sc2002.StaffFiltering;

import sc2002.*;

public class StaffRoleFilter implements StaffFilter {

    private Role role;

    public StaffRoleFilter(Role role) {
        this.role = role;
    }

    public boolean filter(Staff staff) {
        return role.equals(staff.getRole());
    }
}
