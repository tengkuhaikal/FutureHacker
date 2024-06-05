package activity;

import Account.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class EventManager {
    private List<Event> events;
    Event event;
    static String url = "jdbc:mysql://localhost:3306/datastructure";
    public static String pass = "root";

    public EventManager() {
        this.events = new ArrayList<>();
        // Initialize with some sample events
        this.events.add(new Event("Tech Talk", "Learn about the latest in technology.", "2024-06-07","20:00:00"));
        this.events.add(new Event("Hackathon", "Collaborate and create apps within a day.", "2024-07-15", "15:30:00"));
        this.events.add(new Event("Workshop on AI", "Hands-on session on Artificial Intelligence", "2024-07-05", "10:15:00"));
        this.events.add(new Event("Cybersecurity Seminar", "Understanding the importance of cybersecurity", "2024-07-20", "19:00:00"));
        this.events.add(new Event("Web Development Bootcamp", "Learn how to build websites", "2024-07-10", "17:45:00"));
        this.events.add(new Event("Data Science Conference", "Discussing trends in data science", "2024-07-25", "12:00:00"));
        this.events.add(new Event("Mobile App Development", "Create mobile applications.", "2024-07-01", "13:35:00"));
        this.events.add(new Event("Blockchain Basics", "Introduction to blockchain technology", "2024-07-15", "15:00:00"));
        this.events.add(new Event("Cloud Computing", "Exploring cloud services and solutions", "2024-07-01", "18:15:00"));
        this.events.add(new Event("Networking Event", "Meet and connect with professionals", "2024-07-10", "11:20:00"));
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
                joinEvent(selectedLiveEvent, user);
                saveEventChoice(user.getUsername(), user.getParent(), selectedLiveEvent.getTitle(), java.sql.Date.valueOf(selectedLiveEvent.getDate()), java.sql.Time.valueOf(selectedLiveEvent.getTime()), user.getCurrentPoints());
            } else if (selectedLiveNumber != 0) {
                System.out.println("Invalid selection for live events.");
            }
        } else {
            System.out.println("No live events today.");
        }
}

        // Display the closest 3 upcoming events
        System.out.println("Closest 3 Upcoming Events:");
        for (int i = 0; i < Math.min(3, upcomingEvents.size()); i++) {
            Event event = upcomingEvents.get(i);
            System.out.println((i + 1) + ". " + event.getTitle() + " - " + event.getDescription() + " on " + event.getDate());
        }
        //Only young students can choose
        if (user.getRole().equals("Young_Students")) {
            System.out.println("Select the live event number to join: ");
            int selectedUpcomingNumber = sc.nextInt();
            sc.nextLine();
            if (selectedUpcomingNumber > 0 && selectedUpcomingNumber <= upcomingEvents.size()) {
                Event selectedUpcomingEvent = upcomingEvents.get(selectedUpcomingNumber - 1);
                joinEvent(selectedUpcomingEvent, user);
                saveEventChoice(user.getUsername(), user.getParent(), selectedUpcomingEvent.getTitle(), java.sql.Date.valueOf(selectedUpcomingEvent.getDate()), java.sql.Time.valueOf(selectedUpcomingEvent.getTime()), user.getCurrentPoints());

            } else if (selectedUpcomingNumber != 0) {
                System.out.println("Invalid selection for upcoming events.");
            }
        }
    }
  
    public void saveEventChoice(String childUsername, String parentUsername, String eventName, java.sql.Date eventDate, java.sql.Time eventTime, int points) {
        String insertChoiceQuery = "INSERT INTO child_event (child_username, parent_username, event_name, event_date, event_time, points) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connect = DriverManager.getConnection(url, "root", pass); PreparedStatement insertStatement = connect.prepareStatement(insertChoiceQuery)) {
            insertStatement.setString(1, childUsername);
            if (parentUsername != null && !parentUsername.isEmpty()) {
                insertStatement.setString(2, parentUsername);
            } else {
                insertStatement.setString(2, "N/A");
            }
            insertStatement.setString(3, eventName);
            insertStatement.setDate(4, eventDate);
            insertStatement.setTime(5, eventTime);
            insertStatement.setInt(6, points);
            insertStatement.executeUpdate();
            System.out.println("Event choice saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to save event choice.");
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
