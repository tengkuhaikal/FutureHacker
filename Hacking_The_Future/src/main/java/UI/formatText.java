/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

/**
 *
 * @author tmhta_
 */
public class formatText {
    
    public static void formatTitle(String title) {
        int lineLength = title.length() + 15; // Length of the line of dashes
        int padding = (lineLength - title.length()) / 2;
        String line = new String(new char[lineLength]).replace("", "-");
        System.out.println("\n" + line);
        System.out.println(String.format("%" + padding + "s%s%" + padding + "s", "", title, ""));
        System.out.println(line + "\n");
    }
    
    public static void error(String errorMessage){
        System.out.println("!!! " + errorMessage + " !!!");
    }
    
    public static void message(String message){
        System.out.println(">>> " + message + " <<<");
    }
    
}
