package activity;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<Event> events;

    public EventManager() {
        this.events = new ArrayList<>();
        // Initialize with some sample events
        this.events.add(new Event("Tech Talk", "Learn about the latest in technology.", "2024-07-01"));
        this.events.add(new Event("Hackathon", "Collaborate and create apps within a day.", "2024-08-15"));
    }

    // Method to create a new event
    public void createEvent(String title, String description, String date) {
        Event newEvent = new Event(title, description, date);
        events.add(newEvent);
        System.out.println("New event created: " + title);
    }

    // Method to display all events
    public void displayEvents() {
        if (events.isEmpty()) {
            System.out.println("No events available.");
            return;
        }
        System.out.println("Available Events:");
        for (Event event : events) {
            System.out.println(event.getTitle() + " - " + event.getDescription() + " on " + event.getDate());
        }
    }

    // Method for a user to join an event
    public void joinEvent(int eventIndex, User user) {
        if (eventIndex < 0 || eventIndex >= events.size()) {
            System.out.println("Invalid event selection.");
            return;
        }
        Event event = events.get(eventIndex);
        // Add points for joining an event using User's setCurrentPoints method
        int newPoints = user.getCurrentPoints() + 5; // Assuming 5 points for joining an event
        user.setCurrentPoints(newPoints);
        System.out.println(user.getUsername() + " has joined the event: " + event.getTitle());
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
