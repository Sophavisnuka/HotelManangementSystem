package gui;
import JavaProject.Admin;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.SwingConstants;
import constant.commonConstant;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
public class AdminGui extends Form {
    JPanel panel;
    JButton viewCustomer;
    JButton removeCustomer;
    JButton viewReservation;
    JButton removeReservation;
    JButton updateRoomStatus;
    private Admin admin;
    public AdminGui () {
        super("Admin Menu");
        admin = new Admin();
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
        createButton("1. View Customer", 100, 100, e -> admin.viewUsers());
        createButton("2. View Reservation", 100, 150, e -> System.out.println("Haven't do it yet"));
        createButton("3. View All Rooms", 100, 200, e -> admin.loadRooms());
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
}
