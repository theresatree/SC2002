package sc2002.StaffFiltering;

import sc2002.Staff;

public class StaffNoFilter implements StaffFilter{

    public StaffNoFilter(){

    }
    public boolean filter(Staff staff){
        return staff!= null;
    }
    
}
