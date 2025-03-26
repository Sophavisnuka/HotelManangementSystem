package gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import JavaProject.MySQLConnection;

public class InvoiceGui extends Form {
    private String invoiceId;
    private int userId;
    private String userName;
    private String email;
    private double totalPrice;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private String phoneNumber;

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
        createLabel("Phone Number: " + phoneNumber, 30, 225);
        createLabel("-----------------------------", 30, 250);
    
        // Create JTable with Checkboxes
        String[] columnNames = {"Res. ID", "Room ID", "Room Type", "Check-In", "Check-Out", "Duration", "Status", "Pay"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 7) ? Boolean.class : Object.class; // Checkbox for "Pay" column
            }
        };
    
        reservationTable = new JTable(tableModel);
        fetchReservationData();
    
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        scrollPane.setBounds(30, 275, 460, 120);
        add(scrollPane);
    
        // Total Price Label
        JLabel totalPriceLabel = createLabel("Total Price: $0.00", 30, 410);
    
        // Add Listener to JTable for checkbox selection
        tableModel.addTableModelListener(e -> updateTotalPrice(totalPriceLabel));
    
        // Pay Button
        JButton payButton = new JButton("Pay Selected");
        payButton.setBounds(180, 475, 150, 40);
        payButton.addActionListener(e -> processPayment(totalPriceLabel));
        add(payButton);
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
            }
            
            // 2️⃣ Fetch user details
            String userQuery = "SELECT userName, email, phoneNumber FROM users WHERE userId = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();
            
            if (userRs.next()) {
                userName = userRs.getString("userName");
                email = userRs.getString("email");
                phoneNumber = userRs.getString("phoneNumber");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void fetchReservationData() {
        try (Connection conn = MySQLConnection.getConnection()) {
            // 1️⃣ Fetch invoice details
            String invoiceQuery = "SELECT userId, totalPrice FROM invoice WHERE invoiceId = ?";
            PreparedStatement invoiceStmt = conn.prepareStatement(invoiceQuery);
            invoiceStmt.setString(1, invoiceId);
            ResultSet invoiceRs = invoiceStmt.executeQuery();
    
            if (invoiceRs.next()) {
                userId = invoiceRs.getInt("userId");
                totalPrice = invoiceRs.getDouble("totalPrice");
            }
    
            // 2️⃣ Fetch user details
            String userQuery = "SELECT userName, email, phoneNumber FROM users WHERE userId = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();
    
            if (userRs.next()) {
                userName = userRs.getString("userName");
                email = userRs.getString("email");
                phoneNumber = userRs.getString("phoneNumber");
            }
    
            // 3️⃣ Fetch only reservations with Pending payment status
            String query = "SELECT r.reservationId, r.roomId, r.roomType, r.checkInDate, r.checkOutDate, r.durationOfStay, i.status " +
                           "FROM reservation r " +
                           "JOIN invoice i ON r.reservationId = i.reservationId " +
                           "WHERE i.invoiceId = ? AND i.status = 'Pending'"; // Exclude Paid reservations
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, invoiceId);
            ResultSet rs = stmt.executeQuery();
    
            // 4️⃣ Populate table with data only for Pending reservations
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("reservationId"),
                    rs.getInt("roomId"),
                    rs.getString("roomType"),
                    rs.getDate("checkInDate"),
                    rs.getDate("checkOutDate"),
                    rs.getInt("durationOfStay"),
                    rs.getString("status"),
                    true // Enable the checkbox for pending reservations
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    private void processPayment(JLabel totalPriceLabel) {
        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to proceed with the payment?", "Confirm Payment", JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            double totalPayment = 0.0;
            try (Connection conn = MySQLConnection.getConnection()) {
                conn.setAutoCommit(false); // Start transaction
    
                // Iterate in reverse order to avoid row index shifting when deleting from JTable
                for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                    boolean isSelected = (boolean) tableModel.getValueAt(i, 7); // "Pay" Column
                    if (isSelected) {
                        String reservationId = (String) tableModel.getValueAt(i, 0); // Reservation ID
                        int roomId = (int) tableModel.getValueAt(i, 1); // Room ID
                        double roomPrice = getRoomPrice(roomId);
    
                        // 1️⃣ Check if the reservation has already been paid
                        String checkStatusQuery = "SELECT status FROM invoice WHERE reservationId = ?";
                        PreparedStatement checkStatusStmt = conn.prepareStatement(checkStatusQuery);
                        checkStatusStmt.setString(1, reservationId);
                        ResultSet statusRs = checkStatusStmt.executeQuery();
    
                        if (statusRs.next()) {
                            String status = statusRs.getString("status");
    
                            // If already paid, show a message and skip this reservation
                            if ("Paid".equals(status)) {
                                JOptionPane.showMessageDialog(this, "Reservation " + reservationId + " has already been paid.");
                                continue; // Skip this reservation
                            }
                        }
    
                        // 2️⃣ Update invoice as Paid
                        String updateInvoiceQuery = "UPDATE invoice SET status = 'Paid' WHERE reservationId = ?";
                        PreparedStatement updateInvoiceStmt = conn.prepareStatement(updateInvoiceQuery);
                        updateInvoiceStmt.setString(1, reservationId);
                        updateInvoiceStmt.executeUpdate();
    
                        totalPayment += roomPrice;
    
                        // 3️⃣ Mark reservation as paid (optional - if applicable)
                        // You can update the reservation status here if necessary.
    
                        // 4️⃣ Remove row from JTable
                        tableModel.removeRow(i);
                    }
                }
    
                // Update the total price in the invoice
                if (totalPayment > 0) {
                    String updateTotalPriceQuery = "UPDATE invoice SET totalPrice = totalPrice - ? WHERE invoiceId = ?";
                    PreparedStatement totalStmt = conn.prepareStatement(updateTotalPriceQuery);
                    totalStmt.setDouble(1, totalPayment);
                    totalStmt.setString(2, invoiceId);
                    totalStmt.executeUpdate();
    
                    conn.commit(); // Commit transaction
    
                    JOptionPane.showMessageDialog(this, "Payment successful! Reservations have been marked as paid.");
    
                    // Update UI
                    totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice - totalPayment));
                    totalPrice -= totalPayment;
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "No reservation selected for payment.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing payment.");
            }
        }
    }
    
    private void updateTotalPrice(JLabel totalPriceLabel) {
        double newTotal = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean isSelected = (boolean) tableModel.getValueAt(i, 7); // "Pay" Column
            if (isSelected) {
                int roomId = (int) tableModel.getValueAt(i, 1); // "Room ID" Column
                newTotal += getRoomPrice(roomId); // Add room price to total
            }
        }
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", newTotal));
    }
    
    
    private double getRoomPrice(int roomId) {
        double price = 0.0;
        try (Connection conn = MySQLConnection.getConnection()) {
            String query = "SELECT roomPrice FROM room WHERE roomId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                price = rs.getDouble("roomPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }
}
