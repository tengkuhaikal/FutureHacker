/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Event;

import static Account.MySQLConfiguration.pass;
import static Account.MySQLConfiguration.url;
import Account.User;
import static Event.Filepath.filepath;
import UI.Ui;
import activity.EventManager;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 *
 * @author Afiq Zafry
 */
public class CreateEvent {
    private static final Scanner scan = new Scanner(System.in);
    private static EventManager em = new EventManager();

    
    public static void createEvent(User user) {
        System.out.println("\n\n------Creating new event------");
        System.out.println("Educator: " + user.getUsername());

        System.out.print("Event Title: ");
        String eventTitle = scan.nextLine();
        
        System.out.print("Event Description: ");
        String eventDescription = scan.nextLine();
        System.out.print("Event Venue: ");
        String eventVenue=scan.nextLine();
       
        

        Date eventDate = null;
        Time eventTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Event Date (YYYY-MM-DD): ");
                String dateInput = scan.nextLine();
                em.addEvent(eventTitle, eventDescription,dateInput);

                eventDate = new Date(dateFormat.parse(dateInput).getTime());
                
                System.out.print("Event Time (HH:MM:SS): ");
                String timeInput = scan.nextLine();
                eventTime = new Time(timeFormat.parse(timeInput).getTime());
                
                validInput = true; // If parsing is successful, exit loop
            } catch (ParseException e) {
                System.out.println("Invalid date or time format. Please try again.");
            }
        }
        
        // Insert the new event into the database
        String insertEventQuery = "INSERT INTO event (title, description, venue, date, time, educator) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(insertEventQuery)) {

            pstmt.setString(1, eventTitle);
            pstmt.setString(2, eventDescription);
            pstmt.setString(3, eventVenue);
            pstmt.setDate(4, eventDate);
            pstmt.setTime(5, eventTime);
            pstmt.setString(6, user.getUsername());

            pstmt.executeUpdate();
            System.out.println("Event created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create event.");
        }
        
        System.out.println("Do you want to continue creating event > (yes:1 / No:etc)");
        int choice=scan.nextInt();
        if(choice==1){
            System.out.println("\n\n");
            createEvent(user);
        }else{
             Ui starter = new Ui();
             starter.mainmenu(user);
        }
            
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    public static ArrayList<HashMap<String, String>> readLocationsFromFile(String filename) {
//        ArrayList<HashMap<String, String>> locations = new ArrayList<>();
//
//        try {
//            Scanner reader = new Scanner(new FileInputStream(filename)); 
//            String line = null;
//            String blank;
//            String name;
//            String Location;
//            while (reader.hasNextLine()) {
//                line=reader.nextLine();
//                name= line;
//                line = reader.nextLine();
//                Location=line;
//                
//                
//                
//
//                    HashMap<String, String> location = new HashMap<>();
//                    location.put(name, Location);
//                    
//
//                    locations.add(location);
//                
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return locations;
//    }
    
//    public static ArrayList<HashMap<String, Double []>> readLocationsFromFile(String filename) {
//        ArrayList<HashMap<String, Double[]>> locations = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
//            String name = null;
//            String latitude = null;
//            String longitude = null;
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                line = line.trim(); // Trim leading and trailing whitespace
//
//                if (!line.isEmpty()) {
//                    if (name == null) {
//                        name = line; // Store the name
//                    } else if (latitude == null) {
//                        // Split the line to extract latitude and longitude
//                        String[] parts = line.split(",");
//                        if (parts.length == 2) {
//                            latitude = parts[0].trim();
//                            longitude = parts[1].trim();
//                            Double [] coordinate = new Double[2];
//                            coordinate[0]=Double.parseDouble(latitude);
//                            coordinate[1]=Double.parseDouble(longitude);
//                            // Create a HashMap to store the location
//                            HashMap<String, Double[]> location = new HashMap<>();
//                            location.put(name, coordinate);
//                            
//
//                            // Add the location to the list
//                            locations.add(location);
//
//                            // Reset variables for the next location
//                            name = null;
//                            latitude = null;
//                            longitude = null;
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return locations;
//    }
    
    public static ArrayList<HashMap<String, Double[]>> readLocationsFromFile(String filename) {
        ArrayList<HashMap<String, Double[]>> locations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String name = null;
            Double latitude = null;
            Double longitude = null;

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Trim leading and trailing whitespace

                if (!line.isEmpty()) {
                    if (name == null) {
                        name = line; // Store the name
                    } else if (latitude == null) {
                        // Split the line to extract latitude and longitude
                        String[] parts = line.split(",");
                        if (parts.length == 2) {
                            latitude = Double.parseDouble(parts[0].trim());
                            longitude = Double.parseDouble(parts[1].trim());

                            // Create a HashMap to store the location
                            HashMap<String, Double[]> location = new HashMap<>();
                            Double[] coordinates = {latitude, longitude};
                            location.put(name, coordinates);

                            // Add the location to the list
                            locations.add(location);

                            // Reset variables for the next location
                            name = null;
                            latitude = null;
                            longitude = null;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locations;
    }
    
     public static boolean locationExists(ArrayList<HashMap<String, Double[]>> locations, String locationName) {
        for (HashMap<String, Double[]> location : locations) {
            if (location.containsKey(locationName)) {
                return true;
            }
        }
        return false;
    }
    
    
    public static String enterlocation() {
        ArrayList<HashMap<String, Double[]>> locations = readLocationsFromFile(filepath);
        
       for (HashMap<String, Double[]> location : locations) {
        for (String key : location.keySet()) {
            System.out.println("Location: " + key);
            Double[] coordinates = location.get(key);
            System.out.println("[" + coordinates[0] + "" + coordinates[1]+"]");
        }
    }
       String locationName=null;
        Scanner scanner = new Scanner(System.in);
        boolean valid=true;
        while (valid) {
            System.out.print("Event Venue: ");
             locationName = scanner.nextLine();

            if (locationExists(locations, locationName)) {
                break;
                
                
            } else {
                System.out.println("Location not found. Please try again.");
                valid=true;
            }
        }
        return locationName;
    }
}
