package sc2002.StaffFiltering;

import sc2002.models.Staff;

/**
 * A filter that accepts all staff members, used as a default or "no filter" option.
 */
public class StaffNoFilter implements StaffFilter {

    /**
     * Constructs a StaffNoFilter with no specific filtering criteria.
     */
    public StaffNoFilter() {}

    /**
     * Accepts all staff members by default.
     * 
     * @param staff The staff member to be filtered.
     * @return True if the staff member is not null, false otherwise.
     */
    @Override
    public boolean filter(Staff staff) {
        return staff != null;
    }
}