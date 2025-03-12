package JavaProject;

import java.util.Scanner;

public class BookingMenu {
    public void bookingMenu(Scanner scanner) {
        UserReservation reservation = new UserReservation();
        while (true) {
            System.out.println("------------------------------\n");
            System.out.println("Booking Menu");
            System.out.println("1. View rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Show Reservation Detail");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            String input = scanner.nextLine();
            try {
                CheckInputException.isNumberValid(input);
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        System.out.println("------------------------------\n");
                        System.out.println("-------- View Rooms-----------\n");
                        System.out.print("Enter room type: ");
                        String type = scanner.nextLine();
                        Room.displayAvailableRooms(type);
                        System.out.println("------------------------------\n");
                        break;
                    case 2:
                        System.out.println("------------------------------\n");
                        System.out.println("\n----- Make Reservation -----");
                        reservation.makeReservation();
                        System.out.println("------------------------------\n");
                        break;
                    case 3:
                        // reservation.cancelReservation();
                        break;
                    case 4:
                        break;
                    case 5:
                        System.out.println("Exiting system");
                        return;
                    default:
                        break;
                }
            } catch (CheckInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

