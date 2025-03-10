package JavaProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
    
    
    public boolean register (String UserName) {
        // // Validate input fields
        // if (CheckInput(userId, UserName, phoneNumber, email, password, role)) {
        //     System.out.println("Please input all the requirement");
        //     return false;
        // }

        // Check if username already exists
        // if (checkFromFIle(userId, UserName, phoneNumber, email, password, role)) {
        //     return false;
        // }

        return true;
    }

    @Override 
    public boolean login (String email, String password) {
        // Validate input fields
        if (CheckInput(userId, UserName, phoneNumber, email, password, role)) {
            System.out.println("Please input all the requirement");
            return false;
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
