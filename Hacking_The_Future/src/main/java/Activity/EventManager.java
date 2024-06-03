package activity;

import Account.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        this.events.add(new Event("Tech Talk", "Learn about the latest in technology.", "2024-06-03"));
        this.events.add(new Event("Hackathon", "Collaborate and create apps within a day.", "2024-07-15"));
        this.events.add(new Event("Workshop on AI", "Hands-on session on Artificial Intelligence", "2024-07-05"));
        this.events.add(new Event("Cybersecurity Seminar", "Understanding the importance of cybersecurity", "2024-07-20"));
        this.events.add(new Event("Web Development Bootcamp", "Learn how to build websites", "2024-07-10"));
        this.events.add(new Event("Data Science Conference", "Discussing trends in data science", "2024-07-25"));
        this.events.add(new Event("Mobile App Development", "Create mobile applications.", "2024-07-01"));
        this.events.add(new Event("Blockchain Basics", "Introduction to blockchain technology", "2024-07-15"));
        this.events.add(new Event("Cloud Computing", "Exploring cloud services and solutions", "2024-07-01"));
        this.events.add(new Event("Networking Event", "Meet and connect with professionals", "2024-07-10"));
    }

    // Method to create a new event
    public void addEvent(String title, String description, String date) {
        Event newEvent = new Event(title, description, date);
        events.add(newEvent);
        System.out.println("New event created: " + title);
    }

    public void displayEvents() {
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        }
        System.out.println("Available Events:");
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            System.out.println((i + 1) + ". " + event.getTitle() + " - " + event.getDescription() + " on " + event.getDate());
        }
    }

    // Method for a user to join an event
    public void joinEvent(int eventIndex, User user) { //still need correction
        if (eventIndex < 0 || eventIndex >= events.size()) {
            System.out.println("Invalid event selection.");
            return;
        }
        Event event = events.get(eventIndex);
        // Add points for joining an event using User's setCurrentPoints method
        int newPoints = user.getCurrentPoints() + 5; // Assuming 5 points for joining an event

        user.setCurrentPoints(newPoints);
        System.out.println(user.getUsername() + " has joined the event: " + event.getTitle());
        System.out.println(user.getUsername() + "'s current points: " + user.getCurrentPoints());
    }
    
    public void selectAndJoinEvent(User user) { //need correction
        displayLiveAndUpcomingEvents();
        Scanner sc = new Scanner(System.in);

        List<Event> liveEvents = new ArrayList<>();
        List<Event> upcomingEvents = new ArrayList<>();

        // Separate live events and upcoming events
        LocalDate today = LocalDate.now();
        for (Event event : events) {
            LocalDate eventDate = LocalDate.parse(event.getDate());
            if (eventDate.equals(today)) {
                liveEvents.add(event);
            } else if (eventDate.isAfter(today)) {
                upcomingEvents.add(event);
            }
        }

        // Handle user input for live events
        if (!liveEvents.isEmpty()) {
            System.out.println("Select the live event number to join (or enter 0 to skip):");
            int selectedLiveNumber = sc.nextInt();
            sc.nextLine();
            if (selectedLiveNumber > 0 && selectedLiveNumber <= liveEvents.size()) {
                Event selectedLiveEvent = liveEvents.get(selectedLiveNumber - 1);
                joinEvent(events.indexOf(selectedLiveEvent), user);
            } else if (selectedLiveNumber != 0) {
                System.out.println("Invalid selection for live events.");
            }
        }

        // Handle user input for upcoming events
        if (!upcomingEvents.isEmpty()) {
            System.out.println("Select the upcoming event number to join (or enter 0 to skip):");
            int selectedUpcomingNumber = sc.nextInt();
            sc.nextLine();
            if (selectedUpcomingNumber > 0 && selectedUpcomingNumber <= upcomingEvents.size()) {
                Event selectedUpcomingEvent = upcomingEvents.get(selectedUpcomingNumber - 1);
                joinEvent(events.indexOf(selectedUpcomingEvent), user);
            } else if (selectedUpcomingNumber != 0) {
                System.out.println("Invalid selection for upcoming events.");
            }
        }

        if (liveEvents.isEmpty() && upcomingEvents.isEmpty()) {
            System.out.println("No events available to join.");
        }
    }
    
       public void displayLiveAndUpcomingEvents() {
        LocalDate today = LocalDate.now();
        List<Event> liveEvents = new ArrayList<>();
        List<Event> upcomingEvents = new ArrayList<>();

        for (Event event : events) {
            LocalDate date = LocalDate.parse(event.getDate());
            if (event.getDate().equals(today)) {
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
        } else {
            System.out.println("No live events today.");
        }

        // Display the closest 3 upcoming events
        System.out.println("Closest 3 Upcoming Events:");
        for (int i = 0; i < Math.min(3, upcomingEvents.size()); i++) {
            Event event = upcomingEvents.get(i);
            System.out.println((i + 1) + ". " + event.getTitle() + " - " + event.getDescription() + " on " + event.getDate());
        }
    }
  
    public void saveEventChoice(User user, Event event, String parentUsername, java.sql.Date bookingDate, java.sql.Time bookingTime) {
        String insertChoiceQuery = "INSERT INTO event_choices (user_id, event_id, parent_username, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection connect = DriverManager.getConnection(url, "root", pass);
             PreparedStatement insertStatement = connect.prepareStatement(insertChoiceQuery)) {

            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, event.getTitle());
            insertStatement.setString(3, user.getParent());
            insertStatement.setDate(4, bookingDate);
            insertStatement.setTime(5, bookingTime);
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

    public Event(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
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
}
