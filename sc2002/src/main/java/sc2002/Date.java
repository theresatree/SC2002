package sc2002;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";

    // Get the current date and time in the specified format
    public static String getTodayDateAndTime() {
        LocalDateTime now = LocalDateTime.now(); // Get current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT); // Create a formatter
        return now.format(formatter); // Return formatted date
    }

    // Get the current date in the specified format
    public static String getTodayDate() {
        LocalDateTime now = LocalDateTime.now(); // Get current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT); // Create a formatter with the correct constant
        return now.format(formatter); // Return formatted date
    }

    // Get the current time in the specified format
    public static String getTodayTime() {
        LocalDateTime now = LocalDateTime.now(); // Get current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT); // Create a formatter
        return now.format(formatter); // Return formatted time
    }
}
