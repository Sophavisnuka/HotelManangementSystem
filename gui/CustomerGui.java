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
import java.sql.ResultSet;
import java.sql.SQLException;
import JavaProject.MySQLConnection;

import java.awt.event.ActionListener;
public class CustomerGui extends JFrame {
    JPanel panel;
    // private int currentUserId;
    public CustomerGui () {
        super("Admin Menu");
        setSize(520, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //place the gui in the middle
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
        createButton("2. View Invoice", 100, 150, e -> System.out.println("Not yet implement"));
        // 6.exit
        JLabel exit = new JLabel("Exit");
        exit.setBounds(100, 200,300,35);
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
                new ReservationGui(selectedRoomId[0], selectedRoomType[0]);  // Open reservation
                frame.dispose();
            } else {
                System.out.println("Please select a room first!");  // Handle case where no row is selected
            }
        });
        JButton cancelReservation = new JButton("Cancel Reservation");
        cancelReservation.addActionListener(e -> System.out.println("Not yet implement"));
        bottomPanel.add(makeReservation);
        bottomPanel.add(cancelReservation);
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
}
