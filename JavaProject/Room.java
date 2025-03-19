package JavaProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import gui.AdminGui;
import java.awt.BorderLayout;

public class Room {
    private AdminGui adminGui;
    public Room() {
        loadRooms();
    }
    
    // Load rooms from the database instead of a file
    public void loadRooms() {
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
                String roomId = rs.getString("roomId");
                String roomType = rs.getString("roomType");
                String roomPrice = rs.getString("roomPrice");
                String roomStatus = rs.getString("roomStatus");
                model.addRow(new Object[]{roomId, roomType, roomPrice, roomStatus});
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users!");
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(table);
        //add exit button
        JLabel exit = new JLabel("Back");
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                if (adminGui != null) {
                    adminGui.dispose();
                }
                new AdminGui().setVisible(true);
                frame.dispose();
            }
        });
        // Panel for the exit button at the top
        JPanel topPanel = new JPanel();
        topPanel.add(exit);
        // Adding components to the frame
        JButton removeButton = new JButton("Change rooms status");
        removeButton.addActionListener(e -> updateRoomStatus(table, model));
        // Panel for the remove button at the bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(removeButton);
        // Adding components to the frame
        frame.add(topPanel, BorderLayout.NORTH);         // Exit button at the top
        frame.add(scrollPane, BorderLayout.CENTER);      // Table in the center
        frame.add(bottomPanel, BorderLayout.SOUTH);      // Remove user button at the bottom
        frame.setVisible(true);
    }
    public void updateRoomStatus (JTable table, DefaultTableModel model) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1) {
            JOptionPane.showMessageDialog(null, "Please select a room to update.");
            return;
        }
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
    }
    // Display available rooms dynamically from database
    public static void displayAvailableRooms(String type) {
        String query = "SELECT roomId, roomPrice FROM room WHERE roomType = ? AND roomStatus = 'Yes'";
        try {
            ResultSet resultSet = MySQLConnection.executePreparedQuery(query, type).executeQuery();
            System.out.println("Available " + type + " Rooms:");
            while (resultSet.next()) {
                System.out.println("Room ID: " + resultSet.getInt("roomId") + " | Price: $" + resultSet.getDouble("roomPrice"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying available rooms: " + e.getMessage());
            e.getStackTrace();
        }
    }
    // Assign room dynamically and update database instead of file
    public int assignRoom(String type) {
        String selectQuery = "SELECT roomId FROM room WHERE roomType = ? AND roomStatus = 'Yes' LIMIT 1";
        try {
            ResultSet resultSet = MySQLConnection.executePreparedQuery(selectQuery, type).executeQuery();
            if (resultSet.next()) {
                int assignedRoomNumber = resultSet.getInt("roomId");

                // Update room availability in the database
                String updateQuery = "UPDATE room SET roomStatus = 'No' WHERE roomId = ?";
                int rowsUpdated = MySQLConnection.executePreparedUpdate(updateQuery, assignedRoomNumber);
                if (rowsUpdated > 0) {
                    System.out.println("Assigned Room ID: " + assignedRoomNumber);
                    return assignedRoomNumber;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error assigning room: " + e.getMessage());
        }
        System.out.println("No available rooms for " + type);
        return -1;
    }
    // Update room status in database
    public void updateStatus(int roomID, boolean roomStatus) {
        String updateQuery = "UPDATE room SET roomStatus = ? WHERE roomId = ?";
        int rowsUpdated = MySQLConnection.executePreparedUpdate(updateQuery, roomStatus ? "Yes" : "No", roomID);
        if (rowsUpdated > 0) {
            System.out.println("Room status updated: Room ID " + roomID + " is now " + (roomStatus ? "available" : "unavailable"));
        } else {
            System.out.println("Error: Room with ID " + roomID + " not found.");
        }
    }
}
