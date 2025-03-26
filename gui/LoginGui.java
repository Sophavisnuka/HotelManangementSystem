package gui;

import JavaProject.CheckInputException;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import constant.commonConstant;

public class LoginGui extends Form {
    private JTextField emailField;
    private JPasswordField passwordField;
    private String email, password;
    public LoginGui () {
        super("Login");
        getContentPane().setBackground(commonConstant.PRIMARY_COLOR);
        addGuiComponents();
    }
    @Override
    protected void addGuiComponents() {
        // createLabel("Login", 0, 25, 520, 100, 40);
        JLabel loginLabel = createLabel("Login", 0, 0, 520, 100, 40);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        createLabel("Email", 30, 150);
        emailField = createTextField(30, 185, 450, 55);
        
        createLabel("Password", 30, 255);
        passwordField = createPasswordField(30, 285, 450, 55);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 350, 200, 50);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setBackground(commonConstant.TEXT_COLOR);
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setFont(new Font("Poppins", Font.BOLD, 18));
        loginButton.addActionListener(e -> loginUser());
        add(loginButton);

        JLabel registerLabel = new JLabel("Register here");
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(commonConstant.TEXT_COLOR);
        registerLabel.setBounds(150, 400, 200, 30);
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                LoginGui.this.dispose();
                new RegisterGui().setVisible(true);
            }
        });
        add(registerLabel);
    }
    private void loginUser() {
        email = emailField.getText();
        password = new String(passwordField.getPassword());
        UserInterface user = new UserInterface();
        try {
            CheckInputException.isEmptyInput(email);
            CheckInputException.isEmptyInput(password);
            if (user.loginUser(email, password)) {
                this.dispose();  // Close the login window on successful login
                if (email.contains("@admin")) {
                    new AdminGui().setVisible(true);  // Open the dashboard after successful login
                } else {
                    new CustomerGui().setVisible(true);
                }
            }
        } catch (CheckInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
