package gui;

import JavaProject.User;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import constant.commonConstant;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class RegisterGui extends Form {
    private JTextField usernameField, emailField, phoneNumberField;
    private JPasswordField passwordField;
    private User user;

    public RegisterGui () {
        super("Register");
        user = new User();
        addGuiComponents();
    }
    private void addGuiComponents () {
        JLabel registerLabel = new JLabel("Register");
        registerLabel.setBounds(0, 20, 520, 100);
        registerLabel.setForeground(commonConstant.TEXT_COLOR);
        registerLabel.setFont(new Font("Poppins", Font.BOLD, 40));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(registerLabel);
        //add username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(30,130,480,20);
        usernameLabel.setForeground(commonConstant.TEXT_COLOR);
        usernameLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        //create email field
        usernameField = new JTextField();
        usernameField.setBounds(30, 165,450,50);
        usernameField.setBackground(commonConstant.SECONDARY_COLOR);
        usernameField.setBackground(commonConstant.TEXT_COLOR);
        usernameField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(usernameLabel);
        add(usernameField);
        // add email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(30,230,480,20);
        emailLabel.setForeground(commonConstant.TEXT_COLOR);
        emailLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        //create email field
        emailField = new JTextField();
        emailField.setBounds(30, 265,450,50);
        emailField.setBackground(commonConstant.SECONDARY_COLOR);
        emailField.setBackground(commonConstant.TEXT_COLOR);
        emailField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(emailLabel);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(30,330,480,20);
        passwordLabel.setForeground(commonConstant.TEXT_COLOR);
        passwordLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        //create email field
        passwordField = new JPasswordField();
        passwordField.setBounds(30, 365,450,50);
        passwordField.setBackground(commonConstant.SECONDARY_COLOR);
        passwordField.setBackground(commonConstant.TEXT_COLOR);
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(passwordLabel);
        add(passwordField);
        //phone number

        JLabel phoneNumberLabel = new JLabel("Phone number");
        phoneNumberLabel.setBounds(30,430,480,20);
        phoneNumberLabel.setForeground(commonConstant.TEXT_COLOR);
        phoneNumberLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        //create email field
        phoneNumberField = new JTextField();
        phoneNumberField.setBounds(30, 465,450,50);
        phoneNumberField.setBackground(commonConstant.SECONDARY_COLOR);
        phoneNumberField.setBackground(commonConstant.TEXT_COLOR);
        phoneNumberField.setFont(new Font("Poppins", Font.PLAIN, 18));
        add(phoneNumberLabel);
        add(phoneNumberField);
        //add button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 530, 200, 50);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.setBackground(commonConstant.TEXT_COLOR);
        registerButton.setHorizontalAlignment(SwingConstants.CENTER);
        registerButton.setFont(new Font("Poppins", Font.BOLD, 18));
        //input to the database , registerUser is from user class in JavaProject package
        registerButton.addActionListener(e -> registerUser());
        add(registerButton);
        //register button
        JLabel loginLabel = new JLabel("Login here");
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setForeground(commonConstant.TEXT_COLOR);
        loginLabel.setBounds(150, 585, 200, 30);
         //interactivity
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                RegisterGui.this.dispose();
                new LoginGui().setVisible(true);
            }
        });
        add(loginLabel);
    }
    private void registerUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String phoneNumber = phoneNumberField.getText();

        if (user.registerUser(username, phoneNumber, email, password)) {
            this.dispose(); // Close registration form
            new CustomerGui().setVisible(true); // Open login form
        }
    }
}
