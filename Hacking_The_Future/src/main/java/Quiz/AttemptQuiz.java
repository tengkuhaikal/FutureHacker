/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Quiz;
import java.awt.Desktop;
import java.net.URI;
import static Account.MySQLConfiguration.pass;
import static Account.MySQLConfiguration.url;
import Account.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
/**
 *
 * @author Afiq Zafry
 */
public class AttemptQuiz {
    Scanner scan = new Scanner (System.in);
    public  void attemptquiz(User user){
        System.out.println("\n\n-----Welcome to Quiz section!!----\n");
        System.out.println("Choose your preference:\n1.Science\n2.Technology\n3.Engineering\n4.Mathmethics\n5.All themes");
        String theme = null;
        
         boolean valid = true;
        do {
            System.out.print("Quiz Theme: ");
            int choice = scan.nextInt();
            switch (choice) {
                case 1:
                    theme = "Science";
                    valid = false;
                    break;
                case 2:
                    theme = "Technology";
                    valid = false;
                    break;
                case 3:
                    theme = "Engineering";
                    valid = false;
                    break;
                case 4:
                    theme = "Mathematics";
                    valid = false;
                    break;
                 case 5:
                    theme = "All";
                    valid = false;
                    break;
                default:
                    valid = true;
                    break;
            }
        } while (valid);
      scan.nextLine(); // Consume newline left by nextInt()
        
        System.out.println("\n\n");
      
        // Retrieve quizzes based on theme
        String query;
        if (theme.equals("All")) {
            query = "SELECT title, description FROM quiz";
        } else {
            query = "SELECT title, description FROM quiz WHERE theme = ?";
        }

        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (!theme.equals("All")) {
                pstmt.setString(1, theme);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    System.out.println("Title: " + title);
                    System.out.println("Description: " + description);
                    System.out.println();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

       // Allow the user to choose a quiz to attempt
String selectedQuizTitle;
boolean isValidQuizTitle;
do {
    System.out.print("Enter the title of the quiz you want to attempt: ");
    selectedQuizTitle = scan.nextLine();

    // Check if the entered title exists in the quiz table
    isValidQuizTitle = isQuizTitleValid(selectedQuizTitle);
    if (!isValidQuizTitle) {
        System.out.println("Quiz title is not valid. Please try again.");
    }
} while (!isValidQuizTitle);

// Fetch the content column for the selected quiz title
String quizContent = fetchQuizContent(selectedQuizTitle);
attemptSelectedQuiz(quizContent,user);

// Now you can proceed with attempting the quiz

    }

    
    private boolean isQuizTitleValid(String title) {
    String query = "SELECT COUNT(*) FROM quiz WHERE title = ?";
    try (Connection conn = DriverManager.getConnection(url, "root", pass);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, title);
        try (ResultSet rs = pstmt.executeQuery()) {
            rs.next();
            int count = rs.getInt(1);
            return count > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

private String fetchQuizContent(String title) {
    String query = "SELECT content FROM quiz WHERE title = ?";
    try (Connection conn = DriverManager.getConnection(url, "root", pass);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, title);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("content");
            } else {
                return null; // Quiz title not found
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

    
    
    
    private void attemptSelectedQuiz(String link,User user) {
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch (Exception e) {
            e.printStackTrace();
        }
        addPoints(user.getUsername());
        
    }
    
    public static void addPoints(String username) {
        String updateQuery = "UPDATE user SET points = points + 2 WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, "root", pass);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Points added successfully for user: " + username);
            } else {
                System.out.println("User not found or points not updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

