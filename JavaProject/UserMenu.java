package JavaProject;

import java.util.Scanner;

public class UserMenu {
    public void accountMenu () {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n------------------------------\n");
            System.out.println("1. Register");
            System.out.println("2. Log in");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            String input = scanner.nextLine();
            try {
                // Check if the menu option input is a valid number first
                PhoneNumberException.isNumberValid(input);  // This checks if the input is a number
                int choice = Integer.parseInt(input);  // If valid, proceed to parse the choice
                int attempt = 0;
                int maxAttempt = 3;
                switch (choice) {
                    case 1:
                        // Registration process
                        registerUser(scanner, attempt, maxAttempt);
                        break;
                    case 2:
                        // Login process
                        loginUser(scanner, attempt, maxAttempt);
                        break;
                    case 0:
                        System.out.println("Exiting system");
                        return;  // Exit the system
                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                        break;
                }
            } catch (PhoneNumberException e) {
                // Handle the exception for non-numeric input
                System.out.println(e.getMessage());  // Show the error message for non-numeric input
            } 
        }
    }

    private void registerUser(Scanner scanner, int attempt, int maxAttempt) {
        while (attempt < maxAttempt) {
            System.out.println("------------------------------");
            System.out.println("\n--- Register ---");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            // Phone number validation
            String phone;
            while (true) {
                System.out.print("Enter phone number: ");
                phone = scanner.nextLine();
                try {
                    PhoneNumberException.isValidPhoneNumber(phone);  // Validate phone number
                    System.out.println("Phone number is valid.");
                    break;  // Exit the phone number loop
                } catch (PhoneNumberException e) {
                    System.out.println(e.getMessage());  // Show error message if invalid
                    attempt++;
                    if (attempt >= maxAttempt) {
                        System.out.println("Too many failed attempts. Returning to main menu.");
                        return;  // Return to the main menu after max attempts
                    }
                }
            }
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            String role = "customer";
              // Check if the user wants to be an admin
            String adminEmail = "admin@example.com";  // Hardcoded admin email (for example)
            String adminCode = "admin123";  // Hardcoded admin code (for example)
            // Check if the email or password matches the admin credentials
            if (email.equals(adminEmail) || password.equals(adminCode)) {
                role = "admin";  // Assign role as admin if valid admin credentials
            }
            // Register the user
            Customer user = new Customer(userId, username, phone, email, password, role);
            boolean registrationSuccess = user.register(userId, username, phone, email, password, role);
            if (registrationSuccess) {
                System.out.println("Registration successful!\n");
                BookingMenu bookingMenu = new BookingMenu();
                bookingMenu.bookingMenu();
            } else {
                System.out.println("Registration failed. Username might already exist.\n");
            }
            attempt++;
            if (attempt >= maxAttempt) {
                System.out.println("Too many failed attempts. Returning to main menu.");
                return;  // Return to the main menu after max attempts
            }
            System.out.println("------------------------------");
        }
    }

    private void loginUser(Scanner scanner, int attempt, int maxAttempt) {
        while (attempt < maxAttempt) {
            System.out.println("------------------------------");
            System.out.println("\n--- Login ---");
            System.out.print("Enter email: ");
            String loginEmail = scanner.nextLine();
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            // User account = User.findUser(loginEmail, loginPassword); 
            if (account == null) {
                System.out.println("Account not found. Please register.");
                attempt++;
                if (attempt >= maxAttempt) {
                    System.out.println("Too many failed attempts. Returning to main menu.");
                    return;  // Exit the system after max attempts
                }
                continue; // Allow the user to try logging in again
            } else {
                BookingMenu bookingMenu = new BookingMenu();
                bookingMenu.bookingMenu();
                // break;
            }
            attempt++;
            if (attempt >= maxAttempt) {
                System.out.println("Too many failed attempts. Returning to main menu.");
                return;  // Return to the main menu after max attempts
            }
            System.out.println("------------------------------");
        }
    }
}
