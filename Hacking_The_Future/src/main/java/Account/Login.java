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

public class  Login {
//     public static String url = "jdbc:mysql://localhost:3306/datastructure";
//    public static String user = "root";
//    public static String pass ="root";
    Scanner scan = new Scanner(System.in);
    
   FriendManager fm = new FriendManager();
    
    public User userlogin(){
        
          boolean validChoice = false;
        System.out.println("\n\nWelcome to Log in Page !! \n");
        while (!validChoice) {
            System.out.println("Role:");
            System.out.println("1.Young_Student");
            System.out.println("2.Parents");
            System.out.println("3.Educator");
            System.out.print("Choice: ");
           

            int choice = scan.nextInt();
           
            String role;
            switch (choice) {
                case 1:
                    role="Young_Students";
                    validChoice = true;
                    return lgin(role);
                    
                case 2:
                    role="Parents";
                    validChoice = true;
                    return lgin(role);
                    
                case 3:
                    role="Educators";
                    validChoice = true;
                    return lgin(role);
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
                    // Set validChoice to false to repeat the loop
                    break;
                   
            }
        }
        return null;
    }
    public void viewprofile(User user){
      
        System.out.println("Your Profile Details : \n");
          System.out.println("Email: "+user.getEmail());
          System.out.println("Username: "+user.getUsername());
          System.out.println("Role: "+user.getRole());
          System.out.println("Location: "+Arrays.toString(user.getLocationCoordinate()));
          
          switch(user.getRole()){
              case "Young_Students":{
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
            } else {
                                        System.out.println("Children: "+"null");
            }
                                    break;
                                }
                                case "Educators":{
                                    break;
                                }
          }
          System.out.println("---------------------------------                               ");
          System.out.println("You'll be redirected to main menu\n\n");
          Ui u = new Ui();
          u.mainmenu(user);
    }
    
    public User lgin(String role){
         String input;
         input=scan.nextLine();
         java.util.ArrayList <String> data = new ArrayList<>();
        System.out.print("Email: ");
        input=scan.nextLine();
        data.add(input);
        System.out.print("Username: ");
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
                    if(check.getString(3).equals(data.get(1))){
                            usernameExist=true;
                               if(check.getString(4).equals(data.get(2))){
                                passwordExist = true;
                          
                            user.setEmail(data.get(0));
                            user.setPassword(data.get(2));
                            user.setUsername(data.get(1));
                            if(check.getString(5).equals(role)){
                            user.setRole(role);
                            }
                            else{
                                System.out.println("Wrong selected role !!");
                                return null;
                            }
                                   
                            switch(role){
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
            }

            if(emailExist){
                if(passwordExist){
                    System.out.println("Welcome back "+user.getUsername().toUpperCase()+"!"); 
                    return user;
                    //mod.promptAcc();
                    
                }else{
                    System.out.println("Incorrect password, please try again!");
                    return null;
                }
            }else{
                System.out.println("No email found, please register an account if you don't have an account!");
                //successLogin = false;
                return null;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        Login l = new Login();

// Authenticate the user


// Pass the authenticated user to AccountSettings
AccountSettings as = new AccountSettings();

    }
}
