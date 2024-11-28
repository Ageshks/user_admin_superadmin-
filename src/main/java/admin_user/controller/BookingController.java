package admin_user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import admin_user.service.BookingService;

@Controller
@RequestMapping("/user/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // View all available routes
    @GetMapping("/routes")
    public String viewAvailableRoutes(Model model) {
        model.addAttribute("routes", bookingService.getAllRoutes());
        return "user-routes";
    }

    // Show booking form for a specific route
    @GetMapping("/book/{routeId}")
    public String showBookingForm(@PathVariable Long routeId, Model model) {
        double pricePerSeat = bookingService.getPricePerSeat(routeId); // Fetch price per seat
        model.addAttribute("routeId", routeId);
        model.addAttribute("pricePerSeat", pricePerSeat);
        model.addAttribute("STRIPE_PUBLISHABLE_KEY", "pk_test_51QLIbeGMcBpNczFEtp86Alaz0BKQCMbPOiMmY23RAGtZg25nian2Ok8GOze19yTUi721mF3237uutffQxekeJSh00RY7qHTnw");
        return "book-ticket";
    }

    // Handle payment for booking
    @PostMapping("/pay")
    public String payForBooking(
            @RequestParam Long routeId,
            @RequestParam int seats,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            String userEmail = principal.getName(); // Get user's email
            double totalAmount = bookingService.calculateTotalAmount(routeId, seats); // Calculate total amount

            // Set Stripe secret key (use environment variables in production)
            Stripe.apiKey = "sk_test_51QLIbeGMcBpNczFvGfV2wOAKm4hY3Ehr9C7ObeR1hpFAJD0Ds7XACcc1gUBcMv8XO7lLARCPDEtzdrXXPhbb8J5300Wr5IzmEo";

            // Create a Stripe payment session
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/user/bookings/success")
                    .setCancelUrl("http://localhost:8080/user/bookings/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount((long) (totalAmount * 100)) // Amount in cents
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Bus Ticket Booking - Route ID: " + routeId)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            // Redirect to Stripe payment page
            return "redirect:" + session.getUrl();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment failed: " + e.getMessage());
            return "redirect:/user/bookings/book/" + routeId;
        }
    }

    // Payment success handler
    @GetMapping("/success")
    public String paymentSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Payment successful! Your booking has been confirmed.");
        return "redirect:/user/bookings/view";
    }

    // Payment cancel handler
    @GetMapping("/cancel")
    public String paymentCancelled(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Payment was canceled. Please try again.");
        return "redirect:/user/bookings/view";
    }

    // View all bookings for the logged-in user
    @GetMapping("/view")
    public String viewBookings(Model model, Principal principal) {
        model.addAttribute("bookings", bookingService.getBookingsByUser(principal.getName()));
        return "user-bookings";
    }
}
