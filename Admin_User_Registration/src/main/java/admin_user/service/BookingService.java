package admin_user.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import admin_user.model.Booking;
import admin_user.model.BusRoute;
import admin_user.repositories.BookingRepository;
import admin_user.repositories.BusRouteRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRouteRepository busRouteRepository;

    // Fetch bookings for a specific user by email
    public List<Booking> getBookingsByUser(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    // Book a ticket for a specific route
    public Booking bookTicket(Long routeId, String userEmail, int seats) {
        BusRoute route = busRouteRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));

        if (route.getAvailableSeats() < seats) {
            throw new IllegalArgumentException("Not enough seats available.");
        }

        // Reduce available seats and save the updated route
        route.setAvailableSeats(route.getAvailableSeats() - seats);
        busRouteRepository.save(route);

        // Create and save the booking
        Booking booking = new Booking(route, userEmail, seats, LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    // Retrieve all bus routes
    public List<BusRoute> getAllRoutes() {
        return busRouteRepository.findAll();
    }

    // Book a bus route and update seat availability
    public void bookRoute(BusRoute route, int seats) {
        if (route.getAvailableSeats() >= seats) {
            // Update the number of available seats
            int newAvailableSeats = route.getAvailableSeats() - seats;
            route.setAvailableSeats(newAvailableSeats);

            // Save the updated route
            busRouteRepository.save(route);

            // Create a new booking record
            Booking booking = new Booking(route, "user@example.com", seats, LocalDateTime.now()); // Replace "user@example.com" with actual user info
            bookingRepository.save(booking);
        } else {
            throw new IllegalArgumentException("Not enough seats available.");
        }
    }
}
