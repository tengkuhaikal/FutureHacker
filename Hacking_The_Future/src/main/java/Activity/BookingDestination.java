/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Activity;

import java.time.LocalDate;
import java.sql.Time;

/**
 *
 * @author Hasna
 */
public class BookingDestination {
    private String username, destination;
    private double x;
    private double y;
    private LocalDate date;
    private Time time;
    
    public BookingDestination(String destination, double x, double y){
        this.destination = destination;
        this.x = x;
        this.y = y;
    }
    
    public BookingDestination(String username, String destination, LocalDate date, Time time){
        this.username = username;
        this.destination = destination;
        date = LocalDate.now();
        time = new Time(System.currentTimeMillis());
    }
    
    public String getName(){
        return username;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public String getDestination(){
        return destination;
    }
    
    public LocalDate getDate(){
        return date;
    }
    
    public Time getTime(){
        return time;
    }
}

