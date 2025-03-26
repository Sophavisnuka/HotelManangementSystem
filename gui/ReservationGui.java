package gui;

import javax.swing.*;
// import javax.swing.table.DefaultTableModel;

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

    public ReservationGui(int roomId, String roomType) {
        super("Reservation");
        this.roomId = roomId;
        this.roomType = roomType;
        addGuiComponents();
    }
    
    void addGuiComponents() {
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
    
            // Validate dates and calculate duration (existing code)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate checkIn = validateDateFormat(checkInField.getText(), formatter);
            LocalDate checkOut = validateDateFormat(checkOutField.getText(), formatter);
            if (!checkOut.isAfter(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            int duration = (int) (checkOut.toEpochDay() - checkIn.toEpochDay());
            String reservationId = generateReservationId();
    
            // Get room price
            double roomPrice = getRoomPrice(roomId);
    
            // Calculate total price (price * duration)
            double totalPrice = roomPrice * duration;
    
            // Insert reservation
            String reservationQuery = "INSERT INTO reservation (userId, reservationId, roomId, roomType, checkInDate, checkOutDate, durationOfStay) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement reservationStmt = conn.prepareStatement(reservationQuery);
            reservationStmt.setInt(1, UserInterface.userId);
            reservationStmt.setString(2, reservationId);
            reservationStmt.setInt(3, roomId);
            reservationStmt.setString(4, roomType);
            reservationStmt.setDate(5, java.sql.Date.valueOf(checkIn));
            reservationStmt.setDate(6, java.sql.Date.valueOf(checkOut));
            reservationStmt.setInt(7, duration);
    
            int rowsInserted = reservationStmt.executeUpdate();
            if (rowsInserted > 0) {
                // Create invoice after successful reservation
                String invoiceId = generateInvoiceId();
                String invoiceQuery = "INSERT INTO invoice (invoiceId, userId, totalPrice, status,reservationId,amountNight,roomId) VALUES (?, ?, ?, ?,?,?,?)";
                PreparedStatement invoiceStmt = conn.prepareStatement(invoiceQuery);
                invoiceStmt.setString(1, invoiceId);
                invoiceStmt.setInt(2, UserInterface.userId);
                invoiceStmt.setDouble(3, totalPrice);
                invoiceStmt.setString(4, "Pending"); // Initial status
                invoiceStmt.setString(5, reservationId);
                invoiceStmt.setInt(6, duration);
                invoiceStmt.setInt(7, roomId);


                
                int invoiceRows = invoiceStmt.executeUpdate();
                if (invoiceRows > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Reservation successful!\n" +
                        "Reservation ID: " + reservationId + "\n" +
                        "Invoice ID: " + invoiceId, 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    new InvoiceGui(UserInterface.userId, invoiceId);
                    dispose();
                }
            }
        } catch (InvalidDateFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private double getRoomPrice(int roomId) throws SQLException {
        String query = "SELECT roomPrice FROM room WHERE roomId = ?";
        try (Connection conn = MySQLConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("roomPrice");
            }
        }
        throw new SQLException("Room not found or price not available");
    }
    
    private String generateInvoiceId() throws SQLException {
        Connection conn = MySQLConnection.getConnection();
        String query = "SELECT invoiceId FROM invoice ORDER BY CAST(SUBSTRING(invoiceId, 2) AS UNSIGNED) DESC LIMIT 1";
        
        ResultSet rs = conn.createStatement().executeQuery(query);
    
        if (rs.next()) {
            String lastInvoiceId = rs.getString("invoiceId");
            int lastIdNumber = Integer.parseInt(lastInvoiceId.substring(1));
            return "I" + (lastIdNumber + 1);
        } else {
            return "I1";
        }
    }
    
    private LocalDate validateDateFormat(String dateStr, DateTimeFormatter formatter) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd-MM-yyyy.");
        }
    }
    private String generateReservationId() throws SQLException {
        Connection conn = MySQLConnection.getConnection();
        String query = "SELECT reservationId FROM reservation ORDER BY CAST(SUBSTRING(reservationId, 2) AS UNSIGNED) DESC LIMIT 1";
        
        ResultSet rs = conn.createStatement().executeQuery(query);
    
        if (rs.next()) {
            String lastReservationId = rs.getString("reservationId");
            int lastIdNumber = Integer.parseInt(lastReservationId.substring(1)); // Extract numeric part
            return "R" + (lastIdNumber + 1); // Increment and return new ID
        } else {
            return "R1";
        }
    }
}
