/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Activity;
import Account.User;
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
import java.util.Scanner;

/**
 *
 * @author Hasna
 */
public class BookingService {
    static String url = "jdbc:mysql://localhost:3306/datastructure";
    public static String pass = "root";
    private List<BookingDestination> bookingDestinations;

    public BookingService() {
        bookingDestinations = loadBookingDestinations();
    }

    private List<BookingDestination> loadBookingDestinations() {
        List<BookingDestination> destinations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("BookingDestination.txt"))) {
            String line = reader.readLine(), name, coordinate;
            while (line != null) {
                if (line.trim().isEmpty()) {
                    continue;
                } else {
                    name = line.trim();
                    coordinate = reader.readLine();
                    if (coordinate == null) {
                        break;
                    }
                }
                String[] parts = coordinate.split(",");
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                destinations.add(new BookingDestination(name, x, y));
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
    
    private void displaySortedBookingDestination(double userX, double userY) { //still need to be updated based on event clashes
        List<BookingDestination> sortedBooking = getSortedBookingDestinations(userX, userY);

        System.out.println("Booking Page \n=========================================================================");
        int number = 1;
        for (BookingDestination destination : sortedBooking) {
            System.out.println();
            double distance = calculateEuclideanDistance(userX, userY, destination.getX(), destination.getY());
            double distanceInKm = distance / 1000; // Assuming the distance is in meters, convert to kilometers

            System.out.printf("[%d] %-30s%n", number, destination.getName());
            System.out.printf("%.2f km away%n", distanceInKm);

            number++;
        }
    }

    private double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public boolean addBooking(BookingDestination book) {
        String insertBookingQuery = "INSERT INTO booking (username, destination_name, booking_date, booking_time) VALUES (?, ?, ?, ?)";

        try (Connection connect = DriverManager.getConnection(url, "root", pass);
                PreparedStatement insertStatement = connect.prepareStatement(insertBookingQuery)) {

            insertStatement.setString(1, book.getName());
            insertStatement.setString(2, book.getDestination());
            insertStatement.setDate(3, Date.valueOf(book.getDate()));
            insertStatement.setTime(4, book.getTime());

            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void createBooking(User user) {
        List<BookingDestination> sortedDestinations = null;
        Double[] userCoordinate = user.getLocationCoordinate();

        Double x = userCoordinate[0];
        Double y = userCoordinate[1];
        displaySortedBookingDestination(x, y);
        sortedDestinations = getSortedBookingDestinations(x, y);
        

        Scanner sc = new Scanner(System.in);
        System.out.println("Select the destination number to book:");
        int selectedNumber = sc.nextInt();
        sc.nextLine();

        if (selectedNumber > 0 && selectedNumber <= sortedDestinations.size()) {
            BookingDestination selectedDestination = sortedDestinations.get(selectedNumber - 1);

            BookingDestination booking = new BookingDestination(user.getUsername(), selectedDestination.getName(), selectedDestination.getDate(), selectedDestination.getTime());
            boolean success = addBooking(booking);

            if (success) {
                System.out.println("Booking successfully added.");
            } else {
                System.out.println("Failed to add booking.");
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }
}
    

