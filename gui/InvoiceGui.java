package gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
// import java.awt.*;

import JavaProject.MySQLConnection;

public class InvoiceGui extends Form {
    private String invoiceId;
    private int userId;
    private String userName;
    private String email;
    private double totalPrice;
    private String status;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    public InvoiceGui(int userId, String invoiceId) {
        super("Invoice");
        this.userId = userId;
        this.invoiceId = invoiceId;  // Now properly 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Add this line
        fetchInvoiceData();
        addGuiComponents();
    }
    
    void addGuiComponents() {
            JLabel invoiceLabel = createLabel("Invoice Details", 0, 0, 520, 100, 40);
            invoiceLabel.setHorizontalAlignment(SwingConstants.CENTER);

            createLabel("Invoice ID: " + invoiceId, 30, 100);
            createLabel("User ID: " + userId, 30, 130);
            createLabel("Customer Name: " + userName, 30, 165);
            createLabel("Email: " + email, 30, 195);
            createLabel("-----------------------------", 30, 225);

            // 3️⃣ Create JTable for Reservation Details
            String[] columnNames = {"Res. ID", "Room ID", "Room Type", "Check-In", "Check-Out", "Duration"};
            tableModel = new DefaultTableModel(columnNames, 0);
            reservationTable = new JTable(tableModel);
            fetchReservationData();  // Load reservation data into table

            JScrollPane scrollPane = new JScrollPane(reservationTable);
            scrollPane.setBounds(30, 250, 460, 120);
            add(scrollPane);

            // 4️⃣ Total Price & Status below JTable
            createLabel("Total Price: $" + totalPrice, 30, 380);
            createLabel("Status: " + status, 30, 410);
        }
    private void fetchInvoiceData() {
        try (Connection conn = MySQLConnection.getConnection()) {
            // 1️⃣ Fetch invoice details
            String invoiceQuery = "SELECT userId, totalPrice, status FROM invoice WHERE invoiceId = ?";
            PreparedStatement invoiceStmt = conn.prepareStatement(invoiceQuery);
            invoiceStmt.setString(1, invoiceId);
            ResultSet invoiceRs = invoiceStmt.executeQuery();

            if (invoiceRs.next()) {
                userId = invoiceRs.getInt("userId");
                totalPrice = invoiceRs.getDouble("totalPrice");
                status = invoiceRs.getString("status");
            }

            // 2️⃣ Fetch user details
            String userQuery = "SELECT userName, email FROM users WHERE userId = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                userName = userRs.getString("userName");
                email = userRs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchReservationData() {
        try (Connection conn = MySQLConnection.getConnection()) {
            // Get all reservations for the same userId
            String query = "SELECT reservationId, roomId, roomType, checkInDate, checkOutDate, durationOfStay FROM reservation WHERE userId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("reservationId"),
                    rs.getInt("roomId"),
                    rs.getString("roomType"),
                    rs.getDate("checkInDate"),
                    rs.getDate("checkOutDate"),
                    rs.getInt("durationOfStay")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
