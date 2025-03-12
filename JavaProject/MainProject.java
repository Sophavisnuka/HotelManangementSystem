package JavaProject;

import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;

public class MainProject {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try {
            Connection conn = MySQLConnection.getConnection();
            if (conn != null) {
                System.out.println("Database connected successfully.");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        }
        catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            
        }
        while (true) {
            System.out.println("\nWelcome to Hotel Management System");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String input = scan.nextLine();
            try {
                CheckInputException.isNumberValid(input);
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        Admin AdminMenu = new Admin();
                        AdminMenu.adminMenu(scan);
                        break;
                    case 2:
                        CustomerMenu userMenu = new CustomerMenu();
                        userMenu.accountMenu(scan);
                        break;
                    case 3:
                        System.out.println("Exiting system");
                        // scan.close;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } catch (CheckInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
