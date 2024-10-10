package sc2002;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String getTodayDate(String format) {
        LocalDateTime now = LocalDateTime.now(); // Get current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT); // Create a formatter with the specified format
        return now.format(formatter); // Return formatted date
    }
}
