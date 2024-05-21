/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DataStructure.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private List<BookingDestination> bookingDestinations;

    public BookingService() {
        bookingDestinations = loadBookingDestinations();
    }

    private List<BookingDestination> loadBookingDestinations() {
        List<BookingDestination> destinations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("BookingDestination.txt"))) {
            String line = reader.readLine(), name, coordinate;
            while (line != null) {
                if (line.trim().isEmpty()) {
                    continue;
                } else {
                    name = line.trim();
                    coordinate = reader.readLine();
                    if (coordinate == null) {
                        break;
                    }
                }
                String[] parts = coordinate.split(",");
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                destinations.add(new BookingDestination(name, x, y));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinations;
    }

    public List<BookingDestination> getSortedBookingDestinations(double userX, double userY) {
        return bookingDestinations.stream()
                .sorted(Comparator.comparingDouble(dest -> calculateEuclideanDistance(userX, userY, dest.getX(), dest.getY())))
                .toList();
    }

    private double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
