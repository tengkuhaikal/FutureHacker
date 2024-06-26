/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Booking;

import Account.FirstPage;
import Account.User;
import UI.Ui;
import UI.formatText;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Hasna
 */
public class BookingService {

    static String url = "jdbc:mysql://localhost:3306/datastructure";
    public static String pass = "root";
    private List<BookingDestination> bookingDestinations;
    // public static final String filepath = "C:\\_Hasna\\UNIV\\SEM 2\\WIA1002\\Final Project\\MOST FINAL\\WIA1002\\Hacking_The_Future\\src\\main\\java\\Booking\\BookingDestination.txt";

    //public static final String filepath = "C:\\Users\\Afiq Zafry\\OneDrive - Universiti Malaya\\Documents\\NetBeansProjects\\Hacking_The_Future\\WIA1002\\FutureHacker\\Hacking_The_Future\\src\\main\\java\\FutureHacker\\Hacking_The_Future\\src\\main\\java\\Booking\\BookingDestination.txt";
    public static final String filepath = "C:\\Users\\USER\\Desktop\\Hacking The Future Real 1\\FutureHacker\\Hacking_The_Future\\src\\main\\java\\Booking\\BookingDestination.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    BookingDestination book;

    public BookingService() {
        bookingDestinations = loadBookingDestinations();
    }

    private List<BookingDestination> loadBookingDestinations() {
        List<BookingDestination> destinations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String destination = line.trim();
//            destinations.setName(destination);
                String coordinate = reader.readLine();

                if (coordinate == null || coordinate.trim().isEmpty()) {
                    break;
                }

                String[] parts = coordinate.split(",");
                if (parts.length != 2) {
                    formatText.error("Invalid coordinate format: " + coordinate);
                    continue;
                }

                try {
                    double x = Double.parseDouble(parts[0].trim());
                    double y = Double.parseDouble(parts[1].trim());
                    destinations.add(new BookingDestination(destination, x, y));
                } catch (NumberFormatException e) {
                    formatText.error("Invalid coordinate values: " + coordinate);
                }

                // Read the empty line after the coordinate
                reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinations;
    }

    private List<BookingDestination> getSortedBookingDestinations(double userX, double userY) {
        return bookingDestinations.stream()
                .sorted(Comparator.comparingDouble(dest -> calculateEuclideanDistance(userX, userY, dest.getX(), dest.getY())))
                .toList();
    }

    private void displaySortedBookingDestination(double userX, double userY) {
        List<BookingDestination> sortedBooking = getSortedBookingDestinations(userX, userY);

        if (sortedBooking == null || sortedBooking.isEmpty()) {
            System.out.println("No booking destinations available.");
            return;
        }

        formatText.formatTitle("Bookings");
        int number = 1;
        for (BookingDestination destination : sortedBooking) {
            if (destination == null) {
                formatText.error("Skipping null destination.");
                continue;
            }

            System.out.println();
            double distance = calculateEuclideanDistance(userX, userY, destination.getX(), destination.getY());
            double distanceInKm = distance * 111; // Convert degrees to kilometers

            String name = destination.getDestination();
            if (name == null || name.isEmpty()) {
                name = "Unknown Destination";
            }

            System.out.printf("[%d] %-30s%n", number, name);
            if (distanceInKm < 1) {
                double distanceInMeters = distanceInKm * 1000;
                System.out.printf("%.2f meters away%n", distanceInMeters);
            } else {
                System.out.printf("%.2f km away%n", distanceInKm);
            }
            number++;
        }
    }

    private double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public boolean addBooking(BookingDestination book, User user) {
        String insertBookingQuery = "INSERT INTO parent_bookings (parent_username, child_username, booking_name, booking_date, booking_time, booking_date_choice) VALUES (?, ?, ?, ?, ?,?)";

        try (Connection connect = DriverManager.getConnection(url, "root", pass); PreparedStatement insertStatement = connect.prepareStatement(insertBookingQuery)) {

            ArrayList<String> childrenNames = user.getChildren();

            for (String childName : childrenNames) {
                insertStatement.setString(1, user.getUsername());
                insertStatement.setString(2, childName);
                insertStatement.setString(3, book.getDestination());
                LocalDate date = book.getDate();
                insertStatement.setDate(4, Date.valueOf(date));
                insertStatement.setTime(5, book.getTime());
                insertStatement.setDate(6, Date.valueOf(book.getBookingDateChoice()));
                insertStatement.addBatch();
            }

            int[] rowsAffected = insertStatement.executeBatch();
            return Arrays.stream(rowsAffected).sum() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createBooking(User user) {
        ArrayList<String> childrenNames = user.getChildren();
        if (childrenNames == null || childrenNames.isEmpty()) {
            formatText.message("You have no children registered. You cannot make a booking.");
            return;
        }

        List<BookingDestination> sortedDestinations = null;
        Double[] userCoordinate = user.getLocationCoordinate();
        Double x = userCoordinate[0];
        Double y = userCoordinate[1];
        displaySortedBookingDestination(x, y);
        sortedDestinations = getSortedBookingDestinations(x, y);

        Scanner sc = new Scanner(System.in);
        System.out.print("\nDestination number >> ");
        int selectedNumber = sc.nextInt();
        sc.nextLine();

        if (selectedNumber > 0 && selectedNumber <= sortedDestinations.size()) {
            BookingDestination selectedDestination = sortedDestinations.get(selectedNumber - 1);
            // Display available dates for booking
            displayAvailableDatesForBooking(user, selectedDestination.getDestination());

            formatText.formatTitle("Continue booking? [1:Yes || 0:No] ");
            System.out.print("\nOption >> ");
            int choice = sc.nextInt();
            if (choice == 1) {
                createBooking(user);
            } else {
                Ui starter = new Ui();
                starter.mainmenu(user);
            }
        }
    }

    public void displayAvailableDatesForBooking(User user, String destinationName) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalDate endDate = currentDate.plusWeeks(1);

        String query = "SELECT DISTINCT DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY)) AS booking_date_choice "
                + "FROM (SELECT 1 AS i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7) t "
                + "WHERE NOT EXISTS ("
                + "    SELECT 1 "
                + "    FROM parent_bookings pb "
                + "    WHERE pb.parent_username = ? "
                + // Only consider bookings by the current parent
                "    AND pb.booking_date_choice = DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY)) "
                + ") AND NOT EXISTS ("
                + "    SELECT 1 "
                + "    FROM child_event ce "
                + "    WHERE ce.parent_username = ? "
                + // Only consider events for the current parent's children
                "    AND ce.event_date = DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY)) "
                + ")";

//    String query = "SELECT DISTINCT DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY)) AS booking_date_choice " +
//                   "FROM (SELECT 1 AS i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7) t " +
//                   "WHERE NOT EXISTS (" +
//                   "    SELECT 1 " +
//                   "    FROM parent_bookings pb " +
//                   "    WHERE pb.booking_date_choice = DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY)) " +
//                   "    OR EXISTS (" +
//                   "        SELECT 1 " +
//                   "        FROM child_event ce " +
//                   "        WHERE ce.event_date = DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY)) " +
//                   "    )" +
//                   ")";
        try (Connection connect = DriverManager.getConnection(url, "root", pass); PreparedStatement statement = connect.prepareStatement(query)) {
//        statement.setDate(1, java.sql.Date.valueOf(currentDate));
//        statement.setDate(2, java.sql.Date.valueOf(currentDate));
//        statement.setDate(3, java.sql.Date.valueOf(currentDate));

            statement.setDate(1, java.sql.Date.valueOf(currentDate));
            statement.setString(2, user.getUsername());  // Set the user's username
            statement.setDate(3, java.sql.Date.valueOf(currentDate));
            statement.setString(4, user.getUsername());  // Set the user's username again
            statement.setDate(5, java.sql.Date.valueOf(currentDate));

            ResultSet resultSet = statement.executeQuery();
            List<LocalDate> availableDates = new ArrayList<>();
            while (resultSet.next()) {
                LocalDate dateChoice = resultSet.getDate("booking_date_choice").toLocalDate();
                availableDates.add(dateChoice);
            }

            if (!availableDates.isEmpty()) {
                //System.out.println("=========================================================================");
                formatText.formatTitle("Selected booking for: " + destinationName);
                formatText.formatTitle("Available Time Slots");
                for (int i = 0; i < availableDates.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + availableDates.get(i));
                }
                Scanner sc = new Scanner(System.in);
                System.out.print("\nIndex to book >> ");
                int selectedDateIndex = sc.nextInt();
                sc.nextLine();

                // Validate user input
                if (selectedDateIndex > 0 && selectedDateIndex <= availableDates.size()) {
                    LocalDate bookingDateChoice = availableDates.get(selectedDateIndex - 1);

                    // Create a new booking object
                    BookingDestination booking = new BookingDestination(user.getUsername(), destinationName, currentDate, Time.valueOf(currentTime));
                    booking.setBookingDateChoice(bookingDateChoice);

                    // Add the booking to the database (assuming addBooking method exists)
                    boolean success = addBooking(booking, user);
                    if (success) {
                        formatText.message("Booking successfully added.");
                    } else {
                        formatText.error("Failed to add booking.");
                    }
                } else {
                    formatText.error("Invalid date selection.");
                }
            } else {
                formatText.error("No available dates for booking within the next week.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayBookingHistoryForUser(User user) {
        // SQL query to select all bookings made by the specified user
        String query = "SELECT * FROM parent_bookings WHERE parent_username = ?";

        try (Connection connect = DriverManager.getConnection(url, "root", pass); PreparedStatement statement = connect.prepareStatement(query)) {
            statement.setString(1, user.getUsername());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No booking history found for user: " + user.getUsername());
                return;
            }

            System.out.println("Booking history for user: " + user.getUsername());
            System.out.println("=============================================================================================================================================");
            System.out.printf("%-5s | %-20s | %-45s | %-20s | %-15s | %-10s\n",
                    "ID", "Child Username", "Booking Name", "Booking Date Choice", "Booking Time", "Created At");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String childUsername = resultSet.getString("child_username");
                String bookingName = resultSet.getString("booking_name");
                Date bookingDate = resultSet.getDate("booking_date_choice");
                Time bookingTime = resultSet.getTime("booking_time");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                System.out.printf("%-5d | %-20s | %-45s | %-20s | %-15s | %-10s\n",
                        id, childUsername, bookingName, bookingDate, bookingTime, createdAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void main(String[] args) {
        Ui starter = new Ui();
        FirstPage fp = new FirstPage();
        User user = fp.welcome();
        System.out.println("User object obtained from welcome(): " + Arrays.toString(user.getLocationCoordinate()));
        Double[] userCoordinate = user.getLocationCoordinate();

        Double x = userCoordinate[0];
        Double y = userCoordinate[1];
        System.out.println(x);
        BookingService bs = new BookingService();
        bs.createBooking(user);

    }
}
