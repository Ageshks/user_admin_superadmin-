package admin_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import admin_user.model.BusRoute;
import admin_user.service.BookingService;
import admin_user.service.BusRouteService;

@Controller
public class BusRouteController {

    @Autowired
    private BusRouteService busRouteService;

    @Autowired
    private BookingService bookingService;

    // Add Bus Route Page
    @GetMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getAddRoutePage(Model model) {
        model.addAttribute("busRoute", new BusRoute());
        return "add-route";
    }

    // Add Bus Route Action
    @PostMapping("/super-admin/add-route")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addBusRoute(@ModelAttribute("busRoute") BusRoute busRoute) {
        busRouteService.addBusRoute(busRoute);
        return "redirect:/super-admin/view-routes";
    }

    // View All Routes
    @GetMapping("/super-admin/view-routes")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String viewRoutes(Model model) {
        model.addAttribute("routes", busRouteService.getAllRoutes());
        return "view-routes";
    }

    // Edit Bus Route Page
    @GetMapping("/super-admin/edit-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getEditRoutePage(@PathVariable Long id, Model model) {
        BusRoute busRoute = busRouteService.getRouteById(id);
        if (busRoute == null) {
            return "redirect:/super-admin/view-routes";
        }
        model.addAttribute("busRoute", busRoute);
        return "edit-route";
    }
// Update Bus Route Action
@PostMapping("/super-admin/edit-route/{id}")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public String updateRoute(@PathVariable Long id, @ModelAttribute("busRoute") BusRoute busRoute) {
    BusRoute existingRoute = busRouteService.getRouteById(id);
    if (existingRoute != null) {
        existingRoute.setRouteNumber(busRoute.getRouteNumber());
        existingRoute.setStartLocation(busRoute.getStartLocation());
        existingRoute.setEndLocation(busRoute.getEndLocation());
        existingRoute.setDepartureTime(busRoute.getDepartureTime());
        existingRoute.setTotalSeats(busRoute.getTotalSeats());
        existingRoute.setPricePerSeat(busRoute.getPricePerSeat());
        
        // Adjust available seats if total seats change
        int seatDifference = busRoute.getTotalSeats() - existingRoute.getTotalSeats();
        existingRoute.setAvailableSeats(existingRoute.getAvailableSeats() + seatDifference);

        busRouteService.updateBusRoute(existingRoute);
    }
    return "redirect:/super-admin/view-routes";
}


    // Delete Bus Route
    @GetMapping("/super-admin/delete-route/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteRoute(@PathVariable Long id) {
        busRouteService.deleteRoute(id);
        return "redirect:/super-admin/view-routes";
    }

    // User Dashboard
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        List<BusRoute> routes = busRouteService.getAvailableRoutes(); // Only display routes with available seats
        model.addAttribute("routes", routes);
        return "user-dashboard";
    }

    // Book Bus Route
    @PostMapping("/user/book/{id}")
    public String bookBusRoute(@PathVariable Long id, @RequestParam int seats) {
        BusRoute route = busRouteService.getRouteById(id);
        if (route != null && route.getAvailableSeats() >= seats) {
            bookingService.bookRoute(route, seats);
            return "redirect:/user/dashboard";
        } else {
            return "error";
        }
    }


     // Step 1: Initiate Payment
     @PostMapping("/user/initiate-booking/{id}")
     public String initiateBooking(@PathVariable Long id, @RequestParam int seats, Model model) {
         BusRoute route = busRouteService.getRouteById(id);
         
         if (route != null && route.getAvailableSeats() >= seats) {
             // Redirect to payment gateway or initiate payment request
             // Example: Create a payment request with payment gateway API
             
             // Add booking details to model for payment confirmation page
             model.addAttribute("route", route);
             model.addAttribute("seats", seats);
             model.addAttribute("totalAmount", calculateTotalAmount(route, seats)); // Custom method to calculate amount
 
             return "payment-page"; // This should be the view where payment is processed
         } else {
             model.addAttribute("error", "Not enough seats available.");
             return "user-dashboard";
         }
     }
 
     // Step 2: Complete Booking After Payment Confirmation
     @PostMapping("/user/complete-booking")
     public String completeBooking(
             @RequestParam Long routeId,
             @RequestParam int seats,
             @RequestParam String paymentId, // Payment gateway-provided ID
             Model model) {
         
         BusRoute route = busRouteService.getRouteById(routeId);
 
         if (route != null && route.getAvailableSeats() >= seats) {
             // Confirm payment status using payment gateway's API if needed
 
             // Complete booking only if payment is successful
             bookingService.bookRoute(route, seats, paymentId);
             return "redirect:/user/dashboard";
         } else {
             model.addAttribute("error", "Booking failed due to insufficient seats or payment issue.");
             return "payment-page";
         }
     }
}
