package JavaProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Room {
    private static final String FILE_NAME = "RoomDetail.txt";
    private static ArrayList <String[]> roomList = new ArrayList<>();

    public Room() {
        loadRooms();
    }

    private void loadRooms() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                roomList.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading room details: " + e.getMessage());
        }
    }

    public static void displayAvailableRooms(String type) {
        System.out.println("Available " + type + " Rooms:");
        for (String[] room : roomList) {
            if (room[1].equalsIgnoreCase(type) && room[3].equalsIgnoreCase("Yes")) {
                System.out.println("Room ID: " + room[0] + " | Price: $" + room[2]);
            }
        }
    }

    public boolean assignRoom(String type, int reservationID) {
        for (String[] room : roomList) {
            if (room[1].equalsIgnoreCase(type) && room[3].equalsIgnoreCase("Yes")) {
                room[3] = "No"; // Mark as unavailable
                saveRooms();
                System.out.println("Assigned Room ID: " + room[0] + " | Price: $" + room[2]);
                return true;
            }
        }
        System.out.println("No available rooms for " + type);
        return false;
    }

    private void saveRooms() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] room : roomList) {
                writer.write(String.join(",", room) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving room details: " + e.getMessage());
        }
    }
    public void updateStatus(int roomID, boolean isAvailable) {
        for (String[] room : roomList) {
            if (Integer.parseInt(room[0]) == roomID) {  // Compare with room ID
                room[3] = isAvailable ? "Yes" : "No";  // Update availability status
                saveRooms();
                System.out.println("Room status updated: Room ID " + room[0] + " is now " + (isAvailable ? "available" : "unavailable"));
                return;
            }
        }
        System.out.println("Room with ID " + roomID + " not found.");
    }    
}
