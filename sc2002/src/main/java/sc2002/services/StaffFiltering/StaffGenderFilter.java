package sc2002.services.StaffFiltering;
import sc2002.models.Staff;

/**
 * Filters staff members based on gender.
 */
public class StaffGenderFilter implements StaffFilter {
    private String gender;

    /**
     * Constructs a StaffGenderFilter with the specified gender.
     * 
     * @param gender The gender to filter by.
     */
    public StaffGenderFilter(String gender) {
        this.gender = gender;
    }

    /**
     * Filters the staff member based on gender.
     * 
     * @param staff The staff member to be filtered.
     * @return True if the staff member's gender matches the specified gender, false otherwise.
     */
    @Override
    public boolean filter(Staff staff) {
        return gender.equals(staff.getGender());
    }
}