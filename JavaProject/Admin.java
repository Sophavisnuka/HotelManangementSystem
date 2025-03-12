package JavaProject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin extends User {
    public void adminMenu (Scanner scanner) {
        while (true) {
            System.out.println("\n------------------------------\n");
            System.out.println("1. Register");
            System.out.println("2. Log in");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");
            String input = scanner.nextLine();
            try {
                // Check if the menu option input is a valid number first
                CheckInputException.isNumberValid(input);  // This checks if the input is a number
                int choice = Integer.parseInt(input);  // If valid, proceed to parse the choice
                switch (choice) {
                    case 1:
                        // Registration process
                        registerUser(scanner);
                        break;
                    case 2:
                        // Login process
                        loginUser(scanner);
                        break;
                    case 0:
                        System.out.println("Exiting system");
                        return;  // Exit the system
                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                        break;
                }
            } catch (CheckInputException e) {
                // Handle the exception for non-numeric input
                System.out.println(e.getMessage());  // Show the error message for non-numeric input
            } 
        }
    }
    @Override
    protected void handlePostLogin (Scanner scanner, String email) {
        System.out.println("Welcome, Admin");
        while (true) {
            System.out.println("\n------------Admin Menu-------------");
            System.out.println("1. View customer");
            System.out.println("2. Remove customer");
            System.out.println("3. View Reservations");
            System.out.println("4. Remove Reservations");
            System.out.println("5. Exit");
            System.out.println("Choose any option: ");
            String input = scanner.nextLine();
            try {
                CheckInputException.isNumberValid(input);  // This checks if the input is a number
                int choice = Integer.parseInt(input);  // If valid, proceed to parse the choice
                switch (choice) {
                    case 1:
                        viewUsers();
                        break;
                    case 2:
                        removeUsers(scanner);
                        break;
                    case 3: 
                        break;
                    case 4:
                        break;
                    case 5:
                        System.out.println("Exit from admin");
                        return;
                    default:
                        break;
                }
            } catch (CheckInputException e) {
                // Handle the exception for non-numeric input
                System.out.println(e.getMessage());  // Show the error message for non-numeric input
            } 
        }
    }
    public void viewUsers() {
        System.out.println("\n--- User List ---");
    
        String query = "SELECT userId, userName, email, role FROM users";
    
        try (PreparedStatement stmt = MySQLConnection.executePreparedQuery(query);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("userId");  // Get userId
                String userName = rs.getString("userName");
                String email = rs.getString("email");
                String role = rs.getString("role");
                System.out.println(id + ". " + userName + " | " + email + " | " + role);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users!");
            e.printStackTrace();
        }
    }
    public void removeUsers (Scanner scanner) {
        System.out.println("Enter user id to remove: ");
        int userId = Integer.parseInt(scanner.nextLine());
        String query = "DELETE FROM users WHERE userId = ?";
        int rowAffected = MySQLConnection.executePreparedUpdate(query, userId);
        if (rowAffected > 0) {
            System.out.println("User remove successfully");
        } else {
            System.out.println("Can't find the user");
        }
    }
    
}
