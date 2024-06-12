/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Account;

import java.util.Scanner;
import UI.formatText;
/**
 *
 * @author Afiq Zafry
 */
public class FirstPage {

    Scanner scan = new Scanner(System.in);

    public int ui() {
        formatText.formatTitle("Welcome to Hacking the Future!");

        System.out.println("1.Register an account");
        System.out.println("2.Login to your account");
        System.out.println("3.Exit");
        System.out.print("\nOption >> ");
        int userInput = scan.nextInt();
        scan.nextLine();
        System.out.println("");
        return userInput;
    }

    public User acc(int i) {
        boolean success = false;
        switch (i) {
            case 1 -> {

                return Register.registernewuser();

            }
            case 2 -> {
                Login l = new Login();
                return l.lgin();
            }
            case 3 -> {
                formatText.formatTitle("Have a nice day. Goodbye!");
                System.exit(0);
            }

        }
        return null;
    }

    public User welcome() {

        User user = acc(ui());
        if (user == null) {
            return welcome();
        }

        return user;

    }

    public static void main(String[] args) {
        FirstPage fp = new FirstPage();
        Login l = new Login();
        AccountSettings as = new AccountSettings();
        User user = fp.welcome();
        //l.viewprofile(fp.welcome());
        as.Settings(fp.welcome());

    }
}
