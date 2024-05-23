/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Account;

/**
 *
 * @author Afiq Zafry
 */
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
public class AccountSettings {
    
    static String url = "jdbc:mysql://localhost:3306/datastructure";
  
    
    public static String pass ="root";
    Scanner scan = new Scanner(System.in);
    
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
      String newparent;
         
        switch(user.getRole()){
            
            case "Young_Students":{
                System.out.println("Which Account detail you want to change/fill ? :");
                System.out.println("1.Password");
                System.out.println("2.Parents");
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
                        System.out.print("Enter your Parents Username: ");
                        newparent=scan.nextLine();
                        java.util.ArrayList<String> c = new ArrayList<String>();
                        c.add(user.getUsername());
                       
                        updateChildren(newparent,c,getChildren(newparent));
                        success=updateParent(user.getUsername(),newparent);
                        if (success) {
                            System.out.println("Parents detail has been updated");
                           
                            user.setParent(newparent);
                        } else {
                            System.out.println("Your detail failed to be updated");
                        }
                        break;
                    }
                    default: break;
                }
                    break;
                }
                
                
            
            case "Parents":{
                System.out.println("Which Account detail you want to change/fill ? :");
                System.out.println("1.Password");
                System.out.println("2.Children");
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
                        System.out.println("How many children you do you want to add");
                        int quantity=scan.nextInt();
                        scan.nextLine();
                        if(quantity==0){
                            System.out.println("You press zero, no children will be added");
                            break;
                        }
                        
                        String studentname;
                        
                        ArrayList <String> real = user.getChildren();
                        java.util.ArrayList <String> temp = new ArrayList <String>();
                        java.util.ArrayList<String> temp2 = new ArrayList<String>();
                        if(!real.isEmpty()){
                            for (int i = 0; i < real.size(); i++) {
                                temp2.add(real.get(i));
                            }
                        }
                        System.out.println("Type your children usernames below: ");
                        int k;
                        for (int i = 0; i < quantity; i++) {
                            k=i+1;
                            System.out.print(k+". ");
                            studentname=scan.nextLine();
                            
                            temp.add(studentname);
                        }
                        
                        success=updateChildren(user.getUsername(),temp,temp2);
                        if(success){
                            System.out.println("Your Children list has been updated" );
                            for (int i = 0; i < temp.size(); i++) {
                                updateParent(temp.get(i),user.getUsername());
                            }
                            ArrayList<String> combinedList = new ArrayList<>(temp2);
                           combinedList.addAll(temp);
                           user.setChildren(combinedList);
                        }
                        else{
                            System.out.println("Your detail failed to be updated");
                        }
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

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, newPassword);
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
    
    
    public static boolean updateParent(String username, String parentUsername) {
        String checkParentQuery = "SELECT COUNT(*) FROM user WHERE username = ? AND role = 'Parents' ;";
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
            
            // Proceed with the update if the parent username exists
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
                System.out.println(" Children who are/is invalid  : "+ invalidChildren );
            }
            if(validChildren.isEmpty()){
                System.out.println("The Children you want to add are already in your children list");
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

  


}
