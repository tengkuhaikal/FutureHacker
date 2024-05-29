/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Account.AccountSettings;
import Account.FirstPage;
import Account.Login;
import Account.User;
import Activity.AccessManager;
import static Leaderboard.FriendManager.managerequest;
import static Leaderboard.rank.viewrank;
import Quiz.NewQuiz;
import Quiz.AttemptQuiz;

import java.util.Scanner;
/**
 *
 * @author Afiq Zafry
 */
public class Ui {
    Login l =new Login();
    AccountSettings as = new AccountSettings();
    Scanner scan = new Scanner(System.in);
    FirstPage fp = new FirstPage();
    NewQuiz nq = new NewQuiz();
    AttemptQuiz at= new AttemptQuiz();
    
    public void mainmenu(User user){
        System.out.println("\n\n-----------Main Menu-------------");
        
        int choice;
        //Access Manager
       switch(user.getRole()){
           case "Young_Students":{
               
               System.out.println("1.View Profile");
               System.out.println("2.Account Settings");
               System.out.println("3.View Leaderboard and Send Request Friend");
               System.out.println("4.Manage Friend Request");
               System.out.println("5.Attempt Quiz");
               choice=scan.nextInt();
               scan.nextLine();
               switch(choice){
                   case 1 : l.viewprofile(user);
                   case 2 : as.Settings(user);
                   case 3 : viewrank(user);
                   case 4 : managerequest(user);
                   case 5 : at.attemptquiz(user);
                   
           }
           }
           case "Parents":{
               System.out.println("1.View Profile");
                System.out.println("2.Account Settings");
               choice=scan.nextInt();
               scan.nextLine();
               switch(choice){
                   case 1 : l.viewprofile(user);
                   case 2 : as.Settings(user);
           }
           }
           case "Educators":{
               System.out.println("1.View Profile");
               System.out.println("2.Account Settings ");
               System.out.println("3.Create Quiz");
               choice=scan.nextInt();
               scan.nextLine();
               switch(choice){
                   case 1 : l.viewprofile(user);
                   case 2 : as.Settings(user);
                   case 3 : nq.CreateQuiz (user);
           }
           }
               
       }
        
    }
    public static void main(String[] args) {
        Ui starter = new Ui();
         FirstPage fp = new FirstPage();
         User user =fp.welcome();
        starter.mainmenu(user);
        //AccessManager am = new AccessManager();
        
    }

    private void attemptquiz(User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

    
}
