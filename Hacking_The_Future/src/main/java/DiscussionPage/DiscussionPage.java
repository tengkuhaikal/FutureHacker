/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DiscussionPage;
import Account.FirstPage;
import Account.User;
import UI.Ui;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 *
 * @author Hasna
 */

public class DiscussionPage {
    static String url = "jdbc:mysql://localhost:3306/datastructure";
    public static String pass = "root";
    Scanner scanner = new Scanner(System.in);

    private void displayPost(ResultSet rs, int indent) throws SQLException {
        int id = rs.getInt("id");
        String role = rs.getString("role");
        String username = rs.getString("username");
        String message = rs.getString("message");
        int likes = rs.getInt("likes");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = createdAt.format(formatter);

        System.out.println(" ".repeat(indent) + "[" + formattedDateTime + "] ID:" + id + " " + role + "|" + username + " (" + likes + " likes): " + message);
    }

    private void displayDiscussion() {
        String query = "WITH RECURSIVE post_tree AS ("
                + "    SELECT *, 0 AS level, CAST(LPAD(id, 10, '0') AS CHAR(200)) AS path FROM discussion_posts WHERE post_id IS NULL "
                + "    UNION ALL "
                + "    SELECT dp.*, pt.level + 1, CONCAT(pt.path, ',', LPAD(dp.id, 10, '0')) FROM discussion_posts dp "
                + "    JOIN post_tree pt ON dp.post_id = pt.id "
                + ") SELECT * FROM post_tree ORDER BY path";

        try (Connection conn = DriverManager.getConnection(url, "root", pass); PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();
            System.out.println("\n=========== Discussion Board ===========");
            while (rs.next()) {
                int level = rs.getInt("level");
                displayPost(rs, level * 4);  // Indent replies
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void postMessage(String role, String username) {
        System.out.print("Enter your message: ");
        String message = scanner.nextLine();

        String query = "INSERT INTO discussion_posts (role, username, message) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, role);
            pstmt.setString(2, username);
            pstmt.setString(3, message);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int postId = rs.getInt(1);
                    System.out.println("Message posted successfully! Post ID: " + postId);
                }
            } else {
                System.out.println("Failed to post message.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void replyToMessage(String role, String username) {
        System.out.print("Enter the ID of the message you want to reply to: ");
        int postId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();

        String query = "INSERT INTO discussion_posts (role, username, message, post_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, role);
            pstmt.setString(2, username);
            pstmt.setString(3, reply);
            pstmt.setInt(4, postId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reply posted successfully!");
            } else {
                System.out.println("Failed to post reply.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void likeMessage() {
        System.out.print("Enter the ID of the message you want to like: ");
        int postId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String query = "UPDATE discussion_posts SET likes = likes + 1 WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, postId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post liked!");
            } else {
                System.out.println("Failed to like post. Post might not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMessage(String role, String username) {
        System.out.print("Enter the ID of the message you want to delete: ");
        int postId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String query = "DELETE FROM discussion_posts WHERE id = ? AND role = ? AND username = ?";
        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, postId);
            pstmt.setString(2, role);
            pstmt.setString(3, username);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post deleted successfully!");
            } else {
                System.out.println("Failed to delete post. It might not exist or you might not be the author.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void interactInDiscussion(User user) {
        boolean stay = true;
        Scanner sc = new Scanner(System.in);

        while (stay) {
            displayDiscussion();
            System.out.println("\n[1] Post a message");
            System.out.println("[2] Reply to a message");
            System.out.println("[3] Like a message");
            System.out.println("[4] Delete your message");
            System.out.println("[5] Refresh discussion");
            System.out.println("[0] Back to main menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    postMessage(user.getRole(),user.getUsername());
                    break;
                case 2:
                    replyToMessage(user.getRole(),user.getUsername());
                    break;
                case 3:
                    likeMessage();
                    break;
                case 4:
                    deleteMessage(user.getRole(),user.getUsername());
                    break;
                case 5:
                    // Just redisplay, which happens at the start of the loop
                    break;
                case 0:
                    stay = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    public static void main(String[] args) {
        DiscussionPage dp = new DiscussionPage();
                Ui starter = new Ui();
        FirstPage fp = new FirstPage();
        User user = fp.welcome();
        dp.interactInDiscussion(user);
    }
}