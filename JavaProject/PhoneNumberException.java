package JavaProject;

public class PhoneNumberException extends Exception {
    public PhoneNumberException(String message) {
        super(message);
    }
    public static void isValidPhoneNumber(String phoneNumber) throws PhoneNumberException{
        if (phoneNumber.length() < 9 || phoneNumber.length() > 18 ) {
            throw new PhoneNumberException("Phone number must be an integer and between 9 to 18 digits long.");
        } 
    }
    public static void isNumberValid (String number) throws PhoneNumberException {
        if (!number.matches("\\d+")) {
            throw new PhoneNumberException("You can only input number!");
        }
    }
}
