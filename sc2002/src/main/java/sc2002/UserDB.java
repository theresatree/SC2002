package sc2002;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class UserDB {
    private static final String FILE_NAME = "User.xlsx"; //fixed file location for User.xlsx

    public static boolean validateLogin(String hospitalID, String password) throws IOException {
        // Use try-with-resources to ensure the stream is closed automatically
        try (InputStream is = UserDB.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (is == null) {
                throw new IOException("File not found in resources: " + FILE_NAME);
            }

            // Load the workbook from the input stream
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
                boolean isValid = false;

                for (Row row : sheet) {
                    Cell idCell = row.getCell(0);
                    Cell passwordCell = row.getCell(1);
                    
                    if (row.getRowNum() == 0) continue;                    // Skip header row

                    if (idCell != null && passwordCell != null) {
                        String id = idCell.getStringCellValue();
                        String pass = passwordCell.getStringCellValue();

                        if (id.equals(hospitalID) && pass.equals(password)) {
                            isValid = true; // Login is valid
                            break;
                        }
                    }
                }
                return isValid; // Return the validation result
            }
        }
    }
}
