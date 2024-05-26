/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Activity;
import Account.User;
import Account.Login;
import UI.Ui;
import java.util.Scanner;

/**
 *
 * @author Hasna
 */
public class AccessManager {
    BookingService book;
    Login login;
    Ui starter;
    
    public int getPageChoice() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose a page:");
        System.out.println("1. Event Page");
        System.out.println("2. View Profile Page");
        System.out.println("3. Discussion Page");
        System.out.println("4. Create Event Page");
        System.out.println("5. Create Quiz Page");
        System.out.println("6. Booking Page");
        System.out.println("7. Quiz Page");
        System.out.println("8. Back to main menu");
        System.out.print("Enter your choice (1-7): ");
        int choice = sc.nextInt();
        
        return choice;
    }
    
    public boolean accessPageChoice(User user, int choice) { //need to be updated when other classes made
        switch (choice) {
            case 1:
                break; // All roles can access Event Page
            case 2:
                login.viewprofile(user);
                break; // All roles can access View Personal/Others' Profile
            case 3:
                accessDiscussion();
                break; // All roles can access Discussion Page
            case 4:
                if (user.getRole().equals("Educators")) {
              
                } else {
                    System.out.println("Access denied! Educators only");
                    break;
                }
            case 5:
                if (user.getRole().equals("Educators")) {
                    
                } else {
                    System.out.println("Access denied! Educators only");
                } break;
            case 6:
                if (user.getRole().equals("Parents")){
                    accessBooking(user);
                    
                } else {
                    System.out.println("Access denied! Parents only");
                } break;
            case 7:
                if (user.getRole().equals("Young_Students")) {
                    
                } else {
                    System.out.println("Access denied! Students only");
                } break;
            case 8: 
                starter.mainmenu(user);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        getPageChoice();
        return false;
    }
    
    private void accessBooking(User user){
        Double[] userCoordinate = user.getLocationCoordinate();
        if (userCoordinate != null && userCoordinate.length == 2) {
            Double x = userCoordinate[0];
            Double y = userCoordinate[1];
            book.displaySortedBookingDestination(x, y);
        }  
    }
    
    private void accessDiscussion(){ //some extras
        System.out.println("-------------------------------------------------");
        System.out.println("| Home | Discussions | Categories | About us    |");
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic: How to Learn Java Effectively?         |");
        System.out.println("| Description: Tips for beginners.              |");
        System.out.println("-------------------------------------------------"); 
        System.out.println("| User: JohnDoe  | Date: 18/05/2024             |");
        System.out.println("-------------------------------------------------");
        System.out.println("| I've been trying to learn Python but I'm      |");
        System.out.println("| struggling to stay consistent. Any tips?      |");
        System.out.println("-------------------------------------------------");
        System.out.println("| Reply | Like (10) | Dislike (2) | Report      |");
        System.out.println("-------------------------------------------------");
        System.out.println("    | User: JaneSmith  | Date: 19/05/2024       |");
        System.out.println("    | I recommend starting with small projects. |");
        System.out.println("-------------------------------------------------");
        System.out.println("    | Reply | Like (5) | Dislike (1) | Report   |");
        System.out.println("-------------------------------------------------");
        System.out.println("    | User: CodeMaster | Date: 21/05/2024       |");
        System.out.println("    | Check out freeCodeCamp and Codecademy.    |");
        System.out.println("-------------------------------------------------");
        System.out.println("    | Reply | Like (3) | Dislike (0) | Report   |");
        System.out.println("-------------------------------------------------");
    }

}
    

