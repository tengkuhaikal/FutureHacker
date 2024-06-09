/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Account;

/**
 *
 * @author Afiq Zafry
 */
import static Account.Register.hashPassword;
import java.sql.Connection; // Represents a connection to the database
import java.sql.DriverManager; // Helps in obtaining a connection to the database
import java.sql.PreparedStatement; // Used for prepared statements
import java.sql.Statement; // Used for executing SQL statements
import java.sql.ResultSet; // Represents a table of data resulting from a query
import java.sql.SQLException; // Handles SQL exceptions
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import UI.Ui;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class AccountSettings {
    
    static String url = "jdbc:mysql://localhost:3306/datastructure";
  
    
    public static String pass ="root";
    Scanner scan = new Scanner(System.in);
    ParentChild pc = new ParentChild();
    
    public User Settings (User user) {
        
        System.out.println("Your Profile Details : \n");
          System.out.println("Email: "+user.getEmail());
          System.out.println("Username: "+user.getUsername());
          System.out.println("Role: "+user.getRole());
          System.out.println("Location: "+Arrays.toString(user.getLocationCoordinate()));
          
          switch(user.getRole()){
              case "Young_Students":{
                                    System.out.println("Current points: "+user.getCurrentPoints());
                                    if(user.getParent()!=null ) {
                                        System.out.println("Parents: "+user.getParent());
            } else {
                                        System.out.println("Parents: "+"null");
            }
                                    break;
                                }
                                case "Parents":{
                                    
                                       if(user.getChildren() != null && !user.getChildren().isEmpty()) {
                                        System.out.println("Children: "+user.getChildren());
            } else {
                                        System.out.println("Children: "+"null");
            }
                                    break;
                                }
                                case "Educators":{
                                    break;
                                }
          }
          
     int input;
      boolean success=false;  
      String newpass;
      //String newparent;
         
        switch(user.getRole()){
            
            case "Young_Students":{
                System.out.println("Which Account detail you want to change/fill ? :");
                System.out.println("1.Password");
                System.out.println("2.Add Parents");
                System.out.print(" Change Option no. : ");
                input = scan.nextInt();
                scan.nextLine();
                switch (input) {
                    case 1: {
                        System.out.print("Enter new password: ");
                        newpass = scan.nextLine();
                        success = updatePassword(user.getUsername(), newpass);
                        if (success) {
                            System.out.println("Password has been updated");
                        } else {
                            System.out.println("Your detail failed to be updated");
                        }
                        break;
                    }
                    case 2:{
                        pc.updateParentForChild(user);                       
                        break;
                    }
                    default: break;
                }
                    break;
                }
                
                
            
            case "Parents":{
                System.out.println("Which Account detail you want to change/fill ? :");
                System.out.println("1.Password");
                System.out.println("2.Add Children");
                System.out.print(" Change Option no. : ");
                input = scan.nextInt();
                scan.nextLine();
                switch(input){
                    case 1:{
                        System.out.print("Enter new password: ");
                        newpass = scan.nextLine();
                        success = updatePassword(user.getUsername(), newpass);
                        if (success) {
                            System.out.println("Password has been updated");
                        } else {
                            System.out.println("Your detail failed to be updated");
                        }
                        break;
                    }
                    case 2:{
                        pc.updateChildrenForParent(user);
                        break;
                    }
                    default:break;
                }
                break;
            }
            case "Educators":{
                System.out.println("do you want to change your password?");
                System.out.println("1 for yes , 0 for no : ");
                input = scan.nextInt();
                switch(input){
                    case 0: break;
                    case 1 : {
                        System.out.print("Enter new password: ");
                         newpass=scan.nextLine();
             success=updatePassword(user.getUsername(),newpass);
            if(success){
                System.out.println("Password has been updated");
            }
            else
                            System.out.println("Your detail failed to be updated");
            break;
                    }
                    
                }
                break;
            }
    
        }
        Ui u = new Ui();
        
        System.out.println("Do you still want to edit your account details ? (Yes:1/No:0)");
        input=scan.nextInt();
       
        while(input==0 || input==1){
            if(input==1)
            Settings(user);
        else if(input==0)
            u.mainmenu(user);
        else{
            System.out.print("Please type 1 or 0 only: ");
             input=scan.nextInt();
        scan.nextLine();
        }
        }
        
        
        return user;
    }
        
        
        
        
        
    
    
    public static boolean updatePassword(String username, String newPassword) {
        String updateQuery = "UPDATE user SET password = ? WHERE username = ?";
        
        try (Connection connect = DriverManager.getConnection(url, "root", pass);
             PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {

            String hashedpass= hashPassword(newPassword);
            // Set the parameters for the prepared statement
            preparedStatement.setString(1, hashedpass);
            preparedStatement.setString(2, username);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the update was successful
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
//    public static boolean updateParent(String username, String parentUsername) {
//        String checkParentQuery = "SELECT COUNT(*) FROM user WHERE username = ? AND role = 'Parents' ;";
//        String updateQuery = "UPDATE user SET parents = ? WHERE username = ?";
//
//        try (Connection connect = DriverManager.getConnection(url, "root", pass)) {
//            // Check if the parent username exists
//            try (PreparedStatement checkStatement = connect.prepareStatement(checkParentQuery)) {
//                checkStatement.setString(1, parentUsername);
//                try (ResultSet result = checkStatement.executeQuery()) {
//                    if (result.next()) {
//                        int count = result.getInt(1);
//                        if (count == 0) {
//                            System.out.println("Parent username does not exist.");
//                            return false;
//                        }
//                    }
//                }
//            }
//            
//            // Proceed with the update if the parent username exists
//            try (PreparedStatement updateStatement = connect.prepareStatement(updateQuery)) {
//                updateStatement.setString(1, parentUsername);
//                updateStatement.setString(2, username);
//                
//                int rowsAffected = updateStatement.executeUpdate();
//                return rowsAffected > 0;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    
    
    public static boolean updateParent(String username, String parentUsername) {
    String checkParentQuery = "SELECT COUNT(*) FROM user WHERE username = ? AND role = 'Parents';";
    String checkExistingParentQuery = "SELECT COUNT(*) FROM user WHERE username = ? AND parents IS NOT NULL;";
    String updateQuery = "UPDATE user SET parents = ? WHERE username = ?";

    try (Connection connect = DriverManager.getConnection(url, "root", pass)) {
        // Check if the parent username exists
        try (PreparedStatement checkStatement = connect.prepareStatement(checkParentQuery)) {
            checkStatement.setString(1, parentUsername);
            try (ResultSet result = checkStatement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt(1);
                    if (count == 0) {
                        System.out.println("Parent username does not exist.");
                        return false;
                    }
                }
            }
        }

        // Check if the user already has a non-null parents column
        try (PreparedStatement checkExistingParentStatement = connect.prepareStatement(checkExistingParentQuery)) {
            checkExistingParentStatement.setString(1, username);
            try (ResultSet result = checkExistingParentStatement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt(1);
                    if (count > 0) {
                        System.out.println("The user already has a parent assigned.");
                        return false;
                    }
                }
            }
        }

        // Proceed with the update if the parent username exists and the user does not already have a parent
        try (PreparedStatement updateStatement = connect.prepareStatement(updateQuery)) {
            updateStatement.setString(1, parentUsername);
            updateStatement.setString(2, username);

            int rowsAffected = updateStatement.executeUpdate();
            return rowsAffected > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
    public static boolean updateChildren(String username, ArrayList<String> childrenUsernames, ArrayList<String> childrenusernames2) {
        String checkChildQuery = "SELECT COUNT(*) FROM user WHERE username = ? AND role = 'Young_Students' AND parents IS NULL";
        String updateQuery = "UPDATE user SET children = ? WHERE username = ?";

        try (Connection connect = DriverManager.getConnection(url, "root", pass)) {
            ArrayList<String> validChildren = new ArrayList<>();
            ArrayList<String> invalidChildren = new ArrayList<>();
            // Check each child username
            for (String childUsername : childrenUsernames) {
                try (PreparedStatement checkStatement = connect.prepareStatement(checkChildQuery)) {
                    checkStatement.setString(1, childUsername);
                    try (ResultSet result = checkStatement.executeQuery()) {
                        if (result.next() && result.getInt(1) > 0) {
                            validChildren.add(childUsername);
                        }
                        else{
                            invalidChildren.add(childUsername);
                        }
                    }
                }
            }
            if(!invalidChildren.isEmpty()){
                System.out.println( invalidChildren+ "are/is invalid to be added "+username+"'s children list" );
            }
            if(validChildren.isEmpty()){
             //   System.out.println("Redundant update to"+username+"'s children list detected !!");
                return false;
            }
            for (int i = 0; i <childrenusernames2.size(); i++) {
                validChildren.add(childrenusernames2.get(i));
            }
            // Construct the string for existing children
            String childrenString = String.join(" ", validChildren);
            
            // Proceed with the update if there are valid children
            try (PreparedStatement updateStatement = connect.prepareStatement(updateQuery)) {
                updateStatement.setString(1, childrenString);
                updateStatement.setString(2, username);

                int rowsAffected = updateStatement.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static ArrayList<String> getChildren(String parentUsername) {
        String query = "SELECT children FROM user WHERE username = ? AND role = 'Parents'";
        ArrayList<String> childrenList = new ArrayList<>();

        try (Connection connect = DriverManager.getConnection(url, "root", pass);
             PreparedStatement preparedStatement = connect.prepareStatement(query)) {

            // Set the parent username parameter
            preparedStatement.setString(1, parentUsername);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If a result is found
                if (resultSet.next()) {
                    String childrenString = resultSet.getString("children");

                    // Check if the children string is not null or empty
                    if (childrenString != null && !childrenString.trim().isEmpty()) {
                        // Split the string by spaces to get individual usernames
                        String[] childrenArray = childrenString.trim().split(" ");

                        // Convert the array to an ArrayList
                        childrenList = new ArrayList<>(Arrays.asList(childrenArray));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return childrenList;
    }
    
    
public static boolean populateParentChildFromFile(String filePath) {
    String insertQuery = "INSERT INTO user (username, parents, role, email, password, location) VALUES (?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?)";
    String updateQuery = "UPDATE user SET children = CONCAT(IFNULL(children, ''), ?) WHERE username = ?";

    try (Connection connect = DriverManager.getConnection(url, "root", pass);
         BufferedReader reader = new BufferedReader(new FileReader(filePath));
         PreparedStatement insertStatement = connect.prepareStatement(insertQuery);
         PreparedStatement updateStatement = connect.prepareStatement(updateQuery)) {

        String line;
        int counter = 1; // Counter to generate unique values
        while ((line = reader.readLine()) != null) {
            String[] relationship = line.split(",");
            if (relationship.length == 2) {
                String parentUsername = relationship[0].trim();
                String childUsername = relationship[1].trim();
                
                // Generate unique email addresses and passwords
                String parentEmail = "parent" + counter + "@example.com";
                String childEmail = "child" + counter + "@example.com";
                String parentPassword = "password" + counter; // Example password generation
                String childPassword = "password" + counter;
                counter++;
                
                Random rand = new Random();
                // Generate unique coordinates
                    int parentLat = rand.nextInt(1001) - 500; // Latitude in the range -500 to 500
                    int parentLong = rand.nextInt(1001) - 500; // Longitude in the range -500 to 500
                    int childLat = rand.nextInt(1001) - 500; // Latitude in the range -500 to 500
                    int childLong = rand.nextInt(1001) - 500; // Longitude in the range -500 to 500

                    // Combine latitude and longitude into location string
                    String parentLocation = parentLat + "," + parentLong;
                    String childLocation = childLat + "," + childLong;

                // Insert child user
                insertStatement.setString(1, childUsername);
                insertStatement.setString(2, parentUsername);
                insertStatement.setString(3, "Young_Students");
                insertStatement.setString(4, childEmail);
                insertStatement.setString(5, childPassword);
                insertStatement.setString(6, childLocation);

                // Insert parent user
                insertStatement.setString(7, parentUsername);
                insertStatement.setNull(8, java.sql.Types.VARCHAR); // Assuming parents don't have a parent
                insertStatement.setString(9, "Parents");
                insertStatement.setString(10, parentEmail);
                insertStatement.setString(11, parentPassword);
                insertStatement.setString(12, parentLocation);

                insertStatement.addBatch();

                // Update parent's children field
                updateStatement.setString(1, childUsername + ","); // Append child username
                updateStatement.setString(2, parentUsername);
                updateStatement.addBatch();
            }
        }

        insertStatement.executeBatch();
        updateStatement.executeBatch();
        return true;

    } catch (IOException | SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public static boolean viewParentChildRelationships() {
        String query = "SELECT username, children FROM user WHERE role = 'Parents'";

        try (Connection connect = DriverManager.getConnection(url, "root", pass);
             PreparedStatement statement = connect.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Parent-Child Relationships:");
            System.out.println("---------------------------");

            while (resultSet.next()) {
                String parentUsername = resultSet.getString("username");
                String children = resultSet.getString("children");

                if (children != null && !children.isEmpty()) {
                    System.out.println("Parent: " + parentUsername);
                    String[] childrenArray = children.split(",");
                    for (String child : childrenArray) {
                        System.out.println("  - Child: " + child.trim());
                    }
                } else {
                    System.out.println("Parent: " + parentUsername + " has no children.");
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
