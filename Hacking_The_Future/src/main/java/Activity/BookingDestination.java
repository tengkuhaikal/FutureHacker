/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Activity;

/**
 *
 * @author Hasna
 */
public class BookingDestination {
    private String name;
    private double x;
    private double y;
    
    public BookingDestination(String name, double x, double y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public String getName(){
        return name;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
}

