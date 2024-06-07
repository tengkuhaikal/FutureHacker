/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Activity;
import Account.FirstPage;
import Account.User;
import UI.Ui;
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
import java.text.SimpleDateFormat;
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
   // public static final String filepath = "C:\\_Hasna\\UNIV\\SEM 2\\WIA1002\\Final Project\\Netbeans 19\\DataStructure\\Hacking_The_Future\\src\\main\\java\\Activity\\BookingDestination.txt"; 
    public static final String filepath = "C:\\Users\\Afiq Zafry\\OneDrive - Universiti Malaya\\Documents\\NetBeansProjects\\Hacking_The_Future\\WIA1002\\FutureHacker\\Hacking_The_Future\\src\\main\\java\\Activity\\BookingDestination.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    
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
                System.out.println("Invalid coordinate format: " + coordinate);
                continue;
            }
            
            try {
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                destinations.add(new BookingDestination(destination, x, y));
            } catch (NumberFormatException e) {
                System.out.println("Invalid coordinate values: " + coordinate);
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
    
    System.out.println("Booking Page \n=========================================================================");
    int number = 1;
    for (BookingDestination destination : sortedBooking) {
        if (destination == null) {
            System.out.println("Skipping null destination.");
            continue;
        }
        
        System.out.println();
        double distance = calculateEuclideanDistance(userX, userY, destination.getX(), destination.getY());
        double distanceInKm = distance / 1000; // Assuming the distance is in meters, convert to kilometers
        
        String name = destination.getDestination();
        if (name == null || name.isEmpty()) {
            name = "Unknown Destination";
        }
        
        System.out.printf("[%d] %-30s%n", number, name);
        System.out.printf("%.2f km away%n", distanceInKm);
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

    
//    public void createBooking(User user) {
//        ArrayList<String> childrenNames = user.getChildren();
//        if (childrenNames == null || childrenNames.isEmpty()) {
//            System.out.println("You have no children registered. You cannot make a booking.");
//            return;
//        }
//        List<BookingDestination> sortedDestinations = null;
//        Double[] userCoordinate = user.getLocationCoordinate();
//
//        Double x = userCoordinate[0];
//        Double y = userCoordinate[1];
//        displaySortedBookingDestination(x, y);
//        sortedDestinations = getSortedBookingDestinations(x, y);
//        
//
//        Scanner sc = new Scanner(System.in);
//        System.out.println("\nSelect the destination number to book:");
//        int selectedNumber = sc.nextInt();
//        sc.nextLine();
//
//        if (selectedNumber > 0 && selectedNumber <= sortedDestinations.size()) {
//            BookingDestination selectedDestination = sortedDestinations.get(selectedNumber - 1);
//
//            BookingDestination booking = new BookingDestination(user.getUsername(), selectedDestination.getDestination(), selectedDestination.getDate(), selectedDestination.getTime());
//            boolean success = addBooking(booking,user);
//
//            if (success) {
//                System.out.println("Booking successfully added.");
//            } else {
//                System.out.println("Failed to add booking.");
//            }
//        } else {
//            System.out.println("Invalid selection.");
//        }
//    }
    public void createBooking(User user) {
    ArrayList<String> childrenNames = user.getChildren();
    if (childrenNames == null || childrenNames.isEmpty()) {
        System.out.println("You have no children registered. You cannot make a booking.");
        return;
    }

    List<BookingDestination> sortedDestinations = null;
    Double[] userCoordinate = user.getLocationCoordinate();
    Double x = userCoordinate[0];
    Double y = userCoordinate[1];
    displaySortedBookingDestination(x, y);
    sortedDestinations = getSortedBookingDestinations(x, y);

    Scanner sc = new Scanner(System.in);
    System.out.println("\nSelect the destination number to book:");
    int selectedNumber = sc.nextInt();
    sc.nextLine();

    if (selectedNumber > 0 && selectedNumber <= sortedDestinations.size()) {
        BookingDestination selectedDestination = sortedDestinations.get(selectedNumber - 1);

        // Display available dates for booking
        displayAvailableDatesForBooking(user.getUsername(), selectedDestination.getDestination());

        System.out.print("Enter the selected time slot: ");
        int selectedTimeSlot = sc.nextInt();
        sc.nextLine();

        if (selectedTimeSlot > 0 && selectedTimeSlot <= 7) {
            LocalDate bookingDateChoice = LocalDate.now().plusDays(selectedTimeSlot - 1);

            // Create a new booking object with the selected date choice
            BookingDestination booking = new BookingDestination(user.getUsername(), selectedDestination.getDestination(),
                    selectedDestination.getDate(), selectedDestination.getTime());
            booking.setBookingDateChoice(bookingDateChoice);

            // Add the booking to the database
            boolean success = addBooking(booking, user);
            if (success) {
                System.out.println("Booking successfully added.");
            } else {
                System.out.println("Failed to add booking.");
            }
        } else {
            System.out.println("Invalid time slot selection.");
        }
    } else {
        System.out.println("Invalid selection.");
    }
        System.out.print("Do you want to add more bookings ? (yes: 1 , No : etc): ");
        int choice;
        choice = sc.nextInt();
        if(choice==1){
            createBooking( user);
        }else{
    Ui starter = new Ui();     
        starter.mainmenu(user);
        }
}
    
   public void displayAvailableDatesForBooking(String parentUsername, String destinationName) {
    LocalDate currentDate = LocalDate.now();
    LocalDate endDate = currentDate.plusWeeks(1);

    String query = "SELECT DISTINCT DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY)) AS booking_date_choice " +
                   "FROM (SELECT 1 AS i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7) t " +
                   "WHERE NOT EXISTS (" +
                   "    SELECT 1 " +
                   "    FROM child_event ce " +
                   "    JOIN user u ON ce.child_username = u.username " +
                   "    WHERE u.parents = ? AND ce.event_date = DATE(DATE_ADD(?, INTERVAL (t.i - 1) DAY))" +
                   ")";

    try (Connection connect = DriverManager.getConnection(url, "root", pass);
         PreparedStatement statement = connect.prepareStatement(query)) {
        statement.setDate(1, java.sql.Date.valueOf(currentDate));
        statement.setString(2, parentUsername);
        statement.setDate(3, java.sql.Date.valueOf(currentDate));

        ResultSet resultSet = statement.executeQuery();
        List<LocalDate> availableDates = new ArrayList<>();
        while (resultSet.next()) {
            LocalDate dateChoice = resultSet.getDate("booking_date_choice").toLocalDate();
            availableDates.add(dateChoice);
        }

        if (!availableDates.isEmpty()) {
            System.out.println("=========================================================================");
            System.out.println("Selected booking for: " + destinationName);
            System.out.println("Available Time Slots:");
            for (int i = 0; i < availableDates.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + availableDates.get(i));
            }
        } else {
            System.out.println("No available dates for booking within the next week.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
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
    

