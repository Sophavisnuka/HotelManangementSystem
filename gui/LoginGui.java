package gui;

import JavaProject.CheckInputException;
import JavaProject.User;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import constant.commonConstant;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGui extends Form {
    private JTextField emailField;
    private JPasswordField passwordField;
    public LoginGui () {
        super("Login");
        addGuiComponents();
    }
    private void addGuiComponents () {
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setBounds(0, 25, 520, 100);
        loginLabel.setForeground(commonConstant.TEXT_COLOR);
        loginLabel.setFont(new Font("Poppins", Font.BOLD, 40));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(loginLabel);
        // add email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(30,150,480,25);
        emailLabel.setForeground(commonConstant.TEXT_COLOR);
        emailLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(emailLabel);
        //create email field
        emailField = new JTextField();
        emailField.setBounds(30, 185,450,55);
        emailField.setBackground(commonConstant.SECONDARY_COLOR);
        emailField.setBackground(commonConstant.TEXT_COLOR);
        emailField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(emailField);
        //password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30,255,480,25);
        passwordLabel.setForeground(commonConstant.TEXT_COLOR);
        passwordLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(passwordLabel);
        //create password field
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 280,450,55);
        passwordField.setBackground(commonConstant.SECONDARY_COLOR);
        passwordField.setBackground(commonConstant.TEXT_COLOR);
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(passwordField);
        //add button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 350, 200, 50);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setBackground(commonConstant.TEXT_COLOR);
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setFont(new Font("Poppins", Font.BOLD, 18));
         // Action listener for login button
        loginButton.addActionListener(e -> loginUser());
        add(loginButton);
        //register button
        JLabel registerLabel = new JLabel("Register here");
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setForeground(commonConstant.TEXT_COLOR);
        //interactivity
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                LoginGui.this.dispose();
                new RegisterGui().setVisible(true);
            }
        });
        registerLabel.setBounds(150, 400, 200, 30);
        add(registerLabel);
    }
    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        User user = new User();
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
