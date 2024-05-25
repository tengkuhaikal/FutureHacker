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
    
    public void mainmenu(User user){
        System.out.println("\n\n-----------Main Menu-------------");
        
        int choice;
        //Access Manager
       switch(user.getRole()){
           case "Young_Students":{
               int j=2;
               System.out.println("1.View Profile");
               if(user.getParent()==null){
                   j=2;
               }
               else{
                   j=1;
               }
               System.out.println(j+".Account Settings");
               choice=scan.nextInt();
               scan.nextLine();
               switch(choice){
                   case 1 : l.viewprofile(user);
                   case 2 : as.Settings(user);
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
               choice=scan.nextInt();
               scan.nextLine();
               switch(choice){
                   case 1 : l.viewprofile(user);
           }
           }
               
       }
        
    }
    public static void main(String[] args) {
        Ui starter = new Ui();
         FirstPage fp = new FirstPage();
         User user =fp.welcome();
        starter.mainmenu(user);
        AccessManager am = new AccessManager();
        am.accessPageChoice(user, am.getPageChoice());
    }
}
