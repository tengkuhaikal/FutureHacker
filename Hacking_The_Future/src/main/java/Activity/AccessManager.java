/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Activity;
import Account.User;
import Account.Login;
import java.util.Scanner;

/**
 *
 * @author Hasna
 */
public class AccessManager {
    BookingService book;
    Login login;
    
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
        System.out.print("Enter your choice (1-7): ");
        int choice = sc.nextInt();
        
        return choice;
    }
    
    public boolean accessPageChoice(User user, int choice) { //need to be updated when other classes made
        switch (choice) {
            case 1:
                return true; // All roles can access Event Page
            case 2:
                login.viewprofile(user);
                return true; // All roles can access View Personal/Others' Profile
            case 3:
                return true; // All roles can access Discussion Page
            case 4:
                if (user.getRole().equals("Educators")) {
                    return true;
                } else {
                    return false;
                }
            case 5:
                if (user.getRole().equals("Educators")) {
                    return true;
                } else {
                    return false;
                }
            case 6:
                if (user.getRole().equals("Parents")){
                    accessBooking(user);
                    return true;
                } else {
                    return false;
                }
            case 7:
                if (user.getRole().equals("Young_Students")) {
                    return true;
                } else {
                    return false;
                }
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }
    }
    
    private void accessBooking(User user){
        Double[] userCoordinate = user.getLocationCoordinate();
        if (userCoordinate != null && userCoordinate.length == 2) {
            Double x = userCoordinate[0];
            Double y = userCoordinate[1];
            book.displaySortedBookingDestination(x, y);
        }  
    }
}
    

