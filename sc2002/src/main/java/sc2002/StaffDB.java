package sc2002;

import sc2002.StaffFiltering.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Represents the staff database
 */
public class StaffDB {
    /**
     * fixed file location for Staff_List.xlsx
     */
    private static final String FILE_NAME = "Staff_List.xlsx";   
    /**
     * 
     * @return
     * @throws IOException
     */
    public static List<Staff> getStaff(StaffFilter selectedFilter) throws IOException {
        List<Staff> staffs = new ArrayList<>(); 
        try (InputStream is = StaffDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                for (Row row : sheet) {
                    Cell staffIDCell = row.getCell(0);
                    Cell nameCell = row.getCell(1);
                    Cell roleCell = row.getCell(2);
                    Cell genderCell = row.getCell(3);
                    Cell ageCell = row.getCell(4);

                    if (row.getRowNum() == 0)
                        continue; // Skip header row

                    if (staffIDCell != null) {
                        String staffID = staffIDCell.getStringCellValue();
                        String name = nameCell.getStringCellValue();
                        Role role = Role.valueOf(roleCell.getStringCellValue().toUpperCase());
                        String gender = genderCell.getStringCellValue();
                        int age = (int) ageCell.getNumericCellValue();
                        Staff staff = new Staff(staffID, name, gender, age, role);

                        if (selectedFilter == null || selectedFilter.filter(staff)) {
                            staffs.add(staff);
                        }
                    }
                }
            }
        }
        return staffs;
    }

}
