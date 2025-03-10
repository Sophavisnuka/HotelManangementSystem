package JavaProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// User class implementing Authentication interface
public class User implements Authentication {
    
    // User attributes
    private static int userId;
    private static String UserName;
    private static String phoneNumber;
    protected String email;
    protected String password;
    private String role;

    // Static list to store all registered users
    protected static ArrayList <User> userList = new ArrayList<>();

    // Constructor to initialize a new user
    public User (int userId, String UserName, String phoneNumber, String email, String password, String role) {
        User.userId = userId;
        User.UserName = UserName;
        User.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Constructor for login purpose
    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }
    public static String getUserName() {
        return UserName;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }
    // Method to check if any input field is empty
    public boolean CheckInput (int userId, String UserName, String phoneNumber, String email, String password, String role) {
        if (UserName.isBlank() || password.isBlank() || email.isBlank() || phoneNumber.isBlank()) {
            return true;
        } 
        return false;
    }
    public void saveUserToFile (User user) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("CustomerAcc.txt", true));
            writer.write(User.userId + "," + User.UserName + "," +  User.phoneNumber + "," + user.email + "," + user.password + "," + user.role);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("File doesn't exist" + e.getMessage());
        }
    }
    public boolean checkFromFIle (int userId, String UserName, String phoneNumber, String email, String password, String role) {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("CustomerAcc.txt"));
            while ((line = reader.readLine())  != null) {
                String [] userData = line.split(",");
                if (userData.length == 6) {
                    // int id = userData[0];
                    String userName = userData[1];
                    String userEmail = userData[3];
                    if ( userName.equals(UserName) || userEmail.equals(email)) {
                        System.out.println("User already exist");
                        reader.close();
                        return true;
                    } 
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("File doesn't exist" + e.getMessage());
        }
        return false;
    }
    @Override 
    public boolean register (int userId, String UserName, String phoneNumber, String email, String password, String role) {
        // Validate input fields
        if (CheckInput(userId, UserName, phoneNumber, email, password, role)) {
            System.out.println("Please input all the requirement");
            return false;
        }
        if (role == null || role.isEmpty()) {
            role = "customer";
        }
    
        try (Connection con = MySQLConnection.getConnection()) {
            // Check if the user already exists
            String checkQuery = "SELECT * FROM users WHERE email = ? OR UserName = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkQuery);
            checkStatement.setString(1, email);
            checkStatement.setString(2, UserName);
            if (checkStatement.executeQuery().next()) {
                System.out.println("User already exists.");
                return false;
            }
    
            // Insert new user
            String insertQuery = "INSERT INTO users (userId, UserName, phoneNumber, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertQuery);
            insertStatement.setInt(1, userId);
            insertStatement.setString(2, UserName);
            insertStatement.setString(3, phoneNumber);
            insertStatement.setString(4, email);
            insertStatement.setString(5, password);
            insertStatement.setString(6, role);
    
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Registration successful!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        // Create and add new user to the list
        // User newUser = new User(userId, UserName, phoneNumber, email, password, role);
        // userList.add(newUser);
        // saveUserToFile(newUser);
        return true;
    }
    @Override 
    public boolean login (String email, String password) {
        // Validate input fields
        if (CheckInput(userId, UserName, phoneNumber, email, password, role)) {
            System.out.println("Please input all the requirement");
            return false;
        }
        try (Connection con = MySQLConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
    
            if (statement.executeQuery().next()) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Login failed! Incorrect email or password.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return true;
        // User loggedInUser = findUser(password, email);
        // if (loggedInUser != null) {
        //     if (loggedInUser.role.equals("admin")) {
        //         System.out.println("Logged in as Admin.");
        //         // Perform admin-specific actions
        //     } else {
        //         System.out.println("Logged in as Customer.");
        //     }
        //     return true;
        // } else {
        //     System.out.println("Fail attempt! Please try again");
        //     return false;
        // }
    }
    // Method to find a user by their username
    // public static User findUser(String email, String password) {
    //     BufferedReader reader = null;
    //     try {
    //         reader = new BufferedReader(new FileReader("CustomerAcc.txt"));
    //         String line;
    //         while ((line = reader.readLine())  != null) {
    //             String [] userData = line.split(",");
    //             if (userData.length == 6) {
    //                 String userEmail = userData[3];
    //                 String userPassword = userData[4];
    //                 String userRole = userData[5];
    //                 if (userEmail.equals(email) && userPassword.equals(password)) {
    //                     System.out.println("Login successful");
    //                     // return new User (userData[0], userData[1], userData[2], userData[3], userData[4], userData[5]);
    //                 } 
    //             }
    //         }
    //     } catch (IOException e) {
    //         System.out.println("File doesn't exist" + e.getMessage());
    //     } finally {
    //         try {
    //             if (reader != null) {
    //                 reader.close();  // Close the reader outside the loop
    //             }
    //         } catch (IOException e) {
    //             System.out.println("Error closing file: " + e.getMessage());
    //         }
    //     }
    //     System.out.println("Login failed: User does not exist.");
    //     return null;
    // }
}
