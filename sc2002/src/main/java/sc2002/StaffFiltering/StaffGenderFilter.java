package sc2002;

public class StaffGenderFilter implements StaffFilter {
    private String gender;

    public StaffGenderFilter(String gender) {
        this.gender = gender;
    }

    public boolean filter(Staff staff) {
        return gender.equals(staff.getGender());
    }
}
