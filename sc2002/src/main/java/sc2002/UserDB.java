package sc2002;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class UserDB {
    private final String filePath = "sc2002/src/main/resources/User.xlsx"; //fixed file location for User.xlsx

    public boolean validateLogin(String hospitalID, String password) throws IOException {
        try {
            InputStream is = new FileInputStream(filePath);
        }
        catch (FileNotFoundException e){
            throw new IOException("File not found in resources or local path: " + filePath);
        }

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        boolean isValid = false;
        for (Row row : sheet) {
            Cell idCell = row.getCell(0);
            Cell passwordCell = row.getCell(1);
            // Skip header row
            if (row.getRowNum() == 0) continue;
            if (idCell != null && passwordCell != null) {
                String id = idCell.getStringCellValue();
                String pass = passwordCell.getStringCellValue();

                if (id.equals(hospitalID) && pass.equals(password)) {
                    isValid = true;
                    break;
                }
            }
        }
        workbook.close();
        is.close();
        return isValid;
    }
}
