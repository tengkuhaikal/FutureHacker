/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Account;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author Hasna
 */
public class ParentChild {
    Scanner sc = new Scanner (System.in);
    AccountSettings as;
    
    public void updateParentForChild(User user) {
        System.out.print("Enter your Parents Username: ");
        String newparent = sc.nextLine();
        java.util.ArrayList<String> c = new ArrayList<String>();
        c.add(user.getUsername());

        as.updateChildren(newparent, c, as.getChildren(newparent));
        boolean success = as.updateParent(user.getUsername(), newparent);
        if (success) {
            System.out.println("Parents detail has been updated");

            user.setParent(newparent);
        } else {
            System.out.println("Your detail failed to be updated");
        }
    }

    public void updateChildrenForParent(User user) {
        System.out.println("How many children you do you want to add");
        int quantity = sc.nextInt();
        sc.nextLine();
        if (quantity == 0) {
            System.out.println("You press zero, no children will be added");
        }

        String studentname;

        ArrayList<String> real = user.getChildren();
        java.util.ArrayList<String> temp = new ArrayList<String>();
        java.util.ArrayList<String> temp2 = new ArrayList<String>();
        if (!real.isEmpty()) {
            for (int i = 0; i < real.size(); i++) {
                temp2.add(real.get(i));
            }
        }
        System.out.println("Type your children usernames below: ");
        int k;
        for (int i = 0; i < quantity; i++) {
            k = i + 1;
            System.out.print(k + ". ");
            studentname = sc.nextLine();

            temp.add(studentname);
        }

        boolean success = as.updateChildren(user.getUsername(), temp, temp2);
        if (success) {
            System.out.println("Your Children list has been updated");
            for (int i = 0; i < temp.size(); i++) {
                as.updateParent(temp.get(i), user.getUsername());
            }
            ArrayList<String> combinedList = new ArrayList<>(temp2);
            combinedList.addAll(temp);
            user.setChildren(combinedList);
        } else {
            System.out.println("Your detail failed to be updated");
        }
    }

    public static void main(String[] args) {
        AccountSettings.populateParentChildFromFile("ParentChild.txt");
    }
}
