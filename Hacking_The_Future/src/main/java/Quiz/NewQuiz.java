/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Quiz;

import static Account.MySQLConfiguration.pass;
import static Account.MySQLConfiguration.url;
import Account.User;
import UI.Ui;
import UI.formatText;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author Afiq Zafry
 */
public class NewQuiz {

    Scanner scan = new Scanner(System.in);
    String title, desc, theme, content;

    public void CreateQuiz(User user) {

        formatText.formatTitle("New Quiz");
        System.out.println("Educator: " + user.getUsername());
        System.out.print("Quiz Title >> ");
        title = scan.nextLine();
        System.out.print("Quiz Description >> ");
        desc = scan.nextLine();
        System.out.println("Themes:\n1.Science\n2.Technology\n3.Engineering\n4.Mathematics");
        boolean valid = true;
        do {
            System.out.print("\nTheme >> ");
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
                default:
                    valid = true;
                    break;
            }
        } while (valid);
        scan.nextLine(); // Consume newline left by nextInt()

        System.out.print("Quiz Link >> ");
        content = scan.nextLine();

        Quizizz quiz = new Quizizz(title, desc, theme, content);
        // Insert quiz into the database
        try (Connection conn = DriverManager.getConnection(url, "root", pass)) {
            String insertQuery = "INSERT INTO quiz (title, description, theme, Content, username) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, title);
                pstmt.setString(2, desc);
                pstmt.setString(3, theme);
                pstmt.setString(4, content);
                pstmt.setString(5, user.getUsername()); // Insert educator's username
                pstmt.executeUpdate();
                formatText.message("Quiz created successfully");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        formatText.formatTitle("Continue to create quiz? [1:Yes || 0:No] :");
        System.out.println("\nOption >> ");
        int choice = scan.nextInt();
        if (choice == 1) {
            CreateQuiz(user);
        } else {
            Ui starter = new Ui();
            starter.mainmenu(user);
        }

    }
}
