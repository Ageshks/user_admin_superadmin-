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
        model.addAttribute("routeId", routeId);
        return "book-ticket"; 
    }

    // Book seats for a specific route
    @PostMapping("/book")
    public String bookTicket(
        @RequestParam Long routeId,
        @RequestParam int seats,
        Principal principal,
        RedirectAttributes redirectAttributes
    ) {
        try {
            // Get the logged-in user's email
            String userEmail = principal.getName();
            
            // Attempt to book the seats
            bookingService.bookSeats(routeId, userEmail, seats);

            // Add a success message to be displayed on the redirected page
            redirectAttributes.addFlashAttribute("message", "Booking successful!");
        } catch (Exception e) {
            // Add error message to redirect attributes
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/user/bookings/book/" + routeId; // Redirect back to booking form with error
        }

        return "redirect:/user/bookings/view"; // Redirect to bookings view
    }

    // View all bookings for the logged-in user
    @GetMapping("/view")
    public String viewBookings(Model model, Principal principal) {
        // Get the bookings for the logged-in user
        model.addAttribute("bookings", bookingService.getBookingsByUser(principal.getName()));
        return "user-bookings"; 
    }

    // Add a route to the cart for later booking (optional feature)
    @GetMapping("/add-to-cart/{routeId}")
    public String addToCart(@PathVariable Long routeId, Principal principal) {
        // Implement a cart system if required
        return "redirect:/user/bookings/routes"; // Redirect back to routes page
    }
}
