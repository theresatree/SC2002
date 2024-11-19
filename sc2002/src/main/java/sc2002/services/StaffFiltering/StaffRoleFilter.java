package sc2002.services.StaffFiltering;
import sc2002.models.Staff;
import sc2002.enums.Role;

/**
 * Filters staff members based on their role.
 */
public class StaffRoleFilter implements StaffFilter {

    private Role role;

    /**
     * Constructs a StaffRoleFilter with the specified role.
     * 
     * @param role The role to filter by.
     */
    public StaffRoleFilter(Role role) {
        this.role = role;
    }

    /**
     * Filters the staff member based on role.
     * 
     * @param staff The staff member to be filtered.
     * @return True if the staff member's role matches the specified role, false otherwise.
     */
    @Override
    public boolean filter(Staff staff) {
        return role.equals(staff.getRole());
    }
}