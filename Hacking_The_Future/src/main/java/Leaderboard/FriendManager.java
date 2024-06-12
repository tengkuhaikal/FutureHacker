/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Leaderboard;

import static Account.MySQLConfiguration.pass;
import static Account.MySQLConfiguration.url;
import Account.User;
import UI.Ui;
import UI.formatText;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Afiq Zafry
 */
public class FriendManager {

    public static void FriendList(User user) {
        List<String> friendUsernames = new ArrayList<>();

        String queryFrom = "SELECT `from` FROM friendrequest WHERE `to` = ? AND `status` = 'Accepted'";
        String queryTo = "SELECT `to` FROM friendrequest WHERE `from` = ? AND `status` = 'Accepted'";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmtFrom = conn.prepareStatement(queryFrom); PreparedStatement pstmtTo = conn.prepareStatement(queryTo)) {

            // Query where the given user's username is in the 'to' column and status is 'Accepted'
            pstmtFrom.setString(1, user.getUsername());
            try (ResultSet rs = pstmtFrom.executeQuery()) {
                while (rs.next()) {
                    friendUsernames.add(rs.getString("from"));
                }
            }

            // Query where the given user's username is in the 'from' column and status is 'Accepted'
            pstmtTo.setString(1, user.getUsername());
            try (ResultSet rs = pstmtTo.executeQuery()) {
                while (rs.next()) {
                    friendUsernames.add(rs.getString("to"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the friend usernames
        formatText.formatTitle("Friends List:");
        for (String friend : friendUsernames) {
            System.out.println(friend);
        }
        if (friendUsernames.isEmpty()) {
            formatText.message("null");
        }
    }

    public static void listRejectedByOthers(User user) {
        List<String> rejectedUsernames = new ArrayList<>();

        String query = "SELECT `to` FROM friendrequest WHERE `from` = ? AND `status` = 'Rejected'";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rejectedUsernames.add(rs.getString("to"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the usernames rejected by the user
        formatText.message("Usernames Rejected by " + "others " + ":");
        for (String username : rejectedUsernames) {
            System.out.println(username);
        }
    }

    public static void listRejectedByUser(User user) {
        List<String> rejectedUsernames = new ArrayList<>();

        String query = "SELECT `from` FROM friendrequest WHERE `to` = ? AND `status` = 'Rejected'";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rejectedUsernames.add(rs.getString("from"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the usernames that have rejected the user's friend request
        formatText.message("Usernames that rejected by " + user.getUsername() + "'s friend request:");
        for (String username : rejectedUsernames) {
            System.out.println(username);
        }
    }

    public static void acceptFriendRequest(User user) {
        List<String> pendingUsernames = new ArrayList<>();
        List<Integer> requestIds = new ArrayList<>();

        String query = "SELECT idfriendrequest, `from` FROM friendrequest WHERE `to` = ? AND `status` = 'Pending'";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                int index = 1;
                while (rs.next()) {
                    pendingUsernames.add(rs.getString("from"));
                    requestIds.add(rs.getInt("idfriendrequest"));
                    System.out.println(index++ + ": " + rs.getString("from"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Prompt user to choose an index
        Scanner scanner = new Scanner(System.in);
        formatText.formatTitle("Friend request");
        System.out.println("\nAccept >> ");
        int index = scanner.nextInt();

        if (index >= 1 && index <= pendingUsernames.size()) {
            int requestId = requestIds.get(index - 1);

            String updateQuery = "UPDATE friendrequest SET `status` = 'Accepted' WHERE idfriendrequest = ?";
            try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

                pstmt.setInt(1, requestId);
                pstmt.executeUpdate();
                formatText.message("Friend request from " + pendingUsernames.get(index - 1) + " has been accepted.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            formatText.error("Invalid index. Please enter a valid index.");
        }
    }

    public static void rejectFriendRequest(User user) {
        List<String> pendingUsernames = new ArrayList<>();
        List<Integer> requestIds = new ArrayList<>();

        String query = "SELECT idfriendrequest, `from` FROM friendrequest WHERE `to` = ? AND `status` = 'Pending'";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                int index = 1;
                while (rs.next()) {
                    pendingUsernames.add(rs.getString("from"));
                    requestIds.add(rs.getInt("idfriendrequest"));
                    System.out.println(index++ + ": " + rs.getString("from"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Prompt user to choose an index
        Scanner scanner = new Scanner(System.in);
        formatText.formatTitle("Friend request");
        System.out.println("\nReject >> ");
        int index = scanner.nextInt();

        if (index >= 1 && index <= pendingUsernames.size()) {
            int requestId = requestIds.get(index - 1);

            String updateQuery = "UPDATE friendrequest SET `status` = 'Rejected' WHERE idfriendrequest = ?";
            try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

                pstmt.setInt(1, requestId);
                pstmt.executeUpdate();
                formatText.message("Friend request from " + pendingUsernames.get(index - 1) + " has been rejected.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            formatText.error("Invalid index. Please enter a valid index.");
        }
    }

    public static void PendingList(User user) {
        List<String> rejectedUsernames = new ArrayList<>();

        String query = "SELECT `to` FROM friendrequest WHERE `from` = ? AND `status` = 'Pending'";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rejectedUsernames.add(rs.getString("to"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Print the usernames rejected by the user
        formatText.formatTitle("Friend Request Pending List");
        for (String username : rejectedUsernames) {
            System.out.println(username);
        }
    }

    public static void processPendingRequests(User user) {
        List<String> pendingUsernames = new ArrayList<>();
        List<Integer> requestIds = new ArrayList<>();

        String query = "SELECT idfriendrequest, `from` FROM friendrequest WHERE `to` = ? AND `status` = 'Pending'";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            try (ResultSet rs = pstmt.executeQuery()) {
                int index = 1;
                while (rs.next()) {
                    pendingUsernames.add(rs.getString("from"));
                    requestIds.add(rs.getInt("idfriendrequest"));
                    System.out.println(index++ + ": " + rs.getString("from"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (pendingUsernames.isEmpty()) {
            formatText.message("No friend request sent by others\n");
            managerequest(user);
        }
        // Prompt user to choose an index
        Scanner scanner = new Scanner(System.in);
        formatText.formatTitle("Manage friend request");
        System.out.println("\nIndex >> ");
        int index = scanner.nextInt();

        if (index >= 1 && index <= pendingUsernames.size()) {
            int requestId = requestIds.get(index - 1);

            System.out.println("1. Accept");
            System.out.println("2. Reject");
            System.out.println("\nOption >> ");

            int choice = scanner.nextInt();
            if (choice == 1) {
                acceptRequest(requestId);
            } else if (choice == 2) {
                rejectRequest(requestId);
            } else {
                formatText.error("Invalid choice.");
            }
        } else {
            formatText.error("Invalid index. Please enter a valid index.");
        }
    }

    private static void acceptRequest(int requestId) {
        String updateQuery = "UPDATE friendrequest SET `status` = 'Accepted' WHERE idfriendrequest = ?";
        executeUpdateQuery(requestId, updateQuery, "accepted");
    }

    private static void rejectRequest(int requestId) {
        String updateQuery = "UPDATE friendrequest SET `status` = 'Rejected' WHERE idfriendrequest = ?";
        executeUpdateQuery(requestId, updateQuery, "rejected");
    }

    private static void executeUpdateQuery(int requestId, String updateQuery, String action) {
        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setInt(1, requestId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                formatText.message("Friend request " + action + " successfully.");
            } else {
                formatText.error("Failed to " + action + " friend request.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void managerequest(User user) {
        formatText.formatTitle("Friends");
        System.out.println("1.View your Friends");
        System.out.println("2.Manage pending friend request");
        System.out.println("3.View your pending list request");
        System.out.println("4.View your rejected request");
        System.out.println("5.Main Menu");
        Scanner scan = new Scanner(System.in);
        boolean loop = true;
        System.out.println("\nOption >> ");
        int choice = scan.nextInt();
        int decision;
        switch (choice) {
            case 1: {

                FriendList(user);

                break;
            }
            case 2: {
                do {
                    processPendingRequests(user);

                    formatText.formatTitle("Continue? [1:Yes || 0:Friends Menu]");
                    decision = scan.nextInt();
                    if (decision == 1) {
                        loop = true;
                    } else {
                        loop = false;
                    }
                } while (loop);
                break;
            }
            case 3: {

                PendingList(user);

                break;
            }
            case 4: {

                listRejectedByOthers(user);

                break;
            }
            case 5: {
                Ui starter = new Ui();
                starter.mainmenu(user);

            }
            default:
                managerequest(user);

        }
        managerequest(user);
    }

}
