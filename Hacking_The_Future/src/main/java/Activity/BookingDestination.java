/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Activity;

import Account.User;
import java.time.LocalDate;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author Hasna
 */
public class BookingDestination {

    private String username, destination;
    private double x;
    private double y;
    private LocalDate date;
    private LocalDate bookingDateChoice;
    private Time time;
    private User user;

    public BookingDestination() {
    }

    public BookingDestination(String destination, double x, double y) {
        this.destination = destination;
        this.x = x;
        this.y = y;
        date = LocalDate.now();
        time = new Time(System.currentTimeMillis());

    }

    public BookingDestination(String username, String destination, LocalDate date, Time time) {
        this.username = username;
        this.destination = destination;
//        date = LocalDate.now();
//        time = new Time(System.currentTimeMillis());
        this.date = date;
        this.time = time;
        //this.bookingDateChoice = dateChoice;
    }

    public String getName() { //for parents
        return username;
    }

    public ArrayList<String> getChildrenName() {
        return user.getChildren();
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
    
    public void setBookingDateChoice(LocalDate bookingDateChoice){
        this.bookingDateChoice = bookingDateChoice;
    }
    
    public LocalDate getBookingDateChoice(){
        return bookingDateChoice;
    } 

}
