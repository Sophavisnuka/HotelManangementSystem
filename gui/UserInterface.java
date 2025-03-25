package gui;

import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import JavaProject.CheckInputException;
import JavaProject.MySQLConnection;

public class UserInterface implements Authentication {
    public static int userId;
    private String role;
    public static int getUserId() {
        return userId;
    }
    @Override
    public boolean registerUser(String username, String phone, String email, String password) {
        try {
            // Validate inputs
            CheckInputException.isEmptyInput(username);
            CheckInputException.isEmptyInput(phone);
            CheckInputException.isNumberValid(phone);
            CheckInputException.isValidPhoneNumber(phone);
            CheckInputException.isEmptyInput(email);
            CheckInputException.isEmptyInput(password);

            role = "Customer";
            String query = "INSERT INTO users (username, phoneNumber, email, passwords, role) VALUES (?, ?, ?, ?, ?)";
            int rowAffected = MySQLConnection.executePreparedUpdate(query, username, phone, email, password, role);
            
            if (rowAffected > 0) {
                JOptionPane.showMessageDialog(null, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to register user.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (CheckInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    @Override
    public boolean loginUser(String loginEmail, String loginPassword) {
        try {
            CheckInputException.isEmptyInput(loginEmail);
            CheckInputException.isEmptyInput(loginPassword);
            String[] emailParts = loginEmail.split("@");
            String domain = emailParts.length > 1 ? emailParts[1].toLowerCase() : "";
            role = domain.contains("admin") ? "admin" : "customer";
            String query = "SELECT userId, role FROM users WHERE email = ? AND passwords = ?";
            try (PreparedStatement stmt = MySQLConnection.executePreparedQuery(query, loginEmail, loginPassword);
                ResultSet rs = stmt.executeQuery()) {

                if (rs == null || !rs.next()) {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    userId = rs.getInt("userId");
                    role = rs.getString("role");
                    JOptionPane.showMessageDialog(null, "Login successful! Welcome user " + " ID: " +  userId, "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }
        } catch (CheckInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error during login!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}
