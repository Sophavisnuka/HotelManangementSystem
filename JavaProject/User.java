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
    private static String UserName;
    private static String phoneNumber;
    protected String email;
    protected String password;

    // Static list to store all registered users
    protected static ArrayList <User> userList = new ArrayList<>();

    // Constructor to initialize a new user
    public User (String UserName, String phoneNumber, String email, String password) {
        User.UserName = UserName;
        User.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
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
    public boolean CheckInput (String UserName, String phoneNumber, String email, String password) {
        if (UserName.isBlank() || password.isBlank() || email.isBlank() || phoneNumber.isBlank()) {
            return true;
        } 
        return false;
    }
    public void saveUserToFile (User user) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("CustomerAcc.txt", true));
            writer.write(User.UserName + "," +  User.phoneNumber + "," + user.email + "," + user.password);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("File doesn't exist" + e.getMessage());
        }
    }
    public boolean checkFromFIle (String UserName, String phoneNumber, String email, String password) {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader("CustomerAcc.txt"));
            while ((line = reader.readLine())  != null) {
                String [] userData = line.split(",");
                if (userData.length == 4) {
                    String userName = userData[0];
                    String userEmail = userData[2];
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
    public boolean register (String UserName, String phoneNumber, String email, String password) {
        // Validate input fields
        if (CheckInput(UserName, phoneNumber, email, password)) {
            System.out.println("Please input all the requirement");
            return false;
        }
        // Check if username already exists
        if (checkFromFIle(UserName, phoneNumber, email, password)) {
            return false;
        }
        // Create and add new user to the list
        User newUser = new User(UserName, phoneNumber, email, password);
        userList.add(newUser);
        saveUserToFile(newUser);
        return true;
    }

    @Override 
    public boolean login (String email, String password) {
        // Validate input fields
        if (CheckInput(UserName, phoneNumber, email, password)) {
            System.out.println("Please input all the requirement");
            return false;
        }
        User loggedInUser = findUser(password, email);
        if (loggedInUser != null) {
            return true;
        } else {
            System.out.println("Fail attempt! Please try again");
            return false;
        }
    }
    // Method to find a user by their username
    public static User findUser(String email, String password) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("CustomerAcc.txt"));
            String line;
            while ((line = reader.readLine())  != null) {
                String [] userData = line.split(",");
                if (userData.length == 4) {
                    String userEmail = userData[2];
                    String userPassword = userData[3];

                    if (userEmail.equals(email) && userPassword.equals(password)) {
                        System.out.println("Login successful");
                        return new User (userData[0], userData[1], userData[2], userData[3]);
                    } 
                }
            }
        } catch (IOException e) {
            System.out.println("File doesn't exist" + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();  // Close the reader outside the loop
                }
            } catch (IOException e) {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
        System.out.println("Login failed: User does not exist.");
        return null;
    }
}
