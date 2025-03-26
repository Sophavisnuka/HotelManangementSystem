package gui;

import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import constant.commonConstant;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends Form {
    // private Form form;
    private JTextField usernameField, emailField, phoneNumberField;
    private JPasswordField passwordField;
    private String username,email,password,phoneNumber;

    private UserInterface user = new UserInterface(); // Initialize user;

    public RegisterGui () {
        super("Register");
        addGuiComponents();
        getContentPane().setBackground(commonConstant.PRIMARY_COLOR);
    }
    @Override
    protected void addGuiComponents () {
        // Register Title
        JLabel registerLabel = createLabel("Register", 0, 0, 520, 100, 40);
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Username
        createLabel("Username", 30, 100);
        usernameField = createTextField(30, 135, 450, 50);
        // Email
        createLabel("Email", 30, 200);
        emailField = createTextField(30, 235, 450, 50);
        // Password
        createLabel("Password", 30, 300);
        passwordField = createPasswordField(30, 335, 450, 50);
        // Phone Number
        createLabel("Phone number", 30, 400);
        phoneNumberField = createTextField(30, 435, 450, 50);
        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 500, 200, 50);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.setBackground(commonConstant.TEXT_COLOR);
        registerButton.setHorizontalAlignment(SwingConstants.CENTER);
        registerButton.setFont(new Font("Poppins", Font.BOLD, 18));
        registerButton.addActionListener(e -> registerUser());
        add(registerButton);

        // Login Label
        JLabel loginLabel = new JLabel("Login here");
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setForeground(commonConstant.TEXT_COLOR);
        loginLabel.setBounds(150, 555, 200, 30);
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
        username = usernameField.getText();
        email = emailField.getText();
        password = new String(passwordField.getPassword());
        phoneNumber = phoneNumberField.getText();
        if (user.registerUser(username, phoneNumber, email, password)) {
            this.dispose(); // Close registration form
            new CustomerGui().setVisible(true); // Open login form
        }
    }
}
