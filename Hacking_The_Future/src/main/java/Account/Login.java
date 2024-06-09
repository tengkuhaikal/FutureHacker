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
import static Account.MySQLConfiguration.url;
import Booking.BookingService;
import Leaderboard.FriendManager;
import UI.Ui;
import java.sql.Connection; // Represents a connection to the database
import java.sql.DriverManager; // Helps in obtaining a connection to the database
import java.sql.PreparedStatement; // Used for prepared statements
import java.sql.Statement; // Used for executing SQL statements
import java.sql.ResultSet; // Represents a table of data resulting from a query
import java.sql.SQLException; // Handles SQL exceptions
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class  Login {

    Scanner scan = new Scanner(System.in);
    
   FriendManager fm = new FriendManager();
   BookingService bs = new BookingService();
    

    public void viewprofile(User user){
      
        System.out.println("Your Profile Details : \n");
          System.out.println("Email: "+user.getEmail());
          System.out.println("Username: "+user.getUsername());
          System.out.println("Role: "+user.getRole());
          System.out.println("Location: "+Arrays.toString(user.getLocationCoordinate()));
          
          switch(user.getRole()){
              case "Young_Students":{
                  int updatedpoints=countPoints(user);
                  user.setCurrentPoints(updatedpoints);
                                    System.out.println("Current points: "+user.getCurrentPoints());
                                    
                                    if(user.getParent()!=null) {
                                        System.out.println("Parents: "+user.getParent());
            } else {
                                        System.out.println("Parents: "+"null");
            }
                                    fm.FriendList(user);
                                    break;
                                }
                                case "Parents":{
                                    
                                       if(user.getChildren() != null && !user.getChildren().isEmpty()) {
                                        System.out.println("Children: "+user.getChildren());
                                        bs.displayBookingHistoryForUser(user);
            } else {
                                        System.out.println("Children: "+"null");
                                        bs.displayBookingHistoryForUser(user);
            }
                                    break;
                                }
                                case "Educators":{
                                    System.out.println("No. of Quizs created: "+countUserQuizRows(user));
                                    System.out.println("No. of Events created: "+countUserEventRows( user));
                                    break;
                                }
          }
          
          System.out.println("\nYou'll be redirected to main menu\n\n");
          Ui u = new Ui();
          u.mainmenu(user);
    }
    
    public User lgin(){
         String input;
         System.out.println("----------Log In page----------");
         java.util.ArrayList <String> data = new ArrayList<>();
        System.out.print("Email: ");
        input=scan.nextLine();
        data.add(input);

        System.out.print("Password: ");
        input=scan.nextLine();
        data.add(input);
         User user = new User();
          try (
            Connection connect = DriverManager.getConnection(url, "root", pass);
            Statement statement = connect.createStatement()) { // connecting to the database

            // checking the database whether email or username inserted by user has existed
            // or not
            boolean emailExist = false;
            boolean passwordExist = false;
            boolean correctrole = false;
            boolean usernameExist = false;
            String username = null;
            ResultSet check = statement.executeQuery("select * from user");
            while (check.next()) {
                
                if (check.getString(2).equals(data.get(0)) ) {
                    emailExist = true;
                   
                                if(BCrypt.checkpw(data.get(1), check.getString(4))){
                                passwordExist = true;
                          
                            user.setEmail(data.get(0));
                            user.setPassword(data.get(1));
                            
                            user.setUsername(check.getString(3));
                            user.setRole(check.getString(5));
                           
                                   
                            switch(user.getRole()){
                                case "Young_Students" -> {
                                    
                                    
                                    user.setCurrentPoints(check.getInt(7));
                                   
                                   user.setParent(check.getString(8));
                                }
                                case "Parents" -> {
                                    
                                    ArrayList <String> children = new ArrayList <String> ();
                                    String listchildren=check.getString(9);
                                    if(listchildren==null){
                                        user.setChildren(null);
                                        break;
                                    }
                                    String [] kids =listchildren.split(" ");
                                    for(String list : kids){
                                        
                                        children.add(list);
                                    }
                                    user.setChildren(children);
                                }
                                case "Educators" -> {
                                    }
                                
                            }
                            String inputString=check.getString(6);
                             String[] numberString = inputString.replaceAll("[\\[\\]]","").split(",");
                               Double[] doubleList = new Double [2];
                               int i=0;
                               for(String number : numberString){
                                   
                                   doubleList[i]=Double.parseDouble(number.trim());
                                   i++;
                               }
                               
                            user.setLocationCoordinate(doubleList);
                                  
                               }
  
                    
                }
            }

            if(emailExist){
                if(passwordExist){
                    System.out.println("Welcome back "+user.getUsername().toUpperCase()+"!"); 
                    return user;
                   
                    
                }else{
                    System.out.println("Incorrect password, please try again!");
                    return null;
                }
            }else{
                System.out.println("No email found, please register an account if you don't have an account!");
                
                return null;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    
    // count quantity of quiz made by particular educator
    public static int countUserQuizRows(User user) {
        int rowCount = 0;
        String query = "SELECT COUNT(*) FROM quiz WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                rowCount = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    // Method to calculate the number of rows containing User's username in the 'educator' column of the 'event' table
    public static int countUserEventRows(User user) {
        int rowCount = 0;
        String query = "SELECT COUNT(*) FROM event WHERE educator = ?";

        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                rowCount = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowCount;
    }
    
    
    
    
    // method to retrieve students current points from sql 
    public static int countPoints(User user) {
    int points = 0;
    String query = "SELECT points FROM user WHERE username = ?";

    try (Connection conn = DriverManager.getConnection(url, "root", pass);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, user.getUsername());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            points = rs.getInt("points");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return points;
}

    public static void main(String[] args) {
        Login l = new Login();

// Authenticate the user


// Pass the authenticated user to AccountSettings
AccountSettings as = new AccountSettings();

    }
}
