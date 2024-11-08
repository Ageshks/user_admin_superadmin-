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

    public List<Booking> getBookingsByUser(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    public Booking bookTicket(Long routeId, String userEmail, int seats) {
        BusRoute route = busRouteRepository.findById(routeId).orElseThrow();
        // Create a new booking with the updated constructor
        Booking booking = new Booking(route, userEmail, seats, LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    // Add this method to get all routes
    public List<BusRoute> getAllRoutes() {
        return busRouteRepository.findAll();
    }

    public void bookRoute(Booking booking){
        bookingRepository.save(booking);
    }
}
