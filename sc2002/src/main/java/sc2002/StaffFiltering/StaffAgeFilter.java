package sc2002.StaffFiltering;

import sc2002.models.Staff;

/**
 * Filters staff members based on a specified age range.
 */
public class StaffAgeFilter implements StaffFilter {
    private int minAge;
    private int maxAge;

    /**
     * Constructs a StaffAgeFilter with specified minimum and maximum age.
     * 
     * @param minAge The minimum age for the filter.
     * @param maxAge The maximum age for the filter.
     */
    public StaffAgeFilter(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    /**
     * Filters the staff member based on the age range.
     * 
     * @param staff The staff member to be filtered.
     * @return True if the staff member's age is within the specified range, false otherwise.
     */
    @Override
    public boolean filter(Staff staff) {
        return (staff.getAge() >= minAge) && (staff.getAge() <= maxAge);
    }
}