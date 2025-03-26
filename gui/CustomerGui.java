package gui;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import constant.commonConstant;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import JavaProject.MySQLConnection;
import java.awt.event.ActionListener;

public class CustomerGui extends JFrame {
    // Constructor now accepts userId as a parameter
    public CustomerGui() {
        super("Customer");
        // this.userId = userId;  // Store userId
        setSize(520, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null); 
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(commonConstant.PRIMARY_COLOR);
        addGuiComponents();
    }
    private void addGuiComponents () {  
        JLabel viewCustomerLabel = new JLabel("Welcome! Customer");
        viewCustomerLabel.setBounds(0, 25, 520, 100);
        viewCustomerLabel.setForeground(commonConstant.TEXT_COLOR);
        viewCustomerLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        viewCustomerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(viewCustomerLabel);
        //create button
        createButton("1. View Room", 100, 100, e -> loadRooms());
        createButton("2. View Invoice", 100, 150, e -> showInvoice());
        createButton("3. Cancel Reservation", 100, 200, e -> cancelReservation());

        // 6.exit
        JLabel exit = new JLabel("Exit");
        exit.setBounds(100, 250,300,35);
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setForeground(commonConstant.TEXT_COLOR);
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                CustomerGui.this.dispose();
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
        frame.add(scrollPane, BorderLayout.CENTER);      // Table in the center
        // Allow user to select a row and proceed to reservation
        // Store selected room details
        final int[] selectedRoomId = { -1 };
        final String[] selectedRoomType = { null };
        final String [] selectedRoomStatus = { null };
        // Handle row selection
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedRoomId[0] = (int) table.getValueAt(selectedRow, 0);
                    selectedRoomType[0] = (String) table.getValueAt(selectedRow, 1);
                    selectedRoomStatus[0] = (String) table.getValueAt(selectedRow, 3);
                    if ("no".equalsIgnoreCase(selectedRoomStatus[0])) {
                        JOptionPane.showMessageDialog(frame, "Room " +  selectedRoomId[0] + " is currently unavailable.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        });
        // Adding components to the frame
        JPanel bottomPanel = new JPanel();
        JButton makeReservation = new JButton("Book Reservation");
        makeReservation.addActionListener(e -> {
            if (selectedRoomId[0] != -1 && selectedRoomType[0] != null) {
                new ReservationGui(selectedRoomId[0], selectedRoomType[0]);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a room first!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // JButton cancelReservation = new JButton("Cancel Reservation");
        // cancelReservation.addActionListener(e -> cancelReservation());
        // bottomPanel.add(cancelReservation);
        bottomPanel.add(makeReservation);
        //add exit button
        JLabel exit = new JLabel("Home");
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exit.setFont(new Font("Poppins", Font.PLAIN, 18));
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                CustomerGui.this.dispose();
                frame.dispose();
                setVisible(true);
            }
        });
        // Panel for the exit button at the top
        JPanel topPanel = new JPanel();
        topPanel.add(exit);
        // Adding components to the frame
        frame.add(topPanel, BorderLayout.NORTH);         // Exit button at the top
        frame.add(scrollPane, BorderLayout.CENTER);      // Table in the center
        frame.add(bottomPanel, BorderLayout.SOUTH);     // Remove user button at the bottom
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
    private void cancelReservation() {
        // Ask user for Reservation ID and Room ID
        String reservationId = JOptionPane.showInputDialog("Enter Reservation ID to cancel:");
        String roomId = JOptionPane.showInputDialog("Enter Room ID for confirmation:");
    
        if (reservationId == null || roomId == null || reservationId.isEmpty() || roomId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Reservation ID and Room ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Verify if the reservation exists in the database
        String queryCheck = "SELECT * FROM reservation WHERE reservationId = ? AND roomId = ?";
        try (Connection conn = MySQLConnection.getConnection();
            PreparedStatement stmtCheck = conn.prepareStatement(queryCheck)) {
    
            stmtCheck.setString(1, reservationId);
            stmtCheck.setString(2, roomId);
            ResultSet rs = stmtCheck.executeQuery();
    
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "No matching reservation found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Confirm cancellation
            int confirm = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to cancel this reservation?", 
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
    
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete from the database
                String queryDelete = "DELETE FROM reservation WHERE reservationId = ? AND roomId = ?";
                try (PreparedStatement stmtDelete = conn.prepareStatement(queryDelete)) {
                    stmtDelete.setString(1, reservationId);
                    stmtDelete.setString(2, roomId);
                    int rowsDeleted = stmtDelete.executeUpdate();
    
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Reservation canceled successfully.");
    
                        // Refresh the table
                        loadRooms(); // Reload rooms to reflect changes
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to cancel reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showInvoice() {
        String userId = "USER_ID_HERE"; // Replace with actual user ID from session/login
        String query = "SELECT invoiceId, roomId, totalAmount, paymentStatus FROM invoice WHERE userId = '" + userId + "'";
    
        JFrame frame = new JFrame("Your Invoice");
        frame.setSize(470, 300);
        frame.setLocationRelativeTo(null);
    
        String[] columnNames = {"Invoice ID", "Room ID", "Total Amount", "Payment Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
    
        try {
            ResultSet rs = MySQLConnection.executeQuery(query);
            while (rs.next()) {
                int invoiceId = rs.getInt("invoiceId");
                int roomId = rs.getInt("roomId");
                double totalAmount = rs.getDouble("totalAmount");
                String paymentStatus = rs.getString("paymentStatus");
    
                model.addRow(new Object[]{invoiceId, roomId, totalAmount, paymentStatus});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading invoices!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
