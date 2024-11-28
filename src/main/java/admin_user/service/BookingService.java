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

    // Fetch the price per seat for a specific route
    public double getPricePerSeat(Long routeId) {
        BusRoute route = busRouteRepository.findById(routeId)
            .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));
        return route.getPricePerSeat();
    }

    // Calculate the total amount for a booking
    public double calculateTotalAmount(Long routeId, int seats) {
        double pricePerSeat = getPricePerSeat(routeId); // Call the getPricePerSeat method
        return pricePerSeat * seats;
    }

    // Reserve seats for a user (combined from bookTicket and bookSeats)
    public Booking reserveSeats(Long routeId, String userEmail, int seats) {
        BusRoute route = busRouteRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));

        if (seats > route.getAvailableSeats()) {
            throw new IllegalArgumentException("Not enough seats available.");
        }

        // Update available seats
        route.setAvailableSeats(route.getAvailableSeats() - seats);
        busRouteRepository.save(route);

        // Create and save booking
        Booking booking = new Booking(route, userEmail, seats, LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    // Get bookings by user email
    public List<Booking> getBookingsByUser(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail);
    }

    // Create a Stripe session for payment
    public String initiatePayment(Long routeId, int seats, String userEmail) throws StripeException {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");  // Load Stripe secret key securely

        double totalPrice = calculateTotalAmount(routeId, seats);

        // Create Stripe session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/user/bookings/view")
                .setCancelUrl("http://localhost:8080/user/bookings/view")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) (totalPrice * 100)) // Stripe expects amounts in cents
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Bus Ticket Booking")
                                        .build())
                                .build())
                        .setQuantity(1L)
                        .build())
                .build();

        // Create session
        Session session = Session.create(params);
        return session.getUrl(); // Return Stripe Checkout session URL
    }
}
