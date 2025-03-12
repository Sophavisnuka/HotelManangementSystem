package JavaProject;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserReservation {
    private int reservationID;
    private int userId;
    private String userName;
    private String userPhoneNum;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int durationOfStay;
    private  int numRooms;
    private String roomType;
    private int roomNumber;
    private static int id = 0;
    private ArrayList<Integer> roomNumbers = new ArrayList<>();
    private static ArrayList<UserReservation> reservationList = new ArrayList<>();
    private Room roomManager = new Room();

    Scanner input = new Scanner(System.in);

    public UserReservation() {
        this.checkInDate = LocalDate.now();
        this.checkOutDate = LocalDate.now();
        this.reservationID = generateReservationID();
    }

    public UserReservation(String userName, String userPhoneNum, LocalDate checkInDate, LocalDate checkOutDate, int durationOfStay, ArrayList<Integer> roomNumbers, int reservationID) {  
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate; 
        this.durationOfStay = durationOfStay;
        this.roomNumbers = roomNumbers;
        this.reservationID = reservationID;
    }

    private static int generateReservationID() {
        return id++;
    }
    
    public static int calculateDurationStay(LocalDate checkInDate, LocalDate checkOutDate) {
        long totalStay = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        return (int) totalStay;
    }

    public void makeReservation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // Retrieve logged-in user details
        System.out.println("\n--- Enter Reservation Details ---");
        System.out.print("Enter number of rooms to book: ");
        numRooms = input.nextInt();
        input.nextLine();  // Consume newline

        for (int i = 0; i < numRooms; i++) {
            System.out.print("Choose room type for Room " + (i + 1) + " (Single/Double): ");
            roomType = input.nextLine().trim();
            Room.displayAvailableRooms(roomType);
            roomNumber = roomManager.assignRoom(roomType, reservationID);
            if (roomNumber == -1) {
                System.out.println("No available rooms for " + roomType);
                return;
            }
            roomNumbers.add(roomNumber);
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
            return;
        }
        String query = "INSERT INTO reservation (reservationID, userId, checkInDate, checkOutDate, durationOfStay, numRooms, roomType, roomNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Execute the query using PreparedStatement
        int rowAffected = MySQLConnection.executePreparedUpdate(query, reservationID, userId, checkInDate, checkOutDate, durationOfStay, numRooms, roomType, roomNumber);

        if (rowAffected > 0) {
            System.out.println("User added successfully!");
        } else {
            System.out.println("Unable to insert new user.");
        }
        reservationID = generateReservationID();
        UserReservation newReser = new UserReservation(userName, userPhoneNum, checkInDate, checkOutDate, durationOfStay, roomNumbers, reservationID);
        reservationList.add(newReser);
        saveReservationToFile(newReser);

        System.out.println("Rooms Booked: " + roomNumbers);
        System.out.println("Reservation successful! Assigned reservation ID: " + reservationID);
    }
    public LocalDate validateDateFormat(String dateStr, DateTimeFormatter formatter) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd-MM-yyyy.");
        }
    }
    public void saveReservationToFile(UserReservation newReser) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("reservationDetail.txt", true));
            writer.write(reservationID + "," + userName + "," + userPhoneNum + "," + checkInDate + "," + checkOutDate + "," + durationOfStay + "," + roomNumbers);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: File doesn't exist" + e.getMessage());
        }
    }
    @Override
    public String toString() {
        return "Reservation detail:\n" + 
        "Guest: " + userName + "\n" +
        "Phone Number: " + userPhoneNum + "\n" +
        "Check-In Date: " + checkInDate + "\n" +
        "Check-Out Date: " + checkOutDate + "\n" +
        "Duration of Stay: " + durationOfStay + " days\n" +
        "Room numbers: " + roomNumbers + "\n" +
        "Reservation Id: " + reservationID;
    }
}
