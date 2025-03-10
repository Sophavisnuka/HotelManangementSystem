package JavaProject;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            System.out.println("------------------------------");
            System.out.println("\n--- Register ---");

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // String query to insert new user
            String query = "INSERT INTO user (username, phoneNumber, email, passwords) VALUES (?, ?, ?, ?)";

            // Execute the query using PreparedStatement
            int rowAffected = MySQLConnection.executePreparedUpdate(query, username, phone, email, password);

            if (rowAffected > 0) {
                System.out.println("User added successfully!");
            } else {
                System.out.println("Unable to insert new user.");
            }
            // String role = "customer";
            //   // Check if the user wants to be an admin
            // String adminEmail = "admin@example.com";  // Hardcoded admin email (for example)
            // String adminCode = "admin123";  // Hardcoded admin code (for example)
            // // Check if the email or password matches the admin credentials
            // if (email.equals(adminEmail) || password.equals(adminCode)) {
            //     role = "admin";  // Assign role as admin if valid admin credentials
            // }
    }

    private void loginUser(Scanner scanner, int attempt, int maxAttempt) {
        System.out.println("------------------------------");
        System.out.println("\n--- Login ---");

        System.out.print("Enter email: ");
        String loginEmail = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        // String query to verify user
        String query = "SELECT * FROM users WHERE email = ? AND passwords = ?";

        // Execute the query using PreparedStatement
        ResultSet rs = MySQLConnection.executePreparedQuery(query, loginEmail, loginPassword);

        try {
            if (rs == null || !rs.next()) {
                System.out.println("User not found!\nPlease login!");
            }
            System.out.println("Login successful");
            BookingMenu bookingMenu = new BookingMenu();
            bookingMenu.bookingMenu();

        } catch (SQLException e) {
            System.out.println("Error while logging in the user!");
            e.printStackTrace();
        } finally {
            MySQLConnection.closeResultSet(rs);
        }

    }
}
