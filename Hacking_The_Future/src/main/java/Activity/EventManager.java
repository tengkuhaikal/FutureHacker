package activity;

import static Account.MySQLConfiguration.pass;
import static Account.MySQLConfiguration.url;
import Account.User;
import static Event.CreateEvent.createEvent;
import UI.Ui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class EventManager {
    private List<Event> events = loadEventsFromDatabase();
    Event event;
    static String url = "jdbc:mysql://localhost:3306/datastructure";
    public static String pass = "root";

//    public EventManager() {
//        this.events = new ArrayList<>();
//        // Initialize with some sample events
//        this.events.add(new Event("Tech Talk", "Learn about the latest in technology.", "2024-06-07","20:00:00"));
//        this.events.add(new Event("Hackathon", "Collaborate and create apps within a day.", "2024-07-15", "15:30:00"));
//        this.events.add(new Event("Workshop on AI", "Hands-on session on Artificial Intelligence", "2024-07-05", "10:15:00"));
//        this.events.add(new Event("Cybersecurity Seminar", "Understanding the importance of cybersecurity", "2024-07-20", "19:00:00"));
//        this.events.add(new Event("Web Development Bootcamp", "Learn how to build websites", "2024-07-10", "17:45:00"));
//        this.events.add(new Event("Data Science Conference", "Discussing trends in data science", "2024-07-25", "12:00:00"));
//        this.events.add(new Event("Mobile App Development", "Create mobile applications.", "2024-07-01", "13:35:00"));
//        this.events.add(new Event("Blockchain Basics", "Introduction to blockchain technology", "2024-07-15", "15:00:00"));
//        this.events.add(new Event("Cloud Computing", "Exploring cloud services and solutions", "2024-07-01", "18:15:00"));
//        this.events.add(new Event("Networking Event", "Meet and connect with professionals", "2024-07-10", "11:20:00"));
//    }
    
    public List<Event> loadEventsFromDatabase() {
        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT title, description, date, time FROM event")) {
             this.events = new ArrayList<>();
            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String eventDate = rs.getString("date");
                String eventTime = rs.getString("time");
                this.events.add(new Event(title, description, eventDate, eventTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.events;
    }

    // Method to create a new event
    public void addEvent(String title, String description, String date,String time) {
        Event newEvent = new Event(title, description, date,time);
        events.add(newEvent);
        System.out.println("New event created: " + title);
    }


public void joinEvent(Event event, User user) {
    // Add points for joining an event using User's setCurrentPoints method
    int newPoints = user.getCurrentPoints() + 5; // Assuming 5 points for joining an event
    user.setCurrentPoints(newPoints);
    System.out.println(user.getUsername() + " has joined the event: " + event.getTitle());
    System.out.println(user.getUsername() + "'s current points: " + user.getCurrentPoints());
}

    
    public void displayLiveAndUpcomingEvents(User user) {
        Scanner sc = new Scanner(System.in);
        LocalDate today = LocalDate.now();
        List<Event> liveEvents = new ArrayList<>();
        List<Event> upcomingEvents = new ArrayList<>();
        List<Event> events = loadEventsFromDatabase();
        for (Event event : events) {
            LocalDate date = LocalDate.parse(event.getDate());
            if (date.equals(today)) {
                liveEvents.add(event);
            } else if (date.isAfter(today)) {
                upcomingEvents.add(event);
            }
        }

        // Sort the upcoming events by date
        upcomingEvents.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
        // Display live events
        if (!liveEvents.isEmpty()) {
            System.out.println("Live Events:");
            for (int i = 0; i < liveEvents.size(); i++) {
                Event event = liveEvents.get(i);
                System.out.println((i + 1) + ". " + event.getTitle() + " - " + event.getDescription() + " on " + event.getDate());
            }
            //Only young students can choose
            if (user.getRole().equals("Young_Students")) {
                        System.out.println("Select the live event number to join: ");
            int selectedLiveNumber = sc.nextInt();
            if (selectedLiveNumber > 0 && selectedLiveNumber <= liveEvents.size()) {
                Event selectedLiveEvent = liveEvents.get(selectedLiveNumber - 1);
              //  joinEvent(selectedLiveEvent, user);
                saveEventChoice(user.getUsername(), user.getParent(), selectedLiveEvent.getTitle(), java.sql.Date.valueOf(selectedLiveEvent.getDate()), java.sql.Time.valueOf(selectedLiveEvent.getTime()), user.getCurrentPoints());
            } else if (selectedLiveNumber != 0) {
                System.out.println("Invalid selection for live events.");
            }
        } 
}else {
            System.out.println("No live events today.");
        }

        // Display the closest 3 upcoming events
        System.out.println("Closest 3 Upcoming Events:");
        for (int i = 0; i < Math.min(3, upcomingEvents.size()); i++) {
            Event event = upcomingEvents.get(i);
            System.out.println((i + 1) + ". " + event.getTitle() + " - " + event.getDescription() + " on " + event.getDate()+" at "+ event.getTime());
        }
        //Only young students can choose
        if (user.getRole().equals("Young_Students")) {
            System.out.println("Select the live event number to join: ");
            int selectedUpcomingNumber = sc.nextInt();
            sc.nextLine();
            if (selectedUpcomingNumber > 0 && selectedUpcomingNumber <= upcomingEvents.size()) {
                Event selectedUpcomingEvent = upcomingEvents.get(selectedUpcomingNumber - 1);
             //   joinEvent(selectedUpcomingEvent, user);
                saveEventChoice(user.getUsername(), user.getParent(), selectedUpcomingEvent.getTitle(), java.sql.Date.valueOf(selectedUpcomingEvent.getDate()), java.sql.Time.valueOf(selectedUpcomingEvent.getTime()), user.getCurrentPoints());
                
            } else if (selectedUpcomingNumber != 0) {
                System.out.println("Invalid selection for upcoming events.");
            }
        }
        
        if(user.getRole().equals("Parents")|| user.getRole().equals("Educators")){
             Ui starter = new Ui();     
        starter.mainmenu(user);
        }
        
        
        System.out.print("Do you want to join more events ? (yes: 1 , No : etc): ");
        int choice;
        choice = sc.nextInt();
        if(choice==1){
            displayLiveAndUpcomingEvents( user);
        }else{
    Ui starter = new Ui();     
        starter.mainmenu(user);
        }
    }
  
//    public void saveEventChoice(String childUsername, String parentUsername, String eventName, java.sql.Date eventDate, java.sql.Time eventTime, int points) {
//        String insertChoiceQuery = "INSERT INTO child_event (child_username, parent_username, event_name, event_date, event_time, points) VALUES (?, ?, ?, ?, ?, ?)";
//        try (Connection connect = DriverManager.getConnection(url, "root", pass); PreparedStatement insertStatement = connect.prepareStatement(insertChoiceQuery)) {
//            insertStatement.setString(1, childUsername);
//            if (parentUsername != null && !parentUsername.isEmpty()) {
//                insertStatement.setString(2, parentUsername);
//            } else {
//                insertStatement.setString(2, "N/A");
//            }
//            insertStatement.setString(3, eventName);
//            insertStatement.setDate(4, eventDate);
//            insertStatement.setTime(5, eventTime);
//            insertStatement.setInt(6, points);
//            insertStatement.executeUpdate();
//            System.out.println("Event choice saved successfully.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Failed to save event choice.");
//        }
//       
//    }
    
    public void saveEventChoice(String childUsername, String parentUsername, String eventName, java.sql.Date eventDate, java.sql.Time eventTime, int points) {
    String checkSameEventQuery = "SELECT COUNT(*) FROM child_event WHERE child_username = ? AND event_name = ? AND event_date = ?";
    String checkDateClashQuery = "SELECT COUNT(*) FROM child_event WHERE child_username = ? AND event_date = ?";
    String checkParentBookingClashQuery = "SELECT COUNT(*) FROM parent_bookings WHERE child_username = ? AND booking_date = ?";
    String insertChoiceQuery = "INSERT INTO child_event (child_username, parent_username, event_name, event_date, event_time) VALUES (?, ?, ?, ?, ?)";
    
    try (Connection connect = DriverManager.getConnection(url, "root", pass);
         PreparedStatement checkSameEventStmt = connect.prepareStatement(checkSameEventQuery);
         PreparedStatement checkDateClashStmt = connect.prepareStatement(checkDateClashQuery);
         PreparedStatement checkParentBookingClashStmt = connect.prepareStatement(checkParentBookingClashQuery);
         PreparedStatement insertStatement = connect.prepareStatement(insertChoiceQuery)) {

        // Check if the user has already booked the same event
        checkSameEventStmt.setString(1, childUsername);
        checkSameEventStmt.setString(2, eventName);
        checkSameEventStmt.setDate(3, eventDate);
        ResultSet sameEventResult = checkSameEventStmt.executeQuery();
        sameEventResult.next();
        int sameEventCount = sameEventResult.getInt(1);

        if (sameEventCount > 0) {
            System.out.println("The event has already been booked by the user.");
            return;
        }

        // Check if the user has already booked another event on the same date in child_event
        checkDateClashStmt.setString(1, childUsername);
        checkDateClashStmt.setDate(2, eventDate);
        ResultSet dateClashResult = checkDateClashStmt.executeQuery();
        dateClashResult.next();
        int dateClashCount = dateClashResult.getInt(1);

        if (dateClashCount > 0) {
            System.out.println("The user has already booked another event on the same date.");
            return;
        }

        // Check if the user has already booked another event on the same date in parent_bookings
        checkParentBookingClashStmt.setString(1, childUsername);
        checkParentBookingClashStmt.setDate(2, eventDate);
        ResultSet parentBookingClashResult = checkParentBookingClashStmt.executeQuery();
        parentBookingClashResult.next();
        int parentBookingClashCount = parentBookingClashResult.getInt(1);

        if (parentBookingClashCount > 0) {
            System.out.println("The user has already booked another event on the same date in parent bookings.");
            return;
        }

        // Insert the new event choice
        insertStatement.setString(1, childUsername);
        if (parentUsername != null && !parentUsername.isEmpty()) {
            insertStatement.setString(2, parentUsername);
        } else {
            insertStatement.setString(2, "N/A");
        }
        insertStatement.setString(3, eventName);
        insertStatement.setDate(4, eventDate);
        insertStatement.setTime(5, eventTime);
        
        insertStatement.executeUpdate();
        System.out.println("Event registered successfully.");
        addFivePoints(childUsername);
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Failed to register event choice.");
    }
}
    
      public static void addFivePoints(String username) {
        String updateQuery = "UPDATE user SET points = points + 5 WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Points added successfully for user: " + username);
            } else {
                System.out.println("User not found or points not updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}



class Event {
    private String title;
    private String description;
    private String date;
    private String time;

    public Event(String title, String description, String date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
    
    public String getTime(){
        return time;
    }
    
}
