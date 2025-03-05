package JavaProject;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserReservation {
    // Private instance variables
    private int reservationID;
    private String userName;
    private String userPhoneNum;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomType;
    private int durationOfStay;
    private int roomNumber;
    private static int id = 0;
    private static ArrayList <UserReservation> reservationList = new ArrayList<>();
    private Room roomManager = new Room();

    Scanner input = new Scanner(System.in);
    public UserReservation() {
        this.checkInDate = LocalDate.now();
        this.checkOutDate = LocalDate.now();
        this.reservationID = generateReservationID();
    }
    public UserReservation(String userName, String userPhoneNum, LocalDate checkInDate, LocalDate checkOutDate, int durationOfStay, String roomType, int roomNumber, int reservationID) {  
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate; 
        this.durationOfStay = durationOfStay;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.reservationID = reservationID;
    }
    // Static method to get total reservations
    private static int generateReservationID () {
        // if ()
        return  id++;
    }
    public static int calculateDurationStay (LocalDate checkInDate, LocalDate checkOutDate) {
        long totalStay = checkOutDate.toEpochDay() - checkInDate.toEpochDay(); //1970 jan 1 to present 
        return (int) totalStay;
    }
    public void makeReservation() {
        // Scanner input = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
          // Retrieve logged-in user details
        this.userName = User.getUserName();
        this.userPhoneNum = User.getPhoneNumber();
        System.out.println("\n--- Enter Reservation Details ---");
        System.out.print("Choose room type (Single/Double): ");
        roomType = input.nextLine().trim();
        roomNumber = roomManager.assignRoom(roomType, reservationID);
        Room.displayAvailableRooms(roomType);
        if (roomNumber == -1) {
            System.out.println("No available rooms. Please try again later.");
            return;
        }
        try {
            System.out.print("Check-in date (dd-MM-yyyy): ");
            checkInDate = validateDateFormat(input.nextLine(), formatter);
        
            System.out.print("Check-out date (dd-MM-yyyy): ");
            checkOutDate = validateDateFormat(input.nextLine(), formatter);
        
            if (checkOutDate.isAfter(checkInDate)) {    
                durationOfStay = calculateDurationStay(checkInDate, checkOutDate);
                System.out.println("Duration of stay: " + durationOfStay + " days");
            } else {
                System.out.println("Error: Check-out date must be after check-in date.");
                return;
            }
        } catch (InvalidDateFormatException e) {
            System.out.println(e.getMessage());
            return; // Exit the method if the date is invalid
        }
        reservationID = generateReservationID();
        UserReservation newReser = new UserReservation(userName, userPhoneNum, checkInDate, checkOutDate, durationOfStay, roomType, roomNumber, reservationID);
        reservationList.add(newReser);
        saveReservationToFile(newReser);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Reservation successful! Assigned reservation ID: " + reservationID);
        System.out.println("Returning to main menu...\n");
    }
    
    public void cancelReservation () {
        // Scanner input = new Scanner(System.in);
        System.out.println("Enter reservation ID to cancel: ");
        int cancelID = input.nextInt(); 
        for (UserReservation reservation : reservationList) {
            if (reservation.reservationID == cancelID) {
                reservationList.remove(reservation);
                roomManager.updateStatus(reservation.reservationID, true);
                System.out.println("Reservation " + cancelID + "| Room number: " + roomNumber + "is canceled");
                return;
            }
        }
        System.out.println("Reservation ID not found.");
    }
    public LocalDate validateDateFormat(String dateStr, DateTimeFormatter formatter) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd-MM-yyyy.");
        }
    }
    public void saveReservationToFile (UserReservation newReser) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("reservationDetail.txt", true));
            writer.write(newReser.toString() + "\n\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: File doesn't exist" + e.getMessage());
        }
    }
    @Override 
    public String toString () {
        return "Reservation detail:\n" + 
        "Guest: "+ userName + "\n" +
        "Phone Number: " + userPhoneNum +  "\n" +
        "Check-In Date: " + checkInDate + "\n" +
        "Check-Out Date: " + checkOutDate + "\n" +
        "Duration of Stay: " + durationOfStay + " days\n" +
        "Room number: " + roomNumber + "\n" +
        "Reservation Id: " + reservationID;
    }
    public void displayUserReservations () {
        for (UserReservation reservation : reservationList) {
            System.out.println("\n--------------------------------");
            System.out.println(reservation.toString());
            System.out.println("--------------------------------");
        }
    }
}
