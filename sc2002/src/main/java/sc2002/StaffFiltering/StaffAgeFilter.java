package sc2002;

public class StaffAgeFilter implements StaffFilter {
    private int minAge;
    private int maxAge;

    public StaffAgeFilter(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public boolean filter(Staff staff) {
        return (staff.getAge() >= minAge) && (staff.getAge() <= maxAge);
    }
}
