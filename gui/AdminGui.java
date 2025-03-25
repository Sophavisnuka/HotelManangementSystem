package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Cursor;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import JavaProject.MySQLConnection;
import constant.commonConstant;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

public class AdminGui extends Form  {
    // private Admin admin;
    public AdminGui () {
        super("Admin Menu");
        // admin = new Admin();
        addGuiComponents();
    }
    private void addGuiComponents () {  
        JLabel viewCustomerLabel = new JLabel("Welcome! Admin");
        viewCustomerLabel.setBounds(0, 25, 520, 100);
        viewCustomerLabel.setForeground(commonConstant.TEXT_COLOR);
        viewCustomerLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        viewCustomerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(viewCustomerLabel);
        //create button
        createButton("1. View Customer", 100, 100, e -> viewUsers());
        createButton("2. View Reservation", 100, 150, e -> viewAllReservations());
        createButton("3. View All Rooms", 100, 200, e -> loadRooms());
        // 6.exit
        JLabel exit = new JLabel("Exit");
        exit.setBounds(100, 250,300,35);
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setForeground(commonConstant.TEXT_COLOR);
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                AdminGui.this.dispose();
                new LoginGui().setVisible(true);
            }
        });
        add(exit);
    }
    private void createButton (String text, int x, int y, ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 300, 35);
        button.setForeground(commonConstant.TEXT_COLOR);
        button.setBackground(commonConstant.SECONDARY_COLOR);
        button.setFont(new Font("Poppins", Font.PLAIN, 18));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(action);
        add(button);
    }

    private void viewUsers() {
        JFrame frame = new JFrame("User List");
        frame.setSize(470,350);
        frame.setLocationRelativeTo(null);
        String[] columnNames = {"User ID", "Username", "Email", "Role"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        // Adjust row height
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.setFont(new Font("Poppins", Font.PLAIN, 15));  // Set font size to 18
        String query = "SELECT userId, userName, email, role FROM users";
    
        try (PreparedStatement stmt = MySQLConnection.executePreparedQuery(query);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("userId");  // Get userId
                String userName = rs.getString("userName");
                String email = rs.getString("email");
                String role = rs.getString("role");
                model.addRow(new Object[]{id, userName, email, role});
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users!");
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(table);
        //add exit button
        JLabel exit = new JLabel("Home");
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                new AdminGui().setVisible(true);
                frame.dispose();
            }
        });
        // Panel for the exit button at the top
        JPanel topPanel = new JPanel();
        topPanel.add(exit);
        // "Remove user" button
        JButton removeButton = new JButton("Remove user");
        removeButton.addActionListener(e -> removeRowMethod(table, model));
        // Panel for the remove button at the bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(removeButton);
        // Adding components to the frame
        frame.add(topPanel, BorderLayout.NORTH);         // Exit button at the top
        frame.add(scrollPane, BorderLayout.CENTER);      // Table in the center
        frame.add(bottomPanel, BorderLayout.SOUTH);      // Remove user button at the bottom
        frame.setVisible(true);
    }

    private void removeRowMethod (JTable table, DefaultTableModel model) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1) {
            int userId = (int) model.getValueAt(selectRow, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_NO_OPTION) {
                String query = "DELETE FROM users WHERE userId = ?";
                int rowAffected = MySQLConnection.executePreparedUpdate(query, userId);
                if (rowAffected > 0) {
                    model.removeRow(selectRow); // Remove from JTable
                    JOptionPane.showMessageDialog(null, "User deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete user.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select the remove row");
        }
    }
    // @Override
    private void loadRooms() {
        JFrame frame = new JFrame("All Rooms");
        frame.setSize(470,350);
        frame.setLocationRelativeTo(null);
        String[] columnNames = {"roomId", "roomType", "roomPrice", "roomStatus"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        // Adjust row height
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.setFont(new Font("Poppins", Font.PLAIN, 15));  // Set font size to 18
        String query = "SELECT * FROM room";  // Adjust table name if needed
        
        try {
            ResultSet rs = MySQLConnection.executeQuery(query); // ✅ FIXED: Use executeQuery directly
            while (rs.next()) {
                int roomId = rs.getInt("roomId");
                String roomType = rs.getString("roomType");
                float roomPrice = rs.getFloat("roomPrice");
                String roomStatus = rs.getString("roomStatus");
                model.addRow(new Object[]{roomId, roomType, roomPrice, roomStatus});
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users!");
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(table);
        //add exit button
        JLabel exit = new JLabel("Home");
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                new AdminGui().setVisible(true);
                frame.dispose();
            }
        });
        // Panel for the exit button at the top
        JPanel topPanel = new JPanel();
        topPanel.add(exit);
        // Adding components to the frame
        JPanel bottomPanel = new JPanel();
        JButton changeStatus = new JButton("Change rooms status");
        changeStatus.addActionListener(e -> updateRoomStatus(table, model));
        
        JButton changePrice = new JButton("Change rooms Price");
        changePrice.addActionListener(e -> updateRoomPrice(table, model));
        changePrice.setBounds(150, 350, 200, 50);
        // Panel for the remove button at the bottom
        bottomPanel.add(changeStatus);
        bottomPanel.add(changePrice);
        // Adding components to the frame
        frame.add(topPanel, BorderLayout.NORTH);         // Exit button at the top
        frame.add(scrollPane, BorderLayout.CENTER);      // Table in the center
        frame.add(bottomPanel, BorderLayout.SOUTH);     // Remove user button at the bottom
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
    
    private void updateRoomStatus (JTable table, DefaultTableModel model) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1) {
            int roomId = (int) model.getValueAt(selectRow, 0);
            String currentStatus = (String) model.getValueAt(selectRow, 3);
            String newStatus = currentStatus.equals("Yes") ? "No" : "Yes";
            String updateQuery = "UPDATE room SET roomStatus = ? WHERE roomId = ?";
            
            try (Connection conn = MySQLConnection.getConnection();  // ✅ Get connection
                PreparedStatement stmt = conn.prepareStatement(updateQuery)){
                stmt.setString(1, newStatus);
                stmt.setInt(2, roomId);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    model.setValueAt(newStatus, selectRow, 3);
                    JOptionPane.showMessageDialog(null, "Room status updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error updating room status!");
                }
            } catch (SQLException e) {
                System.out.println("Error update room Status: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a room to update.");
            return;
        }
    }

    private void updateRoomPrice (JTable table, DefaultTableModel model) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1) {
            int roomId = (int) model.getValueAt(selectRow, 0);
            Object Price= model.getValueAt(selectRow, 2);
            Double currentPrice;
            if (Price instanceof Float) {
                currentPrice = ((Float) Price).doubleValue();
            } else if (Price instanceof Double) {
                currentPrice = (Double) Price;
            } else {
                // Fallback if it's a string, in case the table model is inconsistent
                currentPrice = Double.parseDouble(Price.toString());
            }
            String newPrice = JOptionPane.showInputDialog("Enter new price for room " + roomId + "(Current Price: " + currentPrice +  ")");
            if (newPrice != null && !newPrice.trim().isEmpty()) {
                String updateQuery = "UPDATE room SET roomPrice = ? WHERE roomId = ?";
                try (Connection conn = MySQLConnection.getConnection();  // ✅ Get connection
                    PreparedStatement stmt = conn.prepareStatement(updateQuery)){
                    stmt.setString(1, newPrice);
                    stmt.setInt(2, roomId);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        model.setValueAt(newPrice, selectRow, 2);
                        JOptionPane.showMessageDialog(null, "Room price updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error updating room price!");
                    }
                } catch (SQLException e) {
                    System.out.println("Error update room Status: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid price input.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a room to update.");
            return;
        }
    }

    public void viewAllReservations() {
        JFrame frame = new JFrame("All Reservations");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        // Define column names
        String[] columnNames = {"User ID", "Reservation ID", "Room ID", "Room Type", "Check-in", "Check-out", "Duration"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        // Adjust row height and font
        table.setRowHeight(30);
        table.setFont(new Font("Poppins", Font.PLAIN, 15));
        String query = "SELECT userId, reservationId, roomId, roomType, checkInDate, checkOutDate, durationOfStay FROM reservation";
        try (ResultSet rs = MySQLConnection.executeQuery(query)) {
            while (rs.next()) {
                int userId = rs.getInt("userId");
                String reservationId = rs.getString("reservationId");
                int roomId = rs.getInt("roomId");
                String roomType = rs.getString("roomType");
                String checkIn = rs.getString("checkInDate");
                String checkOut = rs.getString("checkOutDate");
                int duration = rs.getInt("durationOfStay");
    
                model.addRow(new Object[]{userId, reservationId, roomId, roomType, checkIn, checkOut, duration});
            }
        } catch (SQLException e) {
            System.out.println("Error fetching reservations!");
            e.printStackTrace();
        }
    
        JScrollPane scrollPane = new JScrollPane(table);
    
        // Home button to return to Admin GUI
        JLabel exit = new JLabel("Home");
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AdminGui().setVisible(true);
                frame.dispose();
            }
        });
    
        JPanel topPanel = new JPanel();
        topPanel.add(exit);
        JButton removeButton = new JButton("Remove Reservation");
        removeButton.addActionListener(e -> removeReservation(table, model));
    
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(removeButton);
    
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    public void removeReservation(JTable table, DefaultTableModel model) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1) {
            String reservationId = (String) model.getValueAt(selectRow, 0); // Get reservation ID from the table
            int confirm = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this reservation?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    
            if (confirm == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM reservation WHERE reservationId = ?"; // SQL delete query
                int rowAffected = MySQLConnection.executePreparedUpdate(query, reservationId);
    
                if (rowAffected > 0) {
                    model.removeRow(selectRow); // Remove from JTable
                    JOptionPane.showMessageDialog(null, "Reservation deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete reservation.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a reservation to remove.");
        }
    }
}
