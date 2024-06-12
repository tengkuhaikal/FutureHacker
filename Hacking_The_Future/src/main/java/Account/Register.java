/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Account;

/**
 *
 * @author Afiq Zafry
 */
import static Account.MySQLConfiguration.pass;
import static Geoloaction.GeoLocation.getrealLocation;
import UI.Ui;
import UI.formatText;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
//import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import org.mindrot.jbcrypt.BCrypt;

public class Register extends User {

    public static String url = "jdbc:mysql://localhost:3306/datastructure";
    static Scanner scan = new Scanner(System.in);
    static Random rand = new Random();
    public static String user = "root";

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static User registernewuser() {
        formatText.formatTitle("Register");
        java.util.ArrayList<String> data = new ArrayList<>();
        System.out.print("Email: ");
        String input = scan.nextLine();
        data.add(input);

        System.out.print("Username: ");
        input = scan.nextLine();
        data.add(input);

        System.out.print("Password: ");
        input = scan.nextLine();
        String hashedpassword = hashPassword(input);
        data.add(hashedpassword);

        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("Role:");
            System.out.println("1.Young_Students");
            System.out.println("2.Parents");
            System.out.println("3.Educator");
            System.out.print("\nOption >> ");

            int choice = scan.nextInt();

            switch (choice) {
                case 1:
                    input = "Young_Students";
                    validChoice = true;
                    break;
                case 2:
                    input = "Parents";
                    validChoice = true;
                    break;
                case 3:
                    input = "Educators";
                    validChoice = true;
                    break;
                default:
                    formatText.error("Invalid choice. Please try again.");
                    validChoice = false;
                    // Set validChoice to false to repeat the loop
                    break;
            }

        }
        data.add(input);
        // retrieve user location using realtime coordinate
        System.out.print("Your Exact Current Coordinate: ");
        Double[] reallocation = getrealLocation();
        double lat = reallocation[0];
        
        double log = reallocation[1];
       
        System.out.println("[" + lat + "," + log + "]");
        data.add(lat + "," + log);

        try (Connection connect = DriverManager.getConnection(url, user, pass)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = "SELECT * FROM user WHERE email = ?";
            PreparedStatement checkStatement = connect.prepareStatement(query);
            checkStatement.setString(1, data.get(0));
            ResultSet result = checkStatement.executeQuery();
            //check if email is already exist
            while (result.next()) {
                if (result.getString(2).equals(data.get(0))) {
                    formatText.error("Email already exist");
                    return null;
                }

            }

           // check if username is already exist
            String queryUsername = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement checkStatement2 = connect.prepareStatement(queryUsername)) {
                checkStatement2.setString(1, data.get(1));
                try (ResultSet result2 = checkStatement2.executeQuery()) {
                    if (result2.next()) { // Ensure there's a row in the result set
                        formatText.error("Username already exists");
                        return null;
                    }
                }
            }

            String insertQuery = "INSERT INTO user (email,username,password,role,location) VALUES ( ?,?,?,?,?)";
            PreparedStatement preparedStatement = connect.prepareStatement(insertQuery);

            // Set the values for the parameters in the prepared statement
            for (int i = 0; i < data.size(); i++) {
                preparedStatement.setString(i + 1, data.get(i));
            }

            if (data.get(3).equals("Young_Students")) {
                String insertQuerypoint = "INSERT INTO user (points) VALUES (?)";
                PreparedStatement preparedStatementpoint = connect.prepareStatement(insertQuerypoint);
                preparedStatementpoint.setInt(1, 0);
            }

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                formatText.message("You have successfully registered");

                Login l = new Login();
                return l.lgin();

            } else {
                formatText.error("Failed to register.");
                return null;
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        registernewuser();
    }

}
