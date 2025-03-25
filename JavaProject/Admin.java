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
            System.out.println("5. Update Room Status");
            System.out.println("6. Exit");
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
                        removeReservation(scanner);
                        break;
                    case 5:
                        updateRoomStatus(scanner);
                        break;
                    case 6:
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
    public void removeReservation(Scanner scanner) {
        System.out.print("Enter reservation ID to remove: ");
        int reservationId = Integer.parseInt(scanner.nextLine()); // Read the reservation ID
        
        String query = "DELETE FROM reservation WHERE reservationId = ?";  // SQL query to remove reservation
        
        // Execute the prepared statement to remove the reservation
        int rowAffected = MySQLConnection.executePreparedUpdate(query, reservationId);
        
        if (rowAffected > 0) {
            System.out.println("Reservation removed successfully.");
        } else {
            System.out.println("Can't find the reservation with ID: " + reservationId);
        }
    }
    public void updateRoomStatus(Scanner scanner) {
        System.out.print("Enter room ID to update status: ");
        int roomId = Integer.parseInt(scanner.nextLine()); // Read room ID
    
        // Get the current status of the room
        String currentStatusQuery = "SELECT roomStatus FROM room WHERE roomId = ?";
        String currentStatus = "";
        
        try (PreparedStatement stmt = MySQLConnection.executePreparedQuery(currentStatusQuery, roomId);
            ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                currentStatus = rs.getString("roomStatus");  // Get the current status
            } else {
                System.out.println("Room with ID " + roomId + " not found.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching room status: " + e.getMessage());
            return;
        }
    
        // Prompt admin for action
        System.out.println("Current room status: " + currentStatus);
        System.out.print("Do you want to change the status from " + currentStatus + " to the opposite? (Y/N): ");
        String confirmation = scanner.nextLine();
    
        if (confirmation.equalsIgnoreCase("Y")) {
            // Toggle the status: if it's 'Yes', change to 'No'; if it's 'No', change to 'Yes'
            String newStatus = currentStatus.equals("Yes") ? "No" : "Yes";
            
            String updateQuery = "UPDATE room SET roomStatus = ? WHERE roomId = ?";
            
            // Execute the update query
            int rowsUpdated = MySQLConnection.executePreparedUpdate(updateQuery, newStatus, roomId);
            
            if (rowsUpdated > 0) {
                System.out.println("Room status updated: Room ID " + roomId + " is now " + newStatus + ".");
            } else {
                System.out.println("Error: Room with ID " + roomId + " not found.");
            }
        } else {
            System.out.println("Room status update canceled.");
        }
    }
    
}
