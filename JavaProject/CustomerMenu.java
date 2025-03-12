package JavaProject;

import java.util.Scanner;

public class CustomerMenu extends User {
    public void accountMenu (Scanner scanner) {
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
}
