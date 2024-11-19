package admin_user.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

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

    // Get all bus routes
    public List<BusRoute> getAllRoutes() {
        return busRouteRepository.findAll();
    }

    // Book a ticket (reserve seats)
    public Booking bookTicket(Long routeId, String userEmail, int seats) {
        BusRoute route = busRouteRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));

        if (route.getAvailableSeats() < seats) {
            throw new IllegalArgumentException("Not enough seats available.");
        }

        route.setAvailableSeats(route.getAvailableSeats() - seats);
        busRouteRepository.save(route);

        Booking booking = new Booking(route, userEmail, seats, LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    // Create a Stripe session for payment
    public String initiatePayment(Long routeId, int seats, String userEmail) throws StripeException {
        Stripe.apiKey = "sk_test_51QLIbeGMcBpNczFvGfV2wOAKm4hY3Ehr9C7ObeR1hpFAJD0Ds7XACcc1gUBcMv8XO7lLARCPDEtzdrXXPhbb8J5300Wr5IzmEo"; // Use your own Stripe secret key

        BusRoute route = busRouteRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));

        double totalPrice = route.getPricePerSeat() * seats;  // Calculate total price

        // Create Stripe session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/user/bookings/view")
                .setCancelUrl("http://localhost:8080/user/bookings/view")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) (totalPrice * 100))  // Stripe works in cents
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Bus Ticket Booking")
                                        .build())
                                .build())
                        .setQuantity(1L)
                        .build())
                .build();

        // Create session
        Session session = Session.create(params);
        return session.getUrl();  // Return Stripe Checkout session URL
    }

    // Book seats for a user
public void bookSeats(Long routeId, String userEmail, int seats) {
    BusRoute route = busRouteRepository.findById(routeId)
            .orElseThrow(() -> new IllegalArgumentException("Route not found"));

    if (seats > route.getAvailableSeats()) {
        throw new IllegalArgumentException("Not enough seats available");
    }

    // Update available seats
    route.setAvailableSeats(route.getAvailableSeats() - seats);
    busRouteRepository.save(route);

    // Create booking
    Booking booking = new Booking();
    booking.setRoute(route);  // Correct method name
    booking.setUserEmail(userEmail);
    booking.setSeats(seats);
    booking.setBookingTime(LocalDateTime.now());  // Correct field name
    bookingRepository.save(booking);
}
  

    // Get bookings by user email
    public List<Booking> getBookingsByUser(String email) {
        return bookingRepository.findByUserEmail(email);
    }
}
