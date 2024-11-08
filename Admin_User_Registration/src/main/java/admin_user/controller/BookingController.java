package admin_user.controller;

// import admin_user.model.Booking;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import admin_user.service.BookingService;

@Controller
@RequestMapping("/user/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Show available routes for booking
    @GetMapping("/routes")
    public String viewAvailableRoutes(Model model) {
        model.addAttribute("routes", bookingService.getAllRoutes());
        return "user-routes"; // Display available routes for booking
    }

    // Show booking form
    @GetMapping("/book/{routeId}")
    public String showBookingForm(@PathVariable Long routeId, Model model) {
        model.addAttribute("routeId", routeId);
        return "book-ticket"; // Booking form view
    }

    // Handle booking submission
    @PostMapping("/book")
    public String bookTicket(@RequestParam Long routeId, @RequestParam int seats, Principal principal) {
        bookingService.bookTicket(routeId, principal.getName(), seats);
        return "redirect:/user/bookings/view";
    }

    // View user's bookings
    @GetMapping("/view")
    public String viewBookings(Model model, Principal principal) {
        model.addAttribute("bookings", bookingService.getBookingsByUser(principal.getName()));
        return "user-bookings"; // Display user bookings
    }
}
