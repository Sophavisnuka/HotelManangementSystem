package JavaProject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
// User class implementing Authentication interface
public class User implements Authentication {
    @Override
    public void registerUser(Scanner scanner) {
        // Scanner scanner = new Scanner(System.in);
        System.out.println("------------------------------");
        System.out.println("\n--- Register ---");
        
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            CheckInputException.isEmptyInput(username);
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();
            CheckInputException.isEmptyInput(phone);
            CheckInputException.isNumberValid(phone);
            CheckInputException.isValidPhoneNumber(phone);
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            CheckInputException.isEmptyInput(email);
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            CheckInputException.isEmptyInput(password);
            
            String[] emailParts = email.split("@");
            String domain = emailParts.length > 1 ? emailParts[1].toLowerCase() : "";

            // ✅ Check if domain contains "admin"
            String role = domain.contains("admin") ? "admin" : "customer";

            // String query to insert new user
            String query = "INSERT INTO users (username, phoneNumber, email, passwords, role) VALUES (?, ?, ?, ?, ?)";
            // Execute the query using PreparedStatement
            int rowAffected = MySQLConnection.executePreparedUpdate(query, username, phone, email, password, role);
            if (rowAffected > 0) {
                System.out.println("User added successfully!");
                handlePostLogin(scanner, email);
                // return true;
            } else {
                System.out.println("Unable to insert new user.");
                // return false;
            }
        } catch (CheckInputException e) {
            System.out.println(e.getMessage());
            // return false;
        }
    }
    @Override
    public void loginUser(Scanner scanner) {
        // Scanner scanner = new Scanner(System.in);
        System.out.println("------------------------------");
        System.out.println("\n--- Login ---");
        try {
            System.out.print("Enter email: ");
            String loginEmail = scanner.nextLine();
            CheckInputException.isEmptyInput(loginEmail);
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();
            CheckInputException.isEmptyInput(loginPassword);
            // String query to verify user
            String query = "SELECT userId, role FROM users WHERE email = ? AND passwords = ?";
            // Execute the query using PreparedStatement
            try (PreparedStatement stmt = MySQLConnection.executePreparedQuery(query, loginEmail, loginPassword);
                ResultSet rs = stmt.executeQuery()) {
                if (rs == null || !rs.next()) {
                    System.out.println("User not found!\nPlease login!");
                } else {
                    int userId = rs.getInt("userId");
                    String role = rs.getString("role"); // Get user role
                    System.out.println("Login successful" );
                    System.out.println("User Id: " + userId);
                    handlePostLogin(scanner, loginEmail);
                    if ("admin".equalsIgnoreCase(role)) {
                        new Admin().handlePostLogin(scanner, loginEmail);
                    } else {
                        handlePostLogin(scanner, loginEmail);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error while logging in the user!");
                e.printStackTrace();
            }
        } catch (CheckInputException e) {
            System.out.println(e.getMessage());
        }
    }
    protected void handlePostLogin(Scanner scanner, String email) {
        BookingMenu bookingMenu = new BookingMenu();
        bookingMenu.bookingMenu(scanner);
    }
}

