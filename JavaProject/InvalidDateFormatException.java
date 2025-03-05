package JavaProject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvalidDateFormatException extends Exception {
    public InvalidDateFormatException (String message) {
        super(message);
    }
    public LocalDate validateDateFormat(String dateStr, DateTimeFormatter formatter) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd-MM-yyyy.");
        }
    }
}

