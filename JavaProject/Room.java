package JavaProject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Room {
    private static ArrayList<String[]> roomList = new ArrayList<>();

    public Room() {
        loadRooms();
    }

    // Load rooms from the database instead of a file
    private void loadRooms() {
        roomList.clear();
        String query = "SELECT * FROM room";  // Adjust table name if needed
        ResultSet resultSet = MySQLConnection.executeQuery(query);

        try {
            while (resultSet.next()) {
                String[] roomDetails = {
                    String.valueOf(resultSet.getInt("roomID")),   // Assuming roomID is an integer
                    resultSet.getString("roomType"),
                    String.valueOf(resultSet.getDouble("roomPrice")), // Assuming price is a decimal
                    resultSet.getString("isAvailable")            // Assuming availability is stored as 'Yes' or 'No'
                };
                roomList.add(roomDetails);
            }
        } catch (SQLException e) {
            System.out.println("Error loading room details from database: " + e.getMessage());
        }
    }

    // Display available rooms dynamically from database
    public static void displayAvailableRooms(String type) {
        String query = "SELECT roomId, roomPrice FROM room WHERE roomType = ? AND roomStatus = 'Yes'";
        try {
            ResultSet resultSet = MySQLConnection.executePreparedQuery(query, type).executeQuery();
            System.out.println("Available " + type + " Rooms:");
            while (resultSet.next()) {
                System.out.println("Room ID: " + resultSet.getInt("roomId") + " | Price: $" + resultSet.getDouble("roomPrice"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying available rooms: " + e.getMessage());
        }
    }

    // Assign room dynamically and update database instead of file
    public int assignRoom(String type) {
        String selectQuery = "SELECT roomId FROM room WHERE roomType = ? AND roomStatus = 'Yes' LIMIT 1";
        try {
            ResultSet resultSet = MySQLConnection.executePreparedQuery(selectQuery, type).executeQuery();
            if (resultSet.next()) {
                int assignedRoomNumber = resultSet.getInt("roomId");

                // Update room availability in the database
                String updateQuery = "UPDATE room SET roomStatus = 'No' WHERE roomId = ?";
                int rowsUpdated = MySQLConnection.executePreparedUpdate(updateQuery, assignedRoomNumber);
                if (rowsUpdated > 0) {
                    System.out.println("Assigned Room ID: " + assignedRoomNumber);
                    return assignedRoomNumber;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error assigning room: " + e.getMessage());
        }
        System.out.println("No available rooms for " + type);
        return -1;
    }

    // Update room status in database
    public void updateStatus(int roomID, boolean roomStatus) {
        String updateQuery = "UPDATE room SET roomStatus = ? WHERE roomId = ?";
        int rowsUpdated = MySQLConnection.executePreparedUpdate(updateQuery, roomStatus ? "Yes" : "No", roomID);
        if (rowsUpdated > 0) {
            System.out.println("Room status updated: Room ID " + roomID + " is now " + (roomStatus ? "available" : "unavailable"));
        } else {
            System.out.println("Error: Room with ID " + roomID + " not found.");
        }
    }
}