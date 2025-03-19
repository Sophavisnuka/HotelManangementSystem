package JavaProject;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserReservation {
    private int userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int durationOfStay;
    private  int numRooms;
    private String roomType;
    private Room roomManager = new Room();

    Scanner input = new Scanner(System.in);

    public UserReservation() {
        this.checkInDate = LocalDate.now();
        this.checkOutDate = LocalDate.now();
    }

    public UserReservation(LocalDate checkInDate, LocalDate checkOutDate, int durationOfStay, ArrayList<Integer> roomNumbers) {  
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate; 
        this.durationOfStay = durationOfStay;
    }
    
    public static int calculateDurationStay(LocalDate checkInDate, LocalDate checkOutDate) {
        long totalStay = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        return (int) totalStay;
    }

    public void makeReservation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.println("\n--- Enter Reservation Details ---");
        System.out.print("Enter number of rooms to book: ");
        numRooms = input.nextInt();
        input.nextLine();  // Consume newline

        ArrayList<Integer> assignedRooms = new ArrayList<>();

        for (int i = 0; i < numRooms; i++) {
            System.out.print("Choose room type for Room " + (i + 1) + " (Single/Double): ");
            roomType = input.nextLine().trim();

            Room.displayAvailableRooms(roomType);
            int assignedRoom = roomManager.assignRoom(roomType);
            if (assignedRoom == -1) {
                System.out.println("No available rooms for " + roomType);
                return;  // Exit if any room is unavailable
            }
            assignedRooms.add(assignedRoom);
        }

        try {
            System.out.print("Check-in date (dd-MM-yyyy): ");
            checkInDate = validateDateFormat(input.nextLine(), formatter);

            System.out.print("Check-out date (dd-MM-yyyy): ");
            checkOutDate = validateDateFormat(input.nextLine(), formatter);

            if (!checkOutDate.isAfter(checkInDate)) {
                System.out.println("Error: Check-out date must be after check-in date.");
                return;
            }

            durationOfStay = calculateDurationStay(checkInDate, checkOutDate);
            System.out.println("Duration of stay: " + durationOfStay + " days");

            // Start database transaction
            Connection conn = MySQLConnection.getConnection();
            conn.setAutoCommit(false);  // Disable auto-commit for transaction handling

            try {
                // reservationID = generateReservationID();  // Generate a new reservation ID
                // Insert reservation into MySQL
                String query = "INSERT INTO reservation (userId, checkInDate, checkOutDate, durationOfStay, numRooms, roomType) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    // stmt.setInt(1, reservationID);
                    stmt.setInt(1, userId);
                    stmt.setDate(2, java.sql.Date.valueOf(checkInDate));
                    stmt.setDate(3, java.sql.Date.valueOf(checkOutDate));
                    stmt.setInt(4, durationOfStay);
                    stmt.setInt(5, numRooms);
                    stmt.setString(6, roomType);

                    int rowAffected = stmt.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Reservation added successfully!");
                    } else {
                        System.out.println("Unable to insert reservation.");
                        conn.rollback(); // Rollback transaction
                        return;
                    }
                }

                // Link assigned rooms to the reservation
                String roomQuery = "INSERT INTO reservation (roomNumber) VALUES (?)";
                try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
                    for (int roomID : assignedRooms) {
                        // roomStmt.setInt(1, reservationID);
                        roomStmt.setInt(1, roomID);
                        roomStmt.addBatch();
                    }
                    roomStmt.executeBatch();
                }
                conn.commit(); // Commit transaction after successful operations
                // Add reservation to user list
                // UserReservation newReser = new UserReservation(checkInDate, checkOutDate, durationOfStay, assignedRooms);
                // reservationList.add(newReser);
                System.out.println("Rooms Booked: " + assignedRooms);
                // System.out.println("Reservation successful! Assigned reservation ID: " + reservationID);
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction in case of error
                System.out.println("Error: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true); // Re-enable auto-commit
                conn.close();
            }
        } catch (InvalidDateFormatException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public LocalDate validateDateFormat(String dateStr, DateTimeFormatter formatter) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd-MM-yyyy.");
        }
    }
}
