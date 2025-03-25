package gui;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import JavaProject.InvalidDateFormatException;
import JavaProject.MySQLConnection;

public class ReservationGui extends Form {
    private JTextField checkInField, checkOutField;
    private int  roomId;
    private String roomType;
    // private int userId; // Add this to store the logged-in userId


    public ReservationGui(int roomId, String roomType) {
        super("Reservation");
        this.roomId = roomId;
        this.roomType = roomType;
        // this.userId = userId; // Store the userId
        addGuiComponents();
    }
    
    private void addGuiComponents() {
        JLabel reservationLabel = createLabel("Reservation", 0, 0, 520, 100, 20);
        reservationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        createLabel("Room ID", 50, 100);
        createLabel(String.valueOf(roomId), 50, 135);

        createLabel("Room Type", 50, 200);
        createLabel(roomType, 50, 235);

        createLabel("Check In Date", 50, 300);
        checkInField = createTextField(50, 335);

        createLabel("Check Out Date", 50, 400);
        checkOutField = createTextField(50, 435);

        JButton confirmButton = new JButton("Confirm Reservation");
        confirmButton.setBounds(50, 500, 400, 50);
        confirmButton.addActionListener(e -> makeReservation());
        add(confirmButton);
        setVisible(true);
    }
    
    public void makeReservation() {
        try {
            Connection conn = MySQLConnection.getConnection();
            if (checkInField.getText().isEmpty() || checkOutField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Validate check-in and check-out dates
            LocalDate checkIn = LocalDate.parse(checkInField.getText());
            LocalDate checkOut = LocalDate.parse(checkOutField.getText());
            if (!checkOut.isAfter(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Calculate duration
            int duration = (int) (checkOut.toEpochDay() - checkIn.toEpochDay());
            String reservationId = generateReservationId();
    
            // Insert reservation into the database
            String query = "INSERT INTO reservation (reservationId, userId, roomId, roomType, checkInDate, checkOutDate, durationOfStay) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, reservationId);
            stmt.setInt(2, UserInterface.userId); // Insert the userId
            stmt.setInt(3, roomId);
            stmt.setString(4, roomType);
            stmt.setDate(5, java.sql.Date.valueOf(checkIn));
            stmt.setDate(6, java.sql.Date.valueOf(checkOut));
            stmt.setInt(7, duration);
    
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Reservation successful! ID: " + reservationId, "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();  // Close the current reservation window
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
    
    public LocalDate validateDateFormat(String dateStr, DateTimeFormatter formatter) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd-MM-yyyy.");
        }
    }
    private String generateReservationId() throws SQLException {
        Connection conn = MySQLConnection.getConnection();
        
        // Query to get the highest reservationId from the reservation table
        String query = "SELECT reservationId FROM reservation ORDER BY reservationId DESC LIMIT 1";
        ResultSet rs = conn.createStatement().executeQuery(query);
        
        // Check if there is at least one reservation in the table
        if (rs.next()) {
            String lastReservationId = rs.getString("reservationId");
            
            // Extract the numeric part of the reservationId
            int lastIdNumber = Integer.parseInt(lastReservationId.substring(1)); // Remove the 'R' part and parse the number
            
            // Increment the last ID number to get the next one
            int nextIdNumber = lastIdNumber + 1;
            
            // Create the next reservationId
            return "R" + nextIdNumber;
        } else {
            // If no reservations exist, return "R1" as the first reservation ID
            return "R1";
        }
    }
}
