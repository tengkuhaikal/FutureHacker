/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Leaderboard;

import static Account.AccountSettings.pass;
import static Account.MySQLConfiguration.url;
import Account.User;
import UI.Ui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Afiq Zafry
 */
public class rank  {
    
    
    
    public static void viewrank(User user){
        String query = "SELECT username,points, updated_at, " +
                       "RANK() OVER (ORDER BY points DESC, updated_at) AS user_rank " +
                       "FROM user " +
                       "WHERE role = 'Young_Students' " +
                       "ORDER BY points DESC, updated_at";
List<String> usernames = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Rank\tUsername\tCurrent Points");

            while (rs.next()) {
                int rank = rs.getInt("user_rank");
                String username = rs.getString("username");
                int currentPoints = rs.getInt("points");
                Timestamp datetime = rs.getTimestamp("updated_at");
                usernames.add(username);
                System.out.println(rank + "\t" + username + "\t\t" + currentPoints + "\t\t" );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Prompt message to choose index for friend request
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the index of the user to send a friend request, if you press zero you will be redirected to main menu:");
        int index = scanner.nextInt();

        if (index >= 0 && index < usernames.size()) {
            String selectedUsername = usernames.get(index-1);
            // Now you have the selected username, you can proceed to send the friend request
            System.out.println("You selected user: " + selectedUsername);
            // Call a method to send friend request using selectedUsername
          boolean success=sendRequest(user,selectedUsername);
          if (!success){
              viewrank(user);
          }
        } 
        else if(index==0){
             Ui starter = new Ui();
             starter.mainmenu(user);
        }
        else {
            System.out.println("Invalid index. Please enter a valid index.");
        }
        
        System.out.println("Do you want to continue send friend request or to main menu ? 1 for continue and 0 for mainmenu");
        int i=scanner.nextInt();
        if(i==1){
            viewrank(user);
        }
        else if (i==0){
             Ui starter = new Ui();
             starter.mainmenu(user);
        }
            
        
    }
    
   public static boolean sendRequest(User user, String req) {
    // Check if req is already a friend of user
    String checkFriendQuery = "SELECT COUNT(*) FROM friendrequest WHERE (`from` = ? AND `to` = ? AND `status` = 'Accepted') OR (`from` = ? AND `to` = ? AND `status` = 'Accepted')";
    try (Connection conn = DriverManager.getConnection(url, "root", pass);
         PreparedStatement checkFriendStmt = conn.prepareStatement(checkFriendQuery)) {

        checkFriendStmt.setString(1, user.getUsername());
        checkFriendStmt.setString(2, req);
        checkFriendStmt.setString(3, req);
        checkFriendStmt.setString(4, user.getUsername());
        
        try (ResultSet rs = checkFriendStmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    // req is already a friend of user, return false
                    System.out.println(req + " is already your friend.");
                    return false;
                }
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
    
    // If req is not already a friend, send the friend request
    String friendRequestQuery = "INSERT INTO friendrequest (`from`, `to`, `status`) VALUES (?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(url, "root", pass);
         PreparedStatement friendRequestStmt = conn.prepareStatement(friendRequestQuery)) {

        friendRequestStmt.setString(1, user.getUsername());
        friendRequestStmt.setString(2, req); // Store recipient's username in 'to' column
        friendRequestStmt.setString(3, "Pending");
        friendRequestStmt.executeUpdate();

        System.out.println("Friend request sent from " + user.getUsername() + " to " + req);
        return true; // Request sent successfully
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}
    
    }
    
    
