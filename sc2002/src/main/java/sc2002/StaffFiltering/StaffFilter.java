package sc2002.StaffFiltering;

import sc2002.models.Staff;

/**
 * Interface for filtering staff based on specific criteria.
 */
public interface StaffFilter {

    /**
     * Filters the provided staff member based on a defined criterion.
     * 
     * @param staff The staff member to be filtered.
     * @return True if the staff member meets the filter criteria, false otherwise.
     */
    boolean filter(Staff staff);
}