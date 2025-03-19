package gui;

import javax.swing.*;

import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import JavaProject.InvalidDateFormatException;
// import JavaProject.InvalidDateFormatException;
import JavaProject.MySQLConnection;
import constant.commonConstant;

public class ReservationGui extends Form {
    private JTextField checkInField, checkOutField;
    private JButton confirmButton;
    private int  roomId;
    private String roomType;

    public ReservationGui (int roomId, String roomType) {
        super("Reservation");
        this.roomId = roomId;
        this.roomType = roomType;
        addGuiComponents();
    }

    private void addGuiComponents () {
        JLabel reservationLabel = new JLabel ("Reservations");
        reservationLabel.setBounds(0,20,520,100);
        reservationLabel.setForeground(commonConstant.TEXT_COLOR);
        reservationLabel.setFont(new Font("Poppins", Font.BOLD, 18));
        reservationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(reservationLabel);
        //add Check in date label
        
        createLabel("Room ID", 50, 100);
        JLabel roomIdLabel = new JLabel(String.valueOf(roomId)); // Display room ID
        roomIdLabel.setBounds(50, 135, 400, 50);
        roomIdLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(roomIdLabel);

        createLabel("Room Type", 50, 200);
        JLabel roomTypeLabel = new JLabel(roomType); // Display room type
        roomTypeLabel.setBounds(50, 235, 400, 50);
        roomTypeLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(roomTypeLabel);
        // add check in date
        createLabel("Check In Date", 50, 300);
        checkInField = createTextField(50, 335, 400, 50);
        //add check out date
        createLabel("Check Out Date", 50, 400);
        checkOutField = createTextField(50, 435, 400, 50);
        //add confirm button
        confirmButton = new JButton("Confirm Reservation");
        confirmButton.setBounds(50, 500, 400, 50);
        confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirmButton.setBackground(commonConstant.TEXT_COLOR);
        confirmButton.setHorizontalAlignment(SwingConstants.CENTER);
        confirmButton.setFont(new Font("Poppins", Font.BOLD, 18));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeReservation();
            }
        });
        JLabel exit = new JLabel("Exit");
        exit.setBounds(100, 250,300,35);
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setForeground(commonConstant.TEXT_COLOR);
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                ReservationGui.this.dispose();
                new CustomerGui().setVisible(true);
            }
        });
        add(exit);
        add(confirmButton);
        setVisible(true);
    }
    private void createLabel (String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 480, 20);
        label.setForeground(commonConstant.TEXT_COLOR);
        label.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(label);
    }
    private JTextField createTextField (int x, int y, int w, int h) {
        JTextField field = new JTextField();
        field.setBounds(50, y, 400, 50);
        field.setBackground(commonConstant.SECONDARY_COLOR);
        field.setForeground(commonConstant.TEXT_COLOR);
        field.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(field);
        return field;
    }
    public void makeReservation () {
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
            String query = "INSERT INTO reservation (reservationId, roomId, roomType, checkInDate, checkOutDate, durationOfStay) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, reservationId);
            stmt.setInt(2, roomId);
            stmt.setString(3, roomType);
            stmt.setDate(4, java.sql.Date.valueOf(checkIn));
            stmt.setDate(5, java.sql.Date.valueOf(checkOut));
            stmt.setInt(6, duration);
    
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
        String query = "SELECT COUNT(*) FROM reservation";
        ResultSet rs = conn.createStatement().executeQuery(query);
        int count = (rs.next()) ? rs.getInt(1) + 1 : 1;
        return "R" + count;
    }
}
