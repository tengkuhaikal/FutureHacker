/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Account.AccountSettings;
import static Account.AccountSettings.viewParentChildRelationships;
import Account.FirstPage;
import Account.Login;
import Account.User;
import Activity.AccessManager;
import Activity.BookingService;
import Event.CreateEvent;
import static Event.CreateEvent.createEvent;
import static Leaderboard.FriendManager.managerequest;
import static Leaderboard.rank.viewrank;
import Quiz.NewQuiz;
import Quiz.AttemptQuiz;
import activity.EventManager;

import java.util.Scanner;

/**
 *
 * @author Afiq Zafry
 */
public class Ui {

    Login l = new Login();
    AccountSettings as = new AccountSettings();
    Scanner scan = new Scanner(System.in);
    FirstPage fp = new FirstPage();
    NewQuiz nq = new NewQuiz();
    AttemptQuiz at = new AttemptQuiz();
    CreateEvent ce = new CreateEvent();
    EventManager em = new EventManager();
    BookingService bs = new BookingService();

    public void mainmenu(User user) {
        System.out.println("\n\n-----------Main Menu-------------");

        int choice;
        //Access Manager
        switch (user.getRole()) {
            case "Young_Students": {

                System.out.println("1.View Profile");
                System.out.println("2.Account Settings");
                System.out.println("3.View Leaderboard and Send Request Friend");
                System.out.println("4.Manage Friend Request");
                System.out.println("5.Attempt Quiz");
                System.out.println("6.View and Register Events"); //Not completed Yet'
                System.out.println("7.Log Out");
                choice = scan.nextInt();
                scan.nextLine();
                switch (choice) {
                    case 1:
                        l.viewprofile(user);
                    case 2:
                        as.Settings(user);
                    case 3:
                        viewrank(user);
                    case 4:
                        managerequest(user);
                    case 5:
                        at.attemptquiz(user);
                    case 6:
                        em.displayLiveAndUpcomingEvents(user);
                    case 7: {
                        FirstPage fp = new FirstPage();
                        User u = fp.welcome();
                        mainmenu(u);
                    }

                }
            }
            case "Parents": {
                System.out.println("1.View Profile");    //already a method to see booking history
                System.out.println("2.Account Settings");
                System.out.println("3.View and Book Trips"); //Done
                System.out.println("4.View Events");
                System.out.println("5.Log Out");
                choice = scan.nextInt();
                scan.nextLine();
                switch (choice) {
                    case 1:
                        l.viewprofile(user);
                    case 2:
                        as.Settings(user);
                    case 3:
                        bs.createBooking(user);
                    case 4:
                        em.displayLiveAndUpcomingEvents(user);
                    case 5: {
                        FirstPage fp = new FirstPage();
                        User u = fp.welcome();
                        mainmenu(u);
                    }

                }
            }
            case "Educators": {
                System.out.println("1.View Profile");
                System.out.println("2.Account Settings ");
                System.out.println("3.Create Quiz");
                System.out.println("4.View Events");
                System.out.println("5.Create Event");
                System.out.println("6.View Parent Child Relationship"); //Additional actually but would be better to view all at once
                System.out.println("7.Log Out");
                choice = scan.nextInt();
                scan.nextLine();
                switch (choice) {
                    case 1:
                        l.viewprofile(user);
                    case 2:
                        as.Settings(user);
                    case 3:
                        nq.CreateQuiz(user);
                    case 4:
                        em.displayLiveAndUpcomingEvents(user);
                    case 5:
                        createEvent(user);
                    case 6: {
                        viewParentChildRelationships();
                        Ui starter = new Ui();
                        starter.mainmenu(user);
                    }
                    case 7: {
                        FirstPage fp = new FirstPage();
                        User u = fp.welcome();
                        mainmenu(u);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Ui starter = new Ui();
        FirstPage fp = new FirstPage();
        User user = fp.welcome();
        starter.mainmenu(user);

    }

}
