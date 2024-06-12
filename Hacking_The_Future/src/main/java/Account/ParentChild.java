/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Account;

import static Account.AccountSettings.getChildren;
import static Account.AccountSettings.updateChildren;
import static Account.AccountSettings.updateParent;
import UI.Ui;
import UI.ft;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Hasna
 */
public class ParentChild {

    Scanner sc = new Scanner(System.in);
    AccountSettings as;
    //private static final String defaultFilePath = "C:\\_Hasna\\UNIV\\SEM 2\\WIA1002\\Final Project\\Netbeans 19\\DataStructure\\Hacking_The_Future\\src\\main\\java\\Account\\ParentChild.txt";
    private static final String defaultFilePath = "C:\\Users\\USER\\Desktop\\Hacking The Future Real 1\\FutureHacker\\Hacking_The_Future\\src\\main\\java\\Account\\ParentChild.txt";
    //private static final String defaultFilePath = "C:\\Users\\Afiq Zafry\\OneDrive - Universiti Malaya\\Documents\\NetBeansProjects\\WIA1002\\Hacking_The_Future\\src\\main\\java\\Account\\ParentChild.txt";

    public void updateParentForChild(User user) {
        System.out.print("Enter your Parents Username: ");
        String newparent = sc.nextLine();
        java.util.ArrayList<String> c = new ArrayList<String>();
        c.add(user.getUsername());

        updateChildren(newparent, c, getChildren(newparent));
        boolean success = updateParent(user.getUsername(), newparent);
        if (success) {
            // System.out.println("Parents detail has been updated");

            user.setParent(newparent);
        } else {
            ft.error("Your detail failed to be updated");
        }
    }


    public void updateChildrenForParent(User user) {
        System.out.print("How many children do you want to add?");
        int quantity = sc.nextInt();
        sc.nextLine(); // consume newline
        if (quantity == 0) {
            ft.message("You entered zero, no children will be added.");
            return; // Exit the method early
        }

        String studentname;
        ArrayList<String> existingChildren = user.getChildren();
        ArrayList<String> newChildren = new ArrayList<>();
        ArrayList<String> existingChildrenCopy = new ArrayList<>();

        // Copy existing children to a new list
        if (existingChildren != null && !existingChildren.isEmpty()) {
            existingChildrenCopy.addAll(existingChildren);
            ft.ft("Existing children");
            for (int i = 0; i < existingChildren.size(); i++) {
                System.out.println((i + 1) + ". " + existingChildren.get(i));
            }
        } else {
            ft.error("No existing children found");
        }

        System.out.println("Type your children usernames below:");
        for (int i = 0; i < quantity; i++) {
            System.out.print((i + 1) + ". ");
            studentname = sc.nextLine();
            newChildren.add(studentname);
        }

        // Update children in the database and set parent for new children
        boolean success = updateChildren(user.getUsername(), newChildren, existingChildrenCopy);
        if (success) {
            ft.message("Your Children list has been updated.");
            for (String childUsername : newChildren) {
                updateParent(childUsername, user.getUsername());
            }

            // Combine existing and new children
            ArrayList<String> combinedList = new ArrayList<>(existingChildrenCopy);
            combinedList.addAll(newChildren);
            user.setChildren(combinedList);
        } else {
            ft.error("Failed to update your children list. Please try again");
        }
    }

    public static void main(String[] args) {
        String filePath = (args.length == 1) ? args[0] : defaultFilePath;
        boolean success = AccountSettings.populateParentChildFromFile(filePath);

        if (success) {
            ft.message("Data populated successfully.");
        } else {
            ft.error("Failed to populate data");
        }
        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
    }

}
