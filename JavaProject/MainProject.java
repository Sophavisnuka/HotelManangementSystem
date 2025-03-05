package JavaProject;

import java.util.Scanner;

public class MainProject {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("\nWelcome to Hotel Management System");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String input = scan.nextLine();
            try {
                PhoneNumberException.isNumberValid(input);
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        UserMenu userMenu = new UserMenu();
                        userMenu.accountMenu();
                        break;
                    case 3:
                        System.out.println("Exiting system");
                        scan.nextLine();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } catch (PhoneNumberException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
