package JavaProject;

public class CheckInputException extends Exception {
    public CheckInputException(String message) {
        super(message);
    }
    public static void isValidPhoneNumber(String phoneNumber) throws CheckInputException{
        if (phoneNumber.length() < 9 || phoneNumber.length() > 18 ) {
            throw new CheckInputException("Phone number must be an integer and between 9 to 18 digits long.");
        } 
    }
    public static void isNumberValid (String number) throws CheckInputException {
        if (!number.matches("\\d+")) {
            throw new CheckInputException("You can only input number!");
        }
    }
    public static void isEmptyInput(String input) throws CheckInputException {
        if (input == null || input.trim().isEmpty()) {  // ✅ Use trim() to remove spaces
            throw new CheckInputException("Input can't be empty");
        }
    }
    
}
